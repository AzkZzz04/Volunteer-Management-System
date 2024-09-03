package persistence;

import model.ManagementSystem;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;


// Citation: code adapted from JsonSerializationDemo by Paul Carter
// https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
public class JsonWriterTest extends JsonTest{

    @Test
    void testWriterInvalidFile() {
        JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
        try {
            writer.open();
            fail("IOException thrown unexpectedly");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyManageSystem() {
        JsonWriter writer = new JsonWriter("./data/testWriterEmptyManageSystem.json");
        try {
            ManagementSystem ms1 = new ManagementSystem();
            writer.open();
            writer.write(ms1);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyManageSystem.json");
            ManagementSystem ms2 = reader.read();
            assertEquals("admin", ms2.getAdmin().getCredential());
            assertEquals(1, ms2.getVolunteers().size());
            assertEquals(0, ms2.getEvents().size());
        } catch (IOException e) {
            fail("IOException should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralManageSystem() {

        try {
            ManagementSystem ms2 = writeAndReadManagementSystem();
            assertEquals(4, ms2.getVolunteers().size());
            assertEquals("test", ms2.getAdmin().getCredential());
            assertEquals("v1", ms2.getVolunteers().get("v1").getCredential());
            assertEquals("v2", ms2.getVolunteers().get("v2").getCredential());
            assertEquals("v3", ms2.getVolunteers().get("v3").getCredential());
            assertEquals(2, ms2.getEvents().size());
            checkEvent("e1", 1, ms2.getEvents().get(0));
            checkEvent("e2", 2, ms2.getEvents().get(1));
            assertTrue(ms2.getEvents().get(0).getVolunteerList().contains(ms2.getVolunteers().get("v1")));
            assertTrue(ms2.getEvents().get(1).getVolunteerList().contains(ms2.getVolunteers().get("v2")));
            assertTrue(ms2.getEvents().get(1).getVolunteerList().contains(ms2.getVolunteers().get("v3")));
        } catch (IOException e) {
            fail("IOException should not have been thrown");
        }
    }


}
