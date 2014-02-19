package accounts.web;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
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
 * A controller handling requests for CRUD operations on Accounts and their Beneficiaries.
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
	// TODO 01: complete this method by adding the appropriate annotations to respond
	//          to a GET to /accounts and return a List<Account> to be converted
	public List<Account> accountSummary() {
		return accountManager.getAllAccounts();
	}
	
	/**
	 * Provide the details of an account with the given id.
	 */
	// TODO 03: complete this method by adding the appropriate annotations to respond
	//          to a GET to /accounts/{accountId} and return an Account to be converted
	public Account accountDetails(int id) {
		return retrieveAccount(id);
	}
	
	/**
	 * Creates a new Account, setting its URL as the Location header on the response.
	 */
	// TODO 05: complete this method by adding the appropriate annotations to respond to a
	//          POST to /accounts containing a marshalled Account with a 201 Created status
	public void createAccount(Account newAccount, 
							  HttpServletRequest request, 
							  HttpServletResponse response) {
		Account account = accountManager.save(newAccount);
		// TODO 06: set the Location header on the Response to the location of the created account
	}

	/**
	 * Returns the Beneficiary with the given name for the Account with the given id.   
	 */
	@RequestMapping(value="/accounts/{accountId}/beneficiaries/{beneficiaryName}", method=RequestMethod.GET)
	public @ResponseBody Beneficiary getBeneficiary(@PathVariable("accountId") int accountId, 
			                                        @PathVariable("beneficiaryName") String beneficiaryName) {
		return retrieveAccount(accountId).getBeneficiary(beneficiaryName);
	}

	/**
	 * Adds a Beneficiary with the given name to the Account with the given id,
	 * setting its URL as the Location header on the response. 
	 */
	// TODO 09: complete this method by adding the appropriate annotations to respond to a
	//          POST to /accounts/{accountId}/beneficiaries containing a beneficiary name
	//          with a 201 Created status
	public void addBeneficiary(long accountId, 
							   String beneficiaryName,
							   HttpServletRequest request, 
							   HttpServletResponse response) {
		accountManager.addBeneficiary(accountId, beneficiaryName);
		// TODO 10: set the Location header on the Response to the location of the created beneficiary
	}
	
	/**
	 * Removes the Beneficiary with the given name from the Account with the given id. 
	 */
	// TODO 11: complete this method by adding the appropriate annotations to respond to a
	//          DELETE to /accounts/{accountId}/beneficiaries/{beneficiaryName}
	//          with a 204 No Content status
	public void removeBeneficiary(long accountId, 
			                      String beneficiaryName) {
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
	 * Maps IllegalArgumentExceptions to a 404 Not Found HTTP status code.
	 */
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler({IllegalArgumentException.class})
	public void handleNotFound() {
		// just return empty 404
	}

	// TODO 16 (bonus): add a new exception-handling method that maps
	// DataIntegrityViolationExceptions to a 409 Conflict status code.

	
	/**
	 * Finds the Account with the given id, throwing an IllegalArgumentException
	 * if there is no such Account. 
	 */
	private Account retrieveAccount(long accountId) throws IllegalArgumentException {
		Account account = accountManager.getAccount(accountId);
		if (account == null) {
			throw new IllegalArgumentException("No such account with id " + accountId);
		}
		return account;
	}

	/**
	 * determines URL of child resource based on the full URL of the given request,
	 * appending the path info with the given childIdentifier using a UriTemplate.
	 */ 
	private String getLocationForChildResource(HttpServletRequest request, Object childIdentifier) {
		StringBuffer url = request.getRequestURL();
		UriTemplate template = new UriTemplate(url.append("/{childId}").toString());
		return template.expand(childIdentifier).toASCIIString();
	}

}