package rewards.internal.account;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import common.money.MonetaryAmount;
import common.money.Percentage;

/**
 * A single beneficiary allocated to an account. Each beneficiary has a name
 * (e.g. Annabelle) and a savings balance tracking how much money has been saved
 * for he or she to date (e.g. $1000).
 * <p>
 * <b>IMPLEMENTATION NOTE:</b> In previous labs, the allocationPercentage and
 * savings fields were of type Percentage and MonetaryAmount respectively. If
 * you look at their accessors, below, they still use those types. However JPA
 * can only map to a limited number of known Java types (primitives, String,
 * Date and BigDecimal). We haven't covered enough JPA to map these types, so
 * instead, for simplicity, both are BigDecimals.
 */
@Entity
@Table(name = "T_ACCOUNT_BENEFICIARY")
public class Beneficiary {

	@Id
	@Column(name = "ID")
	@SuppressWarnings("unused")
	private Integer entityId;

	
	// TODO-02: Finish mapping the Beneficiary object. After completing this task, 
	//          run the HibernateAccountRepositoryTests class.
	
	
	
	private String name;

	private BigDecimal allocationPercentage;

	private BigDecimal savings = new BigDecimal("0.00");

	protected Beneficiary() {
		// Required by JPA
	}

	/**
	 * Creates a new account beneficiary.
	 * 
	 * @param name
	 *            the name of the beneficiary
	 * @param allocationPercentage
	 *            the beneficiary's allocation percentage within its account
	 */
	public Beneficiary(String name, Percentage allocationPercentage) {
		this.name = name;
		this.allocationPercentage = allocationPercentage.asBigDecimal();
	}

	/**
	 * Creates a new account beneficiary. This constructor should be called by
	 * privileged objects responsible for reconstituting an existing Account
	 * object from some external form such as a collection of database records.
	 * Marked package-private to indicate this constructor should never be
	 * called by general application code.
	 * 
	 * @param name
	 *            the name of the beneficiary
	 * @param allocationPercentage
	 *            the beneficiary's allocation percentage within its account
	 * @param savings
	 *            the total amount saved to-date for this beneficiary
	 */
	Beneficiary(String name, Percentage allocationPercentage, MonetaryAmount savings) {
		this.name = name;
		this.allocationPercentage = allocationPercentage.asBigDecimal();
		this.savings = savings.asBigDecimal();
	}

	/**
	 * Returns the beneficiary name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the beneficiary's allocation percentage in this account.
	 */
	public Percentage getAllocationPercentage() {
		return new Percentage(allocationPercentage);
	}

	/**
	 * Returns the amount of savings this beneficiary has accrued.
	 */
	public MonetaryAmount getSavings() {
		return new MonetaryAmount(savings);
	}

	/**
	 * Credit the amount to this beneficiary's saving balance.
	 * 
	 * @param amount
	 *            the amount to credit
	 */
	public void credit(MonetaryAmount amount) {
		savings = savings.add(amount.asBigDecimal());
	}

	public String toString() {
		return "name = '" + name + "', allocationPercentage = "
				+ allocationPercentage + ", savings = " + savings + ")";
	}
}