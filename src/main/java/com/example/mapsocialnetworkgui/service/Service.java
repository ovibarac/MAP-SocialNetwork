package com.example.mapsocialnetworkgui.service;

import com.example.mapsocialnetworkgui.domain.Entity;
import com.example.mapsocialnetworkgui.domain.Friendship;
import com.example.mapsocialnetworkgui.repo.Repository;
import com.example.mapsocialnetworkgui.repo.exception.ValidationException;

/**
 * CRUD operations Service interface
 * @param <ID> - type E must have an attribute of type ID
 * @param <E> -  type of entities saved in repository
 */
public interface Service<ID, E extends Entity<ID>> {
    /**
     * save new entity
     * @param entity
     *         entity must not be null
     * @return null- if the given id is saved
     *         otherwise returns the entity (id already exists)
     * @throws ValidationException
     *            if the entity is not valid
     * @throws IllegalArgumentException
     *             if the given entity is null.     *
     */
     E add(E entity);

    /**
     *
     * @return all entities
     */
    Iterable<E> findAll();

    /**
     *  removes the entity with the specified id
     * @param id
     *      id must be not null
     * @return the removed entity or null if there is no entity with the given id
     * @throws IllegalArgumentException
     *                   if the given id is null.
     */
    E delete(ID id);

    /**
     *
     * @param id -the id of the entity to be returned
     *           id must not be null
     * @return the entity with the specified id
     *          or null - if there is no entity with the given id
     * @throws IllegalArgumentException
     *                  if id is null.
     */
    E findOne(ID id);
    /**
     * update entity
     * @param e
     *          entity must not be null
     * @return null - if the entity is updated,
     *                otherwise  returns the entity  - (e.g id does not exist).
     * @throws IllegalArgumentException
     *             if the given entity is null.
     * @throws ValidationException
     *             if the entity is not valid.
     */
    E update(E e);
}
