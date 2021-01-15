package wb.analyse1.parser;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Handles the Meetingsdata
 */
public class MeetingHandler extends DefaultHandler {
    // booleans to detect attributes
    private boolean hasID = false;
    private boolean hasName = false;
    private boolean hasMeetingID = false;
    private boolean hasAttendees = false;
    // List to hold Meetings
    private List<Meeting> meetingsList = null;
    private Meeting meeting = null;
    private Attendee attendee = null;

    /**
     * @return all Meetings in the server
     */
    public List<Meeting> getMeetingslist() {
        return meetingsList;
    }

    /**
     * starts parsing when any start element is found
     *
     * @param uri        of the namesapce
     * @param localName  of the the namesapce
     * @param qName      of the qualified element name
     * @param attributes of the attached element
     * @throws SAXException
     */

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equalsIgnoreCase("meeting")) {
            meeting = new Meeting();
            if (meetingsList == null) {
                meetingsList = new ArrayList<>();
            }
        }
        if (qName.equalsIgnoreCase("meetingID")) {
            hasMeetingID = true;
        }
        if (qName.equalsIgnoreCase("attendee")) {
            hasAttendees = true;
            attendee = new Attendee();
        }

        if (qName.equalsIgnoreCase("UserID")) {
            hasID = true;
        } else if (qName.equalsIgnoreCase("fullName")) {
            hasName = true;
        }
    }

    /**
     * updates meeting data whenever end element tag is found
     *
     * @param uri       of the namesapce
     * @param localName of the the namesapce
     * @param qName     of the qualified element name
     * @throws SAXException
     */

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {

        if (qName.equalsIgnoreCase("meeting")) {
            meetingsList.add(meeting);
        }
    }

    /**
     * sets the data found in the elements
     *
     * @param ch     holds elementsdata
     * @param start  of elementsdata
     * @param length holds end of elementsdata
     * @throws SAXException
     */
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (hasMeetingID) {
            meeting.setMeetingID(new String(ch, start, length));
            hasMeetingID = false;
        }
        if (hasAttendees) {
            meeting.getAttendees().add(attendee);
            hasAttendees = false;
        }
        if (hasID) {
            attendee.setId(new String(ch, start, length));
            hasID = false;
        }
         if (hasName) {
            attendee.setName(new String(ch, start, length));
            hasName = false;
        }
    }
}

