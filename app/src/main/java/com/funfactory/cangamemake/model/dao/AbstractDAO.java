package com.funfactory.cangamemake.model.dao;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import roboguice.util.Ln;
import roboguice.util.Strings;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.funfactory.cangamemake.db.CanGameDatabase;
import com.funfactory.cangamemake.db.IDBManager;
import com.funfactory.cangamemake.model.entity.AbstractBaseEntity;

public abstract class AbstractDAO<T extends AbstractBaseEntity> implements Serializable, IGenericDAO<T> {

    /**
     * The version controller.
     */
    private static final long    serialVersionUID = 1L;

    /**
     * Rows affected.
     */
    protected static final int   ROWS_AFFECTED    = 0;

    /**
     * Table Name for DAO *.
     */
    private final String         table;

    /**
     * The identifiers.
     */
    private transient long[]     identifiers;

    /**
     * The db manager.
     */
    private transient IDBManager dbManager        = CanGameDatabase.getInstance(); ;

    /**
     * Gets the identifiers.
     *
     * @return the identifiers
     */
    public long[] getIdentifiers() {
        return identifiers == null ? new long[] {} : identifiers.clone();
    }

    /**
     * Public constructor.
     *
     * @param table
     *            the table name to set.
     */
    public AbstractDAO(final String table) {
        this.table = table;
    }

    /**
     * Insert and commit.
     *
     * @param entity
     *            the entity
     * @return the long
     * @throws SQLException
     *             the sQL exception
     */
    public long insertAndCommit(final T entity) throws SQLException {
        long entityId;
        // Begin a new database transaction
        beginTransaction();
        try {
            // Executing insert operation
            entityId = insert(entity);
            // Commit the transaction
            setTransactionSuccessful();
        } finally {
            // Finish the transaction, if an Exception was thrown a "rollback"
            // is executed !!!
            endTransaction();
        }
        return entityId;
    }

    /**
     * Insert.
     *
     * @param entity
     *            the entity
     * @return the long
     * @throws SQLException
     *             the sQL exception
     */
    public long insert(final T entity) throws SQLException {
        // Convert Object to be used in SQL Persist Command
        final ContentValues values = objectToContentValues(entity);
        // Executing insert operation
        final long idEntity = getDb().insertOrThrow(table, null, values);
        entity.setIdEntity(idEntity);
        return idEntity;
    }

    /**
     * Insert all and commit.
     *
     * @param entityList
     *            the entity list
     * @return the long[]
     * @throws SQLException
     *             the sQL exception
     */
    public long[] insertAllAndCommit(final List<T> entityList) throws SQLException {
        // Begin a new database transaction
        beginTransaction();
        try {
            // Executing insert operation
            identifiers = insertAll(entityList);
            // Commit the transaction
            setTransactionSuccessful();
        } finally {
            // Finish the transaction, if an Exception was thrown a "rollback"
            // is executed !!!
            endTransaction();
        }
        return identifiers.clone();
    }

    /**
     * Insert all.
     *
     * @param entityList
     *            the entity list
     * @return the long[]
     * @throws SQLException
     *             the sQL exception
     */
    public long[] insertAll(final List<T> entityList) throws SQLException {
        T entity = null;
        identifiers = new long[entityList.size()];
        for (int i = 0; i < entityList.size(); i++) {
            // Convert Object to be used in SQL Persist Command
            entity = entityList.get(i);
            final ContentValues values = objectToContentValues(entity);
            // Executing insert operation
            identifiers[i] = getDb().insertOrThrow(table, null, values);
            entity.setIdEntity(identifiers[i]);
        }
        return identifiers.clone();
    }

    /**
     * Update and commit.
     *
     * @param entity
     *            the entity
     * @return the int
     * @throws SQLException
     *             the sQL exception
     */
    public int updateAndCommit(final T entity) throws SQLException {
        int rowsAffected = ROWS_AFFECTED;
        // Begin a new database transaction
        beginTransaction();
        try {
            // Execute an update operation
            rowsAffected += update(entity);
            // Commit the transaction
            setTransactionSuccessful();
        } finally {
            // Finish the transaction, if an Exception was thrown a "rollback"
            // is executed !!!
            endTransaction();
        }
        return rowsAffected;
    }

