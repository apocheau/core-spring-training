package rewards.internal.restaurant;

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
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import common.money.Percentage;

/**
 * Unit test for the JPA-based RestaurantRepository implementation. Tests application data access behavior to
 * verify the Restaurant JPA mapping is correct.
 */
public class JpaRestaurantRepositoryTests {

	private JpaRestaurantRepository repository;
	
	private EntityManagerFactory entityManagerFactory;
	
	private EntityManager entityManager;
	
	@Before
	public void setupAccountRepository(){
		
		entityManagerFactory = createEntityManagerFactory();
		entityManager = entityManagerFactory.createEntityManager();
		
		repository = new JpaRestaurantRepository();
		repository.setEntityManager(entityManager);
	}
	
	@Test
	public void testFindRestaurantByMerchantNumber() {
		Restaurant restaurant = repository.findByMerchantNumber("1234567890");
		assertNotNull("the restaurant should never be null", restaurant);
		assertEquals("the merchant number is wrong", "1234567890", restaurant.getNumber());
		assertEquals("the name is wrong", "AppleBees", restaurant.getName());
		assertEquals("the benefitPercentage is wrong", Percentage.valueOf("8%"), restaurant.getBenefitPercentage());
		assertEquals("the benefit availability policy is wrong", AlwaysAvailable.INSTANCE, restaurant
				.getBenefitAvailabilityPolicy());
	}

	@Test
	public void testNonParticipatingRestaurant() {
		Restaurant restaurant = repository.findByMerchantNumber("1234567891");
		assertNotNull("the restaurant should never be null", restaurant);
		assertEquals("the merchant number is wrong", "1234567891", restaurant.getNumber());
		assertEquals("the name is wrong", "Barnabees", restaurant.getName());
		assertEquals("the benefitPercentage is wrong", Percentage.valueOf("100%"), restaurant.getBenefitPercentage());
		assertEquals("the benefit availability policy is wrong", NeverAvailable.INSTANCE, restaurant
				.getBenefitAvailabilityPolicy());
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
