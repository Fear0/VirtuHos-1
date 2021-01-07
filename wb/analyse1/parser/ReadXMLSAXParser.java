package wb.analyse1.parser;

import org.xml.sax.InputSource;
import wb.analyse1.bbbapi.*;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Random;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

/**
 * Parses the XML response of the meetings
 */
public class ReadXMLSAXParser {

    private List<Meeting> meetings;

    /**
     * Constructor
     */
    public ReadXMLSAXParser() {
    }

    /**
     * parses the response of the server
     *
     * @param bbb for the bigbluebutton server
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    public void invokeParser(BBBApi bbb) throws ParserConfigurationException, SAXException, IOException {
        String MeetingsXML = bbb.getMeetingsXML();
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        SAXParser saxParser = saxParserFactory.newSAXParser();
        MeetingHandler handler = new MeetingHandler();
        saxParser.parse(new InputSource(new StringReader(MeetingsXML)), handler);
        this.meetings = handler.getMeetingslist();
    }

    /**
     * Tests the Parser with custom input
     * @param msg XML Message
     */
    public void testParser(String msg) throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        SAXParser saxParser = saxParserFactory.newSAXParser();
        MeetingHandler handler = new MeetingHandler();
        saxParser.parse(new InputSource(new StringReader(msg)), handler);
        this.meetings = handler.getMeetingslist();
    }

    /**
     * @return list of all meetings
     */
    public List<Meeting> getMeetings() {
        return meetings;
    }

    /**
     * Just for test
     */
    public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException {
        DemoEnvironment.main(null);
        Random r = new Random();
        // delay for testing, should be omitted later
        try {
            Thread.sleep(r.nextInt(1000) + 7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        BBBApi bbb = new BBBApi();
        ReadXMLSAXParser parser = new ReadXMLSAXParser();
        // call parser
        parser.invokeParser(bbb);
        List<Meeting> meetings = parser.getMeetings();
        //print Meetings Information: MeetingID und Attendees
        System.out.println("\nParsed data: \n");
        for (Meeting meeting : meetings) {
            System.out.println(meeting);
        }
        ReadXMLSAXParser.endAllMeetings(bbb, "1", getMeetingIDS(meetings));
    }

    /**
     * @param meetings
     * @return IDS of the meetings in the parameter
     */
    public static String[] getMeetingIDS(List<Meeting> meetings) {
        String[] res = new String[meetings.size()];
        for (int i = 0; i < meetings.size(); i++) {
            res[i] = meetings.get(i).getMeetingID();
        }
        return res;
    }

    /**
     * ends all Meetings
     *
     * @param bbb         of the bbb server
     * @param password    of the moderators
     * @param MeetingsIDs of all meetings
     */
    public static void endAllMeetings(BBBApi bbb, String password, String[] MeetingsIDs) {
        // for testing purposes
        for (String meetingID : MeetingsIDs) {
            bbb.endMeeting(meetingID, password);
        }
    }


}
