package persistence;

import org.json.JSONObject;

// Represents a class that can be written to JSON
// CITATION: code taken and modified from JsonSerializationDemo by Paul Carter
// https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
public interface Writable {
    // EFFECTS: returns this as JSON object
    JSONObject toJson();
}
