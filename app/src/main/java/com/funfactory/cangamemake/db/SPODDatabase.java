package com.funfactory.cangamemake.db;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import roboguice.util.Ln;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Responsible for manipulating the database.
 */
@Singleton
public final class SPODDatabase extends SQLiteOpenHelper implements IDBManager {

	/**
	 * Comment tag.
	 */
	private static final String COMMENT = "--";

	/**
	 * Database name.
	 */
	private static final String DATABASE_NAME = "catalog";

	/**
	 * The db.sql value to name a file.
	 */
	private static final String DB_SQL = "db.sql";

	/**
	 * The db_2.0.sql value to name a file.
	 */
	private static final String DB_SQL_V2 = "db_v2.sql";
	
	/**
	 * The db_3.0.sql value to name a file.
	 */
	private static final String DB_SQL_V3 = "db_v3.sql";

	/**
	 * Query to enable foreign keys.
	 */
	private static final String KEYS_ENABLE = "PRAGMA foreign_keys=%s;";

	/**
	 * Semicolon.
	 */
	private static final String REG_EXPRESSION = ";";

	/**
	 * Current Db version.
	 */
	private static final int DATABASE_VERSION = 11;

    /**
     * DB Version that included Table.EVENT. This constant must *NOT* be
     * changed. Keep it as 10.
     */
	private static final int DATABASE_VERSION_V10 = 10;
	
    /**
     * DB Version that included Table.EVENT. This constant must *NOT* be
     * changed. Keep it as 11.
     */
	private static final int DATABASE_VERSION_V11 = 11;
	
	/**
	 * App context.
	 */
	private final Context context;

	/**
	 * The buffer, kept as a field in order to avoid sonar violation.
	 */
	private String buff;

	/**
	 * Constructor.
	 *
	 * @param androidContext
	 *            of the application
	 */
	@Inject
	public SPODDatabase(final Context androidContext) {
		super(androidContext, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = androidContext;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite
	 * .SQLiteDatabase)
	 */
	@Override
	public void onCreate(final SQLiteDatabase sqlitedb) {
		executeSQL(getStatementSql(DB_SQL), sqlitedb);
		executeSQL(getStatementSql(DB_SQL_V2), sqlitedb);
		executeSQL(getStatementSql(DB_SQL_V3), sqlitedb);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite
	 * .SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(final SQLiteDatabase sqlitedb, final int oldVersion,
			final int newVersion) {
		if (oldVersion < DATABASE_VERSION_V10) {
		    executeSQL(getStatementSql(DB_SQL_V2), sqlitedb);
		}
		
		if (oldVersion < DATABASE_VERSION_V11) {
		    executeSQL(getStatementSql(DB_SQL_V3), sqlitedb);
		}
	}

	/**
	 * Drop the tables.
	 *
	 * @param sqlitedb
	 *            the bd instance.
	 */
	private void dropTables(final SQLiteDatabase sqlitedb) {
		for (final Table table : Table.values()) {
			executeSQL("DROP TABLE IF EXISTS " + table.getName(), sqlitedb);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.org.sidi.smartcatalog.model.db.IDBManager#dropAllTables()
	 */
	@Override
	public void dropAllTables() {
		final SQLiteDatabase database = getWritableDatabase();
		dropTables(database);
		onCreate(database);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.org.sidi.smartcatalog.model.db.IDBManager#enableForeignKeys(boolean)
	 */
	public void enableForeignKeys(final boolean enable) {
		getDatabase().execSQL(String.format(KEYS_ENABLE, enable));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.org.sidi.smartcatalog.model.db.IDBManager#executeSQLFile(java.lang
	 * .String)
	 */
	public void executeSQLFile(final String fileName) {
		executeSQL(getStatementSql(fileName));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.org.sidi.smartcatalog.model.db.IDBManager#executeSQL(java.lang.String
	 * [])
	 */
	public void executeSQL(final String[] sqls) {
		executeSQL(sqls, getDatabase());
	}

	/**
	 * Executes a list of sql commands to the database passed by parameter.
	 *
	 * @param sqls
	 *            the sqls
	 * @param sqlitedb
	 *            database that will be executed
	 */
	private void executeSQL(final String[] sqls, final SQLiteDatabase sqlitedb) {
		for (final String sql : sqls) {
			if (sql != null && !sql.isEmpty()) {
				sqlitedb.execSQL(sql);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.org.sidi.smartcatalog.model.db.IDBManager#executeSQL(java.lang.String)
	 */
	public void executeSQL(final String sql) {
		executeSQL(sql, getDatabase());
	}

	/**
	 * Execute a sql command in database parameter passed.
	 *
	 * @param sql
	 *            to be executed
	 * @param sqlitedb
	 *            database that will be executed
	 */
	private void executeSQL(final String sql, final SQLiteDatabase sqlitedb) {
		sqlitedb.execSQL(sql);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.org.sidi.smartcatalog.model.db.IDBManager#getStatementSql(java.lang
	 * .String)
	 */
	public String[] getStatementSql(final String fileName) {
		final StringBuilder stringBuilder = new StringBuilder();
		InputStream inputStream;
		BufferedReader sqlFile = null;
		try {
			inputStream = context.getAssets().open(fileName);
			final InputStreamReader reader = new InputStreamReader(inputStream,
					Charset.defaultCharset());
			sqlFile = new BufferedReader(reader);
			while ((setBuff(sqlFile.readLine())) != null) {
				if (!getBuff().trim().startsWith(COMMENT)) {
					stringBuilder.append(getBuff());
				}
			}

		} catch (final IOException e) {
			Ln.e(e);
		} finally {
			if (sqlFile != null) {
				try {
					sqlFile.close();
				} catch (final IOException e) {
					Ln.e(e);
				}
			}
		}

		return stringBuilder.toString().split(REG_EXPRESSION);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.org.sidi.smartcatalog.model.db.IDBManager#getDatabase()
	 */
	public SQLiteDatabase getDatabase() {
		return getWritableDatabase();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.org.sidi.smartcatalog.model.db.ITransactionSupport#beginTransaction()
	 */
	@Override
	public void beginTransaction() {
		getDatabase().beginTransaction();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.org.sidi.smartcatalog.model.db.ITransactionSupport#endTransaction()
	 */
	@Override
	public void endTransaction() {
		getDatabase().endTransaction();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.org.sidi.smartcatalog.model.db.ITransactionSupport#setSuccessfull()
	 */
	@Override
	public void setSuccessfull() {
		getDatabase().setTransactionSuccessful();
	}

	/**
	 * Gets the buff
	 * 
	 * @return the buff
	 */
	private String getBuff() {
		return buff;
	}

	/**
	 * Sets the buff
	 * 
	 * @param buff
	 *            the buff to set
	 */
	private String setBuff(final String buff) {
		this.buff = buff;
		return buff;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.org.sidi.smartcatalog.model.db.IDBManager#start()
	 */
	@Override
	public void start() {
		getDatabase();

	}

}