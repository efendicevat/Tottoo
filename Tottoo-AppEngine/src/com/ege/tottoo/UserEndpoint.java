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
import com.ege.tottoo.helper.TottooHelper;
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
			log.log(Level.WARNING,"interactions : "+user.getInteractions());
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

	@ApiMethod(name = "play")
	public GameState play(@Named("id") Long idOnMobile,@Named("identifier") String identifierOnMobile,
			@Named("currentlevel") int currentLevelOnMobile,@Named("currentturn") int currentTurnOnMobile) throws TottooException
	{
		Interaction action = new Interaction();
		GameState gameState = new GameState();
		EntityManager mgr = getEntityManager();
		EntityTransaction txn = mgr.getTransaction();
		int speedupCount = 0;
		try {
			User user = mgr.find(User.class, idOnMobile);
			if(user==null) {
				throw new NotPlayableException("Play option is forbidden!..");
			} else {
				speedupCount = user.getTotalSpeedupCount();
				log.info("speedupCount : "+speedupCount);
				action.setPlayTime(Calendar.getInstance().getTime());
				boolean isPlayable = PlayHelper.isPlayable(user, identifierOnMobile, currentLevelOnMobile, currentTurnOnMobile);
				int playLevel = user.getCurrentLevel();
				int playTurn = user.getCurrentTurn();
				if(isPlayable) {
					Tottoo tottooOnCloud = user.getTottooList();
					String currentLevelOnCloud = TottooHelper.getCurrentTottooLevel(tottooOnCloud, playLevel);
					if(currentLevelOnCloud.contains(",")) { //HAS BONUS
						log.info("HAS BONUS");
						String[] tmp = currentLevelOnCloud.split(",");
						String state = tmp[0];
						String[] tmp2 = state.split("-");
						String speedupStr = tmp2[0];
						int speedupStrTurn = Integer.valueOf(tmp2[1]);
						String others = tmp[1];
						if(speedupStrTurn==playTurn) {
							if(state.contains("speedup")) {
								String[] tmp3 = speedupStr.split("X");
								int span = Integer.valueOf(tmp3[1]);
								speedupCount +=span;
								user.setTotalSpeedupCount(speedupCount);
								TottooHelper.setCurrentTottooLevel(tottooOnCloud, playLevel, others);
								user.setTottooList(tottooOnCloud);
								gameState.setState("SPEEDUPX"+span);
								playTurn++;
							} else {
								throw new NotDefinedBonusException("Not Defined Bonus. Option is forbidden!..");
							}
						} else {
							gameState.setState("TRYAGAIN");
							playTurn++;
						}
					} else { //NO BONUS
						log.info("NO BONUS");
						String[] tmp = currentLevelOnCloud.split("-");
						String levelx = tmp[0];
						int currentTurnOnCloud = Integer.valueOf(tmp[1]);
						if(currentTurnOnCloud==playTurn) {
							setGameState(user,levelx,gameState,playLevel,playTurn);
						} else {
							gameState.setState("TRYAGAIN");
							playTurn++;
						}
					}
				}
				else {
					throw new NotPlayableException("Play option is forbidden!..");
				}
				action.setGameState(gameState);
				
				txn.begin();
				user.setCurrentLevel(playLevel);
				user.setCurrentTurn(playTurn);
				List<Interaction> interactions = user.getInteractions();
				interactions.add(action);
				user.setInteractions(interactions);
				txn.commit();
			}
		} finally {
			if(txn.isActive())
				txn.rollback();
			mgr.close();
		}
		return gameState;
	}
	
	private void setGameState(User user,String levelx,GameState gameState,int currentLevel,int currentTurn) {
		if(levelx.contains("levelup")) {
			if(currentLevel==9) {
				gameState.setState("WIN");
			} else {
				gameState.setState("PASSLEVEL");
				currentLevel++;
				currentTurn = 1;
			}
		}
		else if(levelx.contains("bonus")) {
			if(currentLevel==9) {
				gameState.setState("WIN");
			} else {
				gameState.setState("2XPASSLEVEL");
				currentLevel++;
				currentTurn = 1;
			}
		}
		else if(levelx.contains("backstep")) {
			if(currentLevel==0) {
				gameState.setState("GAMEOVER");
				currentLevel = 0;
				Tottoo tottoo = new Tottoo();
				TottooHelper.generateLevelByMinLevel(tottoo,currentLevel);
				user.setTottooList(tottoo);
				currentTurn = 1;
			} else {
				gameState.setState("BACKLEVEL");
				currentLevel--;
				Tottoo tottoo = new Tottoo();
				TottooHelper.generateLevelByMinLevel(tottoo,currentLevel);
				user.setTottooList(tottoo);
				currentTurn = 1;
			}
		}
		else if(levelx.contains("smalltrap")) {
			if(currentLevel==0) {
				gameState.setState("GAMEOVER");
				currentLevel = 0;
				Tottoo tottoo = new Tottoo();
				TottooHelper.generateLevelByMinLevel(tottoo,currentLevel);
				user.setTottooList(tottoo);
				currentTurn = 1;
			} else {
				gameState.setState("2XBACKLEVEL");
				currentLevel--;
				Tottoo tottoo = new Tottoo();
				TottooHelper.generateLevelByMinLevel(tottoo,currentLevel);
				user.setTottooList(tottoo);
				currentTurn = 1;
			}
		}
		else if(levelx.contains("bigtrap")) {
			if(currentLevel==0) {
				gameState.setState("GAMEOVER");
				currentLevel = 0;
				Tottoo tottoo = new Tottoo();
				TottooHelper.generateLevelByMinLevel(tottoo,currentLevel);
				user.setTottooList(tottoo);
				currentTurn = 1;
			} else {
				gameState.setState("3XBACKLEVEL");
				currentLevel--;
				Tottoo tottoo = new Tottoo();
				TottooHelper.generateLevelByMinLevel(tottoo,currentLevel);
				user.setTottooList(tottoo);
				currentTurn = 1;
			}
		}
		else {
			gameState.setState("TRYAGAIN"); //defensive
			currentTurn++;
		}
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
