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

	public class NotActivateException extends Exception {
		public NotActivateException() {
			super("NotActivateException");
		}
	}
	
	//ID不能為空
	public class IdCantEmptyException extends Exception {
		public IdCantEmptyException() {
			super("IdCantEmptyException");
		}
	}
	
	//激活密碼不能為空
	public class InitialPasswardCantEmptyException extends Exception {
		public InitialPasswardCantEmptyException() {
			super("InitialPasswardCantEmptyException");
		}
	}
	
	//登錄密碼不能為空
	public class PasswardCantEmptyException extends Exception {
		public PasswardCantEmptyException() {
			super("PasswardCantEmptyException");
		}
	}
}
