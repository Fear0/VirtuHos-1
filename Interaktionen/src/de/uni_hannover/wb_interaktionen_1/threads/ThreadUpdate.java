package de.uni_hannover.wb_interaktionen_1.threads;


import de.uni_hannover.wb_interaktionen_1.Main;
import de.uni_hannover.wb_interaktionen_1.gui.ErrorMessage;
import de.uni_hannover.wb_interaktionen_1.gui.GUIMain;
import de.uni_hannover.wb_interaktionen_1.gui.Request;
import de.uni_hannover.wb_interaktionen_1.logic.Login;
import de.uni_hannover.wb_interaktionen_1.logic.ReadConfig;
import de.uni_hannover.wb_interaktionen_1.logic.User;
import de.uni_hannover.wb_interaktionen_1.rooms.HallGroup;
import de.uni_hannover.wb_interaktionen_1.rooms.Room;
import de.uni_hannover.wb_interaktionen_1.test_db.TestDB;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.application.Platform;


import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLNonTransientConnectionException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * This thread updates the listviews in the GUI and handles the requests
 * @author David Sasse, Meikel Kokowski
 */
public class ThreadUpdate extends Thread{
    //ArrayList<ObservableList<String>> users_in_room_old = new ArrayList<>();
    private ArrayList<ListView<String>> list_room;
    private ArrayList<ListView<String>> list_group;
    private ArrayList<Label> capacityl;
    private ArrayList<HallGroup> hallgroups;
    private TestDB db;
    private Login login;
    private Main main;
    private GUIMain guimain;
    private String building;
    private boolean comm_failed = false;
    private boolean stop_flag = false;
    private boolean stop_create = false;
    //create an object of ThreadUpdateListView
    private static ThreadUpdate instance = null;

    //Get the only object available -- @Meikel Kokowski
    public static ThreadUpdate getInstance(GUIMain g, TestDB db, Login login, Main m, String building){
        if(instance == null) {
            instance = new ThreadUpdate(g, db, login, m, building);
        }
        instance.setBuilding(building);
        instance.setList_room(g.getAll_listviews_room());
        instance.setList_group(g.getAll_listviews_group());
        return instance;
    }

    /** The constructor for the thread
     *
     * @param g the GUI
     * @param db the database
     * @param login the login
     * @param m the main class
     */
    private ThreadUpdate(GUIMain g, TestDB db, Login login, Main m, String building){
        try{
            setDaemon(true);
            this.list_room = g.getAll_listviews_room();
            this.list_group = g.getAll_listviews_group();
            this.capacityl = g.getAll_capacity_labels();
            this.db = db;
            this.login = login;
            this.main = m;
            this.guimain = g;
            this.building = building;
            this.hallgroups = db.getAllHallGroups(building);

        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    /** The setter for the arraylist with the hallgroups
     *
     * @param hallgroups the arraylist with the hallgroups
     */
    public void setHallgroups(ArrayList<HallGroup> hallgroups) {
        this.hallgroups = hallgroups;
    }

    public void setList_room(ArrayList<ListView<String>> list_room) {
        this.list_room = list_room;
    }

    public void setList_group(ArrayList<ListView<String>> list_group) {
        this.list_group = list_group;
    }

    public void setBuilding(String building){
        this.building = building;
    }

    public void stop_create(boolean b){
        this.stop_create = b;
    }

    public void check_connection(){

        try{
            db.userIsOnline(login.getCurrentUser().getId());
        }catch (SQLNonTransientConnectionException ce){
            //reconnect to DB if communication fails
            System.out.println("Problem with Connection\n");
            db.reconnect();
        }catch(SQLException se){
            se.printStackTrace();
            System.out.println("Problem with SQL\n");
        }
    }

    @Override
    public void run(){
        while (!this.isInterrupted()) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //Check for new hallgroups
                            ArrayList<HallGroup> new_hallgroups = db.getAllHallGroups(building);
                            if (hallgroups.size() == new_hallgroups.size()) {
                                for (int i = 0; i < new_hallgroups.size(); i++) {
                                    if (!hallgroups.get(i).equals(new_hallgroups.get(i))) {
                                        main.updatemain(main.getWindow());
                                    }
                                }
                            } else {
                                main.updatemain(main.getWindow());
                            }
                            hallgroups = new_hallgroups;

                            //Update the Listviews
                            //Rooms
                            ArrayList<Room> rooms = db.getAllRooms(building);
                            ArrayList<ObservableList<String>> users_in_room_new = new ArrayList<>();
                            users_in_room_new.add(db.getAllUserWithoutRoomObservable());

                            for (Room r : rooms) {
                                ObservableList<String> o = db.getAllUserInRoomObservable(r.getId());
                                users_in_room_new.add(o);
                            }

                            for (int i = 0; i < list_room.size(); i++) {
                                list_room.get(i).getItems().clear();
                                list_room.get(i).setItems(users_in_room_new.get(i));
                            }

                            //Groups
                            ArrayList<HallGroup> groups = db.getAllHallGroups(building);
                            ArrayList<ObservableList<String>> users_in_group_new = new ArrayList<>();

                            for (HallGroup g : groups) {
                                ObservableList<String> o = db.getAllUserInHallGroupObservable(g.getId());
                                users_in_group_new.add(o);
                            }
                            for (int i = 0; i < groups.size(); i++) {
                                list_group.get(i).getItems().clear();
                                list_group.get(i).setItems(users_in_group_new.get(i));
                            }

                            //Capacity Labels updaten
                            for (int i = 0; i < capacityl.size(); i++) {
                                if (i == 0) {
                                    capacityl.get(0).setText("Kapazität: " + users_in_room_new.get(0).size());
                                } else if (i <= rooms.size()) {
                                    capacityl.get(i).setText("Kapazität: " + users_in_room_new.get(i).size() + "/" + rooms.get(i - 1).getCapacity());
                                } else {
                                    //capacityl.get(i).setText("Kapazität: " + hallgroups.get(i - rooms.size()).s);
                                }
                            }

                            //Check for a request to join a room
                            checkRequest();


                        } catch (SQLException ex) {
                            System.out.println("Problem with SQL in TestDB\n");
                        }

                    }
                });


            do {
                try {
                    sleep(TimeUnit.SECONDS.toMillis(2));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (this.stop_flag);

            check_connection();


        }
    }

    public void stopThread() {
        System.out.println("Update thread stop flag has been set.");
        this.stop_flag = true;
    }
    public void startThread() {
        System.out.println("Update thread start flag has been set.");
        this.stop_flag = false;
    }

    /**
     * This methode checks for requests on the database to the current user
     */
    public void checkRequest(){
        try {
            ArrayList<Room> rooms = db.getAllRooms(building);
            System.out.println(building);
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
                    } else if (r.getType().equals("leave")){
                        receiver.getCurrent_meeting().leaveMeetingAs(receiver, db, receiver.getCurrent_room().getId());
                    } else if (r.getType().equals("self")) {
                        for (Room room : rooms) {
                            if (room.getId() == db.findRoomFor(r.getSender())) {
                                //room.addUser(login.getCurrentUser());
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
                    System.out.print("RoomID: ");
                    System.out.print(room.getId());
                    System.out.print(" Occupants: ");
                    System.out.print(room.occupants);
                    System.out.println();
                }
                System.out.println();

                /* Set online_status_2 (ask Alan for the purpose) */
                db.updateOnline(login.getCurrentUser().getId());
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
}
