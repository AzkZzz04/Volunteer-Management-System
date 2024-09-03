package ui;

//import java.io.FileNotFoundException;


// Represents the system application
public class Main {
    // EFFECTS: runs the system application
    public static void main(String[] args) {
//        try {
//            new SystemApp();
//        } catch (FileNotFoundException e) {
//            System.out.println("Unable to run application: file not found");
//        }
        SystemGuiApp guiApp = new SystemGuiApp();
        guiApp.runSystem();
    }

}


