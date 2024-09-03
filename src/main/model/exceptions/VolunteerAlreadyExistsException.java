package model.exceptions;

// Represents an Exception that is thrown when a volunteer already exists
public class VolunteerAlreadyExistsException extends RuntimeException {
    // EFFECTS: constructs a VolunteerAlreadyExistException
    public VolunteerAlreadyExistsException() {
        super("Volunteer already exists!");
    }
}
