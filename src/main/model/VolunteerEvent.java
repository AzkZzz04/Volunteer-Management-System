package model;

import model.exceptions.TooManyVolunteerException;
import model.exceptions.VolunteerAlreadyExistsException;
import model.log.Event;
import model.log.EventLog;
import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

// Represents an event that needs volunteers
public class VolunteerEvent implements Writable {
    private String eventName;
    private int neededVolunteerNum;
    private final List<User> volunteerList;


    // EFFECTS: constructs an event with given name and number of volunteers needed
    public VolunteerEvent(String eventName, int neededVolunteerNum) {
        setEventName(eventName);
        setNeededVolunteerNum(neededVolunteerNum);
        this.volunteerList = new ArrayList<>();
    }

    // REQUIRES: volunteer is not in the list of volunteers and the event is not full
    // MODIFIES: this
    // EFFECTS: adds a volunteer to the list of volunteers
    // throws VolunteerAlreadyExistsException if the volunteer is already in the list
    // throws TooManyVolunteerException if the event is full
    public boolean addAVolunteer(User volunteer) {
        if (volunteerList.contains(volunteer)) {
            throw new VolunteerAlreadyExistsException();
        }

        if (isFull()) {
            throw new TooManyVolunteerException();
        }
        log("Added a volunteer: " + volunteer.getName() + " to " + this.eventName);
        return volunteerList.add(volunteer);
    }

    // MODIFIES: EventLog
    // EFFECTS: add a log with given message to the event log
    private void log(String message) {
        EventLog.getInstance().logEvent(new Event(message));
    }

    // EFFECTS: returns a string representation of this event
    //          including the name of the event, the number of volunteers needed
    //          and the list of volunteers
    @Override
    public String toString() {
        return  eventName
                + " needing: " + (neededVolunteerNum - volunteerList.size()) + " volunteers"
                + ", Current volunteers: "
                + volunteerList.stream()
                .map(User::getName)
                .collect(Collectors.joining(", "));

    }

    // EFFECTS: returns true if the event has enough volunteers
    public boolean isFull() {
        return volunteerList.size() >= neededVolunteerNum;
    }

    // EFFECTS: returns the name of this event
    public String getEventName() {
        return eventName;
    }

    // EFFECTS: returns the number of volunteers needed for this event
    public int getNeededVolunteerNum() {
        return neededVolunteerNum;
    }

    // EFFECTS: returns the list of volunteers for this event
    public List<User> getVolunteerList() {
        return Collections.unmodifiableList(volunteerList);
    }

    // MODIFIES: this
    // EFFECTS: sets the name of this event
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    // MODIFIES: this
    // EFFECTS: sets the number of volunteers needed for this event
    public void setNeededVolunteerNum(int neededVolunteerNum) {
        this.neededVolunteerNum = neededVolunteerNum;
    }

    // EFFECTS: returns this event as a JSON object
    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("eventName", eventName);
        jsonObject.put("neededVolunteerNum", neededVolunteerNum);
        jsonObject.put("volunteerList", volunteerListToJson());
        return jsonObject;
    }

    // EFFECTS: returns volunteers in this event as a JSON array
    private JSONArray volunteerListToJson() {
        JSONArray jsonArray = new JSONArray();
        for (final var v : volunteerList) {
            jsonArray.put(v.toJson());
        }
        return jsonArray;
    }
}
