package accounts.web;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * A JUnit test case testing the AccountController.
 * <p>
 * Active profile is set to "jpa" but "hibernate" is another supported profile.
 */
@Transactional
@ActiveProfiles("jpa")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:accounts/web/test-config.xml")
public class AccountControllerIntegrationTests extends
		AbstractAccountControllerTests {

	@Autowired
	DataSource dataSource;

	private static int numAccountsInDb = -1;

	@Before
	public void setUp() {
		// The number of test accounts in the database
		// - a static variable so we only do this one.
		if (numAccountsInDb == -1)
			numAccountsInDb = new JdbcTemplate(dataSource)
					.queryForInt("SELECT count(*) FROM T_Account");
	}

	@Override
	protected int getNumAccountsExpected() {
		return numAccountsInDb;
	}

}
