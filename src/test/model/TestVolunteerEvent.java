package model;

import model.exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestVolunteerEvent {
    VolunteerEvent volunteerEvent1;
    VolunteerEvent volunteerEvent2;
    Volunteer volunteer1;
    Volunteer volunteer2;
    Volunteer volunteer3;



    @BeforeEach
    void beforeEach() {
        volunteerEvent1 = new VolunteerEvent("event1",2);
        volunteerEvent2 = new VolunteerEvent("event2",1);
        volunteer1 = new Volunteer("volunteer1");
        volunteer2 = new Volunteer("volunteer2");
        volunteer3 = new Volunteer("volunteer3");

    }

    @Test
    void testConstructor() {
        assertEquals("event1", volunteerEvent1.getEventName());
        assertEquals(2, volunteerEvent1.getNeededVolunteerNum());
        assertEquals(0, volunteerEvent1.getVolunteerList().size());
    }

    @Test
    void testAddVolunteerSingle() {
        assertTrue(volunteerEvent1.addAVolunteer(volunteer1));
        assertEquals(1, volunteerEvent1.getVolunteerList().size());
        assertTrue(volunteerEvent1.getVolunteerList().contains(volunteer1));
    }

    @Test
    void testAddVolunteerVolunteerFull() {
        assertTrue(volunteerEvent2.addAVolunteer(volunteer1));
        assertTrue(volunteerEvent2.isFull());
        assertEquals(1, volunteerEvent2.getVolunteerList().size());
        assertTrue(volunteerEvent2.getVolunteerList().contains(volunteer1));
    }

    @Test
    void testAddVolunteerAlreadyExist() {
        try {
            assertTrue(volunteerEvent1.addAVolunteer(volunteer1));
            assertTrue(volunteerEvent1.getVolunteerList().contains(volunteer1));
            volunteerEvent1.addAVolunteer(volunteer1);
            fail("Did not throw VolunteerAlreadyExistsException");
        } catch (VolunteerAlreadyExistsException e) {
            // expected
        } catch (TooManyVolunteerException e) {
            fail("TooManyVolunteerException thrown unexpectedly");
        }

        assertEquals(1, volunteerEvent1.getVolunteerList().size());
    }

    @Test
    void testAddVolunteerToFull() {
        try {
            assertTrue(volunteerEvent1.addAVolunteer(volunteer1));
            assertTrue(volunteerEvent1.addAVolunteer(volunteer2));
            assertTrue(volunteerEvent1.isFull());
            assertTrue(volunteerEvent1.getVolunteerList().contains(volunteer1));
            assertTrue(volunteerEvent1.getVolunteerList().contains(volunteer2));
            volunteerEvent1.addAVolunteer(volunteer3);
            fail("Did not throw TooManyVolunteerException");
        } catch (TooManyVolunteerException e) {
            // expected
        } catch (VolunteerAlreadyExistsException e) {
            fail("VolunteerAlreadyExistsException thrown unexpectedly");
        }

        assertFalse(volunteerEvent1.getVolunteerList().contains(volunteer3));
    }

    @Test
    void testToStringEmpty() {
        assertEquals("event1 needing: 2 volunteers, Current volunteers: ", volunteerEvent1.toString());
    }

    @Test
    void testToStringSingle() {
        volunteerEvent1.addAVolunteer(volunteer1);
        assertEquals("event1 needing: 1 volunteers, Current volunteers: volunteer1", volunteerEvent1.toString());
    }



}
