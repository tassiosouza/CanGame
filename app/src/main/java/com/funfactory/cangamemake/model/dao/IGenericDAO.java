package com.funfactory.cangamemake.model.dao;

import java.util.List;
import java.util.Map;

import android.database.SQLException;

public interface IGenericDAO<T> {
    /**
     * Insert and commit.
     * 
     * @param entity
     *            the entity
     * @return the long
     * @throws SQLException
     *             the sQL exception
     */
    long insertAndCommit(final T entity) throws SQLException;

    /**
     * Insert.
     *
     * @param entity
     *            the entity
     * @return the long
     * @throws SQLException
     *             the sQL exception
     */
    long insert(final T entity) throws SQLException;

    /**
     * Insert all and commit.
     *
     * @param entityList
     *            the entity list
     * @return the long[]
     * @throws SQLException
     *             the sQL exception
     */
    long[] insertAllAndCommit(final List<T> entityList) throws SQLException;

    /**
     * Insert all.
     *
     * @param entityList
     *            the entity list
     * @return the long[]
     * @throws SQLException
     *             the sQL exception
     */
    long[] insertAll(final List<T> entityList) throws SQLException;

    /**
     * Update and commit.
     *
     * @param entity
     *            the entity
     * @return the int
     * @throws SQLException
     *             the sQL exception
     */
    int updateAndCommit(final T entity) throws SQLException;

    /**
     * Update.
     *
     * @param entity
     *            the entity
     * @return the int
     * @throws SQLException
     *             the sQL exception
     */
    Long update(final T entity) throws SQLException;

    /**
     * Update all and commit.
     *
     * @param entityList
     *            the entity list
     * @return the int
     * @throws SQLException
     *             the sQL exception
     */
    int updateAllAndCommit(final List<T> entityList) throws SQLException;

    /**
     * Update all.
     *
     * @param entityList
     *            the entity list
     * @return the int
     * @throws SQLException
     *             the sQL exception
     */
    int updateAll(final List<T> entityList) throws SQLException;

    /**
     * Delete and commit.
     *
     * @param entity
     *            the entity
     * @return the int
     * @throws SQLException
     *             the sQL exception
     */
    int deleteAndCommit(final T entity) throws SQLException;

    /**
     * Delete.
     *
     * @param entity
     *            the entity to be deleted
     * @return the amount of rows affected
     * @throws SQLException
     *             the sQL exception
     */
    int delete(final T entity) throws SQLException;

    /**
     * Delete all and commit.
     *
     * @param entityList
     *            the entity list
     * @return the int
     * @throws SQLException
     *             the sQL exception
     */
    int deleteAllAndCommit(final List<T> entityList) throws SQLException;

    /**
     * Delete all.
     *
     * @param entityList
     *            the entity list
     * @return the int
     * @throws SQLException
     *             the sQL exception
     */
    int deleteAll(final List<T> entityList) throws SQLException;

    /**
     * Saves the given entity and commit the transaction.
     *
     * @param entity
     *            the entity to save.
     */
    void saveAndCommit(final T entity);

    /**
     * Saves the given entity .
     *
     * @param entity
     *            the entity to save.
     */
    void save(final T entity);

    /**
     * Saves the given entity and return the ID.
     *
     * @param entity
     *            the entity to save.
     *
     * @return The ID of the entity
     */
    long saveAndReturnId(final T entity);

    /**
     * Gets the all.
     *
     * @return the all
     */
    List<T> getAll();

    /**
     * Exist.
     *
     * @param entity
     *            the entity to verify.
     * @return <code>true</code> if the given entity was found on database and <code>false</code> otherwise.
     */
    boolean exist(final T entity);

    /**
     * Exist.
     *
     * @param filters
     *            the filters to filter.
     * @return <code>true</code> if a entity was found with the given filters and <code>false</code> otherwise.
     */
    boolean exist(final Map<String, String> filters);

    /**
     * Gets the entity by id.
     *
     * @param entityId
     *            the entity id
     * @return the entity by id
     */
    T getEntityById(final long entityId);

    /**
     * Gets the entity by field.
     *
     * @param field
     *            the field to filter.
     * @param value
     *            the value to filter.
     * @return A entity given the filters.
     */
    T getEntityByField(final String field, final String value);

    /**
     * Gets the entity by filter.
     *
     * @param filters
     *            the filter to filter.
     * @return A entity given a filter.
     */
    T getEntityByFilter(final Map<String, String> filters);

    /**
     * Get entity list creating the consult by a map with field and value.
     *
     * @param filters
     *            the filters to filter.
     * @param orderBy
     *            the order by clause.
     * @return the list
     */
    List<T> getList(final Map<String, String> filters, final String orderBy);

    /**
     * Get the number of entities.
     *
     */
    int count();
}
