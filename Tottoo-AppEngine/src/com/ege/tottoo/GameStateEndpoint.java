package com.ege.tottoo;

import com.ege.tottoo.EMF;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.datanucleus.query.JPACursorHelper;

import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Named;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.persistence.EntityManager;
import javax.persistence.Query;

@Api(name = "gamestateendpoint", namespace = @ApiNamespace(ownerDomain = "ege.com", ownerName = "ege.com", packagePath = "tottoo"))
public class GameStateEndpoint {

	/**
	 * This method lists all the entities inserted in datastore.
	 * It uses HTTP GET method and paging support.
	 *
	 * @return A CollectionResponse class containing the list of all entities
	 * persisted and a cursor to the next page.
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@ApiMethod(name = "listGameState")
	public CollectionResponse<GameState> listGameState(
			@Nullable @Named("cursor") String cursorString,
			@Nullable @Named("limit") Integer limit) {

		EntityManager mgr = null;
		Cursor cursor = null;
		List<GameState> execute = null;

		try {
			mgr = getEntityManager();
			Query query = mgr.createQuery("select from GameState as GameState");
			if (cursorString != null && cursorString != "") {
				cursor = Cursor.fromWebSafeString(cursorString);
				query.setHint(JPACursorHelper.CURSOR_HINT, cursor);
			}

			if (limit != null) {
				query.setFirstResult(0);
				query.setMaxResults(limit);
			}

			execute = (List<GameState>) query.getResultList();
			cursor = JPACursorHelper.getCursor(execute);
			if (cursor != null)
				cursorString = cursor.toWebSafeString();

			// Tight loop for fetching all entities from datastore and accomodate
			// for lazy fetch.
			for (GameState obj : execute)
				;
		} finally {
			mgr.close();
		}

		return CollectionResponse.<GameState> builder().setItems(execute)
				.setNextPageToken(cursorString).build();
	}

	/**
	 * This method gets the entity having primary key id. It uses HTTP GET method.
	 *
	 * @param id the primary key of the java bean.
	 * @return The entity with primary key id.
	 */
	@ApiMethod(name = "getGameState")
	public GameState getGameState(@Named("id") Long id) {
		EntityManager mgr = getEntityManager();
		GameState gamestate = null;
		try {
			gamestate = mgr.find(GameState.class, id);
		} finally {
			mgr.close();
		}
		return gamestate;
	}

	/**
	 * This inserts a new entity into App Engine datastore. If the entity already
	 * exists in the datastore, an exception is thrown.
	 * It uses HTTP POST method.
	 *
	 * @param gamestate the entity to be inserted.
	 * @return The inserted entity.
	 */
	@ApiMethod(name = "insertGameState")
	public GameState insertGameState(GameState gamestate) {
		EntityManager mgr = getEntityManager();
		try {
			if (containsGameState(gamestate)) {
				throw new EntityExistsException("Object already exists");
			}
			mgr.persist(gamestate);
		} finally {
			mgr.close();
		}
		return gamestate;
	}

	/**
	 * This method is used for updating an existing entity. If the entity does not
	 * exist in the datastore, an exception is thrown.
	 * It uses HTTP PUT method.
	 *
	 * @param gamestate the entity to be updated.
	 * @return The updated entity.
	 */
	@ApiMethod(name = "updateGameState")
	public GameState updateGameState(GameState gamestate) {
		EntityManager mgr = getEntityManager();
		try {
			if (!containsGameState(gamestate)) {
				throw new EntityNotFoundException("Object does not exist");
			}
			mgr.persist(gamestate);
		} finally {
			mgr.close();
		}
		return gamestate;
	}

	/**
	 * This method removes the entity with primary key id.
	 * It uses HTTP DELETE method.
	 *
	 * @param id the primary key of the entity to be deleted.
	 */
	@ApiMethod(name = "removeGameState")
	public void removeGameState(@Named("id") Long id) {
		EntityManager mgr = getEntityManager();
		try {
			GameState gamestate = mgr.find(GameState.class, id);
			mgr.remove(gamestate);
		} finally {
			mgr.close();
		}
	}

	private boolean containsGameState(GameState gamestate) {
		EntityManager mgr = getEntityManager();
		boolean contains = true;
		try {
			GameState item = mgr.find(GameState.class, gamestate.getKey());
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
