package accounts.web;

import java.net.URI;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.util.UriTemplate;

import common.money.Percentage;

import rewards.internal.account.Account;
import rewards.internal.account.Beneficiary;
import accounts.AccountManager;

/**
 * A controller handling requests for CRUD operations on Accounts and their
 * Beneficiaries.
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
	 * Provide a list of all accounts.
	 */
	@RequestMapping(value = "/accounts", method = RequestMethod.GET)
	public @ResponseBody
	List<Account> accountSummary() {
		return accountManager.getAllAccounts();
	}

	/**
	 * Provide the details of an account with the given id.
	 */
	@RequestMapping(value = "/accounts/{accountId}", method = RequestMethod.GET)
	public @ResponseBody
	Account accountDetails(@PathVariable("accountId") int id) {
		return retrieveAccount(id);
	}

	/**
	 * Creates a new Account, setting its URL as the Location header on the
	 * response.
	 */
	@RequestMapping(value = "/accounts", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public HttpEntity<String> createAccount(@RequestBody Account newAccount,
			@Value("#{request.requestURL}") StringBuffer url) {
		Account account = accountManager.save(newAccount);

		return entityWithLocation(url, account.getEntityId());
	}

	/**
	 * Returns the Beneficiary with the given name for the Account with the
	 * given id.
	 */
	@RequestMapping(value = "/accounts/{accountId}/beneficiaries/{beneficiaryName}", method = RequestMethod.GET)
	public @ResponseBody
	Beneficiary getBeneficiary(@PathVariable("accountId") int accountId,
			@PathVariable("beneficiaryName") String beneficiaryName) {
		return retrieveAccount(accountId).getBeneficiary(beneficiaryName);
	}

	/**
	 * Adds a Beneficiary with the given name to the Account with the given id,
	 * setting its URL as the Location header on the response.
	 */
	@RequestMapping(value = "/accounts/{accountId}/beneficiaries", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public HttpEntity<String> addBeneficiary(
			@PathVariable("accountId") long accountId,
			@RequestBody String beneficiaryName,
			@Value("#{request.requestURL}") StringBuffer url) {
		accountManager.addBeneficiary(accountId, beneficiaryName);
		return entityWithLocation(url, beneficiaryName);
	}

	/**
	 * Removes the Beneficiary with the given name from the Account with the
	 * given id.
	 */
	@RequestMapping(value = "/accounts/{accountId}/beneficiaries/{beneficiaryName}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void removeBeneficiary(@PathVariable("accountId") long accountId,
			@PathVariable("beneficiaryName") String beneficiaryName) {
		Account account = accountManager.getAccount(accountId);
		Beneficiary b = account.getBeneficiary(beneficiaryName);

		// We ought to reset the allocation percentages, but for now we won't
		// bother. If we are removing the only beneficiary or the beneficiary
		// has an allocation of zero we don't need to worry. Otherwise, throw an
		// exception.
		if (account.getBeneficiaries().size() != 1
				|| (!b.getAllocationPercentage().equals(Percentage.zero()))) {
			throw new UnsupportedOperationException(
					"Beneficiary allocation rebalancing not implemented.");
		}

		accountManager.removeBeneficiary(accountId, beneficiaryName,
				new HashMap<String, Percentage>());
	}

	/**
	 * Maps UnsupportedOperationException to a 501 Not Implemented HTTP status code.
	 */
	@ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
	@ExceptionHandler({ UnsupportedOperationException.class })
	public void handleUnabletoReallocate() {
		// just return empty 501
	}

	/**
	 * Maps IllegalArgumentExceptions to a 404 Not Found HTTP status code.
	 */
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler({ IllegalArgumentException.class })
	public void handleNotFound() {
		// just return empty 404
	}

	/**
	 * Maps DataIntegrityViolationException to a 409 Conflict HTTP status code.
	 */
	@ResponseStatus(HttpStatus.CONFLICT)
	@ExceptionHandler({ DataIntegrityViolationException.class })
	public void handleAlreadyExists() {
		// just return empty 409
	}

	/**
	 * Finds the Account with the given id, throwing an IllegalArgumentException
	 * if there is no such Account.
	 */
	private Account retrieveAccount(long accountId)
			throws IllegalArgumentException {
		Account account = accountManager.getAccount(accountId);
		if (account == null) {
			throw new IllegalArgumentException("No such account with id "
					+ accountId);
		}
		return account;
	}

	private HttpEntity<String> entityWithLocation(StringBuffer url,
			Object resourceId) {
		// Configure and return an HttpEntity object - it will be used to build
		// the HttpServletResponse
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(getLocationForChildResource(url, resourceId));
		return new HttpEntity<String>(headers);
	}

	/**
	 * determines URL of child resource based on the full URL of the given
	 * request, appending the path info with the given childIdentifier using a
	 * UriTemplate.
	 */
	private URI getLocationForChildResource(StringBuffer url,
			Object childIdentifier) {
		UriTemplate template = new UriTemplate(url.append("/{childId}")
				.toString());
		return template.expand(childIdentifier);
	}

}