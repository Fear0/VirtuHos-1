package wb.analyse1.analyse;

import wb.analyse1.GUI.*;
import org.xml.sax.SAXException;
import wb.analyse1.Database.Database;
import wb.analyse1.bbbapi.*;
import wb.analyse1.parser.Attendee;
import wb.analyse1.parser.Meeting;
import wb.analyse1.parser.ReadXMLSAXParser;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {

    public static void waiting(int time) {
        Random r = new Random();
        try {
            Thread.sleep(r.nextInt(1000) + time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException {

        // just for test, will be omited. Analyse loop will take place here
        System.out.println("Analyse");
        BBBApi bbb = new BBBApi();
        Analyse analyse = new Analyse();
        /*int[][] matrix = {{0, 2, 1, 1, 1, 0, 0, 1},
                {2, 0, 1, 1, 0, 0, 0, 0},
                {1, 1, 0, 0, 0, 0, 0, 0},
                {1, 1, 0, 0, 0, 0, 0, 0},
                {1, 0, 0, 0, 0, 1, 1, 1},
                {0, 0, 0, 0, 1, 0, 1, 0},
                {0, 0, 0, 0, 1, 1, 0, 0},
                {1, 0, 0, 0, 1, 0, 0, 0}};

        analyse.calculateCliques(matrix);*/

        Database db = new Database();
        db.setAnalyse(analyse);
        ReadXMLSAXParser parser = new ReadXMLSAXParser();
        GUIThread guiThread = new GUIThread(analyse.getNetworkMatrix(), analyse.getUsers(), analyse.getOnlineUsers());
        //just for test
        db.deleteAllUsers();
        //just for test
        db.deleteAllInteractions();
        analyse.setUsers(db.fetchUsers());
        analyse.setNetworkMatrix(db.fetchNetworkMatrix());


        //try this loop
       /* ArrayList<ArrayList<String>> meetings = new ArrayList<ArrayList<String>>();
        meetings.add(DemoEnvironment.userTest_1_1);
        meetings.add(DemoEnvironment.userTest_1_2);
        meetings.add(DemoEnvironment.userTest_2_1);
        meetings.add(DemoEnvironment.userTest_2_2);
        DemoEnvironment.generateEnvironment(1, DemoEnvironment.userTest_2_1);
        //ReadXMLSAXParser.endAllMeetings(bbb, "1", ReadXMLSAXParser.getMeetingIDS(parser.getMeetings()));
        waiting(8000);
        while (true){
            Random r = new Random();
            //DemoEnvironment.generateEnvironment(1, meetings.get(r.nextInt(4)));
            //waiting(8000);
            parser.invokeParser(bbb);
            //ReadXMLSAXParser.endAllMeetings(bbb, "1", ReadXMLSAXParser.getMeetingIDS(parser.getMeetings()));
            analyse.updateNetworkMatrix(parser.getMeetings());
            //ReadXMLSAXParser.endAllMeetings(bbb, "1", ReadXMLSAXParser.getMeetingIDS(parser.getMeetings()));
            analyse.degreeCentrality(true);
            analyse.cliqueAnalysis();
            analyse.betweennessAndCloseness();
            analyse.eigenvectorCentrality(10);
            analyse.printUsers();
            db.insertInOrUpdateInteractionTable();
            db.insertInOrUpdateUsersTable();
            guiThread.setModel(analyse.getNetworkMatrix(),analyse.getUsers(),analyse.getUsers());
            //guiThread.refresh();
            waiting(5000);
        }*/


        //ReadXMLSAXParser parser = new ReadXMLSAXParser();
        ReadXMLSAXParser.endAllMeetings(bbb, "1", ReadXMLSAXParser.getMeetingIDS(parser.getMeetings()));
        DemoEnvironment.generateEnvironment(1, DemoEnvironment.userTest_2_1);
        waiting(8000);
        parser.invokeParser(bbb);
        parser.getMeetings().forEach((n) -> System.out.println(n));
        analyse.updateNetworkMatrix(parser.getMeetings());
        analyse.getOnlineUsers().forEach((n) -> System.out.println(n + ", "));
        analyse.cliqueAnalysis();
        //GUIThread guiThread = new GUIThread(analyse.getNetworkMatrix(), analyse.getUsers(), analyse.getOnlineUsers());
        analyse.betweennessAndCloseness();

        ReadXMLSAXParser.endAllMeetings(bbb, "1", ReadXMLSAXParser.getMeetingIDS(parser.getMeetings()));
        analyse.degreeCentrality(true);
        analyse.printUsers();

        db.insertInOrUpdateInteractionTable();
        db.insertInOrUpdateUsersTable();

        guiThread.setModel(analyse.getNetworkMatrix(), analyse.getUsers(), analyse.getOnlineUsers());

        waiting(2000);
        DemoEnvironment.generateEnvironment(1, DemoEnvironment.userTest_2_2);
        waiting(8000);
        parser.invokeParser(bbb);
        parser.getMeetings().forEach((n) -> System.out.println(n));
        analyse.updateNetworkMatrix(parser.getMeetings());
        analyse.getOnlineUsers().forEach((n) -> System.out.println(n + ", "));

        analyse.cliqueAnalysis();

        analyse.betweennessAndCloseness();

        ReadXMLSAXParser.endAllMeetings(bbb, "1", ReadXMLSAXParser.getMeetingIDS(parser.getMeetings()));
        analyse.degreeCentrality(true);
        analyse.printUsers();

        db.insertInOrUpdateInteractionTable();
        db.insertInOrUpdateUsersTable();

        guiThread.setModel(analyse.getNetworkMatrix(), analyse.getUsers(), analyse.getOnlineUsers());


        waiting(2000);
        DemoEnvironment.generateEnvironment(1, DemoEnvironment.userTest_1_1);
        waiting(8000);
        parser.invokeParser(bbb);
        ReadXMLSAXParser.endAllMeetings(bbb, "1", ReadXMLSAXParser.getMeetingIDS(parser.getMeetings()));
        analyse.updateNetworkMatrix(parser.getMeetings());
        analyse.getOnlineUsers().forEach((n) -> System.out.println(n + ", "));

        analyse.betweennessAndCloseness();

        analyse.networkDensity();
        analyse.eigenvectorCentrality(10);
        analyse.degreeCentrality(true);
        analyse.cliqueAnalysis();

        db.insertInOrUpdateUsersTable();
        db.insertInOrUpdateInteractionTable();

        // deletion is just for testing, omit this
        //db.deleteAllUsers();
        //db.deleteAllInteractions();
        guiThread.setModel(analyse.getNetworkMatrix(), analyse.getUsers(), analyse.getOnlineUsers());

        waiting(2000);
        DemoEnvironment.generateEnvironment(1, DemoEnvironment.userTest_3_1);
        waiting(8000);
        parser.invokeParser(bbb);
        ReadXMLSAXParser.endAllMeetings(bbb, "1", ReadXMLSAXParser.getMeetingIDS(parser.getMeetings()));
        analyse.updateNetworkMatrix(parser.getMeetings());
        analyse.getOnlineUsers().forEach((n) -> System.out.println(n + ", "));

        analyse.betweennessAndCloseness();

        analyse.networkDensity();
        analyse.eigenvectorCentrality(10);
        analyse.degreeCentrality(true);
        analyse.cliqueAnalysis();

        db.insertInOrUpdateUsersTable();
        db.insertInOrUpdateInteractionTable();

        guiThread.setModel(analyse.getNetworkMatrix(), analyse.getUsers(), analyse.getOnlineUsers());

        db.close();
        analyse.printUsers();


        //ReadXMLSAXParser.endAllMeetings(bbb, "1", ReadXMLSAXParser.getMeetingIDS(parser.getMeetings()));
        //analyse.eigenvectorCentrality(10);
        //analyse.degreeCentrality(true);
        //analyse.printUsers();*/

       /* List<Meeting> meetings = new ArrayList<Meeting>();
        Attendee attendee1 = new Attendee();
        Attendee attendee2 = new Attendee();
        Attendee attendee3 = new Attendee();
        Attendee attendee4 = new Attendee();
        Attendee attendee5 = new Attendee();
        Attendee attendee6 = new Attendee();
        Attendee attendee7 = new Attendee();
        Attendee attendee8 = new Attendee();
        attendee1.setId("hajba1");
        attendee2.setId("hajba2");
        attendee3.setId("hajba3");
        attendee4.setId("hajba4");
        attendee5.setId("hajba5");
        attendee6.setId("hajba6");
        attendee7.setId("hajba7");
        attendee8.setId("hajba8");
        //attendee4.setName("hajba4");
        List<Attendee> attendeesMeet1 = new ArrayList<>();
        attendeesMeet1.add(attendee1);
        attendeesMeet1.add(attendee2);
        attendeesMeet1.add(attendee3);
        //  attendeesMeet1.add(attendee5);
        Meeting meeting1 = new Meeting();
        //meeting1.setMeetingID("meeting1");
        meeting1.setAttendees(attendeesMeet1);
        Meeting meeting2 = new Meeting();
        //meeting2.setMeetingID("meeting2");
        List<Attendee> attendeesMeet2 = new ArrayList<>();
        attendeesMeet2.add(attendee4);
        attendeesMeet2.add(attendee1);
        Meeting meeting6 = new Meeting();
        List<Attendee> attendeesMeet6 = new ArrayList<>();
        attendeesMeet6.add(attendee3);
        attendeesMeet6.add(attendee4);
        Meeting meeting3 = new Meeting();
        List<Attendee> attendeesMeet3 = new ArrayList<>();
        attendeesMeet3.add(attendee4);
        attendeesMeet3.add(attendee3);
        attendeesMeet3.add(attendee5);
        attendeesMeet3.add(attendee6);
        Meeting meeting4 = new Meeting();
        List<Attendee> attendeesMeet4 = new ArrayList<>();
        attendeesMeet4.add(attendee5);
        attendeesMeet4.add(attendee6);
        Meeting meeting5 = new Meeting();
        List<Attendee> attendeesMeet5 = new ArrayList<>();
        attendeesMeet5.add(attendee5);
        attendeesMeet5.add(attendee6);
        attendeesMeet5.add(attendee7);
        attendeesMeet5.add(attendee8);
        meeting2.setAttendees(attendeesMeet2);
        meeting3.setAttendees(attendeesMeet3);
        meeting4.setAttendees(attendeesMeet4);
        meeting5.setAttendees(attendeesMeet5);
        meeting6.setAttendees(attendeesMeet6);
        meeting1.setMeetingID("WB-a-1-Meeting-1");
        meeting2.setMeetingID("WB-a-1-Meeting-2");
        meeting3.setMeetingID("WB-a-1-Meeting-3");
        meeting4.setMeetingID("WB-a-1-Meeting-4");
        meeting5.setMeetingID("WB-a-1-Meeting-5");
        meeting6.setMeetingID("WB-a-1-Meeting-6");
        //meeting1.setMeetingID();
        meetings.add(meeting1);
        meetings.add(meeting2);
        meetings.add(meeting3);
        meetings.add(meeting4);
        meetings.add(meeting5);
        meetings.add(meeting6);
        //List<Meeting> meetingsPhase2 = new ArrayList<Meeting>();
        /*Meeting meeting3 = new Meeting();
        List<Attendee> attendeesMeet3= new ArrayList<>();
        //Attendee attendee5 = new Attendee();
        //attendee5.setId("hajba5");
        attendeesMeet3.add(attendee5);
        attendeesMeet3.add(attendee2);
        attendeesMeet3.add(attendee1);
        attendeesMeet3.add(attendee6);*/
        //attendeesMeet3.add(attendee4);
        //attendeesMeet3.add(attendee6);
        /*meeting3.setAttendees(attendeesMeet3);
        meetings.add(meeting3);
        //Analyse analyse = new Analyse();
        analyse.updateNetworkMatrix(meetings);*/
        //analyse.updateNetworkMatrix(meetingsPhase2);
        //analyse.degreeCentrality();;


      /*  int[][] adjacencyMatrix = {
                {0,1,0,8,0},
                {1,0,2,3,0},
                {0,2,0,0,0},
                {8,3,0,0,4},
                {0,0,0,4,0}};*/
       /*int[][] adjacencyMatrix = {
                {0,1,1,0,0,0,0},
                {1,0,2,1,0,0,0},
                {1,2,0,1,0,0,0},
                {0,1,1,0,1,0,0},
                {0,0,0,1,0,1,1},
                {0,0,0,0,1,0,1},
                {0,0,0,0,1,1,0}};
        //Results results;
        //results = Dijkstra.calculate(adjacencyMatrix);
        //System.out.println(results);
        Analyse analyse = new Analyse();*/
        /*int[][] adjacencyMatrix = {
                {0, 1, 1, 0, 0, 0, 0},
                {1, 0, 2, 1, 0, 0, 0},
                {1, 2, 0, 1, 0, 0, 0},
                {0, 1, 1, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 1, 1},
                {0, 0, 0, 0, 1, 0, 1},
                {0, 0, 0, 0, 1, 1, 0}};
        //analyse.setNetworkMatrix(adjacencyMatrix);
        List<List<Integer>> connectedComponents = Analyse.getConnectedComponents(adjacencyMatrix);
        System.out.println("Connected Components\n");
        connectedComponents.forEach((n)-> System.out.println(n));
        System.out.println();
        System.out.println("Matrices of each component:\n");*/
        //analyse.betweennessAndCloseness();
        //analyse.updateNetworkMatrix(meetingsPhase2);
        //analyse.eigenvectorCentrality(7);
        //analyse.degreeCentrality(true);
        //analyse.printUsers();
    }
}
