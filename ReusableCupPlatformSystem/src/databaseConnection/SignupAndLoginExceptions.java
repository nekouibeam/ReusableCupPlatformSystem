package databaseConnection;

/**
 * This class contains custom exceptions for signup and login operations.
 */
public class SignupAndLoginExceptions {

    /**
     * Exception thrown when an ID is already used.
     */
    public class IdAlreadyUsedException extends Exception {
        /**
         * Constructs a new IdAlreadyUsedException with a default message.
         */
        public IdAlreadyUsedException() {
            super("IdAlreadyUsedException");
        }
    }

    /**
     * Exception thrown when a password is already used.
     */
    public class PasswordAlreadyUsedException extends Exception {
        /**
         * Constructs a new PasswordAlreadyUsedException with a default message.
         */
        public PasswordAlreadyUsedException() {
            super("PasswordAlreadyUsedException");
        }
    }

    /**
     * Exception thrown when an account does not exist.
     */
    public class AccountNotExistException extends Exception {
        /**
         * Constructs a new AccountNotExistException with a default message.
         */
        public AccountNotExistException() {
            super("AccountNotExistException");
        }
    }

    /**
     * Exception thrown when the password is incorrect.
     */
    public class PasswordWrongException extends Exception {
        /**
         * Constructs a new PasswordWrongException with a default message.
         */
        public PasswordWrongException() {
            super("PasswordWrongException");
        }
    }

    /**
     * Exception thrown when the account is not activated.
     */
    public class NotActivateException extends Exception {
        /**
         * Constructs a new NotActivateException with a default message.
         */
        public NotActivateException() {
            super("NotActivateException");
        }
    }
    
    /**
     * Exception thrown when the ID is empty.
     */
    public class IdCantEmptyException extends Exception {
        /**
         * Constructs a new IdCantEmptyException with a default message.
         */
        public IdCantEmptyException() {
            super("IdCantEmptyException");
        }
    }
    
    /**
     * Exception thrown when the initial password is empty.
     */
    public class InitialPasswordCantEmptyException extends Exception {
        /**
         * Constructs a new InitialPasswardCantEmptyException with a default message.
         */
        public InitialPasswordCantEmptyException() {
            super("InitialPasswardCantEmptyException");
        }
    }
    
    /**
     * Exception thrown when the login password is empty.
     */
    public class PasswordCantEmptyException extends Exception {
        /**
         * Constructs a new PasswardCantEmptyException with a default message.
         */
        public PasswordCantEmptyException() {
            super("PasswardCantEmptyException");
        }
    }
    
    /**
     * Exception thrown when the Name is empty.
     */
    public class NameCantEmptyException extends Exception {
        /**
         * Constructs a new NameCantEmptyException with a default message.
         */
        public NameCantEmptyException() {
            super("NameCantEmptyException");
        }
    }
}
