package seedu.address.storage.exceptions.json;

/**
 * Signals that some given data does not fulfill some constraints.
 */
public class IllegalJsonValueException extends Exception {
    /**
     * @param message should contain relevant information on the failed constraint(s)
     */
    public IllegalJsonValueException(String message) {
        super(message);
    }

    /**
     * @param message should contain relevant information on the failed constraint(s)
     * @param cause of the main exception
     */
    public IllegalJsonValueException(String message, Throwable cause) {
        super(message, cause);
    }
}