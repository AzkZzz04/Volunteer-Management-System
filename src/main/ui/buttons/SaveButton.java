package ui.buttons;

import persistence.JsonWriter;

// Represents a save button
public class SaveButton extends IOButton {
    private final JsonWriter jsonWriter;

    // EFFECTS: constructs a save button
    public SaveButton() {
        super("Save");
        jsonWriter = new JsonWriter(JSON_STORE);
    }

    // EFFECTS: returns the json writer
    public JsonWriter getJsonWriter() {
        return jsonWriter;
    }
}
