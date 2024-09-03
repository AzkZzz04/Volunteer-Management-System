package ui.buttons;

import persistence.JsonReader;

// Represents a load button
public class LoadButton extends IOButton {
    private final JsonReader jsonReader;

    // EFFECTS: constructs a load button
    public LoadButton() {
        super("Load");
        this.jsonReader = new JsonReader(JSON_STORE);
    }

    // EFFECTS: returns the json reader
    public JsonReader getJsonReader() {
        return jsonReader;
    }
}
