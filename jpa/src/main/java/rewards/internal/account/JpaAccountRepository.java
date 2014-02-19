package rewards.internal.account;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

/**
 * Finds account objects using the JPA API.
 */
@Repository
public class JpaAccountRepository implements AccountRepository {

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
	public Account findByCreditCard(String creditCardNumber) {
		// TODO-06: Search for an Account entity with the given credit card number.
		// Run the corresponding unit test JpaAccountRepositoryTests and make sure it is successful.

		
		throw new UnsupportedOperationException("Not implemented yet");
	}
}
