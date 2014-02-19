package accounts.web;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.ExtendedModelMap;

import rewards.internal.account.Account;
import accounts.internal.StubAccountManager;

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
		String viewName = controller.accountDetails(0, model);
		Account account = (Account) model.get("account");
		assertEquals("accountDetails", viewName);
		assertNotNull(account);
		assertEquals(Long.valueOf(0), account.getEntityId());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testHandleListRequest() {
		ExtendedModelMap model = new ExtendedModelMap();
		String viewName = controller.accountList(model);
		List<Account> accounts = (List<Account>) model.get("accounts");
		assertEquals("accountList", viewName);
		assertNotNull(accounts);
		assertEquals(1, accounts.size());
		assertEquals(Long.valueOf(0), accounts.get(0).getEntityId());
	}
}
