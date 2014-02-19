package accounts.internal;

import javax.sql.DataSource;

import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.IllegalTransactionStateException;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * Supports transactional testing of AccountManager implementation in both a
 * manual and a Spring-configured environment.
 * <p>
 * Manual configuration, allows testing of an AccountManager implementation
 * without Spring.
 * <p>
 * Automated configuration using a class annotated with @ContextConfiguration
 * tests both the implementation of AccountManager and the Spring configuration
 * files.
 */
public abstract class AbstractDatabaseAccountManagerTests extends
		AbstractAccountManagerTests {

	@Autowired
	protected PlatformTransactionManager transactionManager;

	@Autowired
	protected DataSource dataSource;

	protected TransactionStatus transactionStatus;

	private static int numAccountsInDb = -1;

	private boolean manualConfig = false;

	@Before
	public void setUp() throws Exception {
		// If no @ContextConfiguration. do it manually
		if (manualConfig = getClass().getAnnotation(ContextConfiguration.class) == null) {
			dataSource = createTestDataSource();
			setupForTest(dataSource);
		}

		// The number of test accounts in the database
		// - a static variable so we only do this one.
		if (numAccountsInDb == -1)
			numAccountsInDb = new JdbcTemplate(dataSource)
					.queryForInt("SELECT count(*) FROM T_Account");

		if (manualConfig)
			beginTransaction();
	}

	protected abstract void setupForTest(DataSource dataSource)
			throws Exception;

	@After
	public void tearDown() throws Exception {
		if (manualConfig)
			rollbackTransaction();
	}

	protected void beginTransaction() throws Exception {
		// Make sure no txn is running
		try {
			transactionStatus = transactionManager
					.getTransaction(new DefaultTransactionDefinition(
							TransactionDefinition.PROPAGATION_MANDATORY));
			assert (false);
		} catch (IllegalTransactionStateException e) {
			// Expected behavior, continue
		}

		// Begin a new transaction - just checked that there isn't one
		transactionStatus = transactionManager.getTransaction(new DefaultTransactionDefinition(
				TransactionDefinition.PROPAGATION_REQUIRED));

		assert (transactionStatus != null);
		assert (transactionStatus.isNewTransaction());
		System.out.println("NEW " + transactionStatus + " - completed = "
				+ transactionStatus.isCompleted());
	}

	protected void rollbackTransaction() throws Exception {
		// Make sure an exception is running
		try {
			transactionManager.getTransaction(new DefaultTransactionDefinition(
					TransactionDefinition.PROPAGATION_MANDATORY));
			// Expected behavior, continue
		} catch (IllegalTransactionStateException e) {
			assert (false);
		}

		// Rollback the transaction to avoid corrupting other tests
		if (transactionManager != null)
			transactionManager.rollback(transactionStatus);
	}

	@Override
	protected int getNumAccountsExpected() {
		return numAccountsInDb;
	}

	/**
	 * Get current transaction - it must exist.
	 * 
	 * @return The transactional status of the current transaction.
	 */
	public TransactionStatus getCurrentTransaction() {
		if (transactionStatus == null && (!manualConfig))
			transactionStatus = transactionManager.getTransaction(null);
		
		System.out.println("EXISTING = " + transactionStatus + " completed = "
				+ transactionStatus.isCompleted());
		return transactionStatus; //transactionManager.getTransaction(null);

	}

	protected DataSource createTestDataSource() {
		return new EmbeddedDatabaseBuilder().setName("rewards")
				.addScript("/rewards/testdb/schema.sql")
				.addScript("/rewards/testdb/test-data.sql").build();
	}

}
