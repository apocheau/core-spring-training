package rewards.internal.account;

/**
 * Convenience class for setting entity ids manually. Shows how an entity id,
 * which would normally be hidden, can be accessed when testing.
 */
public class AccountAccessor {
	/**
	 * Set the entity id of an account object. Normally this would happen when
	 * it is persisted, but during testing domain objects may not actually be
	 * persisted (typically no database is being used).
	 * 
	 * @param account
	 *            Any account object.
	 * @param entityId
	 *            Its id.
	 * @return The same account.
	 */
	static public void setEntityId(Account account, int entityId) {
		account.setEntityId(entityId);
	}

	/**
	 * Get the account's hidden entity id.
	 * 
	 * @param account
	 *            Any account object.
	 * @return It's id.
	 */
	static public int getEntityId(Account account) {
		return account.getEntityId();
	}
}
