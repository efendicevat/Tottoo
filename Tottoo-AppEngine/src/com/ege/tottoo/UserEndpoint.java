package com.ege.tottoo;

import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Nullable;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

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
		log.log(Level.WARNING,"mgr in insertUser : "+mgr);
		try {
			Tottoo tottoo = generateTottoo();
			user.setTottooList(tottoo);
			log.log(Level.WARNING,"Level0 : "+tottoo.getLevel0());
			log.log(Level.WARNING,"Level1 : "+tottoo.getLevel1());
			log.log(Level.WARNING,"Level2 : "+tottoo.getLevel2());
			log.log(Level.WARNING,"Level3 : "+tottoo.getLevel3());
			log.log(Level.WARNING,"Level4 : "+tottoo.getLevel4());
			log.log(Level.WARNING,"Level5 : "+tottoo.getLevel5());
			mgr.persist(user);
		} catch(Exception e){
			log.log(Level.SEVERE,"mgr in insertUser : "+mgr);
		}finally {
			log.log(Level.WARNING,"mgr in insertUser : "+mgr);
			mgr.close();
		}
		return user.getKey();
	}

	
	private Tottoo generateTottoo() {
		Tottoo tottoo = new Tottoo();
		TottooHelper.generateLevels(tottoo);
		return tottoo;
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
	public Interaction play(@Named("id") Long id,@Named("identifier") String identifier,
			@Named("currentlevel") int currentLevel,@Named("currentturn") int currentTurn)
	{
		Interaction result = new Interaction();
		EntityManager mgr = getEntityManager();
		EntityTransaction txn = mgr.getTransaction();
		try {
			User user = mgr.find(User.class, id);
			result.setPlayTime(Calendar.getInstance().getTime());
			String gameState = "";
			boolean isPlayable = PlayHelper.isPlayable(user, identifier, currentLevel, currentTurn);
			if(isPlayable) {
				Tottoo t = user.getTottooList();
				String levelx = TottooHelper.getCurrentTottooLevel(t, currentLevel);
				String[] temp = levelx.split("-");
				int levelTurn = Integer.valueOf(temp[1]);
				if(levelTurn==currentTurn) {
					if(levelx.contains("y")) {
						if(currentLevel==9) {
							gameState="WIN";
						} else {
							gameState="PASSLEVEL";
							currentLevel++;
							currentTurn = 0;
						}
					}
					else if(levelx.contains("k")) {
						if(currentLevel==0) {
							gameState="GAMEOVER";
							currentLevel = 0;
							currentTurn = 0;
						} else {
							gameState="BACKLEVEL";
							currentLevel--;
							currentTurn = 0;
						}
					} else {
						gameState = "TRYAGAIN";
						currentTurn++;
					}
				}
				else {
					gameState = "TRYAGAIN";
					currentTurn++;
				}
			}
			result.setGameState(gameState);
			
			txn.begin();
			user.setCurrentLevel(currentLevel);
			user.setCurrentTurn(currentTurn);
			List<Interaction> interactions = user.getInteractions();
			interactions.add(result);
			user.setInteractions(interactions);
			txn.commit();
		} finally {
			if(txn.isActive())
				txn.rollback();
		}
		return result;
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
