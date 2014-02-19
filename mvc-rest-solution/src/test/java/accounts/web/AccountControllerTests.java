package accounts.web;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.ExtendedModelMap;

import accounts.Account;

/**
 * A JUnit test case testing the AccountController. 
 */
public class AccountControllerTests {

	private AccountController controller;

	@Before
	public void setUp() throws Exception {
		controller = new AccountController(new StubAccountManager());
	}

	@Test
	public void testHandleDetailsRequest() {
		ExtendedModelMap model = new ExtendedModelMap();
		controller.accountDetails(0, model);
		Account account = (Account) model.get("account");
		assertNotNull(account);
		assertEquals(Integer.valueOf(0), account.getEntityId());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testHandleSummaryRequest() {
		ExtendedModelMap model = new ExtendedModelMap();
		controller.accountSummary(model);
		List<Account> accounts = (List<Account>) model.get("accounts");
		assertNotNull(accounts);
		assertEquals(1, accounts.size());
		assertEquals(Integer.valueOf(0), accounts.get(0).getEntityId());
	}
}
