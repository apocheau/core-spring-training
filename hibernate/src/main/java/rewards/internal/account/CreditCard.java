package rewards.internal.account;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "T_ACCOUNT_CREDIT_CARD")
public class CreditCard {

	@Id
	@Column(name = "ID")
	@SuppressWarnings("unused")
	private Integer entityId;

	@Column(name = "NUMBER")
	private String number;

	/**
	 * Get the number if this credit-card.
	 */
	public String getNumber() {
		return number;
	}

	/**
	 * Set the number of this credit-card.
	 * 
	 * @param number
	 *            The new number - this should be a string of digits.
	 */
	public void setNumber(String number) {
		this.number = number;
	}
}
