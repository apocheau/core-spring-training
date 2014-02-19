package rewards.internal.account;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;

import common.money.MonetaryAmount;
import common.money.Percentage;

/**
 * Unit test for the JPA-based AccountRepository implementation. Tests application data access behavior to verify
 * the Account JPA mapping is correct.
 */
public class JpaAccountRepositoryTests {

	private JpaAccountRepository repository;
	
	private EntityManagerFactory entityManagerFactory;
	
	private EntityManager entityManager;
	
	@Before
	public void setupAccountRepository(){
		
		entityManagerFactory = createEntityManagerFactory();
		entityManager = entityManagerFactory.createEntityManager();
		
		repository = new JpaAccountRepository();
		repository.setEntityManager(entityManager);
	}

	@Test
	public void testFindByCreditCard() {
		Account account = repository.findByCreditCard("1234123412341234");
		// assert the returned account contains what you expect given the state of the database
		// and the Account Hibernate mapping configuration
		assertNotNull("account should never be null", account);
		assertEquals("wrong entity id", Long.valueOf(0), account.getEntityId());
		assertEquals("wrong account number", "123456789", account.getNumber());
		assertEquals("wrong name", "Keith and Keri Donald", account.getName());
		assertEquals("wrong beneficiary collection size", 2, account.getBeneficiaries().size());

		Beneficiary b1 = account.getBeneficiary("Annabelle");
		assertNotNull("Annabelle should be a beneficiary", b1);
		assertEquals("wrong savings", MonetaryAmount.valueOf("0.00"), b1.getSavings());
		assertEquals("wrong allocation percentage", Percentage.valueOf("50%"), b1.getAllocationPercentage());

		Beneficiary b2 = account.getBeneficiary("Corgan");
		assertNotNull("Corgan should be a beneficiary", b2);
		assertEquals("wrong savings", MonetaryAmount.valueOf("0.00"), b2.getSavings());
		assertEquals("wrong allocation percentage", Percentage.valueOf("50%"), b2.getAllocationPercentage());
	}
	
	@After
	public void shutdownAccountRepository(){
		if(entityManager != null){
			entityManager.close();
		}
		
		if(entityManagerFactory != null){
			entityManagerFactory.close();
		}
	}
	
	/**
	 * We are not using Spring in this unit test, so we have to setup our
	 * embedded database manually.
	 * 
	 * @return The factory.
	 */
	private EntityManagerFactory createEntityManagerFactory(){
		LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
		entityManagerFactoryBean.setDataSource(createTestDataSource());
		
		HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
		jpaVendorAdapter.setDatabase(Database.HSQL);
		jpaVendorAdapter.setShowSql(true);
		
		entityManagerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter);
		
		Map<String, String> jpaProperties = new HashMap<String, String>();
		jpaProperties.put("hibernate.format_sql", "true");
		entityManagerFactoryBean.setJpaPropertyMap(jpaProperties);
		entityManagerFactoryBean.afterPropertiesSet();
		return entityManagerFactoryBean.getObject();
	}
	
	/**
	 * We are not using Spring in this unit test, so we have to setup our
	 * embedded database manually.
	 * 
	 * @return The factory.
	 */
	private DataSource createTestDataSource() {
		return new EmbeddedDatabaseBuilder()
			.setName("rewards")
			.addScript("/rewards/testdb/schema.sql")
			.addScript("/rewards/testdb/test-data.sql")
			.build();
	}
}
