package rewards.internal.restaurant;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

/**
 * Finds restaurants using the JPA API.
 */
@Repository
public class JpaRestaurantRepository implements RestaurantRepository {

	private EntityManager entityManager;

	/**
	 * Set the entity manager. Assumes automatic dependency injection via the
	 * JPA @PersistenceContext annotation. However this method may still be
	 * called manually in a unit-test.
	 * 
	 * @param entityManager
	 */
	@PersistenceContext
	void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	/**
	 * {@inheritDoc}
	 */
	public Restaurant findByMerchantNumber(String merchantNumber) {
		// TODO-08: Search for a Restaurant entity with the given merchant number
		//			Run the corresponding unit test JpaRestaurantRepositoryTests and make sure it is successful.
		throw new UnsupportedOperationException("Not implemented yet!");
	}
}
