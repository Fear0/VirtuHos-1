package hallserver;

import BigBlueButton.api.BBBException;
import BigBlueButton.api.BBBMeeting;
import BigBlueButton.impl.BBBAPI;
import BigBlueButton.impl.BaseBBBAPI;

import java.util.ArrayList;
import java.util.Random;

public class Meeting {
    private Random random = new Random();
    private Random randomAttendeePW = new Random();
    private Random randomModeratorPW = new Random();
    private BBBAPI bbbApi = new BaseBBBAPI();
    private ArrayList<BBBMeeting> bbbMeetings= new ArrayList<BBBMeeting>();
    private int meetingId= random.nextInt(100)*10533;
    private int attendeePW=0;
    private int moderatorPW=  0;

    public int getMeetingId() { return meetingId; }

    public int getAttendeePW() {
        return attendeePW;
    }

    public void setMeetingId(int meetingId) {
        this.meetingId = meetingId;
    }

    public int getModeratorPW() {
        return moderatorPW;
    }

    public void setModeratorPW(int moderatorPW) {
        this.moderatorPW = moderatorPW;
    }

    public void setAttendeePW(int attendeePW) {
        this.attendeePW = attendeePW;
    }

    public Meeting() throws BBBException {


        bbbMeetings.add( new BBBMeeting(String.valueOf(meetingId)));

        bbbMeetings.get(bbbMeetings.size()-1).setName("Halle");
        bbbMeetings.get(bbbMeetings.size()-1).setAttendeePW(String.valueOf(0));
        bbbMeetings.get(bbbMeetings.size()-1).setModeratorPW(String.valueOf(0));
        bbbApi.createMeeting(bbbMeetings.get(bbbMeetings.size()-1));
    }

    public void join(String name)
    {
             bbbApi.joinMeeting(bbbApi.getJoinMeetingURL(String.valueOf(meetingId), "0", name));

    }
    public void end() throws BBBException {

        bbbApi.endMeeting(bbbMeetings.get(0));

    }
}
