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

@Api(name = "reloadendpoint", namespace = @ApiNamespace(ownerDomain = "ege.com", ownerName = "ege.com", packagePath = "tottoo"))
public class ReloadEndpoint {

	/**
	 * This method lists all the entities inserted in datastore.
	 * It uses HTTP GET method and paging support.
	 *
	 * @return A CollectionResponse class containing the list of all entities
	 * persisted and a cursor to the next page.
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@ApiMethod(name = "listReload")
	public CollectionResponse<Reload> listReload(
			@Nullable @Named("cursor") String cursorString,
			@Nullable @Named("limit") Integer limit) {

		EntityManager mgr = null;
		Cursor cursor = null;
		List<Reload> execute = null;

		try {
			mgr = getEntityManager();
			Query query = mgr.createQuery("select from Reload as Reload");
			if (cursorString != null && cursorString != "") {
				cursor = Cursor.fromWebSafeString(cursorString);
				query.setHint(JPACursorHelper.CURSOR_HINT, cursor);
			}

			if (limit != null) {
				query.setFirstResult(0);
				query.setMaxResults(limit);
			}

			execute = (List<Reload>) query.getResultList();
			cursor = JPACursorHelper.getCursor(execute);
			if (cursor != null)
				cursorString = cursor.toWebSafeString();

			// Tight loop for fetching all entities from datastore and accomodate
			// for lazy fetch.
			for (Reload obj : execute)
				;
		} finally {
			mgr.close();
		}

		return CollectionResponse.<Reload> builder().setItems(execute)
				.setNextPageToken(cursorString).build();
	}

	/**
	 * This method gets the entity having primary key id. It uses HTTP GET method.
	 *
	 * @param id the primary key of the java bean.
	 * @return The entity with primary key id.
	 */
	@ApiMethod(name = "getReload")
	public Reload getReload(@Named("id") Long id) {
		EntityManager mgr = getEntityManager();
		Reload reload = null;
		try {
			reload = mgr.find(Reload.class, id);
		} finally {
			mgr.close();
		}
		return reload;
	}

	/**
	 * This inserts a new entity into App Engine datastore. If the entity already
	 * exists in the datastore, an exception is thrown.
	 * It uses HTTP POST method.
	 *
	 * @param reload the entity to be inserted.
	 * @return The inserted entity.
	 */
	@ApiMethod(name = "insertReload")
	public Reload insertReload(Reload reload) {
		EntityManager mgr = getEntityManager();
		try {
			if (containsReload(reload)) {
				throw new EntityExistsException("Object already exists");
			}
			mgr.persist(reload);
		} finally {
			mgr.close();
		}
		return reload;
	}

	/**
	 * This method is used for updating an existing entity. If the entity does not
	 * exist in the datastore, an exception is thrown.
	 * It uses HTTP PUT method.
	 *
	 * @param reload the entity to be updated.
	 * @return The updated entity.
	 */
	@ApiMethod(name = "updateReload")
	public Reload updateReload(Reload reload) {
		EntityManager mgr = getEntityManager();
		try {
			if (!containsReload(reload)) {
				throw new EntityNotFoundException("Object does not exist");
			}
			mgr.persist(reload);
		} finally {
			mgr.close();
		}
		return reload;
	}

	/**
	 * This method removes the entity with primary key id.
	 * It uses HTTP DELETE method.
	 *
	 * @param id the primary key of the entity to be deleted.
	 */
	@ApiMethod(name = "removeReload")
	public void removeReload(@Named("id") Long id) {
		EntityManager mgr = getEntityManager();
		try {
			Reload reload = mgr.find(Reload.class, id);
			mgr.remove(reload);
		} finally {
			mgr.close();
		}
	}

	private boolean containsReload(Reload reload) {
		EntityManager mgr = getEntityManager();
		boolean contains = true;
		try {
			Reload item = mgr.find(Reload.class, reload.getKey());
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
