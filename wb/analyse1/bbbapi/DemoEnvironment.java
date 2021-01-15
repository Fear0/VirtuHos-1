package wb.analyse1.bbbapi;

import java.security.SecureRandom;
import java.util.*;

/**
 * This class can be used for creating
 * new BBB meetings. You can use it to setup a testing environment
 * by using the GeneratorAPI
 */
public class DemoEnvironment {

    // configuration parameters
    // password for bbb attendees
    private static final String ATTENDEE_PASS = "0";
    // password for bbb moderators
    private static final String MODERATOR_PASS = "1";
    // duration of a meeting in minutes (auto-delete)
    private static final int MEETING_DURATION = 2;


    public static final ArrayList<String> userTest_1_1 = new ArrayList<>(Arrays.asList(
            "Kristina%20Mayer", "May%20Mays", "Robt%20Nguyen"
    ));

    public static final ArrayList<String> userTest_3_1 = new ArrayList<>(Arrays.asList(
            "Bob%20Lenart", "Angie%20Cochran","Hamma%20Zebi"
    ));
    public static final ArrayList<String> userTest_1_2 = new ArrayList<>(Arrays.asList(
            "Kristina%20Mayer", "Lou%20Atkins", "Terry%20Oneal"
    ));
    public static final ArrayList<String> userTest_2_1 = new ArrayList<>(Arrays.asList(
            "Angie%20Cochran", "Jackie%20Lang", "Madge%20Kelly"
    ));
    public static final ArrayList<String> userTest_2_2 = new ArrayList<>(Arrays.asList(
            "Angie%20Cochran", "Jackie%20Lang", "Lionel%20Mcclure"
    ));
    public static final ArrayList<String> demoUsersTiny = new ArrayList<>(Arrays.asList(
            "Kristina%20Mayer", "May%20Mays", "Robt%20Nguyen",
            "Nita%20Pugh","Levi%20Oneal","Briana%20Cuevas"));
    public static final ArrayList<String> demoUsersSmall = new ArrayList<>(Arrays.asList(
            "Kristina%20Mayer", "May%20Mays", "Robt%20Nguyen",
            "Nita%20Pugh", "Lou%20Atkins", "Terry%20Oneal",
            "Evangeline%20Powell", "Levi%20Oneal", "Briana%20Cuevas"));
    public static final ArrayList<String> demoUsersLarge = new ArrayList<>(Arrays.asList(
            "Kristina%20Mayer", "May%20Mays", "Robt%20Nguyen",
            "Nita%20Pugh", "Lou%20Atkins", "Terry%20Oneal",
            "Evangeline%20Powell", "Levi%20Oneal", "Briana%20Cuevas",
            "Graciela%20Golden", "Lionel%20Mcclure", "Walter%20Taylor",
            "Rudy%20Koch", "Lidia%20Castaneda", "Alvaro%20Francis",
            "Angie%20Cochran", "Jackie%20Lang", "Madge%20Kelly",
            "Eugenio%20Maxwell", "Brant%20Mcclain"));

    /**
     * Creating Meetings/Users/etc
     * IMPORTANT: You can't start multiple generateXXX methods at a time
     *
     * @param args ignore
     */
    public static void main(String[] args) {

        // EXAMPLES:

        // only call one of these methods otherwise meetings with same name may be merged together

        //generateRandomEnvironment(demoUsersSmall);
        //generateEnvironment(2, demoUsersSmall);
        //generateLonelyEnvironment(demoUsersTiny);


        /*
        TESTCASES: Call these functions directly from your class *not* from DemoEnvironment.main().
                   You should end all running meetings before starting a new one.#
        *//*
        Testcase 1
           Meeting 1: a, b, c -> generateEnvironment(1, userTest_1_1);
           Meeting 2: a, x, y -> generateEnvironment(1, userTest_1_2);

         Testcase 2
           Meeting 1: a, b, c -> generateEnvironment(1, userTest_2_1);
           Meeting 2: a, b, z -> generateEnvironment(1, userTest_2_2);

        */
    }

    /**
     * Creates 'numberOfMeetings' meetings with always the same users if user pool stays the same
     * Users are equally distributed. If |user pool| mod numberOfMeetings != 0 the remaining users will be ignored.
     * @param numberOfMeetings Number of meetings
     * @param demoUsers        user pool
     */
    public static void generateEnvironment(int numberOfMeetings, ArrayList<String> demoUsers) {
        if (numberOfMeetings <= 0) {
            System.out.println("Error: divisor must be positive!");
            return;
        }
        BBBApi bbbAPI = new BBBApi();

        int sizeMeetings = demoUsers.size() / numberOfMeetings;
        if (sizeMeetings <= 0) {
            System.out.println("Error: divisor too large!");
            return;
        }

        for (int i = 0; i < numberOfMeetings; i++) {
            String mID = "WB-A-1-" + String.valueOf(i);
            System.out.println(bbbAPI.createMeeting("WB-A-1-GEN-" + i, ATTENDEE_PASS, MODERATOR_PASS, mID, MEETING_DURATION));

            for (int j = 0; j < sizeMeetings; j++) {
                String u = demoUsers.remove(0);
                String uID = Base64.getEncoder().encodeToString(u.getBytes());
                System.out.println(bbbAPI.join(u, uID, mID, ATTENDEE_PASS));
            }
        }
    }

    /**
     * Creates Meetings with only one user per meeting
     *
     * @param demoUsers user pool
     */
    public static void generateLonelyEnvironment(ArrayList<String> demoUsers) {
        generateEnvironment(demoUsers.size(), demoUsers);
    }

    /**
     * Creates a random number of meetings with a random set of users
     *
     * @param demoUsers user pool
     */
    public static void generateRandomEnvironment(ArrayList<String> demoUsers) {
        // create an api instance for requests
        BBBApi bbbAPI = new BBBApi();
        // create a CSPRNG for good random results
        SecureRandom r = new SecureRandom(SecureRandom.getSeed(32));

        // shuffle user names for random results
        Collections.shuffle(demoUsers, r);

        // limit the number of meetings
        int max_meetings = r.nextInt(demoUsers.size() / 2) + 1;
        System.out.println("Max number of meetings: " + max_meetings);

        // create meetings with >= 2 users each
        int i = 0;
        while (demoUsers.size() >= 2 && i < max_meetings) {
            // random meeting name
            int meetingNo = r.nextInt(1000);
            String meetingName = "WB-Analyse-1-DemoMeeting-" + meetingNo;
            // random number of users per meeting (/2 because this generates smaller meetings)
            int userCount = r.nextInt(demoUsers.size() / 2) + 2;
            // random meetingID
            String meetingID = UUID.randomUUID().toString();
            // get response: [meetingID, XMLResponse]
            String res = bbbAPI.createMeeting(meetingName, ATTENDEE_PASS, MODERATOR_PASS, meetingID, MEETING_DURATION);
            if (res.charAt(22) != 'S') {
                System.out.println(i + ": ERROR: " + res);
                continue;
            }
            System.out.println(i + ": Created random meeting '" + meetingName + "' with id: " + meetingID);

            System.out.println(i + ": Creating " + userCount + " users for meeting " + meetingNo);
            // join users
            for (int j = 0; j < userCount; j++) {
                String userName = demoUsers.remove(0);
                // in this demo: base64(username) = userID
                String userID = Base64.getEncoder().encodeToString(userName.getBytes());
                bbbAPI.join(userName, userID, meetingID, ATTENDEE_PASS);
                System.out.println("\t" + j + ": User '" + userName + "' has joined");
            }
            i++;
        }
    }

}
