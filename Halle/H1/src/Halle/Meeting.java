package Halle;

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
    private int meetingId;
    private int attendeePW;
    private int moderatorPW;

    public int getMeetingId() { return meetingId; }

    public void setMeetingId(int meetingId) {
        this.meetingId = meetingId;
    }

    public int getAttendeePW() {
        return attendeePW;
    }

    public void setAttendeePW(int attendeePW) {
        this.attendeePW = attendeePW;
    }

    public int getModeratorPW() {
        return moderatorPW;
    }

    public void setModeratorPW(int moderatorPW) {
        this.moderatorPW = moderatorPW;
    }

    public Meeting() throws BBBException {


        bbbMeetings.add( new BBBMeeting(String.valueOf(meetingId)));
        bbbMeetings.get(bbbMeetings.size()-1).setName("Halle");
        bbbMeetings.get(bbbMeetings.size()-1).setAttendeePW(String.valueOf(attendeePW));
        bbbMeetings.get(bbbMeetings.size()-1).setModeratorPW(String.valueOf(moderatorPW));
        bbbApi.createMeeting(bbbMeetings.get(bbbMeetings.size()-1));
    }
    public Meeting(int meetingId ,int attendeePW,int moderatorPW) throws BBBException {

        this.meetingId=meetingId;
        this.attendeePW=0;
        this.moderatorPW=0;
      /*  bbbMeetings.add( new BBBMeeting(String.valueOf(meetingId)));
        bbbMeetings.get(bbbMeetings.size()-1).setName("Halle");
        bbbMeetings.get(bbbMeetings.size()-1).setAttendeePW(String.valueOf(attendeePW));
        bbbMeetings.get(bbbMeetings.size()-1).setModeratorPW(String.valueOf(moderatorPW));
        bbbApi.createMeeting(bbbMeetings.get(bbbMeetings.size()-1));*/

    }
    public void join(String name)
    {
             bbbApi.joinMeeting(bbbApi.getJoinMeetingURL(String.valueOf(meetingId), String.valueOf(attendeePW), name));

    }
    public boolean isrunning()throws BBBException{

      return   bbbApi.isMeetingRunning(Integer.toString(meetingId));
    }
    public void end() throws BBBException {

        bbbApi.endMeeting(bbbMeetings.get(0));

    }
}
