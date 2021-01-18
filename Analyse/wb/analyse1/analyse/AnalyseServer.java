package wb.analyse1.analyse;

import org.xml.sax.SAXException;
import wb.analyse1.Database.Database;
import wb.analyse1.bbbapi.BBBApi;
import wb.analyse1.bbbapi.DemoEnvironment;
import wb.analyse1.parser.ReadXMLSAXParser;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Random;

public class AnalyseServer extends Thread{

    public static void waiting(int time) {
        Random r = new Random();
        try {
            Thread.sleep(r.nextInt(1000) + time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run(){
        System.out.println("Analyse");
        BBBApi bbb = new BBBApi();
        Analyse analyse = new Analyse();

        Database db = new Database();
        db.setAnalyse(analyse);
        ReadXMLSAXParser parser = new ReadXMLSAXParser();
        /*try {
            parser.invokeParser(bbb);
            ReadXMLSAXParser.endAllMeetings(bbb,"test",ReadXMLSAXParser.getMeetingIDS(parser.getMeetings()));
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        //Database db = new Database();
        db.setAnalyse(analyse);
        //ReadXMLSAXParser parser = new ReadXMLSAXParser();
        // GUIThread guiThread = new GUIThread(analyse.getNetworkMatrix(), analyse.getUsers(), analyse.getOnlineUsers());
        //just for test
        db.deleteAllUsers();
        //just for test
        db.deleteAllInteractions();
        analyse.setUsers(db.fetchUsers());
        analyse.setNetworkMatrix(db.fetchNetworkMatrix());

        while (true){

            //Random r = new Random();
            //DemoEnvironment.generateEnvironment(1, meetings.get(r.nextInt(4)));
            //waiting(8000);
            try {
                parser.invokeParser(bbb);
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
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
            //guiThread.setModel(analyse.getNetworkMatrix(),analyse.getUsers(),analyse.getUsers());
            //guiThread.refresh();
            waiting(5000);
        }
    }

    public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException {

        // just for test, will be omited. Analyse loop will take place here
        System.out.println("Analyse");
        BBBApi bbb = new BBBApi();
        Analyse analyse = new Analyse();


        Database db = new Database();
        db.setAnalyse(analyse);
        ReadXMLSAXParser parser = new ReadXMLSAXParser();
        //Database db = new Database();
        db.setAnalyse(analyse);
        //ReadXMLSAXParser parser = new ReadXMLSAXParser();
       // GUIThread guiThread = new GUIThread(analyse.getNetworkMatrix(), analyse.getUsers(), analyse.getOnlineUsers());
        //just for test
        db.deleteAllUsers();
        //just for test
        db.deleteAllInteractions();
        analyse.setUsers(db.fetchUsers());
        analyse.setNetworkMatrix(db.fetchNetworkMatrix());

        while (true){
            //Random r = new Random();
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
            //guiThread.setModel(analyse.getNetworkMatrix(),analyse.getUsers(),analyse.getUsers());
            //guiThread.refresh();
            waiting(5000);
        }
        //just for test
        //db.deleteAllUsers();
        //just for test
        //db.deleteAllInteractions();
        /*analyse.setUsers(db.fetchUsers());
        analyse.setNetworkMatrix(db.fetchNetworkMatrix());
        waiting(2000);
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


        db.insertInOrUpdateUsersTable();
        db.insertInOrUpdateInteractionTable();

        System.out.println("Matrix in Server:");
        analyse.printMatrix(db.fetchNetworkMatrix());

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


        db.insertInOrUpdateUsersTable();
        db.insertInOrUpdateInteractionTable();

        System.out.println("Matrix in Server:");
        analyse.printMatrix(analyse.getNetworkMatrix());

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

        System.out.println("Matrix in Server:");
        analyse.printMatrix(analyse.getNetworkMatrix());
        // deletion is just for testing, omit this
        //db.deleteAllUsers();
        //db.deleteAllInteractions();

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
        System.out.println("Matrix in Server:");
        analyse.printMatrix(analyse.getNetworkMatrix());


        //db.close();
        analyse.printUsers();*/
    }
}
