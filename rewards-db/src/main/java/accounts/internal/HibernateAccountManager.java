package accounts.internal;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.transaction.annotation.Transactional;

import rewards.internal.account.Account;
import accounts.AccountManager;

import common.money.Percentage;

/**
 * An account manager that uses Hibernate to find accounts.
 */
public class HibernateAccountManager implements AccountManager {

	public static final String INFO = "Hibernate";

	private SessionFactory sessionFactory;

	/**
	 * Creates a new Hibernate account manager.
	 * 
	 * @param sessionFactory
	 *            the Hibernate session factory
	 */
	@Autowired
	public HibernateAccountManager(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
		Logger.getLogger(HibernateAccountManager.class).info(
				"Created HibernateAccountManager");
	}

	@Override
	public String getInfo() {
		return INFO;
	}

	@Override
	@Transactional(readOnly = true)
	@SuppressWarnings("unchecked")
	public List<Account> getAllAccounts() {
		return getCurrentSession().createQuery("from Account").list();
	}

	@Override
	@Transactional(readOnly = true)
	public Account getAccount(Long id) {
		try {
			Account account = (Account) getCurrentSession().load(Account.class,
					id);

			// Force beneficiaries to load too - avoid Hibernate lazy loading error
			account.getBeneficiaries().size();

			return account;
		} catch (ObjectNotFoundException e) {
			throw new ObjectRetrievalFailureException(Account.class, id);
		}
	}

	@Override
	@Transactional
	@SuppressWarnings("unused")
	public Account save(Account account) {
		Long accountId = (Long)getCurrentSession().save(account);
		return account;
	}

	@Override
	@Transactional
	public void update(Account account) {
		getCurrentSession().update(account);
	}

	@Override
	@Transactional
	public void updateBeneficiaryAllocationPercentages(Long accountId,
			Map<String, Percentage> allocationPercentages) {
		Account account = getAccount(accountId);
		for (Entry<String, Percentage> entry : allocationPercentages.entrySet()) {
			account.getBeneficiary(entry.getKey()).setAllocationPercentage(
					entry.getValue());
		}
	}

	@Override
	@Transactional
	public void addBeneficiary(Long accountId, String beneficiaryName) {
		getAccount(accountId)
				.addBeneficiary(beneficiaryName, Percentage.zero());
	}

	@Override
	@Transactional
	public void removeBeneficiary(Long accountId, String beneficiaryName,
			Map<String, Percentage> allocationPercentages) {
		getAccount(accountId).removeBeneficiary(beneficiaryName);
		updateBeneficiaryAllocationPercentages(accountId, allocationPercentages);
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