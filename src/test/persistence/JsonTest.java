package persistence;

import model.VolunteerEvent;
import model.ManagementSystem;


import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;


// Citation: code adapted from JsonSerializationDemo by Paul Carter
// https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
public class JsonTest {

    // EFFECTS: check if the event has the same name and number of volunteers
    protected void checkEvent(String name, int numVolunteers, VolunteerEvent volunteerEvent) {
        assertEquals(name, volunteerEvent.getEventName());
        assertEquals(numVolunteers, volunteerEvent.getNeededVolunteerNum());
    }

    // EFFECTS: write a management system to file and read it back
    protected static ManagementSystem writeAndReadManagementSystem() throws IOException {

        ManagementSystem ms = new ManagementSystem("test");
        ms.createAnVolunteerEvent("e1", 1);
        ms.createAnVolunteerEvent("e2", 2);
        ms.createAVolunteer("v1");
        ms.createAVolunteer("v2");
        ms.createAVolunteer("v3");
        ms.getEvents().get(0).addAVolunteer(ms.getVolunteers().get("v1"));
        ms.getEvents().get(1).addAVolunteer(ms.getVolunteers().get("v2"));
        ms.getEvents().get(1).addAVolunteer(ms.getVolunteers().get("v3"));

        JsonWriter writer = new JsonWriter("./data/testWriterGeneralManageSystem.json");
        writer.open();
        writer.write(ms);
        writer.close();

        JsonReader jsonReader  = new JsonReader("./data/testWriterGeneralManageSystem.json");
        return jsonReader.read();

    }
}
