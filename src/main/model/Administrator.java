package model;


import org.json.JSONObject;

// Represents an administrator of the system
public class Administrator implements User {

    public static final String IDENTIFIER = "admin";
    private String passCode;


    // EFFECTS: constructs an administrator with given passcode and name
    public Administrator(String code) {
        setPassCode(code);
    }

    // EFFECTS: returns the passcode of this administrator
    public String getPassCode() {
        return this.passCode;
    }

    // MODIFIES: this
    // EFFECTS: sets the passcode of this administrator
    public void setPassCode(String passCode) {
        this.passCode = passCode;
    }

    // EFFECTS: return this.passcode as its credential
    @Override
    public String getCredential() {
        return getPassCode();
    }

    // EFFECTS: return  Administrator.IDENTIFIER
    @Override
    public String getName() {
        return IDENTIFIER;
    }

    // EFFECTS: returns this as JSON object
    @Override
    public JSONObject toJson() {
        return new JSONObject().put("Credential", getCredential());
    }
}
