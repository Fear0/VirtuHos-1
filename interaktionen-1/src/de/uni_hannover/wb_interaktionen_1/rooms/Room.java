package de.uni_hannover.wb_interaktionen_1.rooms;

import de.uni_hannover.wb_interaktionen_1.Main;
import de.uni_hannover.wb_interaktionen_1.logic.User;
import de.uni_hannover.wb_interaktionen_1.test_db.TestDB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.util.ArrayList;

/** Parent class of all rooms. Leaving a room can be handled trough here.
 *  Joining a room however needs to be handled by each child separately.
 *
 * @author Marvin Deichsel
 */
public class Room {
    private int id;
    private int capacity;
    private String type;
    public ArrayList<User> occupants;
    public TestDB db;

    /** Creates a room
     *
     * @param id the id of the room
     * @param type the type of the room (office, conference, hall)
     * @param capacity the capacity of the room
     * @param db the database
     */
    public Room(int id, String type, int capacity, TestDB db) {
        this.id = id;
        this.type = type;
        this.capacity = capacity;
        this.occupants = new ArrayList<>();
        this.db = db;
    }

    /** Getter for the id of the room.
     *
     * @return the id of the room
     */
    public int getId(){
        return id;
    }

    /** Getter for the type of the room.
     *
     * @return the type of the room
     */
    public String getType(){
        return type;
    }

    /** Getter for the capacity of the room.
     *
     * @return the capacity of the room
     */
    public int getCapacity(){
        return capacity;
    }

    /** Stub function for each addUser function in each Room subclass.
     *
     * @param user : the user to be added
     * @return integer to show success or failure
     * @throws SQLException gets thrown by pinging other users
     */
    public int addUser(User user) throws SQLException {
        return 0;
    }

    /** Converts the ArrayList occupants, which contains all users in the room, to an ObservableList.
     *  Its necessary in the GUI.
     *
     * @return The observableList with the user in the room
     */
    public ObservableList<User> getUserInRoomAsObservable(){
        ObservableList<User> list = FXCollections.observableArrayList();
        for (User u : occupants){
            list.add(u);
        }
        return list;
    }

    /** Lets a user leave a Room and his meeting in it
     *
     * @param user the user that leaves the room
     */
    public void leave_Room(User user, TestDB db, int roomID) {
        update_fromDB(db);

        /*if (!occupants.contains(user)) {
            // Add error case
            return;
        }*/

        // Because rooms without meetings can exist
        if (user.getCurrent_meeting() != null) {
            user.getCurrent_meeting().leaveMeetingAs(user, db, roomID);
        }

        occupants.remove(user);

        //We dont need this at the moment, because first useer is not added to the meeting (see addUser function)
        // To close meetings with only one person left
        /*if (occupants.size() == 1) {
            occupants.get(0).getCurrent_meeting().leaveMeetingAs(occupants.get(0), db, roomID);
        }*/
    }

    /** Quick function to test if a user is in the room.
     *
     * @param user : The user in question
     * */

    public boolean is_in_room(User user) {
        return occupants.contains(user);
    }

    /** function that updates the local room with the newest DB entries
     *
     * @param new_occupants the new current occupants list
     */
    public void update_fromDB(TestDB db) {
        //this.occupants = new_occupants;
        try {
            this.occupants = db.getAllUserInRoomAsUserList(this, db);
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    /** a stubfunction explicitly for hall to use
     *
     * @param user user that wants to join
     * @param meetingName the meeting to join
     * @return the meeting hes in then
     */
    public Meeting user_joins_hallgroup(User user, String meetingName) {return null;}

    public String toString() {
        return "RoomID: " + this.id + " type: " + this.type;
    }

    public boolean isOffice() {return false;}
}
