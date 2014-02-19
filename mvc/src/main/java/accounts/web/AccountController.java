package accounts.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import rewards.internal.account.Account;
import rewards.internal.account.Beneficiary;

import accounts.AccountManager;

/**
 * A Spring MVC @Controller controller handling requests to view and modify
 * Account information.
 * <p>
 * Note that all the Account application classes are imported from the
 * <tt>rewards-db</tt> project:
 * <ul>
 * <li>Domain objects: {@link Account} and {@link Beneficiary}</li>
 * <li>Service layer: {@link AccountManager} interface and its implementations</li>
 * <li>No repository layer is being used - the account-manager does everything</li>
 */
@Controller
public class AccountController {

	private AccountManager accountManager;

	/**
	 * Creates a new AccountController with a given account manager.
	 */
	// TODO-05: Enable autowiring of the AccountManager dependency
	//          After completing this task, re-run your web application and see if everything still works.
	
	
	public AccountController(AccountManager accountManager) {
		this.accountManager = accountManager;
	}

	// TODO-01: Implement the /accountDetails request handling method

	/**
	 * <p>
	 * Provide a model with a list of all accounts for the account List page.
	 * </p>
	 * 
	 * @param model
	 *            the "implicit" model created by Spring MVC
	 */
	@RequestMapping("/accountList")
	public String accountList(Model model) {
		model.addAttribute("accounts", accountManager.getAllAccounts());
		return "/WEB-INF/views/accountList.jsp";
	}
}