    /**
     * Update.
     *
     * @param entity
     *            the entity
     * @return the int
     * @throws SQLException
     *             the sQL exception
     */
    public Long update(final T entity) throws SQLException {
        int rowsAffected = ROWS_AFFECTED;
        // Convert Object to be used in SQL Persist Command
        final ContentValues values = objectToContentValues(entity);
        // Get the conditions for executing an update
        final String whereClause = getPrimaryKeyWhereClause();
        final String[] whereArgs = getPrimaryKeyFromObject(entity);
        // Execute an update operation
        getDb().update(table, values, whereClause, whereArgs);
        return entity.getIdEntity();
    }

    /**
     * Update all and commit.
     *
     * @param entityList
     *            the entity list
     * @return the int
     * @throws SQLException
     *             the sQL exception
     */
    public int updateAllAndCommit(final List<T> entityList) throws SQLException {
        int rowsAffecteds = ROWS_AFFECTED;
        // Begin a new database transaction
        beginTransaction();
        try {
            // Execute an update operation
            rowsAffecteds += updateAll(entityList);
            // Commit the transaction
            setTransactionSuccessful();
        } finally {
            // Finish the transaction, if an Exception was thrown a "rollback"
            // is executed !!!
            endTransaction();
        }
        return rowsAffecteds;
    }

    /**
     * Update all.
     *
     * @param entityList
     *            the entity list
     * @return the int
     * @throws SQLException
     *             the sQL exception
     */
    public int updateAll(final List<T> entityList) throws SQLException {
        int rowsAffecteds = ROWS_AFFECTED;
        for (int i = 0; i < entityList.size(); i++) {
            // Convert Object to be used in SQL Persist Command
            final ContentValues values = objectToContentValues(entityList.get(i));

            // Get the conditions for executing an update
            final String whereClause = getPrimaryKeyWhereClause();
            final String[] whereArgs = getPrimaryKeyFromObject(entityList.get(i));

            // Execute an update operation
            rowsAffecteds += getDb().update(table, values, whereClause, whereArgs);
        }
        return rowsAffecteds;
    }

