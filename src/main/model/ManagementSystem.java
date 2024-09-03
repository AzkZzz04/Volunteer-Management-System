package model;

import model.exceptions.OverMaxEventsException;
import model.exceptions.VolunteerAlreadyExistsException;
import model.log.Event;
import model.log.EventLog;
import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.*;


// Represents the system that manages the events and volunteers
public class ManagementSystem implements Writable {

    public static final int MAX_EVENT_NUM = 3;
    private final List<VolunteerEvent> volunteerEvents;
    private final Map<String,User> volunteers;

    // EFFECTS: constructs a system with no events and no volunteers
    //          and create an admin with default passcode
    public ManagementSystem() {
        volunteerEvents = new ArrayList<>();
        volunteers = new HashMap<>();
        createAnAdmin("admin");
    }

    // EFFECTS: constructs a system with no events and no volunteers
    //          and create an admin with given passcode
    public ManagementSystem(String passCode) {
        volunteerEvents = new ArrayList<>();
        volunteers = new HashMap<>();
        createAnAdmin(passCode);

    }

    // MODIFIES: this
    // EFFECTS: create a admin
    private void createAnAdmin(String passCode) {
        User admin = new Administrator(passCode);
        volunteers.put(admin.getCredential(),admin);
    }

    // REQUIRES: there is no volunteer with the same name
    // MODIFIES: this
    // EFFECTS: create a admin if there is no volunteer, otherwise create a volunteer
    public User createAVolunteer(String name) {
        if (volunteers.containsKey(name)) {
            throw new VolunteerAlreadyExistsException();
        }

        User volunteer = new Volunteer(name);
        volunteers.put(volunteer.getCredential(),volunteer);

        log("Created a volunteer: " + volunteer.getName());

        return volunteer;
    }

    // MODIFIES: this
    // EFFECTS: creates an event with given name and number of volunteers needed
    //          and adds it to the list of events
    //throws OverMaxEventsException if the number of events is over the maximum
    public VolunteerEvent createAnVolunteerEvent(String eventName, int neededVolunteerNum) {
        if (volunteerEvents.size() >= MAX_EVENT_NUM) {
            throw new OverMaxEventsException();
        }

        VolunteerEvent volunteerEvent = new VolunteerEvent(eventName, neededVolunteerNum);
        return addAnVolunteerEvent(volunteerEvent);
    }

    // MODIFIES: this
    // EFFECTS: adds an event to the list of events
    public VolunteerEvent addAnVolunteerEvent(VolunteerEvent volunteerEvent) {
        volunteerEvents.add(volunteerEvent);
        log("Created an event: " + volunteerEvent.getEventName() + " needing: "
                + volunteerEvent.getNeededVolunteerNum() + " volunteers");
        return volunteerEvent;
    }

    // MODIFIES: this
    // EFFECTS: adds a volunteer to the list of volunteers for the event
    public boolean addToList(int eventNumber, String volunteerName) {
        VolunteerEvent volunteerEvent = volunteerEvents.get(eventNumber - 1);
        User volunteer = volunteers.get(volunteerName);
        return volunteerEvent.addAVolunteer(volunteer);
    }

    // EFFECTS: returns the list of events that the volunteer is assigned to
    public List<String> getAssignedEvents(User volunteer) {
        List<String> assignedEvents = new ArrayList<>();
        for (VolunteerEvent e : volunteerEvents) {
            if (e.getVolunteerList().contains(volunteer)) {
                assignedEvents.add(e.getEventName());
            }
        }
        return assignedEvents;
    }

    // EFFECTS: returns the list of events
    public List<VolunteerEvent> getEvents() {
        return Collections.unmodifiableList(volunteerEvents);
    }

    // EFFECTS: returns the list of all users
    public Map<String, User> getVolunteers() {
        return Collections.unmodifiableMap(volunteers);
    }

    // EFFECTS: returns the admin of this system
    public User getAdmin() {
        User admin = null;
        for (User u : volunteers.values()) {
            if (u instanceof Administrator) {
                admin = u;
            }
        }
        return admin;
    }

    // EFFECTS: find event with given event output
    public VolunteerEvent findEvent(String eventOutput) {
        for (VolunteerEvent e : volunteerEvents) {
            if (e.toString().equals(eventOutput)) {
                return e;
            }
        }
        return null;
    }

    // EFFECTS: returns this system as a JSON object
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("admin", Objects.requireNonNull(getAdmin()).toJson());
        json.put("events", eventsToJson());
        json.put("volunteers", volunteersToJson());
        return json;
    }

    // EFFECTS: returns events in this system as a JSON array
    private JSONArray eventsToJson() {
        JSONArray jsonArray = new JSONArray();
        for (final var e : volunteerEvents) {
            jsonArray.put(e.toJson());
        }
        return jsonArray;
    }

    // EFFECTS: returns volunteers in this system as a JSON array
    private JSONArray volunteersToJson() {
        JSONArray jsonArray = new JSONArray();
        for (final var u : volunteers.values()) {
            jsonArray.put(u.toJson());
        }
        return jsonArray;
    }

    // MODIFIES: EventLog
    // EFFECTS: add a log with given message to the event log
    private void log(String message) {
        EventLog.getInstance().logEvent(new Event(message));
    }


}
