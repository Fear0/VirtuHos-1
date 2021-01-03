package de.uni_hannover.wb_interaktionen_1.threads;

import de.uni_hannover.wb_interaktionen_1.logic.User;
import de.uni_hannover.wb_interaktionen_1.rooms.Room;

import java.sql.SQLException;

public class ThreadOpenWindow extends Thread{
    private User current_user;
    private Room current_room;

    public ThreadOpenWindow(User user, Room room) {
        this.current_user = user;
        this.current_room = room;
    }

    public void run() {
        try {
            this.current_room.addUser(this.current_user);
        } catch(SQLException e) {
            e.printStackTrace();
            System.out.println("Program execution continues normally...");
        }
    }
}