package de.uni_hannover.wb_interaktionen_1.i_face;

import de.uni_hannover.wb_interaktionen_1.gui.ErrorMessage;
import de.uni_hannover.wb_interaktionen_1.gui.Request;
import de.uni_hannover.wb_interaktionen_1.logic.Login;
import de.uni_hannover.wb_interaktionen_1.logic.User;
import de.uni_hannover.wb_interaktionen_1.rooms.HallGroup;
import de.uni_hannover.wb_interaktionen_1.rooms.Room;
import de.uni_hannover.wb_interaktionen_1.test_db.TestDB;
import de.uni_hannover.wb_interaktionen_1.threads.ThreadOpenWindow;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * The Controler from Interaktion1. You can access all important functions, that Interaction1
 * provides for the other groups of VirtuHoS 1.
 */
public class InteraktionControl {
    Login login;
    TestDB db;

    /** Thr constructor for the Control.
     *
     * @param login The login class
     * @param db The database.
     */
    public InteraktionControl(Login login, TestDB db){
        this.login = login;
        this.db = db;
    }

    /** This methode adds new rooms to the database.
     *
     * @param capacity The maximum number of users in the room.
     * @param type The type of the room (office, conference or hall).
     * @return Returns the room ID if the add was successful, on error -1 is returned.
     */
    public int addRoom(int capacity, String type, String buildingID){
        try{ //Try adding the building first and then the room
            db.addBuilding(buildingID);
            if(type.equals("office")){
                return db.createOffice(capacity, buildingID);
            } else if(type.equals("conference")){
                return db.createConference(capacity, buildingID);
            } else if(type.equals("hall")){
                return db.createHall(capacity, buildingID);
            }
        } catch (SQLException exception){ //Catch the exception the DB throws if a building with that name already exists
            try{ // Add the rooms normally without creating the building
                if(type.equals("office")){
                    return db.createOffice(capacity, buildingID);
                } else if(type.equals("conference")){
                    return db.createConference(capacity, buildingID);
                } else if(type.equals("hall")){
                    return db.createHall(capacity, buildingID);
                }
            } catch (SQLException e){
                e.printStackTrace();
                return -1;
            }
        }
        return -1;
    }

    /** Adds groups in the hall to the database.
     *
     * @param roomID The roomID of the corresponding hall.
     */
    public void addHallGroup(int roomID){
        try{
            db.createGroup(roomID);
        } catch (SQLException s){
            s.printStackTrace();
        }
    }

    /** Deletes a group in a hall in the database.
     *
     * @param groupID The ID of the group that has to be deleted.
     */
    public void deleteHallGroup(int groupID){
        try{
            db.deleteGroup(groupID);
        } catch (SQLException s){
            s.printStackTrace();
        }
    }

    /** Logs in the user.
     *
     * @param userID The ID of user entered on the login screen.
     * @return True, if the login was successfully and false, if not.
     */
    public boolean login(String userID){
        if(login.isOnline(userID)){

            try{
                db.loginOnline(userID);
                login.setCurrentUser(new User(userID, db.getUserName(userID), db.getRoomWithRoomID(0, db), null));
                //db.addUserToRoom(userID, 0);
                return true;
            } catch (SQLException ex){
                ex.printStackTrace();
            }
        }
        return false;
    }

    /**
     *  Logs out the user.
     */
    public void logout(){
        System.out.println("Wird aufgerufen");
        if(login.getCurrentUser() !=null) {
            if (login.getCurrentUser().getCurrent_room() != null) {
                login.getCurrentUser().getCurrent_room().leave_Room(login.currentUser, db, login.getCurrentUser().getCurrent_room().getId());
            }
            System.out.println("kurz vor offline");
            login.setCurrentUserOffline();
            login.resetCurrentUser();
        }
    }

