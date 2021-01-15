package wb.analyse1.parser;

import wb.analyse1.bbbapi.*;

import java.util.ArrayList;
import java.util.List;

public class Meeting {
    private String MeetingID;
    private List<Attendee> Attendees;

    public Meeting() {
        Attendees = new ArrayList<>();
    }

    public void setAttendees(List<Attendee> attendees) {
        this.Attendees = attendees;
    }

    public void setMeetingID(String meetingID) {
        this.MeetingID = meetingID;
    }

    public String getMeetingID() {
        return MeetingID;
    }

    public List<Attendee> getAttendees() {
        return Attendees;
    }

    @Override
    public String toString() {
        String res = "";
        for (Attendee att : this.Attendees) {
            res = res + att.toString() + ", ";
        }
        String output = "Meeting: " + this.getMeetingID() + " has Attendees: " + res;
        return output.substring(0, output.length() - 2);

    }
}
