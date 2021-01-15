package de.uni_hannover.wb_interaktionen_1.rooms;

import de.uni_hannover.wb_interaktionen_1.bbb_api.BBBMeeting;
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
public class HallGroup {

    private int id;
    private Meeting meeting;
    public ArrayList<User> occupants;
    public TestDB db;

    /**
     * Only initiates the group call. Members need to be added later.
     *
     * @param id          : id of the group
     */
    public HallGroup(int id, TestDB db) {
        //super("hall_group_" + id, "test", "test");
        this.id = id;
        this.db = db;
        this.occupants = new ArrayList<>();
    }

    /** Getter for the id of the group.
     *
     * @return the id of the group
     */
    public int getId(){
        return this.id;
    }


    public int addUser(User user) throws SQLException {
        update_fromDB(db);
        user.setGroup(this);
        //user.setCurrent_room(this, db);
        if (occupants.size() == 0) {
            occupants.add(user);  // add user to user list
            meeting = new Meeting("hallgroup_" + getId(), "test", "test");
            meeting.joinGroupAs(user, db, getId());
        } else {
            occupants.add(user);  // add user to user list
            db.addUserToGroup(user.getId(), getId());
            meeting = new Meeting();
            user.setCurrent_meeting(meeting);
            user.openWebpage(meeting.joinMeeting(db.getMeetingGroup(getId()), "test", user.getName().replaceAll(" ", "_"), user.getId()));
            //meeting.joinGroupAs(user, db, getId());
        }
        return 0;
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

    /** function that updates the local room with the newest DB entries
     *
     * @param db The database
     */
    public void update_fromDB(TestDB db) {
        try {
            this.occupants = db.getAllUserInGroupAsUserList(this);
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
}