    /**
     * Count all rows into the table
     * 
     * @return number of rows
     */
    public int count() {
        final Cursor cursor = getDb().rawQuery("select count(*) from " + this.table, null);
        int result = -1;

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                result = cursor.getInt(0);
            }
            cursor.close();
        }
        return result;
    }

    /**
     * Delete and commit.
     *
     * @param entity
     *            the entity
     * @return the int
     * @throws SQLException
     *             the sQL exception
     */
    public int deleteAndCommit(final T entity) throws SQLException {

        if (entity == null) {
            return ROWS_AFFECTED;
        }

        int rowsAffecteds = ROWS_AFFECTED;
        // Begin a new database transaction
        beginTransaction();
        try {
            // Execute an delete operation
            rowsAffecteds += delete(entity);
            // Commit the transaction
            setTransactionSuccessful();
        } finally {
            // Finish the transaction, if an Exception was thrown a "rollback"
            // is executed !!!
            endTransaction();
        }
        return rowsAffecteds;
    }

    /**
     * Delete.
     *
     * @param entity
     *            the entity to be deleted
     * @return the amount of rows affected
     * @throws SQLException
     *             the sQL exception
     */
    public int delete(final T entity) throws SQLException {
        int rowsAffecteds = ROWS_AFFECTED;
        // Get the conditions for executing an update
        final String whereClause = getPrimaryKeyWhereClause();
        final String[] whereArgs = getPrimaryKeyFromObject(entity);
        // Execute an delete operation
        rowsAffecteds += getDb().delete(table, whereClause, whereArgs);
        return rowsAffecteds;
    }

    /**
     * Delete all and commit.
     *
     * @param entityList
     *            the entity list
     * @return the int
     * @throws SQLException
     *             the sQL exception
     */
    public int deleteAllAndCommit(final List<T> entityList) throws SQLException {
        int rowsAffecteds = ROWS_AFFECTED;
        // Begin a new database transaction
        beginTransaction();
        try {
            // Execute an delete operation
            rowsAffecteds += deleteAll(entityList);
            // Commit the transaction
            setTransactionSuccessful();
        } finally {
            // Finish the transaction, if an Exception was thrown a "rollback"
            // is executed !!!
            endTransaction();
        }
        return rowsAffecteds;
    }

    /**
     * Delete all.
     *
     * @param entityList
     *            the entity list
     * @return the int
     * @throws SQLException
     *             the sQL exception
     */
    public int deleteAll(final List<T> entityList) throws SQLException {
        int rowsAffecteds = ROWS_AFFECTED;
        for (int i = 0; i < entityList.size(); i++) {
            // Get the conditions for executing an update
            final String whereClause = getPrimaryKeyWhereClause();
            final String[] whereArgs = getPrimaryKeyFromObject(entityList.get(i));
            // Execute an delete operation
            rowsAffecteds += getDb().delete(table, whereClause, whereArgs);
        }
        return rowsAffecteds;
    }

    /**
     * Saves the given entity and commit the transaction.
     *
     * @param entity
     *            the entity to save.
     */
    public void saveAndCommit(final T entity) {
        // Begin a new database transaction
        beginTransaction();
        try {
            // Execute save
            save(entity);
            // Commit the transaction
            setTransactionSuccessful();
        } finally {
            // Finish the transaction, if an Exception was thrown a "rollback"
            // is executed !!!
            endTransaction();
        }
    }

    /**
     * Saves the given entity .
     *
     * @param entity
     *            the entity to save.
     */
    public void save(final T entity) {
        entity.toString();
        if (entity.getIdEntity() == null) {
            insert(entity);
        } else {
            update(entity);
        }
    }

    /**
     * Saves the given entity and return the id.
     *
     * @param entity
     *            the entity to save.
     * 
     * @return The ID of the entity
     */
    public long saveAndReturnId(final T entity) {
        if (entity.getIdEntity() == null) {
            return insert(entity);
        } else {
            return (long) update(entity);
        }
    }

    /**
     * Helper method to parse Cursor do object List.
     *
     * @param cursor
     *            records queried in table
     * @return List of Model objects
     */
    protected List<T> parseCursorToList(final Cursor cursor) {
        final List<T> result = new ArrayList<T>();

        // If records are found then add in result list
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    result.add(cursorToObject(cursor));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        return result;
    }

    /**
     * Gets the all.
     *
     * @return the all
     */
    public List<T> getAll() {
        // Query database table for all records
        final Cursor cursor = getDb().query(table, null, null, null, null, null, null);
        return parseCursorToList(cursor);
    }

    /**
     * Exist.
     *
     * @param entity
     *            the entity to verify.
     * @return <code>true</code> if the given entity was found on database and <code>false</code>
     *         otherwise.
     */
    public boolean exist(final T entity) {
        return getEntityById(entity.getIdEntity()) != null;
    }

    /**
     * Exist.
     *
     * @param filters
     *            the filters to filter.
     * @return <code>true</code> if a entity was found with the given filters and <code>false</code>
     *         otherwise.
     */
    public boolean exist(final Map<String, String> filters) {
        return getEntityByFilter(filters) != null;
    }

    /**
     * Gets the entity by id.
     *
     * @param entityId
     *            the entity id
     * @return the entity by id
     */
    public T getEntityById(final long entityId) {
        final Map<String, String> filters = new HashMap<String, String>();
        filters.put(AbstractBaseEntity.ID_COLUMN, String.valueOf(entityId));
        return getEntityByFilter(filters);
    }

    /**
     * Gets the entity by field.
     *
     * @param field
     *            the field to filter.
     * @param value
     *            the value to filter.
     * @return A entity given the filters.
     */
    public T getEntityByField(final String field, final String value) {
        final Map<String, String> filters = new HashMap<String, String>();
        filters.put(field, value);
        return getEntityByFilter(filters);
    }

    /**
     * Gets the entity by filter.
     *
     * @param filters
     *            the filter to filter.
     * @return A entity given a filter.
     */
    public T getEntityByFilter(final Map<String, String> filters) {
        final List<T> entities = getList(filters, null);
        return entities.isEmpty() ? null : entities.get(0);
    }

    /**
     * Get entity list creating the consult by a map with field and value.
     *
     * @param filters
     *            the filters to filter.
     * @param orderBy
     *            the order by clause.
     * @return the list
     */
    public List<T> getList(final Map<String, String> filters, final String orderBy) {
        return getList(getTable(), filters, orderBy);
    }

    /**
     * Gets the list sql.
     *
     * @param sql
     *            the sql.
     * @param filters
     *            the filters to complement the query.
     * @param orderBy
     *            the order by
     * @return A list of all entities found with the mounted query.
     */
    protected List<T> getListSQL(final String sql, final Map<String, String> filters, final String orderBy) {
        final String selection = builderSelection(filters);
        final StringBuilder query = new StringBuilder(sql);
        if (!selection.isEmpty()) {
            query.append(" Where ").append(selection);
        }
        if (orderBy != null && !orderBy.isEmpty()) {
            query.append(" order by ").append(orderBy);
        }
        final String[] selectionArgs = builderSelectionArgs(filters);
        final Cursor cursor = getDb().rawQuery(query.toString(), selectionArgs);
        return parseCursorToList(cursor);
    }

    /**
     * Get entity list creating the consult by a map with field and value.
     *
     * @param table
     *            the table
     * @param filters
     *            the filters
     * @param orderBy
     *            the order by
     * @return the list
     */
    protected List<T> getList(final String table, final Map<String, String> filters, final String orderBy) {
        final String selection = builderSelection(filters);
        final String[] selectionArgs = builderSelectionArgs(filters);
        final Cursor cursor = getDb().query(table, null, selection, selectionArgs, null, null, orderBy);
        return parseCursorToList(cursor);
    }

    /**
     * Create the string filter.
     *
     * @param filter
     *            the filter
     * @return the string
     */
    protected String builderSelection(final Map<String, String> filter) {
        final StringBuilder builder = new StringBuilder();
        for (final String field : filter.keySet()) {
            builder.append(field);
            builder.append(" = ?");
            // builder.append(field);
            builder.append(" and ");
        }
        if (!builder.toString().isEmpty()) {
            builder.delete(builder.length() - 5, builder.length());
        }
        return builder.toString();
    }

    /**
     * Binding the values with parameters.
     *
     * @param filter
     *            the filter
     * @return the string[]
     */
    protected String[] builderSelectionArgs(final Map<String, String> filter) {
        final Object[] objectArray = filter.values().toArray();
        final String[] stringArray = Arrays.copyOf(objectArray, objectArray.length, String[].class);
        return stringArray;
    }

    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'.'S";

    /**
     * Parses a string date to a Date object
     *
     * @param date
     *            the string date to parse
     * @return the date object
     */
    public Date parse(final String date) {
        final SimpleDateFormat dateFormatter = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
        Date resultDate = null;
        try {
            if (Strings.notEmpty(date)) {
                resultDate = dateFormatter.parse(date);
            }
        } catch (final ParseException e) {
            Ln.e(e, "Error during date parse");
        }

        return resultDate;
    }

    /**
     * Formats a date object into a string date
     *
     * @param date
     *            the date object
     * @return the string formatted date
     */
    public String format(final Date date) {
        final SimpleDateFormat dateFormatter = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
        return dateFormatter.format(date);
    }

    /**
     * Formats a date object into a string date
     *
     */
    protected Cursor executeSelectQuery(String sql) {
        Cursor c = getDb().rawQuery(sql, null);
        return c;
    }

    /**
     * Execute a pure sql query
     *
     */
    protected void executeSQL(String sql) {
        getDb().execSQL(sql);
    }

    /**
     * Get the Primary Key where clause to model object.
     *
     * @return The Primary Key where clause
     */
    protected String getPrimaryKeyWhereClause() {
        return AbstractBaseEntity.ID_COLUMN + " = ?";
    }

    /**
     * Begin a transaction.
     */
    private void beginTransaction() {
        dbManager.beginTransaction();
    }

    /**
     * Set a transaction with successful.
     */
    private void setTransactionSuccessful() {
        dbManager.setSuccessfull();
    }

    /**
     * Finalize a transaction.
     */
    private void endTransaction() {
        dbManager.endTransaction();
    }

    /**
     * Gets the db.
     *
     * @return the db
     */
    public SQLiteDatabase getDb() {
        return dbManager.getDatabase();
    }

    /**
     * Get table name.
     *
     * @return string with the name of table
     */
    public String getTable() {
        return table;
    }

    /**
     * Get the array of Primary Key values.
     *
     * @param entity
     *            Model Object
     * @return The array with the Primary Key
     */
    protected String[] getPrimaryKeyFromObject(final T entity) {
        return getPrimaryKeyFromObject(entity.getIdEntity());
    }

    /**
     * Convert the primary key of an entity in a String[].
     *
     * @param entityId
     *            id of the entity
     * @return String[] with primary key value
     */
    protected String[] getPrimaryKeyFromObject(final long entityId) {
        return new String[] { String.valueOf(entityId) };
    }

    /**
     * Convert a Cursor record in Model Object.
     *
     * @param cursor
     *            Cursor with a record
     * @return A Model Object with the Cursor data
     */
    protected abstract T cursorToObject(final Cursor cursor);

    /**
     * Convert a Model Object in ContentValues.
     *
     * @param entity
     *            Model Object
     * @return A ContentValues with the Model Object data
     */
    protected abstract ContentValues objectToContentValues(final T entity);

}
