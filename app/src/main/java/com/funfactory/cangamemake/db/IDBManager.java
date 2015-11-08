package com.funfactory.cangamemake.db;

import android.database.sqlite.SQLiteDatabase;

/**
 * Responsible for controlling access to the database.
 */
public interface IDBManager extends ITransactionSupport {

    /**
     * Get the SQLiteDataBase.
     * 
     * @return an instance of SQLiteDatabase
     */
    SQLiteDatabase getDatabase();

    /**
     * Enables or disables the foreign key constraints.
     * 
     * @param enable
     *            true to enable
     */
    void enableForeignKeys(boolean enable);

    /**
     * Executes a sql.
     * 
     * @param sql
     *            to be executed
     */
    void executeSQL(String sql);

    /**
     * Executes a list of commands sqls.
     * 
     * @param sqls
     *            list to be executed
     */
    void executeSQL(String[] sqls);

    /**
     * Executes a file containing commands sqls separated by ";".
     * 
     * @param fileName
     *            in asserts
     */
    void executeSQLFile(String fileName);

    /**
     * Get a list of commands in the file sqls passer by parameter.
     * 
     * @param fileName
     *            in asserts
     * @return command list sqls
     */
    String[] getStatementSql(String fileName);

    /**
     * Starts the database
     */
    void start();

    /**
     * Drop all tables in database
     */
    void dropAllTables();

}
