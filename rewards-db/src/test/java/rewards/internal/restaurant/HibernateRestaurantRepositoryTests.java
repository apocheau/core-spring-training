package rewards.internal.restaurant;

import javax.sql.DataSource;

import junit.framework.Assert;

import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * Manually configured integration test for the Hibernate based restaurant
 * repository implementation. Tests repository behavior and verifies the
 * Restaurant Hibernate mapping is correct.
 */
public class HibernateRestaurantRepositoryTests extends
		AbstractRestaurantRepositoryTests {

	private PlatformTransactionManager transactionManager;

	private TransactionStatus status;

	@Before
	public void setUp() throws Exception {
		SessionFactory sessionFactory = createTestSessionFactory();
		restaurantRepository = new HibernateRestaurantRepository(sessionFactory);
		transactionManager = new HibernateTransactionManager(sessionFactory);
		status = transactionManager
				.getTransaction(new DefaultTransactionDefinition());
	}

	@After
	public void tearDown() throws Exception {
		// rollback the transaction to avoid corrupting other tests
		if (transactionManager != null)
			transactionManager.rollback(status);
	}

	@Test
	@Override
	public void testProfile() {
		Assert.assertTrue("Hibernate expected",
				restaurantRepository instanceof HibernateRestaurantRepository);
	}

	private SessionFactory createTestSessionFactory() throws Exception {
		// simulate the Spring bean initialization lifecycle
		LocalSessionFactoryBean factoryBean = new LocalSessionFactoryBean();
		factoryBean.setDataSource(createTestDataSource());
		Class<?>[] annotatedClasses = new Class<?>[] { Restaurant.class };
		factoryBean.setAnnotatedClasses(annotatedClasses);
		factoryBean.afterPropertiesSet();
		return (SessionFactory) factoryBean.getObject();
	}

	private DataSource createTestDataSource() {
		return new EmbeddedDatabaseBuilder().setName("rewards")
				.addScript("/rewards/testdb/schema.sql")
				.addScript("/rewards/testdb/test-data.sql").build();
	}

}
