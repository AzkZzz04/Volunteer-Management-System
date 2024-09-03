package model.exceptions;

// Represents an Exception that is thrown when a value is not valid
public abstract class NotValidValueException extends RuntimeException {
    // EFFECTS: constructs a NotValidValueException object with given message
    public NotValidValueException(String s) {
        super(s);
    }
}
