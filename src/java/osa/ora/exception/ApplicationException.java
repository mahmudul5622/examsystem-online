package osa.ora.exception;

/*
 * @author Osama Oransa
 */
public abstract class ApplicationException extends Exception {

    public ApplicationException(Throwable cause) {
        super(cause);
    }

    public ApplicationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApplicationException(String message) {
        super(message);
    }

    public ApplicationException() {
    }
}
