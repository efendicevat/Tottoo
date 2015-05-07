package com.ege.tottoo;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
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
import com.google.appengine.api.memcache.ErrorHandlers;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.appengine.datanucleus.query.JPACursorHelper;

@Api(name = "userendpoint", namespace = @ApiNamespace(ownerDomain = "ege.com", ownerName = "ege.com", packagePath = "tottoo"))
public class UserEndpoint {

	private static final Logger log = Logger.getLogger(UserEndpoint.class.getName());
	
	private static final String tryAgain = "TRYAGAIN";
	
	private static final String speedupx = "SPEEDUPX";
	
	private static final String win = "WIN";
	
	private static final String backlevel = "BACKLEVEL";
	
	private static final String backlevelx2 = "2XBACKLEVEL";
	
	private static final String backlevelx3 = "3XBACKLEVEL";
	
	private static final String passlevel = "PASSLEVEL";
	
	private static final String passlevelx2 = "2XPASSLEVEL";
	
	private static final String gameover = "GAMEOVER";
	
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
			@Named("currentcoin") int currentCoin,@Named("speedupCount") int speedupCount) throws TottooException
	{
		GameState[] states = new GameState[speedupCount];
		EntityManager mgr = getEntityManager();
		User user = mgr.find(User.class, idOnMobile);
		int coin = PlayHelper.calculateCoinOnCloud(user);
		user.setRemainCoin(coin);
		boolean isPlayable = PlayHelper.isPlayable(user, identifierOnMobile, currentLevelOnMobile, currentTurnOnMobile, currentCoin);
		boolean isSpeedUpFirstTurn = false;
		for (int i = 0; i < speedupCount; i++) {
			if(i==0)
				isSpeedUpFirstTurn = true;
			else
				isSpeedUpFirstTurn = false;
			GameState state = play(idOnMobile,identifierOnMobile,currentLevelOnMobile,currentTurnOnMobile,currentCoin,true,isPlayable,isSpeedUpFirstTurn);
			states[i]=state;
		}
		return states;
	}
	
	@ApiMethod(name = "play")
	public GameState play(@Named("id") Long idOnMobile,@Named("identifier") String identifierOnMobile,
			@Named("currentlevel") int currentLevelOnMobile,@Named("currentturn") int currentTurnOnMobile,
			@Named("currentcoin") int currentCoin,@Named("isSpeedUp") boolean isSpeedUp,
			@Named("isSpeedUpPlayable") boolean isSpeedupPlayable, @Named("isSpeedUpFirstTurn") boolean isSpeedUpFirstTurn) throws TottooException
	{
		Interaction action = new Interaction();
		GameState gameState = new GameState();
		EntityManager mgr = getEntityManager();
		EntityTransaction txn = mgr.getTransaction();
		int speedupCount = 0;
		boolean isPlayable = false;
		try {
			User user = mgr.find(User.class, idOnMobile);
			if(user==null) {
				throw new NotPlayableException("Play option is forbidden!..");
			} else {
				speedupCount = user.getTotalSpeedupCount();
				log.warning("speedupCount : "+speedupCount);
				if(isSpeedUp) {
					speedupCount--;
					user.setTotalSpeedupCount(speedupCount);
				}
				action.setPlayTime(Calendar.getInstance().getTime());
				int coin = PlayHelper.calculateCoinOnCloud(user);
				user.setRemainCoin(coin);
				if(isSpeedUp)
					isPlayable = isSpeedupPlayable;
				else
					isPlayable = PlayHelper.isPlayable(user, identifierOnMobile, currentLevelOnMobile, currentTurnOnMobile, currentCoin);
				int playLevel = user.getCurrentLevel();
				int playTurn = user.getCurrentTurn();
				if(isPlayable) {
					if(isSpeedUp)
					{
						if(isSpeedUpFirstTurn)
							coin--;
					}
					else
						coin--;
					user.setRemainCoin(coin);
					user.setCoinUsageTime(Calendar.getInstance().getTime());
					Tottoo tottooOnCloud = user.getTottooList();
					String currentLevelOnCloud = TottooHelper.getCurrentTottooLevel(tottooOnCloud, playLevel);
					if(currentLevelOnCloud.contains(",")) { //HAS BONUS
						log.info("HAS BONUS");
						String[] tmp = currentLevelOnCloud.split(",");
						String state = tmp[0];
						String[] tmp2 = state.split("-");
						String speedupStr = tmp2[0];
						int speedupStrTurn = Integer.valueOf(tmp2[1]);
						String others = "";
						for (int i = 1; i < tmp.length; i++) {
							others += tmp[i]+",";
						}
						if(others.endsWith(",")) {
							others = others.substring(0, others.length()-1);
						}
						if(speedupStrTurn==playTurn) {
							if(state.contains("speedup")) {
								String[] tmp3 = speedupStr.split("X");
								int span = Integer.valueOf(tmp3[1]);
								speedupCount +=span;
								user.setTotalSpeedupCount(speedupCount);
								TottooHelper.setCurrentTottooLevel(tottooOnCloud, playLevel, others);
								user.setTottooList(tottooOnCloud);
								gameState.setState(speedupx+span);
								playTurn++;
								user.setCurrentTurn(playTurn);
							} else {
								throw new NotDefinedBonusException("Not Defined Bonus. Option is forbidden!..");
							}
						} else {
							gameState.setState(tryAgain);
							playTurn++;
							user.setCurrentTurn(playTurn);
						}
					} else { //NO BONUS
						log.info("NO BONUS");
						String[] tmp = currentLevelOnCloud.split("-");
						String levelx = tmp[0];
						int currentTurnOnCloud = Integer.valueOf(tmp[1]);
						if(currentTurnOnCloud==playTurn) {
							setGameState(user,levelx,gameState,playLevel,playTurn);
						} else {
							gameState.setState(tryAgain);
							playTurn++;
							user.setCurrentTurn(playTurn);
						}
					}
				}
				else {
					throw new NotPlayableException("Play option is forbidden!..");
				}
				action.setGameState(gameState);
				
				txn.begin();
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
				gameState.setState(win);
				user.setTotalSpeedupCount(0);
			} else {
				gameState.setState(passlevel);
				currentLevel++;
				currentTurn = 1;
			}
		}
		else if(levelx.contains("bonus")) {
			if(currentLevel==9) {
				gameState.setState(win);
				user.setTotalSpeedupCount(0);
			} else {
				gameState.setState(passlevelx2);
				currentLevel+=2;
				currentTurn = 1;
			}
		}
		else if(levelx.contains("backstep")) {
			if(currentLevel==0) {
				gameState.setState(gameover);
				currentLevel = 0;
				Tottoo tottoo = new Tottoo();
				TottooHelper.generateLevelByMinLevel(tottoo,currentLevel);
				user.setTottooList(tottoo);
				currentTurn = 1;
				user.setTotalSpeedupCount(0);
			} else {
				gameState.setState(backlevel);
				currentLevel--;
				Tottoo tottoo = new Tottoo();
				TottooHelper.generateLevelByMinLevel(tottoo,currentLevel);
				user.setTottooList(tottoo);
				currentTurn = 1;
			}
		}
		else if(levelx.contains("smalltrap")) {
			if(currentLevel==0) {
				gameState.setState(gameover);
				currentLevel = 0;
				Tottoo tottoo = new Tottoo();
				TottooHelper.generateLevelByMinLevel(tottoo,currentLevel);
				user.setTottooList(tottoo);
				currentTurn = 1;
				user.setTotalSpeedupCount(0);
			} else {
				gameState.setState(backlevelx2);
				currentLevel-=2;
				Tottoo tottoo = new Tottoo();
				TottooHelper.generateLevelByMinLevel(tottoo,currentLevel);
				user.setTottooList(tottoo);
				currentTurn = 1;
			}
		}
		else if(levelx.contains("bigtrap")) {
			if(currentLevel==0) {
				gameState.setState(gameover);
				currentLevel = 0;
				Tottoo tottoo = new Tottoo();
				TottooHelper.generateLevelByMinLevel(tottoo,currentLevel);
				user.setTottooList(tottoo);
				currentTurn = 1;
				user.setTotalSpeedupCount(0);
			} else {
				gameState.setState(backlevelx3);
				currentLevel-=3;
				Tottoo tottoo = new Tottoo();
				TottooHelper.generateLevelByMinLevel(tottoo,currentLevel);
				user.setTottooList(tottoo);
				currentTurn = 1;
			}
		}
		else {
			gameState.setState(tryAgain); //defensive
			currentTurn++;
		}
		user.setCurrentLevel(currentLevel);
		user.setCurrentTurn(currentTurn);
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
