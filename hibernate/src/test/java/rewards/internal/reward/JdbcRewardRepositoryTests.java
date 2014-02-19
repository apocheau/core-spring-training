package rewards.internal.reward;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;

import rewards.AccountContribution;
import rewards.Dining;
import rewards.RewardConfirmation;
import rewards.internal.account.Account;
import rewards.internal.account.AccountAccessor;

import common.money.MonetaryAmount;
import common.money.Percentage;

/**
 * Tests the JDBC reward repository with a test data source to verify data
 * access and relational-to-object mapping behavior works as expected.
 */
public class JdbcRewardRepositoryTests {

	private JdbcRewardRepository repository;

	private DataSource dataSource;

	private JdbcTemplate jdbcTemplate;

	private static String FIND_REWARD_AMOUNT = "select REWARD_AMOUNT from T_REWARD where CONFIRMATION_NUMBER = ?";

	@Before
	public void setUp() throws Exception {
		repository = new JdbcRewardRepository();
		dataSource = createTestDataSource();
		repository.setDataSource(dataSource);
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Test
	public void testCreateReward() throws SQLException {
		Dining dining = Dining.createDining("100.00", "1234123412341234",
				"0123456789");

		Account account = new Account("1", "Keith and Keri Donald");
		AccountAccessor.setEntityId(account, 0); // Set Entity Id of account to
													// 0
		account.addBeneficiary("Annabelle", Percentage.valueOf("50%"));
		account.addBeneficiary("Corgan", Percentage.valueOf("50%"));

		AccountContribution contribution = account
				.makeContribution(MonetaryAmount.valueOf("0.08"));
		RewardConfirmation confirmation = repository.confirmReward(
				contribution, dining);
		assertNotNull("confirmation should not be null", confirmation);
		assertNotNull("confirmation number should not be null",
				confirmation.getConfirmationNumber());
		assertEquals("wrong contribution object", contribution,
				confirmation.getAccountContribution());
		verifyRewardInserted(confirmation);
	}

	private void verifyRewardInserted(RewardConfirmation confirmation)
			throws DataAccessException {
		// There should now be one reward in the database
		int nRewards = jdbcTemplate
				.queryForInt("select count(*) from T_REWARD");
		assertEquals(1, nRewards);

		// Contribution amount should match value in database
		MonetaryAmount amount = new MonetaryAmount(jdbcTemplate.queryForObject(
				FIND_REWARD_AMOUNT, BigDecimal.class,
				confirmation.getConfirmationNumber()));
		assertEquals(confirmation.getAccountContribution().getAmount(), amount);
	}

	private DataSource createTestDataSource() {
		return new EmbeddedDatabaseBuilder().setName("rewards")
				.addScript("/rewards/testdb/schema.sql")
				.addScript("/rewards/testdb/test-data.sql").build();
	}
}
