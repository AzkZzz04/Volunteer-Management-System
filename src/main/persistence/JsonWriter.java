package persistence;



import model.ManagementSystem;
import model.log.Event;
import model.log.EventLog;
import org.json.JSONObject;

import java.io.FileNotFoundException;

import java.io.PrintWriter;

// Represents a writer that writes JSON representation of workroom to file
// Citation: code adapted from JsonSerializationDemo by Paul Carter
// https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
public class JsonWriter {
    private static final int TAB = 4;
    private PrintWriter writer;
    private final String destination;

    // EFFECTS: constructs writer to write to destination file
    public JsonWriter(String destination) {
        this.destination = destination;
    }

    // MODIFIES: this
    // EFFECTS: opens writer; throws FileNotFoundException if destination file cannot
    // be opened for writing
    public void open() throws FileNotFoundException {
        writer = new PrintWriter(destination);
    }

    // MODIFIES: this
    // EFFECTS: writes JSON representation of system to file
    public void write(ManagementSystem system) {
        log("Saving data to " + destination);
        JSONObject json = system.toJson();
        saveToFile(json.toString(TAB));
    }

    // MODIFIES: this
    // EFFECTS: writes string to file
    private void saveToFile(String json) {
        writer.print(json);
    }

    // MODIFIES: this
    // EFFECTS: closes writer
    public void close() {
        writer.close();
    }

    // MODIFIES: EventLog
    // EFFECTS: add a log with given message to the event log
    private void log(String message) {
        EventLog.getInstance().logEvent(new Event(message));
    }




}
