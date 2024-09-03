package model;

import org.json.JSONObject;

// Represents a volunteer of the system
public class Volunteer implements User {
    private final String name;

    // MODIFIES: this
    // EFFECTS: constructs a volunteer with given name
    //          and no event to volunteer
    public Volunteer(String name) {
        this.name = name;
    }

    // EFFECTS: returns the name of this volunteer
    public String getName() {
        return this.name;
    }

    // EFFECTS: returns the credential of this volunteer
    @Override
    public String getCredential() {
        return getName();
    }

    // EFFECTS: returns this as JSON object
    @Override
    public JSONObject toJson() {
        return new JSONObject().put("Credential", getCredential());
    }
}
