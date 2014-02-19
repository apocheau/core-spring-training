package rewards.internal.account;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import accounts.internal.HibernateAccountManager;

/**
 * Integration test for the Hibernate based account repository implementation.
 * Verifies that the HibernateAccountRepository works with its underlying
 * components and that Spring is configuring things properly.
 */
@ActiveProfiles("hibernate")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:rewards/internal/rewards-test-config.xml")
public class HibernateAccountRepositoryIntegrationTests extends
		AbstractAccountRepositoryTests {

	@Test
	@Override
	public void testProfile() {
		Assert.assertTrue(
				"Hibernate expected but found " + accountRepository.getInfo(),
				accountRepository.getInfo()
						.equals(HibernateAccountManager.INFO));
	}

}
