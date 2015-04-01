package com.ege.tottoo;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Nullable;
import javax.inject.Named;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;

import org.mortbay.log.Log;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.datanucleus.query.JPACursorHelper;

@Api(name = "tottooendpoint", namespace = @ApiNamespace(ownerDomain = "ege.com", ownerName = "ege.com", packagePath = "tottoo"))
public class TottooEndpoint {

	private static final Logger log = Logger.getLogger(TottooEndpoint.class.getName());
	
	/**
	 * This method lists all the entities inserted in datastore.
	 * It uses HTTP GET method and paging support.
	 *
	 * @return A CollectionResponse class containing the list of all entities
	 * persisted and a cursor to the next page.
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@ApiMethod(name = "listTottoo")
	public CollectionResponse<Tottoo> listTottoo(
			@Nullable @Named("cursor") String cursorString,
			@Nullable @Named("limit") Integer limit) {

		EntityManager mgr = null;
		Cursor cursor = null;
		List<Tottoo> execute = null;

		try {
			mgr = getEntityManager();
			Query query = mgr.createQuery("select from Tottoo as Tottoo");
			if (cursorString != null && cursorString != "") {
				cursor = Cursor.fromWebSafeString(cursorString);
				query.setHint(JPACursorHelper.CURSOR_HINT, cursor);
			}

			if (limit != null) {
				query.setFirstResult(0);
				query.setMaxResults(limit);
			}

			execute = (List<Tottoo>) query.getResultList();
			cursor = JPACursorHelper.getCursor(execute);
			if (cursor != null)
				cursorString = cursor.toWebSafeString();

			// Tight loop for fetching all entities from datastore and accomodate
			// for lazy fetch.
			for (Tottoo obj : execute)
				;
		} finally {
			mgr.close();
		}

		return CollectionResponse.<Tottoo> builder().setItems(execute)
				.setNextPageToken(cursorString).build();
	}

	/**
	 * This method gets the entity having primary key id. It uses HTTP GET method.
	 *
	 * @param id the primary key of the java bean.
	 * @return The entity with primary key id.
	 */
	@ApiMethod(name = "getTottoo")
	public Tottoo getTottoo(@Named("id") Long id) {
		EntityManager mgr = getEntityManager();
		Tottoo tottoo = null;
		try {
			tottoo = mgr.find(Tottoo.class, id);
		} finally {
			mgr.close();
		}
		return tottoo;
	}

	/**
	 * This inserts a new entity into App Engine datastore. If the entity already
	 * exists in the datastore, an exception is thrown.
	 * It uses HTTP POST method.
	 *
	 * @param tottoo the entity to be inserted.
	 * @return The inserted entity.
	 */
	@ApiMethod(name = "insertTottoo")
	public Tottoo insertTottoo(Tottoo tottoo) {
		EntityManager mgr = getEntityManager();
		try {
			if (containsTottoo(tottoo)) {
				throw new EntityExistsException("Object already exists");
			}
			mgr.persist(tottoo);
		} finally {
			mgr.close();
		}
		return tottoo;
	}

	/**
	 * This method is used for updating an existing entity. If the entity does not
	 * exist in the datastore, an exception is thrown.
	 * It uses HTTP PUT method.
	 *
	 * @param tottoo the entity to be updated.
	 * @return The updated entity.
	 */
	@ApiMethod(name = "updateTottoo")
	public Tottoo updateTottoo(Tottoo tottoo) {
		EntityManager mgr = getEntityManager();
		try {
			if (!containsTottoo(tottoo)) {
				throw new EntityNotFoundException("Object does not exist");
			}
			mgr.persist(tottoo);
		} finally {
			mgr.close();
		}
		return tottoo;
	}

	/**
	 * This method removes the entity with primary key id.
	 * It uses HTTP DELETE method.
	 *
	 * @param id the primary key of the entity to be deleted.
	 */
	@ApiMethod(name = "removeTottoo")
	public void removeTottoo(@Named("id") Long id) {
		EntityManager mgr = getEntityManager();
		try {
			Tottoo tottoo = mgr.find(Tottoo.class, id);
			mgr.remove(tottoo);
		} finally {
			mgr.close();
		}
	}

	private boolean containsTottoo(Tottoo tottoo) {
		EntityManager mgr = getEntityManager();
		log.log(Level.SEVERE,"mgr : "+mgr);
		log.log(Level.SEVERE,"tottoo : "+tottoo);
		boolean contains = true;
		try {
			Key key = tottoo.getKey();
			Tottoo item = mgr.find(Tottoo.class, key);
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
