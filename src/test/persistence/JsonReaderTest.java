package persistence;

import model.ManagementSystem;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;


// Citation: code adapted from JsonSerializationDemo by Paul Carter
// https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
public class JsonReaderTest extends JsonTest{
    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            reader.read();
            fail("IOException thrown unexpectedly");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyWorkRoom() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyManageSystem.json");
        try {
            ManagementSystem ms = reader.read();
            assertEquals("admin", ms.getAdmin().getCredential());
            assertEquals(1, ms.getVolunteers().size());
        } catch (IOException e) {
            fail("IOException should not have been thrown");
        }
    }

    @Test
    void testReaderGeneralWorkRoom() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralManageSystem.json");
        try {
            ManagementSystem ms = reader.read();
            assertEquals(4, ms.getVolunteers().size());
            assertEquals("test", ms.getAdmin().getCredential());
            assertTrue(ms.getVolunteers().containsKey("v1"));
            assertTrue(ms.getVolunteers().containsKey("v2"));
            assertTrue(ms.getVolunteers().containsKey("v3"));
            assertEquals(2, ms.getEvents().size());
            checkEvent("e1", 1, ms.getEvents().get(0));
            checkEvent("e2", 2, ms.getEvents().get(1));
            assertTrue(ms.getEvents().get(0).getVolunteerList().contains(ms.getVolunteers().get("v1")));
            assertTrue(ms.getEvents().get(1).getVolunteerList().contains(ms.getVolunteers().get("v2")));
            assertTrue(ms.getEvents().get(1).getVolunteerList().contains(ms.getVolunteers().get("v3")));
        } catch (IOException e) {
            fail("IOException should not have been thrown");
        }
    }
}

