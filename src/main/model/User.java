package model;

// Represents a user of the system

import org.json.JSONObject;
import persistence.Writable;

// Represents a user of the system
public interface User extends Writable {
    // EFFECTS: returns the credential of this user
    String getCredential();

    // EFFECTS: returns the name of this user
    String getName();

    // EFFECTS: returns this as JSON object
    JSONObject toJson();
}
