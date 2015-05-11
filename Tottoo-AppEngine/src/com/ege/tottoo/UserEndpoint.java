package com.ege.tottoo;

import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Nullable;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import com.ege.tottoo.exceptions.NotDefinedBonusException;
import com.ege.tottoo.exceptions.NotPlayableException;
import com.ege.tottoo.exceptions.TottooException;
import com.ege.tottoo.helper.PlayHelper;
import com.ege.tottoo.helper.ReloadHelper;
import com.ege.tottoo.helper.TottooHelper;
import com.ege.tottoo.mobile.MobilePlayPlan;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.datanucleus.query.JPACursorHelper;

@Api(name = "userendpoint", namespace = @ApiNamespace(ownerDomain = "ege.com", ownerName = "ege.com", packagePath = "tottoo"))
public class UserEndpoint {

	private static final Logger log = Logger.getLogger(UserEndpoint.class.getName());
	
	public static final int MAX_COIN=10;
	
	public static final int COIN_RELOAD_MINUTE=1;
	
	private static final int MIN_LEVEL=0;
	
	private static final int MIN_TURN=1;
	
	private static final int MIN_SPEEDUP=0;
	
	/**
	 * This method lists all the entities inserted in datastore.
	 * It uses HTTP GET method and paging support.
	 *
	 * @return A CollectionResponse class containing the list of all entities
	 * persisted and a cursor to the next page.
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@ApiMethod(name = "listUser")
	public CollectionResponse<User> listUser(
			@Nullable @Named("cursor") String cursorString,
			@Nullable @Named("limit") Integer limit) {

		EntityManager mgr = null;
		Cursor cursor = null;
		List<User> execute = null;

		try {
			mgr = getEntityManager();
			Query query = mgr.createQuery("select from User as User");
			if (cursorString != null && cursorString != "") {
				cursor = Cursor.fromWebSafeString(cursorString);
				query.setHint(JPACursorHelper.CURSOR_HINT, cursor);
			}

			if (limit != null) {
				query.setFirstResult(0);
				query.setMaxResults(limit);
			}

			execute = (List<User>) query.getResultList();
			cursor = JPACursorHelper.getCursor(execute);
			if (cursor != null)
				cursorString = cursor.toWebSafeString();

			// Tight loop for fetching all entities from datastore and accomodate
			// for lazy fetch.
			for (User obj : execute)
				;
		} finally {
			mgr.close();
		}

		return CollectionResponse.<User> builder().setItems(execute)
				.setNextPageToken(cursorString).build();
	}

	/**
	 * This method gets the entity having primary key id. It uses HTTP GET method.
	 *
	 * @param id the primary key of the java bean.
	 * @return The entity with primary key id.
	 */
	@ApiMethod(name = "getUser")
	public User getUser(@Named("id") Long id) {
		EntityManager mgr = getEntityManager();
		User user = null;
		try {
			user = mgr.find(User.class, id);
		} finally {
			mgr.close();
		}
		return user;
	}

	/**
	 * This inserts a new entity into App Engine datastore. If the entity already
	 * exists in the datastore, an exception is thrown.
	 * It uses HTTP POST method.
	 *
	 * @param user the entity to be inserted.
	 * @return The inserted entity.
	 */
	@ApiMethod(name = "insertUser")
	public Key insertUser(User user) {
		EntityManager mgr = getEntityManager();
		try {
			Tottoo tottoo = new Tottoo();
			TottooHelper.generateAllLevels(tottoo);
			user.setTottooList(tottoo);
			user.setMaxCoin(MAX_COIN);
			user.setCoinReloadMinute(COIN_RELOAD_MINUTE);
			user.setRemainCoin(MAX_COIN);
			user.setCurrentLevel(MIN_LEVEL);
			user.setCurrentTurn(MIN_TURN);
			user.setTotalSpeedupCount(MIN_SPEEDUP);
			mgr.persist(user);
		} catch(Exception e){
			log.log(Level.SEVERE,"mgr in insertUser : "+mgr);
		}finally {
			mgr.close();
		}
		return user.getKey();
	}

