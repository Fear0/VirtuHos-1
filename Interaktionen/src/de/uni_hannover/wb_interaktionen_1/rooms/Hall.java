package de.uni_hannover.wb_interaktionen_1.rooms;

import de.uni_hannover.wb_interaktionen_1.Main;
import de.uni_hannover.wb_interaktionen_1.logic.User;
import de.uni_hannover.wb_interaktionen_1.test_db.TestDB;

import java.util.ArrayList;
import java.util.List;

/** The room called hall. Usage:
 * A call to the constructor will initialize the hall.
 * A group of users can be added and removed (which will also end the meeting) from the hall.
 * To get a specific group use the get_group function.
 *
 * @author Marvin Deichsel
 * */
public class Hall extends Room{
    private ArrayList<HallGroup> all_groups;
    private ArrayList<String> meetingNames;

    /** Initializes the Hall.
     *
     * @param id : id of the hall
     * @param capacity : maximum capacity of the hall
     * @param db : the database
     * */
    public Hall(int id, int capacity, TestDB db) {
        super(id, "hall", capacity, db);
        this.all_groups = new ArrayList<>();
        this.meetingNames = new ArrayList<>();
    }

    /** Adds a group to the hall.
     *  Usage: IF an entire Group gets thrown together and a meeting should be created
     *
     * @param meetingName : Name of the Meeting (appears as meeting name in BBB)
     * @param attendee : attendee password
     * @param moderator : moderator password (used to perform action such as end meeting)
     * @param users : All the users to be added
     * */
    /*public void add_entire_Group(String meetingName, String attendee, String moderator, User[] users) {
        if (all_groups.size() == getCapacity()) {
           // Add error case
            return;
        }
        all_groups.add(new HallGroup(meetingName, attendee, moderator, users));
        meetingNames.add(meetingName);
    }*/

    /** Returns the meeting identified by the name of the meeting.
     *
     * @param meetingName : Name of the Meeting (appears as meeting name in BBB)
     * @return Meeting object or null if there is no meeting with that name
     * */
    public HallGroup get_group(String meetingName){
        if(!meetingNames.contains(meetingName)) {
            // Add error case
            return null;
        }
        // implies index of group == index of meeting name, but should be true
        return all_groups.get(meetingNames.indexOf(meetingName));
    }


    /** Lets a user enter the hall.
     *
     * @param user : User that enters the hall.
     * */
    public int addUser(User user) {
        if (occupants.size() == getCapacity() || occupants.contains(user)) {
            // Add error case
            return 1;
        }
        user.setCurrent_room(this, db);
        occupants.add(user);
        return 0;
    }

    /** Lets a user join a specified group in the hall.
     *  Usage implies a creation of a new group AFTER checking the person is not alone.
     *  Wrong usage creates a group with only one Person!
     *
     * @param user : Adds a user to a specified group.
     */
    /*public Meeting user_joins_hallgroup(User user, String meetingName) {
        if (!occupants.contains(user)) {
            // Add error case
            return null;
        } else if (!meetingNames.contains(meetingName)) {
            // new group in hall
            HallGroup call = new HallGroup(meetingName, "test", "test");
            all_groups.add(call);
            meetingNames.add(meetingName);
            call.joinMeetingAs(user);
            return call;
        }
        // joining an existing meeting
        this.get_group(meetingName).joinMeetingAs(user);
        return null;
    }*/

}
