package accounts.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import accounts.AccountManager;

/**
 * A Spring MVC @Controller controller handling requests for both the
 * account summary and the account details pages.   
 */
@Controller
public class AccountController {
	
	private AccountManager accountManager;
	
	/**
	 * Creates a new AccountController with a given account manager.
	 */
	@Autowired 
	public AccountController(AccountManager accountManager) {
		this.accountManager = accountManager;
	}
	
	/**
	 * <p>Provide a model with an account for the account detail page.</p>
	 * 
	 * @param id the id of the account
	 * @param model the "implicit" model created by Spring MVC
	 */
	@RequestMapping(value="/accounts/{accountId}", method=RequestMethod.GET)
	public String accountDetails(@PathVariable("accountId") int id, Model model) {
		model.addAttribute("account", accountManager.getAccount(id));
		return "accountDetails";
	}
	
	/**
	 * <p>Provide a model with a list of all accounts for the summary page.</p>
	 * 
	 * @param model the "implicit" model created by Spring MVC
	 */
	@RequestMapping(value="/accounts", method=RequestMethod.GET)
	public String accountSummary(Model model) {
		model.addAttribute("accounts", accountManager.getAllAccounts());
		return "accountSummary";
	}
	
	// TODO 01: add an addBeneficiary method for POSTs to "/accounts/{accountId}/beneficiaries"
	// which adds a beneficiary based on the form parameter and returns a redirect to ../{accountId} 

	
	// TODO 02: add a removeBeneficiary method for DELETEs of "/accounts/{accountId}/beneficiaries/{beneficiaryName}"
	// which removed a beneficiary and returns a redirect to ../../{accountId} 
	
}
