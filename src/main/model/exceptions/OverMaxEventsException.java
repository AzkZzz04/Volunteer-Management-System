package model.exceptions;

// Represents an Exception that is thrown when the number of events is over the maximum
public class OverMaxEventsException extends NotValidValueException {

    // EFFECTS: constructs a OverMaxEventsException
    public OverMaxEventsException() {
        super("Over the maximum number of events!");
    }
}
