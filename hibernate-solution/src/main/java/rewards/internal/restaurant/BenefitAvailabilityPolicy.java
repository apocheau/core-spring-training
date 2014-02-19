package rewards.internal.restaurant;

import rewards.Dining;
import rewards.internal.account.Account;

/**
 * Determines if benefit is available for an account for dining.
 *<p> 
 * A value object. A strategy. Scoped by the Restaurant aggregate.
 *<p>
 * See lab-notes for information on how this class is mapped by JPA.
 */
public enum BenefitAvailabilityPolicy {
	NEVER_AVAILABLE(false), ALWAYS_AVAILABLE(true);

	private boolean available;

	BenefitAvailabilityPolicy(boolean available) {
		this.available = available;
	}

	/**
	 * Calculates if an account is eligible to receive benefits for a dining.
	 * 
	 * @param account
	 *            the account of the member who dined
	 * @param dining
	 *            the dining event
	 * @return benefit availability status
	 */
	public boolean isBenefitAvailableFor(Account account, Dining dining) {
		// Currently independent of provided parameters - either the
		// Restaurant offers benefits or it does not.
		return this.available;
	}
}
