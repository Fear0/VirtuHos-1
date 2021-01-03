package de.uni_hannover.wb_interaktionen_1.logic;

import java.sql.SQLException;
import java.util.ArrayList;

import de.uni_hannover.wb_interaktionen_1.Main;
import de.uni_hannover.wb_interaktionen_1.gui.GUIMain;
import de.uni_hannover.wb_interaktionen_1.threads.ThreadUpdate;
import de.uni_hannover.wb_interaktionen_1.threads.ThreadUpdateData;
import de.uni_hannover.wb_interaktionen_1.gui.ErrorMessage;
import de.uni_hannover.wb_interaktionen_1.mic_cam.CamMicValidation;
import de.uni_hannover.wb_interaktionen_1.rooms.Room;
import de.uni_hannover.wb_interaktionen_1.test_db.TestDB;
import javafx.scene.Scene;
import javafx.stage.Stage;

/** Class with necessary functions to log into the system.
 * Keeps track of the users currently using the system
 * @author David Sebode
 * @version 1.5
 */
public class Login {

    public User currentUser; //User that is currently logged into the system
    public TestDB db;
    private ThreadUpdate threadUpdate;
    ErrorMessage err = new ErrorMessage();
    //private final Room starting_room = new Room(0, "office", 10, db); // will be changed later anyways

    /** Fill the list of users with the database
     *
     * @param db the database
     */
    public Login(TestDB db){
        this.db = db;
    }

    /** The getter for the Thread threadupdate
     *
     * @return the thread
     */
    public ThreadUpdate getThreadUpdate() {
        return threadUpdate;
    }

    /** Check whether the user is in the list
     * @return Returns user if the user is in the list of valid users, else return null
     */
    public User validateCredentials(String userID){
        if(isOnline(userID)){
            //if (!activeMicAndCam()) return null;
            System.out.println("Valid");
            try{
                db.loginOnline(userID);
                setCurrentUser(new User(userID, userID, db.getRoomWithRoomID(0, db)));
                db.addUserToRoom(userID, 0);
                return getCurrentUser();
            } catch (SQLException ex){
                System.out.println("Error on setting DB status to online");
            }
        }
        ErrorMessage m = new ErrorMessage();
        m.createError("Die Personalnummer ist nicht korrekt.\nBitte überprüfen Sie ihre Eingabe.");
        System.out.print("Invalid\n");
        return null;
    }

    /** Set the status of the current user as offline in the database when the button "logout" is pressed.
     */
    public void setCurrentUserOffline(){
        try{
            if(db.userIsInDB(getCurrentUser().getName())) db.setOnlineStatus(getCurrentUser().getName(), false);
        } catch (SQLException ex){
            System.out.println("Error when trying to communicating with the database");
        }
    }


    /** Check if a user with a given ID is online somewhere else or not
     * @param userID User that will be checked
     * @return False if the user is not in the database or not online, else true
     */
    public boolean isOnline(String userID){
        try{
            if(db.userIsInDB(userID) && !db.userIsOnline(userID)){
                return true;
            } else {
                System.out.println("User has not been found in database or is logged in somewhere else");
                return false;
            }
        } catch (SQLException ex){
            System.out.println("Error retrieving data from the database");
            return false;
        }
    }

    /** Check if there is a webcam and microphone the user can use
     * @return True if a webcam and mic are present, else false;
     * @author Alan Dryaev & David Sebode
     */
    public boolean activeMicAndCam(){
        CamMicValidation cm = new CamMicValidation();
        String cm_input = "yes";

        while(cm_input.equals("yes")) {
            String cm_message = "";
            cm.update();
            cm_input = "";
            if (!cm.getCam() || !cm.getMic()) {
                if (cm.getCam() == false) cm_message += "Keine Webcam erkannt. Bitte überprüfen Sie Ihre Webcam-Verbindung.\n";
                if (cm.getMic() == false) cm_message += "Kein Mikrofon erkannt. Bitt überprüfen Sie Inre Mikrofon-Verbindung.\n";
                cm_message += "Versuchen Sie anschließend sich erneut zu verbinden.\n";
                err.createError(cm_message);
                return false;
            }
        }
        return true;
    }


    /** Switch the window of the GUI to main scene when the user enters the correct ID
     * @param window Stage of the GUI
     * @param input ID of the user to be checked
     * @param next Next scene that will be displayed when the credentials are correct
     */
    public void switchScene(Stage window, String input, Scene next, GUIMain g, Main m){
        currentUser = validateCredentials(input);
        if(currentUser != null) {
            System.out.println("You are currently logged in as: " + this.currentUser.getName());
            threadUpdate = ThreadUpdate.getInstance(g, db, this, m);
            if (!threadUpdate.isAlive()) {
                threadUpdate.start();
            }
            window.setScene(next);
        } else {
            System.out.println("ERROR");
        }
    }


    private ThreadUpdateData callUpdate() {
        /* Initialize data */
        try {
            // Parse Rooms from DB
            ArrayList<Room> rooms = this.db.getAllRooms();
            /* Call update DB */
            System.out.println(getCurrentUser().getName());
            ThreadUpdateData update_database = new ThreadUpdateData(getCurrentUser(), rooms, getDB());
            update_database.start();
            return update_database;
        } catch(SQLException ex) {
            System.out.println("s");
        }
        return null;
    }


    /** Reset all the values of the current user in the database
     */
    public void resetCurrentUser(){
        try {
            db.resetUser(currentUser.getId());
        } catch (SQLException ex) {
            System.out.println("SQL exception");
        }
    }


    /** Set the user logged into the system
     * @param currentUser User that will be set as the current user of the system
     */
    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }


    /** Get the current user of the system
     * @return Current user
     */
    public User getCurrentUser(){ return currentUser;}


    /** Get the database
     * @return The database of the logic object
     */
    public TestDB getDB(){return db;}


}
