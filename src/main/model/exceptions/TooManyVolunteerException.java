package model.exceptions;

// Represents an Exception that is thrown when the number of volunteers is more than needed
public class TooManyVolunteerException extends NotValidValueException {
    // EFFECTS: constructs a TooManyVolunteerException
    public TooManyVolunteerException() {
        super("Too many volunteers!");
    }
}
