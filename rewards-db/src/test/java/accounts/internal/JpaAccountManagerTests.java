package accounts.internal;

import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

/**
 * Unit test for the Jpa-based account manager implementation. Tests manager
 * behavior and verifies the Account JPA mapping is correct.
 */
public class JpaAccountManagerTests extends AbstractDatabaseAccountManagerTests {

	@Test
	@Override
	public void testProfile() {
		Assert.assertTrue("JPA expected",
				accountManager instanceof JpaAccountManager);
		log.info("JPA with Hibernate");
	}

	@Override
	protected void setupForTest(DataSource dataSource) throws Exception {
		EntityManagerFactory entityManagerFactory = getEntityManagerFactory();
		EntityManager entityManager = entityManagerFactory
				.createEntityManager();

		JpaAccountManager jpaAccountManager = new JpaAccountManager(
				entityManager);
		accountManager = jpaAccountManager;
		transactionManager = new JpaTransactionManager(entityManagerFactory);
		//jpaAccountManager.setTransactionManager(transactionManager);
	}

	protected JpaVendorAdapter getImplementation() {
		return new HibernateJpaVendorAdapter();
	}

	protected Properties createJpaProperties() {
		Properties properties = new Properties();
		// turn on formatted SQL logging (very useful to verify Jpa is
		// issuing proper SQL)
		properties.setProperty("hibenate.show_sql", "true");
		properties.setProperty("hibernate.format_sql", "true");
		return properties;
	}

	protected final EntityManagerFactory getEntityManagerFactory()
			throws Exception {

		// create a FactoryBean to help create a Jpa SessionFactory
		LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
		factoryBean.setDataSource(dataSource);
		factoryBean.setJpaVendorAdapter(getImplementation());
		factoryBean.setJpaProperties(createJpaProperties());

		// Not using persistence unit or persistence.xml, so need to tell
		// JPA where to find Entities
		factoryBean.setPackagesToScan("rewards.internal");

		// initialize according to the Spring InitializingBean contract
		factoryBean.afterPropertiesSet();
		// get the created session factory
		return (EntityManagerFactory) factoryBean.getObject();
	}

}
