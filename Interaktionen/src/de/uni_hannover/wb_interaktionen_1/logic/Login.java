package de.uni_hannover.wb_interaktionen_1.logic;

import java.io.IOException;
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
import javafx.scene.layout.VBox;
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
     *
     * @param userID The entered personal ID of the user.
     * @param building The name of the selected building.
     * @return Returns user if the user is in the list of valid users, else return null
     */
    public User validateCredentials(String userID, String building){
        if(isOnline(userID)){
            if (!activeMicAndCam()) return null;
            System.out.println("Valid");
            try{
                db.loginOnline(userID);
                int start_room = db.getStartingHall(building);
                if(start_room != -1) {
                    setCurrentUser(new User(userID, db.getUserName(userID), db.getRoomWithRoomID(start_room, db), null));
                    db.addUserToRoom(userID, start_room);
                } else {
                    ArrayList<Room> rooms = db.getAllRooms(building);
                    for(Room r : rooms){
                        if(r.getType().equals("hall")){
                            setCurrentUser(new User(userID, db.getUserName(userID), r, null));
                            db.addUserToRoom(userID, r.getId());
                            break;
                        }
                    }
                }
                return getCurrentUser();
            } catch (SQLException ex){
                ex.printStackTrace();
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
            if(db.userIsInDB(getCurrentUser().getName())) {
                db.setOnlineStatus(getCurrentUser().getName(), false);
            }
            //threadUpdate.stopThread();
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

        String cm_message = "";
        cm.update();
            if (!cm.getCam() || !cm.getMic()) {
                if (cm.getCam() == false) cm_message += "Es wurde keine Webcam erkannt.\nOhne Webcam besteht kein Zugang zum System.\nBitte überprüfen Sie ihre Webcam-Verbindung.";
                if (cm.getMic() == false) cm_message += "Es wurde kein Mikrofon erkannt.\nOhne Mikrofon besteht kein Zugang zum System.\nBitte überprüfen Sie Inre Mikrofon-Verbindung.\n";
                err.createError(cm_message);
                return false;
            }

        return true;
    }


    /** Switch the window of the GUI to main scene when the user enters the correct ID
     * @param window Stage of the GUI
     * @param input ID of the user to be checked
     * @param l the login class
     * @param m the main class
     * @param building the name of the selected building
     */
    public void switchScene(Stage window, String input, Login l, Main m, String building){
        ReadConfig rc;
        try {
            rc = new ReadConfig();
            rc.write_Building(building);
            rc.Update();
        } catch (IOException e){
            e.printStackTrace();
        }
        setCurrentUser(input, building);
        if(currentUser != null) {
            System.out.println("You are currently logged in as: " + this.currentUser.getName());
            // Build the Main GUI
            GUIMain g = new GUIMain(l, db, m);
            VBox layout_main = new VBox();
            Scene mains = new Scene(layout_main, 700, 450);
            layout_main.getChildren().add(g.createLogoutButton(window, m.getLoginScene()));
            layout_main.getChildren().add(g.createMainGripPane2(false));
            layout_main.getChildren().add(g.createHallControl(window, m));
            window.setOnCloseRequest(e -> {
                if (getCurrentUser() != null) {
                    g.confirmLogout(window, null);
                    try {
                        db.closeConnection();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            });

            threadUpdate = ThreadUpdate.getInstance(g, db, this, m, building);
            if (!threadUpdate.isAlive()) {
                threadUpdate.start();
            }else{
                threadUpdate.startThread();
            }

            window.setScene(mains);
        } else {
            System.out.println("ERROR");
        }
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

    /** Checks that the user has the permission to log into the system and sets the current user
     * @param input Name of the user that will be set as the current user of the system
     * @param building The name of the selected building
     */
    public void setCurrentUser(String input, String building) {
        this.currentUser = validateCredentials(input, building);
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
