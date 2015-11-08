package com.funfactory.cangamemake.db;

/**
 * Interface to represents a transaction.
 */
public interface ITransactionSupport {

    /**
     * Begins the transaction.
     */
    void beginTransaction();

    /**
     * Ends a transaction.
     */
    void endTransaction();

    /**
     * Commits a transaction.
     */
    void setSuccessfull();
}
