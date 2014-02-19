package accounts.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Random;

import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import rewards.internal.account.Account;
import rewards.internal.account.Beneficiary;

import common.money.Percentage;

public class AccountClientTests {
	
	/**
	 * server URL ending with the servlet mapping on which the application can be reached.
	 */
	private static final String BASE_URL = "http://localhost:8080/rest-ws-start/app";
	
	private RestTemplate restTemplate = new RestTemplate();
	private Random random = new Random();
	
	@Test
	public void listAccounts() {
		// TODO 02: use the restTemplate to retrieve an array containing all Account instances
		Account[] accounts = null;
		
		assertNotNull(accounts);
		assertTrue(accounts.length >= 21);
		assertEquals("Keith and Keri Donald", accounts[0].getName());
		assertEquals(2, accounts[0].getBeneficiaries().size());
		assertEquals(Percentage.valueOf("50%"), accounts[0].getBeneficiary("Annabelle").getAllocationPercentage());
	}
	
	@Test
	public void getAccount() {
		// TODO 04: use the restTemplate to retrieve the Account with id 0 using a URI template
		Account account = null; 
		
		assertNotNull(account);
		assertEquals("Keith and Keri Donald", account.getName());
		assertEquals(2, account.getBeneficiaries().size());
		assertEquals(Percentage.valueOf("50%"), account.getBeneficiary("Annabelle").getAllocationPercentage());
	}
	
	@Test
	public void createAccount() {
		// use a unique number to avoid conflicts
		String number = String.format("12345%4d", random.nextInt(10000));
		Account account = new Account(number, "John Doe");
		account.addBeneficiary("Jane Doe");
		
		// TODO 07: create a new Account by POSTing to the right URL and store its location in a variable

		// TODO 08: retrieve the Account you just created from the location that was returned
		Account retrievedAccount = null;
		
		assertNotNull(retrievedAccount);
		assertNotNull(retrievedAccount.getEntityId());
		assertEquals(account, retrievedAccount);
	}
	
	@Test
	public void addAndDeleteBeneficiary() {
		// perform both add and delete to avoid issues with side effects
		
		// TODO 12: create a new Beneficiary called "David" for the account with id 0 using a URI Template
		// and store its location in a variable
		
		// TODO 13: retrieve the Beneficiary you just created from the location that was returned
		Beneficiary newBeneficiary = null;
		
		assertNotNull(newBeneficiary);
		assertEquals("David", newBeneficiary.getName());
		
		// TODO 14: delete the new Beneficiary
		
		try {
			// TODO 15: try to retrieve the new Beneficiary again: this should give a 404 Not Found 
			
			fail("Should have received 404 Not Found after deleting beneficiary");
		} catch (HttpClientErrorException e) {
			assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
		}
	}
}
