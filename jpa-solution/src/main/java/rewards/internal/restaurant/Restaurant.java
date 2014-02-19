package rewards.internal.restaurant;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import rewards.Dining;
import rewards.internal.account.Account;

import common.money.MonetaryAmount;
import common.money.Percentage;

/**
 * A restaurant establishment in the network. Like AppleBee's.
 * 
 * Restaurants calculate how much benefit may be awarded to an account for
 * dining based on a availability policy and a benefit percentage.
 */
@Entity
@Table(name = "T_RESTAURANT")
public class Restaurant {

	@Id
	@GeneratedValue
	@Column(name = "id")
	private Long entityId;

	@Column(name = "MERCHANT_NUMBER")
	private String number;

	@Column(name = "NAME")
	private String name;

	@Embedded
	@AttributeOverride(name="value",column=@Column(name="BENEFIT_PERCENTAGE"))
	private Percentage benefitPercentage;

	// USEFUL MAPPING TECHNIQUE:
	//    Custom accessors for JPA only.
	//
	// PROBLEM:
	//    Map policy code, a varchar(1) column, to the correct
	//    BenefitAvailabilityPolicy object.
	//
	// SOLUTION:
	//    The mapping instead will occur in database specific getters and
	//    setters indicated by the @Access annotation - see end of class.
	//    This is the most flexible way to translate between a column value
	//    and a Java object - more powerful than using @Embedded.  Often
	//    used for mapping Enums held as a code in the database.
	//
	//    Note this is not really a transient data-member, we are just hiding
	//    it from the default JPA column mapping process.
	//
	//    This is part of the BONUS section for the lab.  See the methods:
	//      getDbBenefitAvailabilityPolicy()
    //      setDbBenefitAvailabilityPolicy()
	//
	@Transient  // Always available unless overridden
	private BenefitAvailabilityPolicy benefitAvailabilityPolicy = AlwaysAvailable.INSTANCE;

	/**
	 * Default constructor needed by JPA, but no-one else can should it.
	 */
	@SuppressWarnings("unused")
	private Restaurant() {
	}

	/**
	 * Creates a new restaurant.
	 * 
	 * @param number
	 *            the restaurant's merchant number
	 * @param name
	 *            the name of the restaurant
	 */
	public Restaurant(String number, String name) {
		this.number = number;
		this.name = name;
	}

	/**
	 * Sets the percentage benefit to be awarded for eligible dining
	 * transactions.
	 * 
	 * @param benefitPercentage
	 *            the benefit percentage
	 */
	public void setBenefitPercentage(Percentage benefitPercentage) {
		this.benefitPercentage = benefitPercentage;
	}

	/**
	 * Sets the policy that determines if a dining by an account at this
	 * restaurant is eligible for benefit.
	 * 
	 * @param benefitAvailabilityPolicy
	 *            the benefit availability policy
	 */
	public void setBenefitAvailabilityPolicy(
			BenefitAvailabilityPolicy benefitAvailabilityPolicy) {
		this.benefitAvailabilityPolicy = benefitAvailabilityPolicy;
	}

	/**
	 * Returns the name of this restaurant.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the merchant number of this restaurant.
	 */
	public String getNumber() {
		return number;
	}

	/**
	 * Returns this restaurant's benefit percentage.
	 */
	public Percentage getBenefitPercentage() {
		return benefitPercentage;
	}

	/**
	 * Returns this restaurant's benefit availability policy.
	 */
	public BenefitAvailabilityPolicy getBenefitAvailabilityPolicy() {
		return benefitAvailabilityPolicy;
	}

	/**
	 * Returns the id for this restaurant.
	 */
	public Long getEntityId() {
		return entityId;
	}

	/**
	 * Calculate the benefit eligible to this account for dining at this
	 * restaurant.
	 * 
	 * @param account
	 *            the account that dined at this restaurant
	 * @param dining
	 *            a dining event that occurred
	 * @return the benefit amount eligible for reward
	 */
	public MonetaryAmount calculateBenefitFor(Account account, Dining dining) {
		if (benefitAvailabilityPolicy.isBenefitAvailableFor(account, dining)) {
			return dining.getAmount().multiplyBy(benefitPercentage);
		} else {
			return MonetaryAmount.zero();
		}
	}

	public String toString() {
		return "Number = '" + number + "', name = '" + name
				+ "', benefitPercentage = " + benefitPercentage
				+ ", benefitAvailabilityPolicy = " + benefitAvailabilityPolicy;
	}
	
	// Internal methods for JPA only - hence they are protected.
	/**
	 * Sets this restaurant's benefit availability policy from the code stored
	 * in the underlying column. This is a database specific accessor using the
	 * JPA 2 @Access annotation.
	 */
	@Access(AccessType.PROPERTY)
	@Column(name = "BENEFIT_AVAILABILITY_POLICY")
	protected void setDbBenefitAvailabilityPolicy(String policyCode) {
		if ("A".equals(policyCode)) {
			benefitAvailabilityPolicy = AlwaysAvailable.INSTANCE;
		} else if ("N".equals(policyCode)) {
			benefitAvailabilityPolicy = NeverAvailable.INSTANCE;
		} else {
			throw new IllegalArgumentException("Not a supported policy code "
					+ policyCode);
		}
	}

	/**
	 * Returns this restaurant's benefit availability policy code for storage in
	 * the underlying column. This is a database specific accessor using the JPA
	 * 2 @Access annotation.
	 */
	@Access(AccessType.PROPERTY)
	@Column(name = "BENEFIT_AVAILABILITY_POLICY")
	protected String getDbBenefitAvailabilityPolicy() {
		if (benefitAvailabilityPolicy == AlwaysAvailable.INSTANCE) {
			return "A";
		} else if (benefitAvailabilityPolicy == NeverAvailable.INSTANCE) {
			return "N";
		} else {
			throw new IllegalArgumentException("No policy code for "
					+ benefitAvailabilityPolicy.getClass());
		}
	}
}