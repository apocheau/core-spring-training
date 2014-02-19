package rewards.internal.restaurant;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Integration test for the JPA-based restaurant repository implementation.
 * Verifies that the JpaRestaurantRepository works with its underlying
 * components and that Spring is configuring things properly.
 */
@ActiveProfiles("jpa")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:rewards/internal/rewards-test-config.xml")
public class JpaRestaurantRepositoryIntegrationTests extends
		AbstractRestaurantRepositoryTests {

	@Test
	@Override
	public void testProfile() {
		Assert.assertTrue(
				"JPA expected but found " + restaurantRepository.getInfo(),
				restaurantRepository.getInfo().equals(
						JpaRestaurantRepository.INFO));
	}

}
