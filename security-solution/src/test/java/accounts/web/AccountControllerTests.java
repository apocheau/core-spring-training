package accounts.web;

import org.junit.Before;

import accounts.AccountManager;
import accounts.internal.StubAccountManager;

/**
 * A JUnit test case testing the AccountController.
 */
public class AccountControllerTests extends AbstractAccountControllerTests {

	/**
	 * Setup an {@link AccountController} using a stub {@link AccountManager}
	 * 
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		manager = new StubAccountManager();
		controller = new AccountController(manager);
	}

	@Override
	protected int getNumAccountsExpected() {
		// The number of accounts defined in the stub
		return StubAccountManager.NUM_ACCOUNTS_IN_STUB;
	}
}
