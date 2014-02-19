package rewards.internal.restaurant;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;

import common.money.Percentage;

/**
 * Loads restaurants from a data source using the JDBC API.
 *
 * This implementation should cache restaurants to improve performance. The cache should be populated on initialization
 * and cleared on destruction.
 */


/* TODO-05: Configure Dependency Injection for dataSource (and decide if you should do it at the field level or constructor level)
			After making the changes, run the integration test again. You should see that it succeeds */

/* TODO-08: Annotate the class with appropriate stereotype annotation to indicate that the class is an auto scan component. */

/* TODO-12: what if you had decided to use setter injection instead of constructor injection? It is interesting to understand 
 * 			what happens then.  Change from constructor injection to setter injection. */

/* TODO-13: Execute RewardNetworkTests to verify. It should fail and you should see a NullPointerException. 
			Can you explain why? (if not, you can refer to read the detailed instructions to understand 
			what happened). */


public class JdbcRestaurantRepository implements RestaurantRepository {

	private DataSource dataSource;

	/**
	 * The Restaurant object cache. Cached restaurants are indexed by their merchant numbers.
	 */
	private Map<String, Restaurant> restaurantCache;

	/**
	 * The constructor sets the data source this repository will use to load restaurants.
	 * When the instance of JdbcRestaurantRepository is created, a Restaurant cache is 
	 * populated for read only access
	 *
	 * @param dataSource the data source
	 */

	public JdbcRestaurantRepository(DataSource dataSource){
		this.dataSource = dataSource;
		this.populateRestaurantCache();
	}
	
	public JdbcRestaurantRepository(){}
	
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public Restaurant findByMerchantNumber(String merchantNumber) {
	  	   return queryRestaurantCache(merchantNumber);
	}

	/**
	 * Helper method that populates the {@link #restaurantCache restaurant object cache} from rows in the T_RESTAURANT
	 * table. Cached restaurants are indexed by their merchant numbers. This method should be called on initialization.
	 */
	
	
	/* TODO-14: Register for an initialization lifecycle callback that will execute this method and populate the cache.
				Run the RewardNetworkTests test. You should see the test succeed */
	
	
	void populateRestaurantCache() {
		restaurantCache = new HashMap<String, Restaurant>();
		String sql = "select MERCHANT_NUMBER, NAME, BENEFIT_PERCENTAGE from T_RESTAURANT";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = dataSource.getConnection();
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				Restaurant restaurant = mapRestaurant(rs);
				// index the restaurant by its merchant number
				restaurantCache.put(restaurant.getNumber(), restaurant);
			}
		} catch (SQLException e) {
			throw new RuntimeException("SQL exception occurred finding by merchant number", e);
		} finally {
			if (rs != null) {
				try {
					// Close to prevent database cursor exhaustion
					rs.close();
				} catch (SQLException ex) {
				}
			}
			if (ps != null) {
				try {
					// Close to prevent database cursor exhaustion
					ps.close();
				} catch (SQLException ex) {
				}
			}
			if (conn != null) {
				try {
					// Close to prevent database connection exhaustion
					conn.close();
				} catch (SQLException ex) {
				}
			}
		}
	}

	/**
	 * Helper method that simply queries the cache of restaurants.
	 *
	 * @param merchantNumber the restaurant's merchant number
	 * @return the restaurant
	 * @throws EmptyResultDataAccessException if no restaurant was found with that merchant number
	 */
	private Restaurant queryRestaurantCache(String merchantNumber) {
		Restaurant restaurant = restaurantCache.get(merchantNumber);
		if (restaurant == null) {
			throw new EmptyResultDataAccessException(1);
		}
		return restaurant;
	}

	/**
	 * Helper method that clears the cache of restaurants.  This method should be called on destruction
	 */
	
	/* TODO-15: Add a breakpoint inside clearRestaurantCache() and re-run RewardNetworkTests in debug mode.
	 * It seems that this method is never called. Register for a destruction lifecycle callback that 
	 * will execute this method and clear the cache. */
	
	public 	void clearRestaurantCache() {
		restaurantCache.clear();
	}

	/**
	 * Maps a row returned from a query of T_RESTAURANT to a Restaurant object.
	 *
	 * @param rs the result set with its cursor positioned at the current row
	 */
	private Restaurant mapRestaurant(ResultSet rs) throws SQLException {
		// get the row column data
		String name = rs.getString("NAME");
		String number = rs.getString("MERCHANT_NUMBER");
		Percentage benefitPercentage = Percentage.valueOf(rs.getString("BENEFIT_PERCENTAGE"));
		// map to the object
		Restaurant restaurant = new Restaurant(number, name);
		restaurant.setBenefitPercentage(benefitPercentage);
		return restaurant;
	}
}