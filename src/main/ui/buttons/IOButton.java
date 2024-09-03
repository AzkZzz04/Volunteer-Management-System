package ui.buttons;

import javax.swing.JButton;

// Represents a button for loading(input) and saving(output)
public class IOButton extends JButton {
    protected static final String JSON_STORE = "./data/save.json";

    // EFFECTS: constructs a button with given name
    public IOButton(String name) {
        super(name);
    }

    // EFFECTS: returns the path of the file to be loaded or saved
    public String getPath() {
        return JSON_STORE;
    }

}
