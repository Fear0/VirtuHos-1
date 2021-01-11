package de.uni_hannover.wb_interaktionen_1.bbb_api;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;

/** This Class  can generate Links for the BBB-API. With them you can do different tasks,
 * like creating new meetings, join a meeting, end a meeting or get all active meetings.
 *
 * @author David Sasse
 */
public class BBBMeeting {

    //Start of the Link. It is ever the same.
    private String API_URL = "https://bbb2.se.uni-hannover.de/bigbluebutton/api/";

    /** Creates a new meeting
     *
     * This methode crates a new meeting and gives all important informations about the meeting back.
     *
     * @param name The name of the meeting.
     * @param attendeePW The password for the attendees of the meeting.
     * @param moderatorPW The password for the moderator of the meeting.
     * @return A String with the XML response of the BBB-Server. It contains the informations and attributes of the meeting.
     */
    public String createMeeting(String name, String attendeePW, String moderatorPW){
        String queryString = "name=" + name +
                            "&meetingID=" + UUID.randomUUID() + //Creates a random ID for the meeting
                            "&attendeePW=" + attendeePW +
                            "&moderatorPW=" + moderatorPW;

        String response = this.CallAPI("create", queryString);
        return response;
    }

    /** Let you join a meeting
     *
     * This methode creates a link to join a meeting and opens the link in the standard webbrowser.
     *
     * @param meetingID ID of the meeting to join.
     * @param password Password for the meeting (attendee Password or moderator password).
     * @param fullName Name that is displayed in the meeting.
     * @return The URL for joining the meeting.
     */
    public String joinMeeting(String meetingID, String password, String fullName){
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

    /** Join a meeting with the webcam option turned on automatically
     *
     * @param meetingID Meeting ID of the meeting
     * @param password Password for the meeting
     * @param fullName Name that will be displayed in the meeting
     * @return URL after joining the meeting
     */
    public String joinMeetingWithCam(String meetingID, String password, String fullName){
        String queryString = "meetingID=" + meetingID +
                "&password=" + password +
                "&fullName=" + fullName +
                "&userdata-bbb_auto_join_audio=true" +
                "&userdata-bbb_skip_check_audio=true" +
                "&userdata-bbb_listen_only_mode=false" +
                "&userdata-bbb_auto_share_webcam=true" +
                "&userdata-bbb_skip_video_preview=true";

        String response = this.CallAPI("join", queryString);
        return response;
    }

    /** Get all ongoing meetings
     *
     * Creates a link to get all ongoing meetings and returns the ongoing meeting in a string.
     *
     * @return A String with the XML response of the BBB-Server. Contains the informations about the ongoing meetings.
     */
    public String getMeetings() {
        String response = this.CallAPI("getMeetings", "");
        return  response;
    }

    /** Get infos about a specific meeting
     *
     * Creates a link to get the informations of the meeting with the given ID and returns the
     * information of the meeting in a string.
     *
     * @param meetingID ID of the meeting from which you want the informations
     * @return A String with the XML response of the BBB-Server. Contains the informations about the meeting.
     */
    public String getMeetingInfo(String meetingID) {
        String query = "meetingID=" + meetingID;
        String response = this.CallAPI("getMeetingInfo", query);
        return response;
    }

    /** Ends a meeting
     *
     * Crates a link to the BBB-API to end a meeting. It ends the meeting and kicks out all participants.
     *
     * @param meetingID ID of the meeting to end.
     * @param password Moderator password of the meeting.
     * @return A String with the XML response of the BBB-Server. Contains a confirmation about the end of the meeting.
     */
    public String endMeeting(String meetingID, String password){
        String queryString = "meetingID=" + meetingID +
                "&password=" + password;
        String response = this.CallAPI("end", queryString);
        return response;
    }

    /** Makes a call to the BBB-API.
     *
     * This methode creates the full URL for the BBB-API and calls the URL via the HTTPClient.
     *
     * @param apiMethod The methode of the API (e.g. join, create, ...).
     * @param queryString The String with the necessary informations for the API call.
     * @return The XML response from the BBB-Server as a string and for the join the url to join the meeting as a string.
     */
    public String CallAPI(String apiMethod, String queryString) {
        String requestUri;
        // Creates the complete URL for the API call.
        if (queryString == null) { //Case to get all ongoing meetings.
            requestUri = API_URL + apiMethod + "?checksum=" + this.ComputeChecksum(apiMethod, queryString);
        } else { //Case for all other API calls.
            requestUri = API_URL + apiMethod + '?' + queryString + "&checksum=" + this.ComputeChecksum(apiMethod, queryString);
        }

        // For the join methode only URL as a string should be returned, because we have to open this in the browser.
        if(apiMethod == "join"){
            return requestUri;
        }

        // The HttpClient calls the url and writes the answer from the BBB-Server in a String
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(requestUri)).build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (IOException ex){
            ex.printStackTrace();
        } catch (InterruptedException ex){
            ex.printStackTrace();
        }
        return null;
    }

    /** Computes the checksum for the URL.
     *
     * Every API call or URL needs a unique checksum. It ist calculated with the decryption SHA-1
     * out of the parameters and name of the API methode.
     *
     * @param callName The name of the API methode (e.g. join, create, ...).
     * @param queryString The parameters for the API call.
     * @return The checksum as a string.
     */
    private String ComputeChecksum(String callName, String queryString) {
        String securitySalt = "Z69sY1UtTsFRg2jXez1lNhfG4f5dTza4ZLZSixTycI"; //Secret ID of the BBB-Server

        String data = callName + queryString + securitySalt;

        DecryptSHA1 decrypt_sha1 = new DecryptSHA1(data);
        return decrypt_sha1.decode();
    }

    /* Creates meeting where the participants are muted at the start. May be used for Halle. */
    protected String createMeetingMutedOnStart(String name, String attendeePW, String moderatorPW){
        String queryString = "name=" + name +
                "&meetingID=" + UUID.randomUUID() + //Creates a random ID for the meeting
                "&attendeePW=" + attendeePW +
                "&moderatorPW=" + moderatorPW +
                "&muteOnStart=true";

        String response = this.CallAPI("create", queryString);
        return response;
    }
}
