package de.uni_hannover.wb_interaktionen_1.rooms;

import de.uni_hannover.wb_interaktionen_1.bbb_api.BBBMeeting;
import de.uni_hannover.wb_interaktionen_1.logic.User;
import de.uni_hannover.wb_interaktionen_1.test_db.TestDB;
import de.uni_hannover.wb_interaktionen_1.xml.XMLParser;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/** Parent class of the different rooms. A class to generally initiate a Meeting.
 * Let new rooms extend this class and call this class (with super()) in the constructor.
 *
 * @author Meikel Kokowski
 * */
public class Meeting extends BBBMeeting {
    private String meetingID;
    private XMLParser parse;
    public String create_response;
    private String moderatorPW;
    private String attendeePW;

    /**
     * Initializes the BBB Meeting with the given Meeting name
     * and setting important properties such as the meeting ID
     *
     * @param meetingName : Name of the Meeting (appears as meeting name in BBB)
     * @param attendee    : attendee password
     * @param moderator   : moderator password (used to perform action such as end meeting)
     */
    public Meeting(String meetingName, String attendee, String moderator) {
        super();
        this.create_response = this.createMeeting(meetingName, attendee, moderator);
        this.parse = new XMLParser();
        this.meetingID = parse.getAttribute(create_response, "meetingID");
        this.attendeePW = attendee;
        this.moderatorPW = moderator;
    }

    public Meeting(){
        super();
    }

    public String getMeetingID(){
        return meetingID;
    }

    public String getMeetingInfo() {
        return super.getMeetingInfo(this.meetingID);
    }

    public void joinMeetingAs(User user, TestDB db, int roomID) {
        user.setCurrent_meeting(this);
        try {
            if (!db.hasMeeting(roomID)){
                db.setMeeting(roomID, meetingID);
                String url = joinMeeting(this.meetingID, attendeePW, user.getName().replaceAll(" ", "_"));
                user.setCreator(true);
                user.openWebpage(url);
            } else {
                user.setCreator(false);
                user.openWebpage(joinMeeting(db.getMeeting(roomID), attendeePW, user.getName().replaceAll(" ", "_")));
                //user.openWebpage(db.getMeeting(roomID));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void joinMeetingWithWebcamAs(User user, TestDB db, int roomID){
        user.setCurrent_meeting(this);
        try {
            if (!db.hasMeeting(roomID)){
                db.setMeeting(roomID, meetingID);
                String url = joinMeetingWithCam(this.meetingID, attendeePW, user.getName().replaceAll(" ", "_"));
                user.setCreator(true);
                user.openWebpage(url);
            } else {
                user.setCreator(false);
                user.openWebpage(joinMeetingWithCam(db.getMeeting(roomID), attendeePW, user.getName().replaceAll(" ", "_")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void joinGroupAs(User user, TestDB db, int groupID) {
        user.setCurrent_meeting(this);
        try {
            db.addUserToGroup(user.getId(), groupID);
            if (db.getAllUserInHallGroupObservable(groupID).size() == 1){
                db.setMeetingGroup(groupID, meetingID);
                String url = joinMeeting(this.meetingID, attendeePW, user.getName().replaceAll(" ", "_"));
                user.openWebpage(url);
            } else {
                user.openWebpage(joinMeeting(db.getMeetingGroup(groupID), attendeePW, user.getName().replaceAll(" ", "_")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void endMeeting() {
        super.endMeeting(this.meetingID, this.moderatorPW);
    }

    public int numMembers() {
        return Integer.parseInt(this.parse.getAttribute(this.getMeetingInfo(), "participantCount"));
    }

    public void leaveMeetingAs(User user, TestDB db, int roomID) {
        try {
            user.closeWebpage();
            user.setCurrent_meeting(null);
            db.deleteUserFromGroup(user.getId());
            if (user.getGroup() != null) {
                db.deleteUserFromGroup(user.getId());
                if (db.getAllUserInHallGroupObservable(user.getGroup().getId()).size() == 0){
                    endMeeting();
                    db.setMeetingGroup(user.getGroup().getId(), null); //meeting doesn't exist anymore in this case
                }
                user.setGroup(null);
                return;
            }
            if (db.getAllUserInRoom(roomID).size() <= 2) {
                //System.out.println(roomID);
                endMeeting();
                db.setMeeting(roomID, null); //meeting doesn't exist anymore in this case
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public void setUserMeeting(User user) {
        user.setCurrent_meeting(this);
    }

    /** Join a meeting with the webcam option turned on automatically
     *
     * @param meetingID Meeting ID of the meeting
     * @param password Password for the meeting
     * @param fullName Name that will be displayed in the meeting
     * @return URL after joining the meeting
     */
    public String joinMeetingWithCam(User user, String meetingID, String password, String fullName){
        user.setCurrent_meeting(this);
        String queryString = "meetingID=" + meetingID +
                "&password=" + password +
                "&fullName=" + fullName +
                "&userdata-bbb_auto_join_audio=true" +
                "&userdata-bbb_skip_check_audio=true" +
                "&userdata-bbb_listen_only_mode=false" +
                "&userdata-bbb_auto_share_webcam=true" +
                "&userdata-bbb_skip_video_preview=true";

        String response = super.CallAPI("join", queryString);
        return response;
    }

    public String joinMeeting(User user, String meetingID, String password, String fullName){
        user.setCurrent_meeting(this);
        String queryString = "meetingID=" + meetingID +
                "&password=" + password +
                "&fullName=" + fullName +
                "&userdata-bbb_skip_video_preview=true" + //No effect when not joining automatically with a webcam
                "&userdata-bbb_auto_join_audio=true" +
                "&userdata-bbb_skip_check_audio=true" +
                "&userdata-bbb_listen_only_mode=false";

        String response = this.CallAPI("join", queryString);
        return response;
        /*try {
            Desktop.getDesktop().browse(new URI(response));
        } catch (IOException e){
            e.printStackTrace();
        } catch (URISyntaxException e){
            e.printStackTrace();
        }*/
        //WebsiteOpener opener = new WebsiteOpener(response); //Creates a new BBB_URL with the created URL
        //opener.open(); // Opens the URL in the standard webbrowser
    }
}
