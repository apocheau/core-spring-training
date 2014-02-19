package rewards.internal;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import rewards.RewardNetwork;
import rewards.internal.account.AccountRepository;
import rewards.internal.account.JdbcAccountRepository;
import rewards.internal.restaurant.JdbcRestaurantRepository;
import rewards.internal.restaurant.RestaurantRepository;
import rewards.internal.reward.JdbcRewardRepository;
import rewards.internal.reward.RewardRepository;

// Commented out to prevent this class from being picked up by component scanning,
// as the classes involved will be component scanned themselves already and this
// would otherwise result in double bean definitions.
//@Configuration
@SuppressWarnings("unused")
public class ApplicationConfig {

	@Autowired
	private DataSource dataSource;
	
	@Bean
	RewardNetwork rewardNetwork() {
		return new RewardNetworkImpl(accountRepository(), restaurantRepository(), rewardRepository());
	}
	
	@Bean
	AccountRepository accountRepository() {
		JdbcAccountRepository accountRepository = new JdbcAccountRepository();
		accountRepository.setDataSource(dataSource);
		return accountRepository;
	}
	
	@Bean
	RestaurantRepository restaurantRepository() {
		JdbcRestaurantRepository restaurantRepository = new JdbcRestaurantRepository();
		restaurantRepository.setDataSource(dataSource);
		return restaurantRepository;
	}
	
	@Bean
	RewardRepository rewardRepository() {
		JdbcRewardRepository rewardRepository = new JdbcRewardRepository();
		rewardRepository.setDataSource(dataSource);
		return rewardRepository;
	}
}
