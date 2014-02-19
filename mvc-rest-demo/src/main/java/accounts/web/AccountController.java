package accounts.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import accounts.AccountManager;

/**
 * A Spring MVC @Controller controller handling requests for both the
 * account summary and the account details pages.   
 */
@Controller
@RequestMapping("/accounts")
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
	 * <p>Provide a model with a list of all accounts for the summary page.</p>
	 * 
	 * @param model the "implicit" model created by Spring MVC
	 */
	@RequestMapping(method=RequestMethod.GET)
	public String accountSummary(Model model) {
		model.addAttribute("accounts", accountManager.getAllAccounts());
		return "accountSummary";
	}
	
	/**
	 * <p>Provide a model with an account for the account detail page.</p>
	 * 
	 * @param id the id of the account
	 * @param model the "implicit" model created by Spring MVC
	 */
	@RequestMapping(value="{accountId}", method=RequestMethod.GET)
	public String accountDetails(@PathVariable("accountId") int id, Model model) {
		model.addAttribute("account", accountManager.getAccount(id));
		return "accountDetails";
	}
	
	@RequestMapping(value="{accountId}/beneficiaries", method=RequestMethod.POST)
	public String addBeneficiary(@PathVariable("accountId") int accountId, 
			                      @RequestParam String beneficiaryName) {
		accountManager.addBeneficiary(accountId, beneficiaryName);
		return "redirect:/accounts/" + accountId;
	}
	
	@RequestMapping(value="{accountId}/beneficiaries/{beneficiaryName}", method=RequestMethod.DELETE)
	public String removeBeneficiary(@PathVariable("accountId") int accountId, 
			                         @PathVariable("beneficiaryName") String beneficiaryName) {
		accountManager.removeBeneficiary(accountId, beneficiaryName);
		return "redirect:/accounts/" + accountId;
	}
	
}
