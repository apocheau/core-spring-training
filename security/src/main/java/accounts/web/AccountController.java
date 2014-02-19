package accounts.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

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
@SessionAttributes("account")
public class AccountController {
	private AccountManager accountManager;

	@Autowired
	public AccountController(AccountManager accountManager) {
		this.accountManager = accountManager;
	}

	@RequestMapping("/accountList")
	public String getAccountList(Model model) {
		model.addAttribute("accounts", accountManager.getAllAccounts());
		return "accountList";
	}

	@RequestMapping("/accountDetails")
	public String getAccountDetails(Long entityId, Model model) {
		model.addAttribute("account", accountManager.getAccount(entityId));
		return "accountDetails";
	}
	
	@RequestMapping("/hidden")
	public String hidden() {
		return "hidden";
	}

	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	// The next 4 methods enable form handling so that beneficiaries can
	// be edited. This, and much more, is covered by the Spring-Web course.
	//
	// You do NOT need to understand the following to do this lab.
	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.setRequiredFields(new String[] { "number", "name" });
	}

	@RequestMapping(method = RequestMethod.GET, value = "/editAccount")
	public void getEditAccount(Long entityId, Model model) {
		model.addAttribute("account", accountManager.getAccount(entityId));
	}

	@RequestMapping(method = RequestMethod.POST, value = "/editAccount")
	public String postEditAccount(Account account, BindingResult bindingResult, SessionStatus status) {
		validateAccount(account, bindingResult);
		if (bindingResult.hasErrors()) {
			return "editAccount";
		}
		accountManager.update(account);
		status.setComplete();
		return "redirect:/accounts/accountDetails?entityId="
				+ account.getEntityId();
	}

	public void validateAccount(Account account, Errors errors) {
		if (!StringUtils.hasText(account.getNumber())) {
			errors.rejectValue("number", "empty.value");
		}
		if (!StringUtils.hasText(account.getName())) {
			errors.rejectValue("name", "empty.value");
		}
	}

}
