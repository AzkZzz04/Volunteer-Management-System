package ui;

import model.VolunteerEvent;
import model.ManagementSystem;
import model.User;
import model.log.Event;
import model.log.EventLog;
import ui.buttons.SaveButton;
import ui.buttons.LoadButton;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Color;
import java.awt.Image;

import java.io.FileNotFoundException;
import java.io.IOException;


// Represents the Volunteer Management System application with GUI
public class SystemGuiApp {

    private JFrame frame;
    private ManagementSystem system;
    private JList<String> eventList;
    private JList<String> userList;
    private JPanel adminPanel;
    private final Image iconImage = new ImageIcon("./data/icon.png").getImage();

    // EFFECTS: runs the system
    public void runSystem() {
        frame.setVisible(true);
    }

    // EFFECTS: set up a system application
    public SystemGuiApp() {
        setUp();
    }

    // MODIFIES: this
    // EFFECTS: sets up the system application
    private void setUp() {
        system = new ManagementSystem();
        initialFrame();
        adminPanel = createAdminPanel();
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                for (var eventIterator = EventLog.getInstance().iterator(); eventIterator.hasNext();) {
                    Event logEvent = eventIterator.next();
                    System.out.println(logEvent);
                }
                EventLog.getInstance().clear();
                System.out.println(EventLog.getInstance().iterator().next());
            }
        });
    }

    // MODIFIES: this
    // EFFECTS: creates a frame
    private void initialFrame() {
        frame = new JFrame("Volunteer Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setSize(1000, 500);
        frame.setLocationRelativeTo(null);
        frame.add(setWelcomePanel(), BorderLayout.NORTH);
        frame.add(setIOButtons(), BorderLayout.SOUTH);
        updateEventListModel();
        updateUserListModel();
    }

    // EFFECTS: creates a panel with welcome message
    private JPanel setWelcomePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 3));
        panel.setLocation(0,0);
        Image image = iconImage.getScaledInstance(50, 50, Image.SCALE_DEFAULT);
        ImageIcon icon = new ImageIcon(image);
        JLabel welcome = new JLabel("Please enter your name/passcode to login:" + "  ", icon, JLabel.CENTER);
        panel.add(welcome);

        JTextField textField = new JTextField();
        panel.add(textField);

        JButton loginButton = setLoginButton(textField);
        panel.add(loginButton);

        return panel;
    }

    // EFFECTS: creates a panel with login button
    private JButton setLoginButton(JTextField textField) {
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> {
            String credential = textField.getText();
            if (credential.equals("exit")) {
                removeAdminPanel();
                return;
            }

            var users = system.getVolunteers();
            if (users.containsKey(credential)) {
                var user = users.get(credential);
                if (user == system.getAdmin()) {
                    addAdminPanel();
                } else {
                    loginDialog(user);
                }
            } else {
                User volunteer = system.createAVolunteer(credential);
                signUpDialog(volunteer.getName());
                updateUserListModel();
            }
        });

        return loginButton;
    }

    // MODIFIES: this
    // EFFECTS: add admin panel to the frame
    private void addAdminPanel() {
        frame.add(adminPanel, BorderLayout.EAST);
        frame.revalidate();
    }

    // MODIFIES: this
    // EFFECTS: remove admin panel from the frame
    private void removeAdminPanel() {
        frame.remove(adminPanel);
        frame.revalidate();
    }


    // EFFECTS: creates a panel with admin buttons
    private JPanel createAdminPanel() {
        JPanel adminPanel = new JPanel();
        adminPanel.setBackground(Color.BLACK);
        adminPanel.setLayout(new GridLayout(4, 1));
        JTextField eventName = new JTextField("Event Name");
        JTextField neededVolunteerNum = new JTextField("Needed Volunteer Number");
        adminPanel.add(eventName);
        adminPanel.add(neededVolunteerNum);

        JButton addEventButton = createAddEventButton(eventName, neededVolunteerNum);
        adminPanel.add(addEventButton);

        JButton addVolunteerButton = createAddVolunteerButton();
        adminPanel.add(addVolunteerButton);

        return adminPanel;
    }


    // EFFECTS: creates a addEventButton
    private JButton createAddEventButton(JTextField eventName, JTextField neededVolunteerNum) {
        JButton addEventButton = new JButton("add Event");
        addEventButton.addActionListener(e -> {
            String name = eventName.getText();
            int num = Integer.parseInt(neededVolunteerNum.getText());
            system.createAnVolunteerEvent(name, num);
            updateEventListModel();
        });
        return addEventButton;
    }

    // EFFECTS: creates a addVolunteerButton
    private JButton createAddVolunteerButton() {
        JButton addVolunteerButton = new JButton("add Volunteers");
        addVolunteerButton.addActionListener(e -> {
            User volunteer = system.getVolunteers().get(userList.getSelectedValue());
            VolunteerEvent volunteerEvent = system.findEvent(eventList.getSelectedValue());
            volunteerEvent.addAVolunteer(volunteer);
            updateEventListModel();
        });
        return addVolunteerButton;
    }


    // MODIFIES: this
    //EFFECTS: create a dialog for login
    private void loginDialog(User user) {
        var assignedEvents = system.getAssignedEvents(user);
        JDialog dialog = new JDialog(frame, "Hello, " + user.getName() + "!");
        dialog.setSize(250, 250);
        dialog.setLayout(new GridLayout(assignedEvents.size() + 1, 1));
        dialog.add(new JLabel("You are assigned to:"));
        for (String s : assignedEvents) {
            dialog.add(new JLabel(s));
        }
        dialog.setVisible(true);
    }

    // EFFECTS: creates a dialog for sign up
    private void signUpDialog(String credential) {
        JDialog dialog = new JDialog(frame, "Sign Up");
        dialog.setSize(250, 250);
        dialog.setLayout(new GridLayout(2, 1));
        dialog.add(new JLabel("Hi, " + credential + "!"));
        dialog.add(new JLabel("You have successfully signed up!"));
        dialog.setVisible(true);
    }

    // EFFECTS: creates a panel with buttons
    private JPanel setIOButtons() {
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2));

        LoadButton loadButton = createLoadButton();
        SaveButton saveButton = createSaveButton();

        buttonPanel.add(loadButton);
        buttonPanel.add(saveButton);

        return buttonPanel;
    }

    // EFFECTS: creates a load button
    private LoadButton createLoadButton() {
        LoadButton loadButton = new LoadButton();
        loadButton.addActionListener(e -> {
            try {
                system = loadButton.getJsonReader().read();
                updateEventListModel();
                updateUserListModel();
            } catch (IOException ex) {
                System.out.println("Unable to read from file: " + loadButton.getPath());
            }
        });

        return loadButton;
    }


    // EFFECTS: creates a save button
    private SaveButton createSaveButton() {
        SaveButton saveButton = new SaveButton();
        saveButton.addActionListener(e -> {
            try {
                saveButton.getJsonWriter().open();
            } catch (FileNotFoundException ex) {
                System.out.println("Unable to save system to " + saveButton.getPath());
            }
            saveButton.getJsonWriter().write(system);
            saveButton.getJsonWriter().close();
        });

        return saveButton;
    }


    // MODIFIES: this
    // EFFECTS: creates a ListModel for events
    private void updateEventListModel() {
        DefaultListModel<String> eventListModel = new DefaultListModel<>();
        for (VolunteerEvent e : system.getEvents()) {
            eventListModel.addElement(e.toString());
        }

        if (eventList != null) {
            eventList.setModel(eventListModel);
        } else {
            eventList = new JList<>(eventListModel);
            frame.add(eventList, BorderLayout.CENTER);
        }

    }

    // MODIFIES: this
    // EFFECTS: creates a ListModel for users
    private void updateUserListModel() {
        DefaultListModel<String> userListModel = new DefaultListModel<>();
        for (User u : system.getVolunteers().values()) {
            if (u != system.getAdmin()) {
                userListModel.addElement(u.getName());
            }
        }

        if (userList != null) {
            userList.setModel(userListModel);
        } else {
            userList = new JList<>(userListModel);
            userList.setBackground(Color.lightGray);
            frame.add(userList, BorderLayout.WEST);
        }

    }


}
