package databaseConnection;

public class SignupAndLoginExceptions {

	public class IdAlreadyUsedException extends Exception {
		public IdAlreadyUsedException() {
			super("IdAlreadyUsedException");
		}
	}

	public class PasswordAlreadyUsedException extends Exception {
		public PasswordAlreadyUsedException() {
			super("PasswordAlreadyUsedException");
		}
	}

	public class AccountNotExistException extends Exception {
		public AccountNotExistException() {
			super("AccountNotExistException");
		}
	}

	public class PasswordWrongException extends Exception {
		public PasswordWrongException() {
			super("PasswordWrongException");
		}
	}

}
