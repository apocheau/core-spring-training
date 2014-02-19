package accounts;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoSuchAccountException extends RuntimeException {

	public NoSuchAccountException(int accountId) {
		super("No Account with id " + accountId);
	}

}
