package de.uni_hannover.wb_interaktionen_1.threads;

import com.mysql.cj.jdbc.exceptions.CommunicationsException;
import de.uni_hannover.wb_interaktionen_1.Main;
import de.uni_hannover.wb_interaktionen_1.gui.GUIMain;
import de.uni_hannover.wb_interaktionen_1.gui.Request;
import de.uni_hannover.wb_interaktionen_1.logic.Login;
import de.uni_hannover.wb_interaktionen_1.logic.User;
import de.uni_hannover.wb_interaktionen_1.rooms.HallGroup;
import de.uni_hannover.wb_interaktionen_1.rooms.Room;
import de.uni_hannover.wb_interaktionen_1.test_db.TestDB;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.application.Platform;


import java.sql.SQLException;
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
    private boolean comm_failed = false;
    private boolean stop_flag = false;

    //create an object of ThreadUpdateListView
    private static ThreadUpdate instance = null;

    //Get the only object available -- @Meikel Kokowski
    public static ThreadUpdate getInstance(GUIMain g, TestDB db, Login login, Main m){
        if(instance == null) {
            instance = new ThreadUpdate(g, db, login, m);
        }
        return instance;
    }

    /** The constructor for the thread
     *
     * @param list a ArrayList with all ListViews from the GUI
     * @param db the database
     * @param login the login
     */
    private ThreadUpdate(GUIMain g, TestDB db, Login login, Main m){
        setDaemon(true);
        this.list_room = g.getAll_listviews_room();
        this.capacityl = g.getAll_capacity_labels();
        this.db = db;
        this.login = login;
        this.main = m;
        this.guimain = g;
        try {
            hallgroups = db.getAllHallGroups();
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

    @Override
    public void run(){
        while (!this.isInterrupted() || !this.stop_flag){
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    try {
                        //Check for new hallgroups
                        ArrayList<HallGroup> new_hallgroups = db.getAllHallGroups();
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
                        ArrayList<Room> rooms = db.getAllRooms();
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
                        //users_in_room_old = users_in_room_new;

                        //Groups
                        list_group = guimain.getAll_listviews_group();
                        ArrayList<HallGroup> groups = db.getAllHallGroups();
                        ArrayList<ObservableList<String>> users_in_group_new = new ArrayList<>();
                        for (HallGroup g : groups) {
                            ObservableList<String> o = db.getAllUserInHallGroupObservable(g.getId());
                            users_in_group_new.add(o);
                        }
                        for (int i = 0; i < list_group.size(); i++) {
                            list_group.get(i).getItems().clear();
                            list_group.get(i).setItems(users_in_group_new.get(i));
                        }
                        //users_in_room_old = users_in_room_new;

                        //Capacity Labels updaten
                        System.out.println("OKOKOKOKOK");
                        System.out.println(capacityl.size());
                        System.out.println(users_in_room_new.size());
                        System.out.println(rooms.size());
                        for(int i = 0; i < capacityl.size(); i++){
                            if(i == 0){
                                capacityl.get(0).setText("Kapazität: " + users_in_room_new.get(0).size());
                            } else if (i <= rooms.size()){
                                capacityl.get(i).setText("Kapazität: " + users_in_room_new.get(i).size() + "/" + rooms.get(i - 1).getCapacity());
                            } else {
                                //capacityl.get(i).setText("Kapazität: " + hallgroups.get(i - rooms.size()).s);
                            }
                        }

                        //Check for a request to join a room

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
                                            //room.addUser(login.getCurrentUser());
                                            ThreadOpenWindow open_window = new ThreadOpenWindow(login.getCurrentUser(), room);
                                            open_window.start();

                                        }
                                    }
                                }
                                db.removeRequest(r.getSender(), receiver.getId());
                            }
                        }

                        if (login.getCurrentUser() != null) {
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

                    } catch (CommunicationsException ce) {
                        //reconnect to DB if communication fails
                        db.setComFailed(true);
                        System.out.println("Problem with Connection\n");


                    } catch (SQLException ex) {
                        if(!db.getComFailed()){
                            System.out.println("Problem with SQL in TestDB\n");
                        }
                    }
                }


            });

            if(db.getComFailed() == true){
                db.reconnect();
                db.setComFailed(false);
            }
            try{
                sleep(TimeUnit.SECONDS.toMillis(2));
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    public void stopThread() {
        System.out.println("Update thread stop flag has been set.");
        this.stop_flag = true;
    }
}
