package de.uni_hannover.wb_interaktionen_1.rooms;

import de.uni_hannover.wb_interaktionen_1.logic.User;
import de.uni_hannover.wb_interaktionen_1.test_db.TestDB;

import java.sql.SQLException;
import java.util.ArrayList;

/** A single group call in the hall. Usage:
 * A call to the constructor will start a conference.
 * A call to the second constructor can initialize and add all members of a group.
 * Using joinAs a new group member is added.
 *
 * @author Marvin Deichsel
 * */
public class HallGroup extends Meeting {

    private int id;

    /**
     * Only initiates the group call. Members need to be added later.
     *
     * @param id          : id of the group
     */
    public HallGroup(int id) {
        super("hall_group_" + id, "test", "test");
        this.id = id;
    }



    /** Getter for the id of the group.
     *
     * @return the id of the group
     */
    public int getId(){
        return this.id;
    }

    /**
     * Initiates the group call and adds the initial members (only working for one instance for now).
     * Usable if groups in hall get shuffled into bigger initial groups.
     *
     * @param meetingName : Name of the Meeting (appears as meeting name in BBB)
     * @param attendee    : attendee password
     * @param moderator   : moderator password (used to perform action such as end meeting)
     * @param users       : All Users in the Group
     */
    /*public HallGroup(String meetingName, String attendee, String moderator, User[] users) {
        super(meetingName, attendee, moderator);
        for (User user : users) {
            this.joinMeetingAs(user);
            this.current_users.add(user);
        }
    }*/

    public boolean equals(HallGroup h){
        if(h.getId() == getId()){
            return true;
        } else {
            return false;
        }
    }
}