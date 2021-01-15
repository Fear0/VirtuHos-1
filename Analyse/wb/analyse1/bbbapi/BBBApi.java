package wb.analyse1.bbbapi;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Handles all BBB API calls
 */
public class BBBApi extends BBBRequest {

    private String testMeetings = null;

    /**
     * constructor
     */
    public BBBApi() {
        super();
    }

    /**
     * Creates a new meeting
     *
     * @param name        of meeting
     * @param attendeePW  attendee password
     * @param moderatorPW moderator password
     * @param meetingID   unique id
     * @param duration    minutes to end
     * @return BBB server XML response
     */
    public String createMeeting(String name,
                                String attendeePW,
                                String moderatorPW,
                                String meetingID,
                                int duration) {
        String query = "name=" + name + "&meetingID=" + meetingID
                + "&attendeePW=" + attendeePW + "&moderatorPW=" + moderatorPW
                + "&duration=" + duration;

        return super.get("create", query);
    }

    /**
     * Ends a meeting
     *
     * @param meetingID id of meeting to end
     * @param password  moderator password
     * @return BBB server XML response
     */
    public String endMeeting(String meetingID, String password) {
        String query = "meetingID=" + meetingID
                + "&password=" + password;

        return super.get("end", query);
    }

    public void setMeetingsXMLForTesting(String meetingsXML) {
        this.testMeetings = meetingsXML;
    }

    /**
     * Gets all meetings
     *
     * @return BBB server XML response
     */
    public String getMeetingsXML() {
        if (this.testMeetings != null) {
            return this.testMeetings;
        }
        return super.get("getMeetings", null);
    }

    /**
     * Gets info of a specific meeting
     *
     * @param meetingID id of meeting
     * @return BBB server XML response
     */
    public String getMeetingInfoXML(String meetingID) {
        String query = "meetingID=" + meetingID;
        return super.get("getMeetingInfo", query);

    }

    /**
     * Enables a client to join (via web browser)
     *
     * @param fullName
     * @param userID
     * @param meetingID
     * @param password
     * @return "SUCCESS" or "ERROR"
     */
    public String join(String fullName, String userID, String meetingID, String password) {
        String query = "fullName=" + fullName + "&userID=" + userID
                + "&meetingID=" + meetingID
                + "&password=" + password + "&redirect=true"
                + "&userdata-bbb_auto_join_audio=true"
                + "&userdata-bbb_force_listen_only=true"
                + "&userdata-bbb_skip_check_audio=true";

        String res = super.get("join", query);
        //System.out.println(res);
        return openBrowser(res);
    }

    /**
     * Opens the web browser and browses url
     *
     * @param url url to open
     * @return "SUCCESS" or "ERROR"
     */
    private String openBrowser(String url) {

        Desktop desktop = java.awt.Desktop.getDesktop();
        try {
            URI oURL = new URI(url);
            desktop.browse(oURL);
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
            return "Error: Could not open browser. " + e.toString();
        }
        return "SUCCESS";
    }

}
