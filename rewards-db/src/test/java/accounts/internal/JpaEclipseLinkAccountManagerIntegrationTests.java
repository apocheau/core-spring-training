package accounts.internal;

import javax.sql.DataSource;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Integration test for the JPA-based account manager implementation. Verifies
 * that the JpaAccountManager works with its underlying components and that the
 * Spring configuration is valid.
 */
@ActiveProfiles("jpa-elink")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:accounts/internal/accounts-test-config.xml")
public class JpaEclipseLinkAccountManagerIntegrationTests extends
		AbstractDatabaseAccountManagerTests {

	@Test
	@Override
	public void testProfile() {
		Assert.assertTrue("JPA expected but found " + accountManager.getInfo(),
				accountManager.getInfo().equals(JpaAccountManager.INFO));
	}

	@Override
	protected void setupForTest(DataSource dataSource) {
		// Nothing else to do - Spring does it all
	}
}
