package rewards.internal.restaurant;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Loads restaurants from a data source using the Hibernate API.
 */
public class HibernateRestaurantRepository implements RestaurantRepository {

	public static final String INFO = "Hibernate";

	private SessionFactory sessionFactory;

	@Override
	public String getInfo() {
		return INFO;
	}

	@Autowired
	public HibernateRestaurantRepository(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public Restaurant findByMerchantNumber(String merchantNumber) {
		return (Restaurant) getCurrentSession().createQuery("from Restaurant r where r.number = :merchantNumber")
				.setString("merchantNumber", merchantNumber).uniqueResult();
	}

	protected Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}
	
}
