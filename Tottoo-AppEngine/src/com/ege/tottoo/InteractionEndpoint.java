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

@Api(name = "interactionendpoint", namespace = @ApiNamespace(ownerDomain = "ege.com", ownerName = "ege.com", packagePath = "tottoo"))
public class InteractionEndpoint {

	/**
	 * This method lists all the entities inserted in datastore.
	 * It uses HTTP GET method and paging support.
	 *
	 * @return A CollectionResponse class containing the list of all entities
	 * persisted and a cursor to the next page.
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@ApiMethod(name = "listInteraction")
	public CollectionResponse<Interaction> listInteraction(
			@Nullable @Named("cursor") String cursorString,
			@Nullable @Named("limit") Integer limit) {

		EntityManager mgr = null;
		Cursor cursor = null;
		List<Interaction> execute = null;

		try {
			mgr = getEntityManager();
			Query query = mgr
					.createQuery("select from Interaction as Interaction");
			if (cursorString != null && cursorString != "") {
				cursor = Cursor.fromWebSafeString(cursorString);
				query.setHint(JPACursorHelper.CURSOR_HINT, cursor);
			}

			if (limit != null) {
				query.setFirstResult(0);
				query.setMaxResults(limit);
			}

			execute = (List<Interaction>) query.getResultList();
			cursor = JPACursorHelper.getCursor(execute);
			if (cursor != null)
				cursorString = cursor.toWebSafeString();

			// Tight loop for fetching all entities from datastore and accomodate
			// for lazy fetch.
			for (Interaction obj : execute)
				;
		} finally {
			mgr.close();
		}

		return CollectionResponse.<Interaction> builder().setItems(execute)
				.setNextPageToken(cursorString).build();
	}

	/**
	 * This method gets the entity having primary key id. It uses HTTP GET method.
	 *
	 * @param id the primary key of the java bean.
	 * @return The entity with primary key id.
	 */
	@ApiMethod(name = "getInteraction")
	public Interaction getInteraction(@Named("id") Long id) {
		EntityManager mgr = getEntityManager();
		Interaction interaction = null;
		try {
			interaction = mgr.find(Interaction.class, id);
		} finally {
			mgr.close();
		}
		return interaction;
	}

	/**
	 * This inserts a new entity into App Engine datastore. If the entity already
	 * exists in the datastore, an exception is thrown.
	 * It uses HTTP POST method.
	 *
	 * @param interaction the entity to be inserted.
	 * @return The inserted entity.
	 */
	@ApiMethod(name = "insertInteraction")
	public Interaction insertInteraction(Interaction interaction) {
		EntityManager mgr = getEntityManager();
		try {
			if (containsInteraction(interaction)) {
				throw new EntityExistsException("Object already exists");
			}
			mgr.persist(interaction);
		} finally {
			mgr.close();
		}
		return interaction;
	}

	/**
	 * This method is used for updating an existing entity. If the entity does not
	 * exist in the datastore, an exception is thrown.
	 * It uses HTTP PUT method.
	 *
	 * @param interaction the entity to be updated.
	 * @return The updated entity.
	 */
	@ApiMethod(name = "updateInteraction")
	public Interaction updateInteraction(Interaction interaction) {
		EntityManager mgr = getEntityManager();
		try {
			if (!containsInteraction(interaction)) {
				throw new EntityNotFoundException("Object does not exist");
			}
			mgr.persist(interaction);
		} finally {
			mgr.close();
		}
		return interaction;
	}

	/**
	 * This method removes the entity with primary key id.
	 * It uses HTTP DELETE method.
	 *
	 * @param id the primary key of the entity to be deleted.
	 */
	@ApiMethod(name = "removeInteraction")
	public void removeInteraction(@Named("id") Long id) {
		EntityManager mgr = getEntityManager();
		try {
			Interaction interaction = mgr.find(Interaction.class, id);
			mgr.remove(interaction);
		} finally {
			mgr.close();
		}
	}

	private boolean containsInteraction(Interaction interaction) {
		EntityManager mgr = getEntityManager();
		boolean contains = true;
		try {
			Interaction item = mgr
					.find(Interaction.class, interaction.getKey());
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