	/**
	 * This method is used for updating an existing entity. If the entity does not
	 * exist in the datastore, an exception is thrown.
	 * It uses HTTP PUT method.
	 *
	 * @param user the entity to be updated.
	 * @return The updated entity.
	 */
	@ApiMethod(name = "updateUser")
	public User updateUser(User user) {
		EntityManager mgr = getEntityManager();
		try {
			/*if (!containsUser(user)) {
				throw new EntityNotFoundException("Object does not exist");
			}*/
			mgr.persist(user);
		} finally {
			mgr.close();
		}
		return user;
	}

	/**
	 * This method removes the entity with primary key id.
	 * It uses HTTP DELETE method.
	 *
	 * @param id the primary key of the entity to be deleted.
	 */
	@ApiMethod(name = "removeUser")
	public void removeUser(@Named("id") Long id) {
		EntityManager mgr = getEntityManager();
		try {
			User user = mgr.find(User.class, id);
			mgr.remove(user);
		} finally {
			mgr.close();
		}
	}

	@ApiMethod(name = "speedup")
	public GameState[] speedup(@Named("id") Long idOnMobile,@Named("identifier") String identifierOnMobile,
			@Named("currentlevel") int currentLevelOnMobile,@Named("currentturn") int currentTurnOnMobile,
			@Named("currentcoin") int currentCoinOnMobile,@Named("speedupCount") int speedupCount) throws TottooException
	{
		log.warning("Speedup Called");
		GameState[] states = new GameState[speedupCount];
		EntityManager mgr = getEntityManager();
		User user = mgr.find(User.class, idOnMobile);
		boolean isSpeedUpFirstTurn = false;
		MobilePlayPlan mobilePlayPlan = new MobilePlayPlan();
		mobilePlayPlan.setCurrentCoin(currentCoinOnMobile);
		mobilePlayPlan.setCurrentLevel(currentLevelOnMobile);
		mobilePlayPlan.setCurrentTurn(currentTurnOnMobile);
		mobilePlayPlan.setIdentifier(identifierOnMobile);
		for (int i = 0; i < speedupCount; i++) {
			if(i==0)
				isSpeedUpFirstTurn = true;
			else
				isSpeedUpFirstTurn = false;
			GameState state = PlayHelper.playTottoo(user, mobilePlayPlan, false, isSpeedUpFirstTurn);
			states[i]=state;
		}
		return states;
	}
	
	@ApiMethod(name = "play")
	public GameState play(@Named("id") Long idOnMobile,@Named("identifier") String identifierOnMobile,
			@Named("currentlevel") int currentLevelOnMobile,@Named("currentturn") int currentTurnOnMobile,
			@Named("currentcoin") int currentCoinOnMobile,@Named("isSpeedUp") boolean isSpeedUp,
			@Named("isSpeedUpFirstTurn") boolean isSpeedUpFirstTurn) throws TottooException
	{
		EntityManager mgr = getEntityManager();
		EntityTransaction txn = mgr.getTransaction();
		GameState gameState = new GameState();
		try {
			User user = mgr.find(User.class, idOnMobile);
			if(user==null) {
				throw new NotPlayableException("Play option is forbidden!..");
			} else {
				MobilePlayPlan mobilePlayPlan = new MobilePlayPlan();
				mobilePlayPlan.setCurrentCoin(currentCoinOnMobile);
				mobilePlayPlan.setCurrentLevel(currentLevelOnMobile);
				mobilePlayPlan.setCurrentTurn(currentTurnOnMobile);
				mobilePlayPlan.setIdentifier(identifierOnMobile);
				txn.begin();
				gameState = PlayHelper.playTottoo(user, mobilePlayPlan, isSpeedUp, isSpeedUpFirstTurn);
				txn.commit();
			}
		} finally {
			if(txn.isActive())
				txn.rollback();
			mgr.close();
		}
		return gameState;
	}
	
	private boolean containsUser(User user) {
		EntityManager mgr = getEntityManager();
		boolean contains = true;
		try {
			User item = mgr.find(User.class, user.getKey());
			if (item == null) {
				contains = false;
			}
		} finally {
			mgr.close();
		}
		return contains;
	}

	private static EntityManager getEntityManager() {
		return EMF.get().createEntityManager();
	}

}
