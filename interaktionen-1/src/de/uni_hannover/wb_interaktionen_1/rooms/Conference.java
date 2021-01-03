package de.uni_hannover.wb_interaktionen_1.rooms;

import de.uni_hannover.wb_interaktionen_1.Main;
import de.uni_hannover.wb_interaktionen_1.bbb_api.BBBMeeting;
import de.uni_hannover.wb_interaktionen_1.gui.ErrorMessage;
import de.uni_hannover.wb_interaktionen_1.logic.User;
import de.uni_hannover.wb_interaktionen_1.test_db.TestDB;
import java.lang.Thread;

import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;

/** A conference. Usage:
 * Base class that represents an conference room. Is used to manage all conference ongoings
 * like meetings.
 * @author Joshua Berger and David Sasse
 * */
public class Conference extends Room{
    private ErrorMessage err = new ErrorMessage();

    /** Creates the conference room. A call to the constructor will start a conference.
     * @param id: the id of the conference room
     * @param capacity: determines how many user can be in this room at the same time
     * @param db: the database
     * */
    public Conference(int id, int capacity, TestDB db) {
        super(id, "conference", capacity, db);
    }

    /** Adds a user to the conference. If the user is the second occupant, a BBB-Conference is started. If the user is
     * the third or higher occupant he is added to the meeting unless the maximum capacity is reached then the user does
     * not get added.
     * @param user: user to add
     * @return 0 if the user has been successfully added. 1 if the user could not be added
     */
    public int addUser(User user) throws SQLException {
        update_fromDB(db);
        if (false) {
           /* if (db.getAllUserInRoom(getId()).contains(user.getId())) {
                user.setCreator(false);
                //current_meeting.joinMeetingWithWebcamAs(user, db, getId());
                System.out.println("OKOOKOKOKOKOKO: " + getId() + "     " + db.getMeeting(getId()));
                Meeting current_meeting = new Meeting(getType() + "_" + getId(), "test", "test");
                user.openWebpage(current_meeting.joinMeetingWithCam(user, db.getMeeting(getId()), "test", user.getName()));
                return 0;
            } else {
                err.createError("Dieser Raum ist bereits voll.");
                user.getCurrent_room().addUser(user);
                return 1;  // return 1 when capacity is already full
            } */
            System.out.println("no");
            return 1;
        }
        else {
            user.setCurrent_room(this, db);
            if (occupants.size() == 0) {
                occupants.add(user);  // add user to user list
                user.setCreator(false);
            } else if (occupants.size() == 1) {
                occupants.add(user);  // add user to user list
                user.setCreator(true);
                Meeting current_meeting = new Meeting(getType() + "_" + getId(), "test", "test");
                System.out.println("OKOKOKKOKOKOKOKOKKOKO:" + current_meeting.create_response);
                //current_meeting.joinMeetingAs(occupants.get(0)); //add the first person
                //db.setPingFor(occupants.get(0).getId(), true);
                db.sendRequest(occupants.get(0).getId(), occupants.get(0).getId(), "self");
                current_meeting.joinMeetingWithWebcamAs(occupants.get(1), db, getId());
                System.out.println("I'm here");

            } else {
                occupants.add(user);  // add user to user list
                user.setCreator(false);
                //current_meeting.joinMeetingWithWebcamAs(user, db, getId());
                System.out.println("OKOOKOKOKOKOKO: " + getId() + "     " + db.getMeeting(getId()));
                Meeting current_meeting = new Meeting(getType() + "_" + getId(), "test", "test");
                user.openWebpage(current_meeting.joinMeetingWithCam(user, db.getMeeting(getId()), "test", user.getName()));
            }
            return 0;
        }
    }
}
