package de.uni_hannover.wb_interaktionen_1.threads;
import de.uni_hannover.wb_interaktionen_1.logic.Login;
import de.uni_hannover.wb_interaktionen_1.logic.User;
import de.uni_hannover.wb_interaktionen_1.rooms.Room;
import de.uni_hannover.wb_interaktionen_1.test_db.TestDB;

import java.sql.SQLException;
import java.util.ArrayList;

/** Class to start a thread that retrieves and sets data from the DB
 * @author Meikel Kokowski
 * @deprecated : Merged this class with Davids class due to performance issues
 * */
public class ThreadUpdateData extends Thread {
    User user;
    ArrayList<Room> rooms;
    TestDB db;
    public Boolean stop_flag;
    public ArrayList<String> user_sent_request = new ArrayList<>();

    public ThreadUpdateData(User user, ArrayList<Room> rooms, TestDB db) {
        this.user = user;
        this.rooms = rooms;
        this.db = db;
        this.stop_flag = false;
    }

    public void run() {
        try {
            db.updateData(this.user, this.rooms, this);
        } catch(SQLException ex) {
            System.out.println("Whoops, something went wrong with the DB");
        } catch(InterruptedException ex) {
            System.out.println("Whoops, something went wrong with the Thread");
        }
    }

    public void stopThread() {
        System.out.println("Update thread stop flag has been set.");
        this.stop_flag = true;
    }
}
