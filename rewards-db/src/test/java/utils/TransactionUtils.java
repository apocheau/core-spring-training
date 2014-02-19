package utils;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

public class TransactionUtils {

	protected final PlatformTransactionManager txnMgr;

	protected Logger log;

	public TransactionUtils(PlatformTransactionManager txnMgr) {
		this.txnMgr = txnMgr;
		log = Logger.getLogger(getClass());
		log.setLevel(Level.INFO);
	}

	public TransactionStatus getCurrentTransaction() {
		TransactionDefinition definition = new DefaultTransactionDefinition(
				DefaultTransactionDefinition.PROPAGATION_MANDATORY);
		TransactionStatus transaction = txnMgr.getTransaction(definition);
		System.out.println("TRANSACTION = " + transaction);
		return transaction;
	}

	public TransactionStatus getTransaction() {
		TransactionDefinition definition = new DefaultTransactionDefinition(
				DefaultTransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus transaction = txnMgr.getTransaction(definition);
		System.out.println("TRANSACTION = " + transaction);
		return transaction;
	}

	public TransactionStatus getNewTransaction() {
		TransactionDefinition definition = new DefaultTransactionDefinition(
				DefaultTransactionDefinition.PROPAGATION_REQUIRES_NEW);
		TransactionStatus transaction = txnMgr.getTransaction(definition);
		System.out.println("TRANSACTION = " + transaction);
		return transaction;
	}

	public boolean transactionExists() {
		try {
			TransactionStatus transaction = getCurrentTransaction();
			
			if (transaction == null)
				throw new IllegalStateException("No transaction in progress");

			System.out.println("TRANSACTION EXISTS - new ? " + transaction.isNewTransaction());
			return true;
		} catch (Exception e) {
			System.out.println(e);
			log.error("NO TRANSACTION: " + e);
			return false;
		}
	}
}
