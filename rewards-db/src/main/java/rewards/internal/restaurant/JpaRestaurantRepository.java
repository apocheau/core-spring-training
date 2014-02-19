package rewards.internal.restaurant;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

/**
 * Loads restaurants from a data source using the JPA.
 */
public class JpaRestaurantRepository implements RestaurantRepository {

	public static final String INFO = "JPA";

	private EntityManager entityManager;

	protected JpaRestaurantRepository() {
		Logger.getLogger(JpaRestaurantRepository.class).info("Created JpaRestaurantRepository");
	}

	/**
	 * Creates a new JPA account manager.
	 * 
	 * @param entityManager
	 *            the JPA entity manager
	 */
	public JpaRestaurantRepository(EntityManager entityManager) {
		this.entityManager = entityManager;
		Logger.getLogger(JpaRestaurantRepository.class).info("Created JpaRestaurantRepository");
	}

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public String getInfo() {
		return INFO;
	}

	public Restaurant findByMerchantNumber(String merchantNumber) {
		return (Restaurant) entityManager.createQuery("from Restaurant r where r.number = :merchantNumber")
				.setParameter("merchantNumber", merchantNumber).getSingleResult();
	}

}
