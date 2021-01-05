package BigBlueButton.api;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Object for a BigBlueButton meeting.
 * @author Adrian Fish
 * Based on: https://github.com/sakaicontrib/bbb-tool/blob/master/api/src/java/org/sakaiproject/bbb/api/BBBMeeting.java
 * 
 * Last modified by Yunkai Wang
 * Last modified by Mohamed Moussa
 */
@Getter @Setter @ToString
public class BBBMeeting {
	private String name = null;
	private String meetingID;
	private String attendeePW = null;
	private String moderatorPW = null;
	private String dialNumber = null;
	private String voiceBridge = null;
	private String webVoice = null;
	private String logoutURL = null;
	private Boolean record = null;
	private Long duration = null;
	
	// user cannot directly modify this field
	@Setter (AccessLevel.NONE)
	private Map<String, String> meta = new HashMap<String, String>();
	private String moderatorOnlyMessage = null;
	private Boolean autoStartRecording = null;
	private Boolean allowStartStopRecording = null;
	private Boolean webcamsOnlyForModerator = null;
	private String logo = null;
	private String copyright = null;
	private Boolean muteOnStart = null;
	private String welcome = null;
	private Date startDate = null;
	private Date endDate = null;
	
	public BBBMeeting(String meetingID) {
		this.meetingID = meetingID;
	}
	
	public void addMeta(String key, String value) {
		meta.put(key, value);
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setModeratorPW(String moderatorPW) {
		this.moderatorPW = moderatorPW;
	}

	public void setAttendeePW(String attendeePW) {
		this.attendeePW = attendeePW;
	}

	public void setAllowStartStopRecording(Boolean allowStartStopRecording) {
		this.allowStartStopRecording = allowStartStopRecording;
	}

	public void setAutoStartRecording(Boolean autoStartRecording) {
		this.autoStartRecording = autoStartRecording;
	}

	public void setCopyright(String copyright) {
		this.copyright = copyright;
	}

	public void setDialNumber(String dialNumber) {
		this.dialNumber = dialNumber;
	}

	public void setDuration(Long duration) {
		this.duration = duration;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public void setLogoutURL(String logoutURL) {
		this.logoutURL = logoutURL;
	}

	public void setMeetingID(String meetingID) {
		this.meetingID = meetingID;
	}

	public void setMeta(Map<String, String> meta) {
		this.meta = meta;
	}

	public void setModeratorOnlyMessage(String moderatorOnlyMessage) {
		this.moderatorOnlyMessage = moderatorOnlyMessage;
	}

	public void setMuteOnStart(Boolean muteOnStart) {
		this.muteOnStart = muteOnStart;
	}

	public void setRecord(Boolean record) {
		this.record = record;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public void setVoiceBridge(String voiceBridge) {
		this.voiceBridge = voiceBridge;
	}

	public void setWebcamsOnlyForModerator(Boolean webcamsOnlyForModerator) {
		this.webcamsOnlyForModerator = webcamsOnlyForModerator;
	}

	public void setWebVoice(String webVoice) {
		this.webVoice = webVoice;
	}

	public void setWelcome(String welcome) {
		this.welcome = welcome;
	}

	public String getAttendeePW() {
		return attendeePW;
	}

	public String getName() {
		return name;
	}

	public String getModeratorPW() {
		return moderatorPW;
	}

	public String getMeetingID() {
		return meetingID;
	}

	public Boolean getAllowStartStopRecording() {
		return allowStartStopRecording;
	}

	public Boolean getAutoStartRecording() {
		return autoStartRecording;
	}

	public Boolean getMuteOnStart() {
		return muteOnStart;
	}

	public Boolean getRecord() {
		return record;
	}

	public Boolean getWebcamsOnlyForModerator() {
		return webcamsOnlyForModerator;
	}

	public Date getEndDate() {
		return endDate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public Long getDuration() {
		return duration;
	}

	public Map<String, String> getMeta() {
		return meta;
	}

	public String getCopyright() {
		return copyright;
	}

	public String getDialNumber() {
		return dialNumber;
	}

	public String getLogo() {
		return logo;
	}

	public String getLogoutURL() {
		return logoutURL;
	}

	public String getModeratorOnlyMessage() {
		return moderatorOnlyMessage;
	}

	public String getVoiceBridge() {
		return voiceBridge;
	}

	public String getWebVoice() {
		return webVoice;
	}

	public String getWelcome() {
		return welcome;
	}

	public void removeMeta(String key) {
		if (meta.containsKey(key))
			meta.remove(key);
	}
}
