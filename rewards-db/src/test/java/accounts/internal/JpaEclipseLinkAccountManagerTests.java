package accounts.internal;

import java.util.Properties;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

/**
 * Unit test for the JPA-based account manager implementation using EclipseLink.
 * Tests manager behavior and verifies the Account JPA mapping is correct.
 */
public class JpaEclipseLinkAccountManagerTests extends JpaAccountManagerTests {

	@Test
	@Override
	public void testProfile() {
		Assert.assertTrue("JPA expected",
				accountManager instanceof JpaAccountManager);
		log.info("JPA with EclipseLink");
	}

	protected JpaVendorAdapter getImplementation() {
		return new HibernateJpaVendorAdapter();
	}

	protected Properties createJpaProperties() {
		Properties properties = new Properties();
		// turn on formatted SQL logging (very useful to verify Jpa is
		// issuing proper SQL)
		//properties.setProperty("eclipselink.logging.level", "FINEST");
		//properties.setProperty("eclipselink.logging.parameters", "true");
		properties.setProperty("eclipselink.weaving", "false");
		return properties;
	}
}
