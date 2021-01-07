import GUI.*;
import javafx.application.Application;
import definitions.DatabaseCommunication;

public class Main {
    public static void main(String[] args) {
        DatabaseCommunication.initialize();
        Application.launch(MainMenu.class);
    }
}