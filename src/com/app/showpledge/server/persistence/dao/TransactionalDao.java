package com.app.showpledge.server.persistence.dao;

import java.util.ConcurrentModificationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.objectify.ObjectifyOpts;

/**
 * DAO that encapsulates a single transaction. Create it and forget about it.
 * Also provides very convenient static methods for making GAE/Python-like
 * transactions.
 * 
 * @author Jeff Schnitzer
 */
public class TransactionalDao extends ObjectifyGenericDao {

	/** */
	private static final Logger log = LoggerFactory
			.getLogger(TransactionalDao.class);

	/** Alternate interface to Runnable for executing transactions */
	public static interface Transactable {
		void run(TransactionalDao daot);
	}

	/**
	 * Provides a place to put the result too. Note that the result is only
	 * valid if the transaction completes successfully; otherwise it should be
	 * ignored because it is not necessarily valid.
	 */
	abstract public static class Transact<T> implements Transactable {
		protected T result;

		public T getResult() {
			return this.result;
		}
	}

	/** Create a default DAOT and run the transaction through it */
	public static void runInTransaction(Transactable t) {
		TransactionalDao daot = new TransactionalDao();
		daot.doTransaction(t);
	}

	/**
	 * Run this task through transactions until it succeeds without an
	 * optimistic concurrency failure.
	 */
	public static void repeatInTransaction(Transactable t) {
		while (true) {
			try {
				runInTransaction(t);
				break;
			} catch (ConcurrentModificationException ex) {
				if (log.isWarnEnabled())
					log.warn("Optimistic concurrency failure for " + t + ": "
							+ ex);
			}
		}
	}

	/** Starts out with a transaction and session cache */
	public TransactionalDao() {
		super(new ObjectifyOpts().setSessionCache(true).setBeginTransaction(
				true));
	}

	/** Adds transaction to whatever you pass in */
	public TransactionalDao(ObjectifyOpts opts) {
		super(opts.setBeginTransaction(true));
	}

	/**
	 * Executes the task in the transactional context of this DAO/ofy.
	 */
	public void doTransaction(final Runnable task) {
		this.doTransaction(new Transactable() {
			@Override
			public void run(TransactionalDao daot) {
				task.run();
			}
		});
	}

	/**
	 * Executes the task in the transactional context of this DAO/ofy.
	 */
	public void doTransaction(Transactable task) {
		try {
			task.run(this);
			ofy().getTxn().commit();
		} finally {
			if (ofy().getTxn().isActive())
				ofy().getTxn().rollback();
		}
	}
}
