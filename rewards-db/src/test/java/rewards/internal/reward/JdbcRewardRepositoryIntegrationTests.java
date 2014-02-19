package rewards.internal.reward;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Integration test for the JDBC-based rewards repository implementation.
 * Verifies that the JdbcRewardRepository works with its underlying components
 * and that Spring is configuring things properly.
 */
@ActiveProfiles("hibernate")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:rewards/internal/rewards-test-config.xml")
public class JdbcRewardRepositoryIntegrationTests extends
		AbstractRewardRepositoryTests {

	@Test
	@Override
	public void testProfile() {
		Assert.assertTrue(
				"Hibernate expected but found " + rewardRepository.getInfo(),
				rewardRepository.getInfo().equals(JdbcRewardRepository.TYPE));
	}

}