    /** Sends a request to another user to turn on the webcam.
     *
     * @param userID The ID of the user, who should turn on the webcam.
     */
    public void requestWebcam(String userID){
            if (login.currentUser.getCurrent_room().isOffice()) {
                try {
                    if (db.getAllUserInRoom(login.currentUser.getCurrent_room().getId()).contains(userID)) {
                        db.sendRequest(login.currentUser.getId(), userID, "webcam");
                        //ToDo Kann auch durch eine Error Nachricht von Editor ersetzt werden.
                        ErrorMessage e = new ErrorMessage();
                        e.createError("Die Anfrage zur Aktivierung der Webcam wurde erfolgreich an " + db.getUserName(userID) + " verschickt.");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
    }

    /** Moves the user from one room to another. It has to be called, when the own user is moved to
     * another room and when the own user moves another user in his room (invite the other user). It
     * can also add a user to a group in a hall.
     *
     * @param userID The ID of the user that is moved.
     * @param roomID The ID of the room, where the user is moved to. If is_hallgroup is true, than it is the ID of the group.
     * @param is_hallgroup Indicates if the room is a real room (false) or its a group in the hall (true).
     */
    public void moveUser(String userID, int roomID, boolean is_hallgroup){
        System.out.println(userID);
        System.out.println(roomID);
        try {
            if (userID.equals(login.currentUser.getId())) {
                if (!is_hallgroup) {
                    ArrayList<Room> existent_rooms = db.getAllRooms();
                    for (Room room : existent_rooms) {
                        if (room.getId() == roomID) {
                            if (db.getRoomWithRoomID(roomID, db).getCapacity() == db.getAllUserInRoom(roomID).size()) {
                                ErrorMessage err = new ErrorMessage();
                                err.createError("Dieser Raum ist bereits voll.");
                            } else {
                                if (login.currentUser.getCurrent_room() != null) {
                                    login.currentUser.getCurrent_room().leave_Room(login.currentUser, db, login.currentUser.getCurrent_room().getId());
                                }
                                room.addUser(login.currentUser);
                            }
                        }
                    }
                } else {
                    ArrayList<HallGroup> hall_groups = db.getAllHallGroups();
                    for (HallGroup g : hall_groups) {
                        if (g.getId() == roomID) {
                            if (login.currentUser.getCurrent_room().getId() != db.getCorrespondingHalltoHallGroup(g.getId())) {
                                //ToDo Kann auch durch eine Error Nachricht von Editor ersetzt werden.
                                ErrorMessage e = new ErrorMessage();
                                e.createError("Please enter the hall first! (Fehlerfall nur relevant im Mockup tbh)");
                                return;
                            } else {
                                if (login.currentUser.getCurrent_room() != null) {
                                    login.currentUser.getCurrent_room().leave_Room(login.currentUser, db, login.currentUser.getCurrent_room().getId());
                                }
                                g.addUser(login.currentUser);
                            }
                        }
                    }
                }

                // Invite another user in the same room
            } else if (login.getCurrentUser().getCurrent_room().getId() == roomID) {
                if (db.getAllUserInRoom(roomID).size() < db.getRoomWithRoomID(roomID, db).getCapacity()) {
                    db.sendRequest(login.currentUser.getId(), userID, "join");
                    //ToDo Kann auch durch eine Error Nachricht von Editor ersetzt werden.
                    ErrorMessage e = new ErrorMessage();
                    e.createError("Die Einladung wurde erfolgreich an " + db.getUserName(userID) + " verschickt.");
                } else {
                    //ToDo Kann auch durch eine Error Nachricht von Editor ersetzt werden.
                    ErrorMessage e = new ErrorMessage();
                    e.createError("Die Einladung wurde nicht an " + db.getUserName(userID) + " verschickt, weil der Raum bereits voll ist.");
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * This methode checks for requests on the database to the current user
     */
    public void checkRequest(){
        try {
            ArrayList<Room> rooms = db.getAllRooms();
            if (login.getCurrentUser() != null) {
                User receiver = login.getCurrentUser();
                ArrayList<Request> request = db.getRequests(receiver);
                if (request.size() != 0) {
                    Request r = request.get(0);
                    if (r.getType().equals("join")) {
                        Room roomToJoin = db.getRoomWithRoomID(db.findRoomFor(r.getSender()), db);
                        r.createRequest(roomToJoin, rooms);
                    } else if (r.getType().equals("reject")) {
                        r.createRejectMessage("join");
                    } else if (r.getType().equals("webcam")) {
                        r.createWebcamRequest();
                    } else if (r.getType().equals("rejectwebcam")) {
                        r.createRejectMessage("webcam");
                    } else if (r.getType().equals("self")) {
                        for (Room room : rooms) {
                            if (room.getId() == db.findRoomFor(r.getSender())) {
                                ThreadOpenWindow open_window = new ThreadOpenWindow(login.getCurrentUser(), room);
                                open_window.start();
                            }
                        }
                    }
                    db.removeRequest(r.getSender(), receiver.getId());
                }

                /* updating user in rooms; room name has to be room id, integer ... */
                for (Room room : rooms) {
                    room.occupants = db.getAllUserInRoomAsUserList(room, db);
                }

                /* Set online_status_2 (ask Alan for the purpose) */
                db.updateOnline(login.getCurrentUser().getId());
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    /** Return all the rooms for the given building
     *
     * @param buildingID
     * @return
     */
    public ArrayList<Room> getAllRoomsInBuilding(String buildingID) {
        try {
            return db.getAllRooms(buildingID);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /** Get the name of the user to a given ID
     *
     * @param userID ID of the user
     * @return Username of the users given ID
     */
    public String getUserName(String userID){
        try{
            return db.getUserName(userID);
        } catch (SQLException ex){
            ex.printStackTrace();
            return null;
        }
    }

    /** Delete a building with a given ID/name
     *
     * @param buildingID ID of the building you want to delete
     */
    public void deleteBuilding(String buildingID){
        try{
            db.deleteBuilding(buildingID);
        } catch (SQLException ex){
            ex.printStackTrace();
            return;
        }
    }
}
