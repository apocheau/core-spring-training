package rewards.internal.account;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 * Finds account objects using the Hibernate API.
 */
public class HibernateAccountRepository implements AccountRepository {

	private SessionFactory sessionFactory;

	/**
	 * Creates an new hibernate-based account repository.
	 * 
	 * @param sessionFactory
	 *            the Hibernate session factory required to obtain sessions
	 */
	public HibernateAccountRepository(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}	

	/**
	 * {@inheritDoc}
	 */
	public Account findByCreditCard(String creditCardNumber) {
		Query query = getCurrentSession().createQuery(
				"select a from Account a join a.creditCards c where c.number = ?");
		query.setString(0, creditCardNumber);
		return (Account) query.uniqueResult();
	}

	/**
	 * Returns the session associated with the ongoing reward transaction.
	 * 
	 * @return the transactional session
	 */
	protected Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}
}