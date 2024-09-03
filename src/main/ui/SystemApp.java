package ui;


import model.VolunteerEvent;
import model.ManagementSystem;
import model.User;
import model.exceptions.OverMaxEventsException;
import model.exceptions.TooManyVolunteerException;
import model.exceptions.VolunteerAlreadyExistsException;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

// Represents an application that manages events and volunteers
public class SystemApp {

    private static final String JSON_STORE = "./data/save.json";
    private final Scanner input;
    private ManagementSystem system;
    private final JsonWriter jsonWriter;
    private final JsonReader jsonReader;

    // Construct a volunteer management system application
    public SystemApp() throws FileNotFoundException {
        this.system = new ManagementSystem();
        this.input = new Scanner(System.in);
        this.jsonWriter = new JsonWriter(JSON_STORE);
        this.jsonReader = new JsonReader(JSON_STORE);
//        this.system = loadSystem();
        runSystem();
    }

    // EFFECTS: runs the system
    public void runSystem() {
        printInstructions();
        getCredentials();

    }

    // EFFECTS: prints out instructions
    private void printInstructions() {
        System.out.println("Welcome to the Volunteer Management System!\n");
        System.out.println("Enter your name/passcode to login:");
        System.out.println("If you are a volunteer, enter your name to login as a volunteer.");
        System.out.println("If you are a new user, enter your name to create a new volunteer account.");
        System.out.println("If you are an admin, enter your passcode to login as an admin.");
        System.out.println("If you want to exit and save the files, enter \"exit\".");
        System.out.println("if you want to load the files, enter \"load\".");
    }

    // EFFECTS: gets the credential from user
    private void getCredentials() {
        if (input.hasNextLine()) {
            String credential = input.nextLine();

            if (credential.equals("exit")) {
                saveSystem();
                System.out.println("Bye!");
            } else if (credential.equals("load")) {
                this.system = loadSystem();
                runSystem();
            } else {
                userLogIn(credential);
                runSystem();
            }
        }
    }

    // EFFECTS: classify the user type and log in
    private void userLogIn(String credential) {
        var users = system.getVolunteers();
        if (users.containsKey(credential)) {
            var user = users.get(credential);
            if (user == system.getAdmin()) {
                adminLogIn();
            } else {
                volunteerLogIn(credential);
            }
        } else {
            volunteerLogIn(credential);
        }


    }

    // EFFECTS: log in as an admin
    private void adminLogIn() {
        System.out.println("Welcome, admin!");
        System.out.println("1. View all events");
        System.out.println("2. View all volunteers");
        System.out.println("3. Create an event");
        System.out.println("4. add a volunteer to an event");
        System.out.println("5. exit");
        getCommand();
    }

    // EFFECTS: gets the command from admin
    //          if the command is valid, execute the command
    //          otherwise, print out "Invalid command!"
    private void getCommand() {
        if (input.hasNextLine()) {
            String command = input.nextLine();
            executeCommand(command);
        }

    }

    // EFFECTS: executes the command
    private void executeCommand(String command) {
        switch (command) {
            case "1":
                viewAllEvents();
                break;
            case "2":
                viewAllVolunteers();
                break;
            case "3":
                createAnEvent();
                break;
            case "4":
                addAVolunteerToAnEvent();
                break;
            case "5":
                System.out.println("Bye!");
                break;
            default:
                System.out.println("Invalid command!");
                break;
        }

        if (!command.equals("5")) {
            adminLogIn();
        }
    }

    

    // EFFECTS: prints out all events
    private void viewAllEvents() {
        var events = system.getEvents();
        if (events.isEmpty()) {
            System.out.println("There is no event.");
            return;
        }


        for (int i = 0; i < events.size(); i++) {
            System.out.println((i + 1) + ". " + events.get(i).toString());
        }

    }

    // EFFECTS: prints out all volunteers
    private void viewAllVolunteers() {
        var volunteers = system.getVolunteers();
        if (volunteers.isEmpty()) {
            System.out.println("There is no volunteer.");
            return;
        }

        for (final var u : volunteers.values()) {
            if (u != system.getAdmin()) {
                System.out.println(u.getName());
            }
        }
    }

    // MODIFIES: this.system
    // EFFECTS: asks the admin to enter the name of the event and the number of volunteers needed
    //          if the number of events is over the max number, print out "Over max events!"
    //          otherwise, create an event and print out "Event created: " + event
    private void createAnEvent() {
        System.out.println("Enter the name of the event:");
        String eventName = input.nextLine();

        System.out.println("Enter the number of volunteers needed:");
        int neededVolunteerNum = input.nextInt();
        input.nextLine();
        try {
            VolunteerEvent volunteerEvent = system.createAnVolunteerEvent(eventName, neededVolunteerNum);
            System.out.println("Event created: " + volunteerEvent);
        } catch (OverMaxEventsException e) {
            System.out.println(e.getMessage());
        }

    }

    // MODIFIES: this.system
    // EFFECTS: asks the admin to enter the number of the event and the name of the volunteer
    private void addAVolunteerToAnEvent() {

        if (!system.getEvents().isEmpty()
                && !system.getVolunteers().isEmpty()) {

            viewAllEvents();
            System.out.println("Enter the number of the event:");
            int eventNumber = input.nextInt();
            input.nextLine();
            viewAllVolunteers();
            System.out.println("Enter the name of the volunteer:");
            String volunteerName = input.nextLine();


            try {
                boolean added = system.addToList(eventNumber, volunteerName);
                if (added) {
                    System.out.println("Volunteer added!");
                }
            } catch (TooManyVolunteerException
                     | VolunteerAlreadyExistsException e1) {
                System.out.println(e1.getMessage());
            }
        } else {
            System.out.println("There is no event or volunteer.");
        }
    }

    // EFFECTS: log in as a volunteer
    //          if the volunteer does not exist, create a new volunteer
    //          Then, print out "Welcome, " + volunteer.getName() + "!"
    //          Then, print out the events that the volunteer is assigned to
    private void volunteerLogIn(String credential) {
        var users = system.getVolunteers();
        if (!users.containsKey(credential)) {
            system.createAVolunteer(credential);
        }
        var volunteer = users.get(credential);
        System.out.println("Welcome, " + volunteer.getName() + "!");
        getAssignedEvents(volunteer);
    }

    // EFFECTS: prints out the events that the volunteer is assigned to
    private void getAssignedEvents(User volunteer) {
        viewAllEvents();
        var events = system.getEvents();
        StringBuilder assigned = new StringBuilder();
        for (final var e : events) {
            if (e.getVolunteerList().contains(volunteer)) {
                assigned.append(e.getEventName()).append(", ");
            }
        }

        if (assigned.length() == 0) {
            System.out.println("You are not assigned to any event.");
        } else {
            System.out.println("You are assigned to " + assigned);
            System.out.println();
        }
    }

    // EFFECTS: saves the system to file
    private void saveSystem() {
        try {
            jsonWriter.open();
            jsonWriter.write(system);
            jsonWriter.close();
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + "./data/testReaderGeneralManageSystem.json");
        }
    }

    // EFFECTS: loads system from file
    private ManagementSystem loadSystem() {
        ManagementSystem s;
        try {
            s = jsonReader.read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return s;
    }


}
