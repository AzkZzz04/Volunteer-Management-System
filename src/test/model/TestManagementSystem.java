package model;

import model.exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestManagementSystem {
    ManagementSystem system, defaultSystem;
    VolunteerEvent volunteerEvent1, volunteerEvent2, volunteerEvent3, volunteerEvent4;
    @BeforeEach
    void beforeEach() {
        defaultSystem = new ManagementSystem();
        system = new ManagementSystem("code");
        volunteerEvent1 = volunteerEvent2 = volunteerEvent3 = volunteerEvent4 = null;
    }

    @Test
    void testConstructor() {
        assertEquals(0,defaultSystem.getEvents().size());
        assertEquals(1,defaultSystem.getVolunteers().size());
        assertEquals(Administrator.IDENTIFIER,defaultSystem.getVolunteers().get("admin").getName());

        assertEquals(0,system.getEvents().size());
        assertEquals(1,system.getVolunteers().size());
        assertEquals(Administrator.IDENTIFIER,system.getVolunteers().get("code").getName());
        assertEquals(ManagementSystem.MAX_EVENT_NUM,3);
    }

    @Test
    void testCreateAUserSingle() {
        var user = system.createAVolunteer("Volunteer");
        var volunteers = system.getVolunteers();
        assertEquals(user,volunteers.get("Volunteer"));
        assertEquals(2,volunteers.size());
    }

    @Test
    void testCreateAUser() {
        var user1 = system.createAVolunteer("Volunteer1");
        var user2 = system.createAVolunteer("Volunteer2");
        var volunteers = system.getVolunteers();
        assertEquals(user1,volunteers.get("Volunteer1"));
        assertEquals(user2,volunteers.get("Volunteer2"));
        assertEquals(3,volunteers.size());
    }

    @Test
    void testCreateAUserAlreadyExist() {
        User user1 = null, user2 = null;
        try {
            user1 = system.createAVolunteer("user1");
            user2 = system.createAVolunteer("user1");
            fail("Did not throw VolunteerAlreadyExistsException");
        } catch (VolunteerAlreadyExistsException e) {
            // expected
        }

        assertTrue(system.getVolunteers().containsValue(user1));
        assertTrue(system.getVolunteers().containsKey("user1"));
        assertFalse(system.getVolunteers().containsValue(user2));
        assertFalse(system.getVolunteers().containsKey("user2"));
        assertEquals(2,system.getVolunteers().size());
    }

    @Test
    void testCreateAnEventSingle() {
        volunteerEvent1 = system.createAnVolunteerEvent("event1",1);
        var events = system.getEvents();
        assertEquals(1,events.size());
    }

    @Test
    void testCreateAnEventFull() {
        volunteerEvent1 = system.createAnVolunteerEvent("event1",1);
        assertEquals(1, system.getEvents().size());
        volunteerEvent2 = system.createAnVolunteerEvent("event2",2);
        assertEquals(2, system.getEvents().size());
        volunteerEvent3 = system.createAnVolunteerEvent("event3",3);
        assertEquals(3, system.getEvents().size());

    }


    @Test
    void testCreateAnEventOverFull() {
        try {
            volunteerEvent1 = system.createAnVolunteerEvent("event1",1);
            assertEquals(1, system.getEvents().size());
            volunteerEvent2 = system.createAnVolunteerEvent("event2",2);
            assertEquals(2, system.getEvents().size());
            volunteerEvent3 = system.createAnVolunteerEvent("event3",3);
            assertEquals(3, system.getEvents().size());
            volunteerEvent4 = system.createAnVolunteerEvent("event4",4);
            fail("Did not throw OverMaxEventsException");
        } catch (OverMaxEventsException e) {
            // expected
        }

        assertTrue(system.getEvents().contains(volunteerEvent1));
        assertTrue(system.getEvents().contains(volunteerEvent2));
        assertTrue(system.getEvents().contains(volunteerEvent3));
        assertFalse(system.getEvents().contains(volunteerEvent4));
        assertEquals(3,system.getEvents().size());
    }

    @Test
    void testAddToListSingle() {
        volunteerEvent1 = system.createAnVolunteerEvent("event1",1);
        var user = system.createAVolunteer("user");
        assertTrue(system.addToList(1,"user"));
        assertEquals(1, volunteerEvent1.getVolunteerList().size());
        assertTrue(volunteerEvent1.getVolunteerList().contains(user));
    }

    @Test
    void testAddToListMultiple() {
        volunteerEvent1 = system.createAnVolunteerEvent("event1",2);
        var user1 = system.createAVolunteer("user1");
        var user2 = system.createAVolunteer("user2");
        assertTrue(system.addToList(1,"user1"));
        assertTrue(system.addToList(1,"user2"));
        assertEquals(2, volunteerEvent1.getVolunteerList().size());
        assertTrue(volunteerEvent1.getVolunteerList().contains(user1));
        assertTrue(volunteerEvent1.getVolunteerList().contains(user2));
    }

    @Test
    void testGetAdmin() {
        assertEquals("admin",defaultSystem.getAdmin().getCredential());
        assertEquals("code",system.getAdmin().getCredential());
    }

    @Test
    void testFindEvent() {
        volunteerEvent1 = system.createAnVolunteerEvent("event1",1);
        volunteerEvent2 = system.createAnVolunteerEvent("event2",2);
        volunteerEvent3 = system.createAnVolunteerEvent("event3",3);
        User user1 = system.createAVolunteer("user1");
        User user2 = system.createAVolunteer("user2");
        volunteerEvent1.addAVolunteer(user1);
        assertTrue(system.getAssignedEvents(user1).contains("event1"));
        volunteerEvent2.addAVolunteer(user1);
        assertTrue(system.getAssignedEvents(user1).contains("event2"));
        volunteerEvent3.addAVolunteer(user2);
        assertTrue(system.getAssignedEvents(user2).contains("event3"));
        assertEquals(2,system.getAssignedEvents(user1).size());
        assertEquals(volunteerEvent1,system.findEvent("event1 needing: 0 volunteers, Current volunteers: user1"));
        assertEquals(volunteerEvent2,system.findEvent("event2 needing: 1 volunteers, Current volunteers: user1"));
        assertEquals(volunteerEvent3,system.findEvent("event3 needing: 2 volunteers, Current volunteers: user2"));
        assertNull(system.findEvent("event4 needing: 3 volunteers, Current volunteers: user3"));
    }






}
