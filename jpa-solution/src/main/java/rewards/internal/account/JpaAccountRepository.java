package rewards.internal.account;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;

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
		return (Account) entityManager
				.createQuery(
						"select a from Account a  where a.creditCardNumber = ?")
				.setParameter(1, creditCardNumber).getSingleResult();
	}

	/**
	 * Alternative version of {@link #findByCreditCard} using the type-safe
	 * Criteria Query API. For simplicity, the {@link Account_} meta-model class
	 * has been provided for you manually. Normally you would automate this as
	 * part of the build.
	 */
	public Account cqByCreditCard(String creditCardNumber) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Account> cq = builder.createQuery(Account.class);
		Predicate condition = builder.equal(
				cq.from(Account.class).get(Account_.creditCardNumber),
				creditCardNumber);
		cq.where(condition);

		return entityManager.createQuery(cq).getSingleResult();
	}
}
