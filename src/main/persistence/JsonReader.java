package persistence;


import model.VolunteerEvent;
import model.ManagementSystem;
import model.User;
import model.log.Event;
import model.log.EventLog;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

// Represents a reader that reads system from JSON data stored in file
// Citation: code adapted from JsonSerializationDemo by Paul Carter
// https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
public class JsonReader {
    private final String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads system from file and returns it
    // throws IOException if an error occurs reading data from file
    public ManagementSystem read() throws IOException {
        log("Reading data from " + source);
        String jsonData = readFile(source);
        var systemObject = this.parseSystem(new JSONObject(jsonData));
        log("Finished reading data from " + source);
        return systemObject;
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        return new String(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(source)));
    }

    // EFFECTS: parses system from JSON object and returns it
    private ManagementSystem parseSystem(JSONObject dataObject) {
        var admin = dataObject.getJSONObject("admin");
        ManagementSystem ms = new ManagementSystem(admin.getString("Credential"));
        addVolunteers(ms, dataObject);
        addEvents(ms, dataObject);
        return ms;
    }

    // MODIFIES: ms
    // EFFECTS: parses volunteers from JSON object and adds them to system
    private void addVolunteers(ManagementSystem ms, JSONObject jsonObject) {
        JSONArray users = jsonObject.getJSONArray("volunteers");
        for (final var user : users) {
            JSONObject nextUser = (JSONObject) user;
            addVolunteer(ms, nextUser);
        }
    }

    // MODIFIES: ms
    // EFFECTS: parses volunteer from JSON object and adds it to system
    private void addVolunteer(ManagementSystem ms, JSONObject jsonObject) {
        String credential = jsonObject.getString("Credential");
        if (!ms.getVolunteers().containsKey(credential)) {
            ms.createAVolunteer(credential);
        }
    }

    // MODIFIES: ms
    // EFFECTS: parses events from JSON object and adds them to system
    private void addEvents(ManagementSystem ms, JSONObject jsonObject) {
        JSONArray events = jsonObject.getJSONArray("events");
        for (final var event : events) {
            JSONObject nextEvent = (JSONObject) event;
            addEvent(ms, nextEvent);
        }
    }

    // MODIFIES: ms
    // EFFECTS: parses event from JSON object and adds it to system
    private void addEvent(ManagementSystem ms, JSONObject nextEvent) {
        String eventName = nextEvent.getString("eventName");
        int neededVolunteerNum = nextEvent.getInt("neededVolunteerNum");

        VolunteerEvent volunteerEvent = new VolunteerEvent(eventName, neededVolunteerNum);
        JSONArray volunteerList = nextEvent.getJSONArray("volunteerList");
        for (final var volunteer : volunteerList) {
            JSONObject nextVolunteer = (JSONObject) volunteer;
            addVolunteerToEvent(ms, volunteerEvent, nextVolunteer);
        }
        ms.addAnVolunteerEvent(volunteerEvent);
    }

    // MODIFIES: event
    // EFFECTS: parses volunteer from JSON object and adds it to event
    private void addVolunteerToEvent(ManagementSystem ms, VolunteerEvent volunteerEvent, JSONObject nextVolunteer) {
        String volunteerName = nextVolunteer.getString("Credential");
        User volunteer = ms.getVolunteers().get(volunteerName);
        volunteerEvent.addAVolunteer(volunteer);

    }

    // MODIFIES: EventLog
    // EFFECTS: logs event with given message to EventLog
    private void log(String message) {
        EventLog.getInstance().logEvent(new Event(message));
    }



}
