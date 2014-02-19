package accounts.web;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.MapBindingResult;
import org.springframework.web.bind.support.SimpleSessionStatus;

import rewards.internal.account.Account;
import accounts.AccountManager;

/**
 * A JUnit test case testing the AccountController.
 */
public abstract class AbstractAccountControllerTests {

	private static final long VALID_ACCOUNT_ID = 0L;
	private static final long ILLEGAL_ACCOUNT_ID = 10101;

	@Autowired
	protected AccountController controller;

	@Autowired
	protected AccountManager manager;

	@Test
	public void getAccountDetails() {
		ExtendedModelMap model = new ExtendedModelMap();
		controller.getAccountDetails(VALID_ACCOUNT_ID, model);
		Account account = (Account) model.get("account");
		assertNotNull(account);
		assertEquals(Long.valueOf(0), account.getEntityId());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void getAccountList() {
		ExtendedModelMap model = new ExtendedModelMap();
		controller.getAccountList(model);
		List<Account> accounts = (List<Account>) model.get("accounts");
		assertNotNull(accounts);
		assertEquals(getNumAccountsExpected(), accounts.size());
		assertEquals(Long.valueOf(0), accounts.get(0).getEntityId());
	}

	@Test(expected = ObjectRetrievalFailureException.class)
	public void invalidId() throws Exception {
		ExtendedModelMap model = new ExtendedModelMap();
		controller.getAccountDetails(ILLEGAL_ACCOUNT_ID, model);
	}

	@Test
	public void validateAllValid() {
		Account account = new Account("1", "Ben");
		Errors errors = new BindException(account, "account");
		controller.validateAccount(account, errors);
		assertEquals("No errors should be registered", 0,
				errors.getErrorCount());
	}

	@Test
	public void validateInvalidName() {
		Account account = new Account("1", "");
		Errors errors = new BindException(account, "account");
		controller.validateAccount(account, errors);
		assertEquals("One error should be registered", 1,
				errors.getErrorCount());
		FieldError error = errors.getFieldError("name");
		assertNotNull("Should have an error registred for the name field",
				error);
		assertEquals("Should have registered an empty value error",
				"empty.value", error.getCode());
	}

	@Test
	public void validateInvalidNumber() {
		Account account = new Account("", "Ben");
		Errors errors = new BindException(account, "account");
		controller.validateAccount(account, errors);
		assertEquals("One error should be registered", 1,
				errors.getErrorCount());
		FieldError error = errors.getFieldError("number");
		assertNotNull("Should have an error registred for the number field",
				error);
		assertEquals("Should have registered an empty value error",
				"empty.value", error.getCode());
	}

	@Test
	public void validateAllInvalid() {
		Account account = new Account(null, null);
		Errors errors = new BindException(account, "account");
		controller.validateAccount(account, errors);
		assertEquals("Two errors should be registered", 2,
				errors.getErrorCount());
	}

	@Test
	public void editAccount() throws Exception {
		ExtendedModelMap model = new ExtendedModelMap();
		controller.getEditAccount(VALID_ACCOUNT_ID, model);
		Account account = (Account) model.get("account");
		assertNotNull(account);
		assertEquals(Long.valueOf(0), account.getEntityId());
	}

	@Test
	public void successfulPost() throws Exception {
		ExtendedModelMap model = new ExtendedModelMap();
		controller.getEditAccount(VALID_ACCOUNT_ID, model);
		Account account = (Account) model.get("account");
		account.setName("Ben");
		account.setNumber("987654321");
		BindingResult br = new MapBindingResult(model, "account");
		String viewName = controller.postEditAccount(account, br, new SimpleSessionStatus());
		Account modifiedAccount = manager.getAccount(VALID_ACCOUNT_ID);
		assertEquals("Object name has not been updated by post", "Ben",
				modifiedAccount.getName());
		assertEquals("Object number has not been updated by post", "987654321",
				modifiedAccount.getNumber());
		assertEquals("Post has not redirected to the correct URL",
				"redirect:/accounts/accountDetails?entityId=0", viewName);
	}

	@Test
	public void unsuccessfulPost() throws Exception {
		ExtendedModelMap model = new ExtendedModelMap();
		controller.getEditAccount(VALID_ACCOUNT_ID, model);
		Account account = (Account) model.get("account");
		account.setName("");
		account.setNumber("");
		BindingResult br = new MapBindingResult(model, "account");
		String viewName = controller.postEditAccount(account, br, new SimpleSessionStatus());
		assertEquals("Invalid Post has not returned to correct view",
				"editAccount", viewName);
	}

	/**
	 * How many accounts are defined for use in the test?
	 * 
	 * @return Number of accounts available.
	 */
	protected abstract int getNumAccountsExpected();

}
