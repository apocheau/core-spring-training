package accounts.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import accounts.Account;
import accounts.AccountManager;
import accounts.Beneficiary;

import common.money.MonetaryAmount;
import common.money.Percentage;

/**
 * system test for the Hibernate-based account manager implementation. Tests application behavior to verify the Account
 * Hibernate mapping is correct.
 */
@ContextConfiguration("file:src/main/webapp/WEB-INF/app-config.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class HibernateAccountManagerTests {

	@Autowired AccountManager accountManager;

	@Test
	public void getAllAccounts() {
		List<Account> accounts = accountManager.getAllAccounts();
		assertEquals("Wrong number of accounts", 21, accounts.size());
	}

	@Test
	public void getAccount() {
		Account account = accountManager.getAccount(Integer.valueOf(0));
		// assert the returned account contains what you expect given the state
		// of the database
		assertNotNull("account should never be null", account);
		assertEquals("wrong entity id", Integer.valueOf(0), account.getEntityId());
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

	@Test
	public void updateAccountBeneficiaries() {
		Map<String, Percentage> allocationPercentages = new HashMap<String, Percentage>();
		allocationPercentages.put("Annabelle", Percentage.valueOf("25%"));
		allocationPercentages.put("Corgan", Percentage.valueOf("75%"));
		accountManager.updateBeneficiaryAllocationPercentages(Integer.valueOf(0), allocationPercentages);
		Account account = accountManager.getAccount(Integer.valueOf(0));
		assertEquals("Invalid adjusted percentage", Percentage.valueOf("25%"), 
				account.getBeneficiary("Annabelle").getAllocationPercentage());
		assertEquals("Invalid adjusted percentage", Percentage.valueOf("75%"), 
				account.getBeneficiary("Corgan").getAllocationPercentage());
	}

	@Test
	public void addBeneficiary() {
		accountManager.addBeneficiary(Integer.valueOf(0), "Ben");
		Account account = accountManager.getAccount(Integer.valueOf(0));
		assertEquals("Should only have three beneficiaries", 3, account.getBeneficiaries().size());
	}

	@Test
	public void removeBeneficiary() {
		Map<String, Percentage> allocationPercentages = new HashMap<String, Percentage>();
		allocationPercentages.put("Corgan", Percentage.oneHundred());
		accountManager.removeBeneficiary(Integer.valueOf(0), "Annabelle", allocationPercentages);
		Account account = accountManager.getAccount(Integer.valueOf(0));
		assertEquals("Should only have one beneficiary", 1, account.getBeneficiaries().size());
		assertEquals("Corgan should now have 100% allocation", Percentage.oneHundred(), 
				account.getBeneficiary("Corgan").getAllocationPercentage());
	}

}
