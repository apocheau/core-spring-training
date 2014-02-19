package accounts.internal;

import java.util.Properties;

import javax.sql.DataSource;

import junit.framework.Assert;

import org.hibernate.SessionFactory;
import org.hibernate.engine.transaction.jta.platform.spi.JtaPlatform;
import org.junit.Test;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;

import rewards.internal.account.Account;
import rewards.internal.account.Beneficiary;

/**
 * Unit test for the Hibernate-based account manager implementation. Tests
 * manager behavior and verifies the Account Hibernate mapping is correct.
 */
public class HibernateAccountManagerTests extends
		AbstractDatabaseAccountManagerTests {

	@Test
	@Override
	public void testProfile() {
		Assert.assertTrue("Hibernate expected",
				accountManager instanceof HibernateAccountManager);
	}

	protected void setupForTest(DataSource dataSource) throws Exception {
		SessionFactory sessionFactory = getTestSessionFactory(dataSource);
		accountManager = new HibernateAccountManager(sessionFactory);
		transactionManager = new HibernateTransactionManager(sessionFactory);
	}

	private SessionFactory getTestSessionFactory(DataSource dataSource)
			throws Exception {

		// create a FactoryBean to help create a Hibernate SessionFactory
		LocalSessionFactoryBean factoryBean = new LocalSessionFactoryBean();
		factoryBean.setDataSource(dataSource);
		factoryBean.setAnnotatedClasses(new Class[] { Account.class,
				Beneficiary.class });
		factoryBean.setHibernateProperties(createHibernateProperties());
		// initialize according to the Spring InitializingBean contract
		factoryBean.afterPropertiesSet();
		// get the created session factory
		return (SessionFactory) factoryBean.getObject();
	}

	protected Properties createHibernateProperties() {
		Properties properties = new Properties();
		// turn on formatted SQL logging (very useful to verify Hibernate is
		// issuing proper SQL)
		properties.setProperty("hibernate.show_sql", "true");
		properties.setProperty("hibernate.format_sql", "true");
		return properties;
	}

}
