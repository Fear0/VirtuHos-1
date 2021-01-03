package de.uni_hannover.wb_interaktionen_1.test_db;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;
/* From this project */
import com.mysql.cj.jdbc.exceptions.CommunicationsException;
import de.uni_hannover.wb_interaktionen_1.Main;
import de.uni_hannover.wb_interaktionen_1.gui.Request;
import de.uni_hannover.wb_interaktionen_1.threads.ThreadUpdateData;
import de.uni_hannover.wb_interaktionen_1.logic.Login;
import de.uni_hannover.wb_interaktionen_1.logic.User;
import de.uni_hannover.wb_interaktionen_1.rooms.*;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/** TestDB class. An instance of this class will let you retrieve data from the Database without using any SQL.
 * You should handle the SQLException as this class only passes the exception to the caller.
 * @author Meikel Kokowski
 * */
public class TestDB {
    private Connection dbConnection;
    private Statement stmt;
    private Properties info;

    //attempts of reconnection
    private int timeout_counter = 0;
    //time per reconnect attempt + 1000ms
    final private int timeout_ms = 3000;
    //maximum reconnect attempts
    final private int TIMEOUT_LIMIT = 3;
    private boolean comFailed = false;
    //DB specific data for connection
    final private String url = "jdbc:mysql://goethe.se.uni-hannover.de:3306/?user=Interaktion_1";
    final private String user = "Interaktion_1";
    final private String password = "JVx;2brXZzFq";

    /* Change database name or table names here */
    final private String DATABASE_NAME = "VirtuHoS_1.";
    final private String USER_TABLE = DATABASE_NAME + "i1_user";
    final private String ROOM_TABLE = DATABASE_NAME + "i1_room";
    final private String CONFERENCE_TABLE = DATABASE_NAME + "i1_conference";
    final private String OFFICE_TABLE = DATABASE_NAME + "i1_office";
    final private String HALL_TABLE = DATABASE_NAME + "i1_hall";
    final private String GROUP_TABLE = DATABASE_NAME + "i1_group";
    final private String REQUEST_TABLE = DATABASE_NAME + "i1_request";
    final private String DOCUMENT_TABLE = DATABASE_NAME + "i1_document";
    final private String ACCESSES_TABLE = DATABASE_NAME + "i1_accesses";

    public TestDB() {

        info = new Properties();
        info.put("user", user);
        info.put("password", password);

        this.connect();

    }

    public void connect(){
        this.dbConnection = null;
        try {

            this.dbConnection = DriverManager.getConnection(url, info);
            this.stmt = dbConnection.createStatement();
            if(this.dbConnection!=null) {
                timeout_counter = 0;
                System.out.println("Successfully connected to DB");
            }else{
                System.out.println("dbConnection is invalid");
                System.exit(1);
            }

        }catch(CommunicationsException ce){
            try {
                if(timeout_counter == 0){
                    System.out.println("Check your Internet Connection User 403");
                    System.exit(403);
                }
                if(timeout_counter < TIMEOUT_LIMIT) {
                    System.out.println("Failed to connect to DB User");
                    Thread.sleep(timeout_ms);

                } else {
                    System.out.println("Connection Timeout User 404");
                    System.exit(404);
                }
            }catch(InterruptedException ie){
                System.out.println("Interrupt Communication User");
            }
        }catch(SQLException sqe){

            System.out.println("Invalid Data SQL");
            System.exit(1);
        }
    }

    public void reconnect() {
        try {
            do {
                this.timeout_counter++;
                System.out.printf("Retry to Connect to DB %d/%d\n", timeout_counter, TIMEOUT_LIMIT);
                Thread.sleep(1000);
                this.connect();
            } while (this.dbConnection == null);
        }catch(InterruptedException ie){
            System.out.println("Interrupt Communication User");
        }
    }

    /* Main Method to be called on program start */
    /** Method to update data in program from DB each second. Make sure to call this method in a thread different from
     * the main process as it operates inside an endless loop.
     * @author Meikel Kokowski
     * @deprecated
     * */
    public void updateData(User user, ArrayList<Room> rooms, ThreadUpdateData thread) throws InterruptedException, SQLException {
        while(!thread.stop_flag) {
            /* updating user in rooms; room name has to be room id, integer ... */
            for(Room room : rooms) {
                //room.occupants = convertToUserList(this.getAllUserInRoom(room.getId())); //
                room.occupants = getAllUserInRoomAsUserList(room, this);
                System.out.print("RoomID: ");
                System.out.print(room.getId());
                System.out.print(" Occupants: ");
                System.out.print(room.occupants);
                System.out.println();
            }
            System.out.println();

            updateOnline(user.getId());
            // Sleep a second
            Thread.sleep(1000);
        }
    }

    /** Method to facilitate query-calls.
     *
     * @param sql : String containing query to be executed
     * @return ResultSet of executed query
     * @author Meikel Kokowski
     * */
    private ResultSet executeQuery(String sql) throws SQLException  {
        return this.stmt.executeQuery(sql);
    }


    /** Method to retrieve all matching elements in column in query.
     *
     * @param query_result : Compiled query result.
     * @param column : Name of the column to get the data from
     * @param dtype : Datatype of column in Database [either String or Integer]
     * @return A String ArrayList regardless of the columns data_type. ToString is called on Integers
     * @exception SQLException : In case of query failure.
     * @throws SQLException: If the query returns failure.
     * @author Meikel Kokowski
     * */
    private ArrayList<String> getAllEntriesFromColumn(ResultSet query_result, String column, String dtype) throws SQLException {
        /* possible data types: String, Integer, */
        ArrayList<String> result = new ArrayList<>();
        while(query_result.next()) {
            if (dtype.toLowerCase().equals("integer") || dtype.toLowerCase().equals("int")) {
                result.add(Integer.toString(query_result.getInt(column)));
            } else if(dtype.toLowerCase().equals("string")) {
                result.add(query_result.getString(column));
            }
        }
        return result;
    }

    /*------------------------------- User specific Methods ------------------------------------- */

    /** Method to check whether a personalID is inside the DB or not.
     *
     * @param personalID : String representation of a user's personalID
     * @return true iff user is inside the DB, else false
     * @throws SQLException: : In case of query failure due to external issues in DB
     * @author Meikel Kokowski
     * */
    public boolean userIsInDB(String personalID) throws SQLException {
        String sql = "SELECT * FROM " + USER_TABLE + " WHERE personalID = ?";
        PreparedStatement intermediate = dbConnection.prepareStatement(sql);
        intermediate.setString(1, personalID);
        ResultSet res = intermediate.executeQuery();
        return res.next();
    }

    /* Online status specific */

    /** Method to check if a user is online right now.
     *
     * @param personalID : String representation of a user's personalID
     * @return true iff user is online
     * @throws SQLException: : In case of query failure due to external issues in DB or if
     *                         someone changed "online_status" to another name in the table
     * @author Meikel Kokowski
     * */
    public boolean userIsOnline(String personalID) throws SQLException {
        String query = "SELECT * FROM " + USER_TABLE + " WHERE personalID = ?";
        PreparedStatement intermediate = dbConnection.prepareStatement(query);
        intermediate.setString(1, personalID);
        ResultSet results = intermediate.executeQuery();
        if(!results.next()) return false;
        return results.getBoolean("online_status");
    }

    /** Method to set online status of a user.
     *
     * @param personalID : PersonalID to identify user
     * @param online_status : true or false
     * @throws SQLException: : In case of query failure due to external issues in DB or if
     *                         someone changed one of the column names in the DB
     * @author Meikel Kokowski
     * */
    public void setOnlineStatus(String personalID, boolean online_status) throws  SQLException {
        String sql = "UPDATE " + USER_TABLE + " SET online_status = ? WHERE personalID = ?";
        PreparedStatement intermediate = dbConnection.prepareStatement(sql);
        intermediate.setBoolean(1, online_status);
        intermediate.setString(2, personalID);
        intermediate.executeUpdate();
    }

    /** Method to set online_status and online_status_2 for login
     *
     * @param personalID : PersonalID to identify user
     * @exception SQLException : In case of query failure.
     * @throws SQLException: If the query returns failure.
     * */
    public void loginOnline(String personalID) throws  SQLException {
        String sql = "UPDATE " + USER_TABLE + " SET online_status = 1, online_status_2 = 1 WHERE personalID = ?";
        PreparedStatement intermediate = dbConnection.prepareStatement(sql);
        intermediate.setString(1, personalID);
        intermediate.executeUpdate();
    }


    /** updates the second online state of the User with personalID to DB
     * @exception SQLException : In case of query failure.
     * @throws SQLException
     * @author Alan Dryaev
     */
    public void updateOnline(String personalID) throws  SQLException {
        String sql = "UPDATE " + USER_TABLE + " SET online_status_2 = true WHERE personalID = ? AND online_status_2 = false";
        PreparedStatement intermediate = dbConnection.prepareStatement(sql);
        intermediate.setString(1, personalID);
        intermediate.executeUpdate();
    }

    /** sets all values of User with personalID to 0 or NULL
     * @exception SQLException : In case of query failure.
     * @throws SQLException
     * @author Alan Dryaev
     */
    public void resetUser(String personalID) throws  SQLException {
        String sql = "UPDATE " + USER_TABLE + " SET online_status = false, online_status_2 = false, roomID = NULL, groupID = NULL WHERE personalID = ?";
        PreparedStatement intermediate = dbConnection.prepareStatement(sql);
        intermediate.setString(1, personalID);
        intermediate.executeUpdate();
    }

    /** Method to check if a user is already inside a meeting in BBB.
     *
     * @param personalID : PersonalID to identify user
     * @return true if a user is inside meeting, else false
     * @throws SQLException: : In case of query failure due to external issues in DB or if
     *                         someone changed one of the column names in the DB
     * @author Meikel Kokowski
     * */
    public boolean userIsInMeeting(String personalID) throws SQLException {
        String user_join_rooms = "SELECT meeting_id FROM " + USER_TABLE + " JOIN (" + ROOM_TABLE + " JOIN ( SELECT roomID, meeting_id FROM " + CONFERENCE_TABLE + " UNION SELECT roomID, meeting_id FROM " + OFFICE_TABLE + " ) as offic_and_conference using(roomID)) using(roomID) WHERE personalID = ?;";
        PreparedStatement intermediate = dbConnection.prepareStatement(user_join_rooms);
        intermediate.setString(1, personalID);
        ResultSet result = intermediate.executeQuery();
        if(!result.next()) return false;
        return result.getString("meeting_id") != null;
    }


    /* Ping-methods; When does a user have to join a conference? */

    /** Method to see if a user has to join a meeting. It's realized through the variable
     * "has_to_join" in the Database. If the function results to true, you can join them.
     * Note: This "ping" can be set with setPing
     *
     * @param personalID : PersonalID to identify user
     * @return Returns true if the user has to join the meeting.
     * @throws SQLException: : In case of query failure due to external issues in DB or if
     *                         someone changed one of the column names in the DB
     * @author Meikel Kokowski
     * @deprecated
     * */
    public boolean userHasToJoinMeeting(String personalID) throws  SQLException {
        String sql = "SELECT * FROM " + USER_TABLE + " WHERE personalID = ?";
        PreparedStatement intermediate = dbConnection.prepareStatement(sql);
        intermediate.setString(1, personalID);
        ResultSet results = intermediate.executeQuery();
        results.next();
        return results.getBoolean("has_to_join");
    }

    /** Method to set a ping for a specific person in the database to
     * mark him as "person who has to join a conference"
     * Note: You can call userHasToJoinMeeting() to see whether a user has to join or not.
     *
     * @param personalID : PersonalID to identify user
     * @param has_to_join : true if user has to join a conference, else false
     * @throws SQLException: : In case of query failure due to external issues in DB or if
     *                         someone changed one of the column names in the DB
     * @author Meikel Kokowski
     * @deprecated
     * */
    public void setPingFor(String personalID, boolean has_to_join) throws  SQLException {
        String sql = "UPDATE " + USER_TABLE + " SET has_to_join = ? WHERE personalID = ?";
        PreparedStatement intermediate = dbConnection.prepareStatement(sql);
        intermediate.setBoolean(1, has_to_join);
        intermediate.setString(2, personalID);
        intermediate.executeUpdate();
    }

    /** Method to find the room for a specific person.
     *
     * @param personalID : ID of person to find the room for
     * @return The ID of the room the person is inside
     * @throws SQLException: : Thrown if user does not exist in DB,
     *                         In case of query failure due to external issues in DB or if
     *                         someone changed one of the column names in the DB
     * @author Meikel Kokowski
     * */
    public int findRoomFor(String personalID) throws SQLException {
        String sql = "SELECT * FROM " + USER_TABLE + " WHERE personalID = ?";
        PreparedStatement intermediate = dbConnection.prepareStatement(sql);
        intermediate.setString(1, personalID);
        ResultSet results = intermediate.executeQuery();
        if(!results.next()) {
            throw new SQLException();
        }
        return results.getInt("roomID");
    }

    /** Method to add a user to a database
     *
     * @param user_name : PersonalID of the user to be added to the DB
     * @param online_status : Integer to represent online status: either 1 or 0
     * @throws SQLException: : Thrown if:
     *                         personaLID already exists in DB (Primary Kay constraint)
     *                         In case of query failure due to external issues in DB or if
     *                         someone changed one of the column names in the DB
     * @author Meikel Kokowski
     * */
    public void addUser(String user_name, boolean online_status) throws SQLException {
        String query = "INSERT INTO " + USER_TABLE + " (personalID, online_status) VALUES (?, ?)";
        PreparedStatement prepare_stmt = this.dbConnection.prepareStatement(query);
        prepare_stmt.setString(1, user_name);
        prepare_stmt.setBoolean(2, online_status);
        //prepare_stmt.execute();
        // according to documentation:
        prepare_stmt.executeUpdate();
    }

    /** Method to delete a user from the Database.
     * Use this method carefully, it will completely erase the user's existence.
     *
     * @param personalID : personalID of the user
     * @throws SQLException: : In case of query failure due to external issues in DB or if
     *                         someone changed one of the column names in the DB
     * @author Meikel Kokowski
     * */
    public void deleteUser(String personalID) throws SQLException {
        /* Now actually delete user */
        String query = "DELETE FROM " + USER_TABLE + " WHERE personalID = ?";
        PreparedStatement intermediate = dbConnection.prepareStatement(query);
        intermediate.setString(1, personalID);
        intermediate.executeUpdate();
    }

    /** Method to get all users personalID from the DB as arraylist.
     *
     * @return list of all users names in db
     * @throws SQLException: : In case of query failure due to external issues in DB or if
     *                         someone changed one of the column names in the DB
     * @author Meikel Kokowski
     * */
    public ArrayList<String> getAllUserInDB() throws SQLException {
        return getAllEntriesFromColumn(this.executeQuery("SELECT personalID FROM " + USER_TABLE), "personalID", "string");
    }

    /** Method to get all users personalID from the DB as arraylist.
     * Note: this has to be kept a private method because it's highly vulnerable to SQL-Injection.
     *
     * @param where : String in the format of "WHERE x = y [...]"
     * @return list of all users names in db which fulfil the where condition
     * @throws SQLException: : Thrown:
     *                         if WHERE clause is syntactically incorrect,
     *                         in case of query failure due to external issues in DB or if
     *                         someone changed one of the column names in the DB
     * @author Meikel Kokowski
     * */
    private ArrayList<String> getAllUserInDB(String where) throws SQLException {
        return getAllEntriesFromColumn(this.executeQuery("SELECT personalID FROM " + USER_TABLE + " " + where), "personalID", "string");
    }

    public ArrayList<User> getAllUserInDBasUserList(TestDB db) throws SQLException {
        ArrayList<User> res = new ArrayList<>();
        String sql = "SELECT * FROM " + USER_TABLE + ";";
        ResultSet users = executeQuery(sql);
        while(users.next()) {
            if(users.getInt("roomID") == 0){
                res.add(new User(
                        users.getString("personalID"),
                        users.getString("personalID"),
                        null));
            } else {
                res.add(new User(
                        users.getString("personalID"),
                        users.getString("personalID"),
                        getRoomWithRoomID(users.getInt("roomID"), db)));
            }
        }
        return res;
    }

    public Room getRoomWithRoomID(int roomID, TestDB db) throws SQLException {
        String sql = "SELECT * FROM " + ROOM_TABLE + " WHERE roomID = ?";
        PreparedStatement intermediate = dbConnection.prepareStatement(sql);
        intermediate.setInt(1, roomID);
        ResultSet room = intermediate.executeQuery();
        if (!room.next()){
            return null;
        }
        return new Room(room.getInt("roomID"), room.getString("type"), room.getInt("capacity"), db);
    }

    public ArrayList<String> getAllUserInRoom(int roomID) throws SQLException {
        ArrayList<String> result = new ArrayList<>();
        String sql = "SELECT personalID FROM " + USER_TABLE + " WHERE roomID = ?";
        PreparedStatement intermediate = dbConnection.prepareStatement(sql);
        intermediate.setInt(1, roomID);
        ResultSet user = intermediate.executeQuery();
        while(user.next()) {
            result.add(user.getString("personalID"));
        }
        return result;
    }

    /** Get all users in a room as an observablelist. It is used to show the user in a room in the GUI.
     *
     * @param roomID The ID of the room for which we want the users
     * @return An observablelist with the users as string
     * @throws SQLException If the query returns failure.
     */
    public ObservableList<String> getAllUserInRoomObservable(int roomID) throws SQLException {
        ObservableList<String> result = FXCollections.observableArrayList();
        String sql = "SELECT personalID FROM " + USER_TABLE + " WHERE roomID = ?";
        PreparedStatement intermediate = dbConnection.prepareStatement(sql);
        intermediate.setInt(1, roomID);
        ResultSet user = intermediate.executeQuery();
        while(user.next()) {
            result.add(user.getString("personalID"));
        }
        return result;
    }

    /** Get all users which are not in a room as an observablelist. It is used to show the user in the GUI.
     *
     * @return An observablelist with the users as string
     * @throws SQLException If the query returns failure
     */
    public ObservableList<String> getAllUserWithoutRoomObservable() throws SQLException {
        ObservableList<String> result = FXCollections.observableArrayList();
        String sql = "SELECT personalID FROM " + USER_TABLE + " WHERE roomID IS NULL";
        PreparedStatement intermediate = dbConnection.prepareStatement(sql);
        ResultSet user = intermediate.executeQuery();
        while(user.next()) {
            result.add(user.getString("personalID"));
        }
        return result;
    }

    /** Get all users in a hall group as an observablelist. It is used to show the user in a hall group in the GUI.
     *
     * @param groupID The ID of the group for which we want the users
     * @return An observablelist with the users as string
     * @throws SQLException If the query returns failure
     */
    public ObservableList<String> getAllUserInHallGroupObservable(int groupID) throws SQLException {
        ObservableList<String> result = FXCollections.observableArrayList();
        String sql = "SELECT personalID FROM " + USER_TABLE + " WHERE groupID = ?";
        PreparedStatement intermediate = dbConnection.prepareStatement(sql);
        intermediate.setInt(1, groupID);
        ResultSet user = intermediate.executeQuery();
        while(user.next()) {
            result.add(user.getString("personalID"));
        }
        return result;
    }

    public ArrayList<User> getAllUserInRoomAsUserList(Room room, TestDB db) throws SQLException {
        ArrayList<User> result = new ArrayList<>();
        String sql = "SELECT * FROM " + USER_TABLE + " WHERE roomID = ?";
        PreparedStatement intermediate = dbConnection.prepareStatement(sql);
        intermediate.setInt(1, room.getId());
        ResultSet users = intermediate.executeQuery();
        while(users.next()) {
            result.add(new User(
                    users.getString("personalID"),
                    users.getString("personalID"),
                    room));
        }
        return result;
    }

    /** Adds a user to a room in the database
     *
     * @param personalID The personalID of the user which has to be added
     * @param roomID The ID of the room where the user has to be added
     * @throws SQLException: : In case of query failure due to external issues in DB or if
     *                         someone changed one of the column names in the DB
     * @author Meikel Kokowski
     */
    public void addUserToRoom(String personalID, int roomID) throws  SQLException {
        String sql = "UPDATE " + USER_TABLE + " SET roomID = ? WHERE personalID = ?";
        PreparedStatement intermediate = dbConnection.prepareStatement(sql);
        intermediate.setInt(1, roomID);
        intermediate.setString(2, personalID);
        intermediate.executeUpdate();
    }

    /** Adds a user to a group in the database
     *
     * @param personalID The personalID of the user which has to be added
     * @param groupID The ID of the group where the user has to be added
     * @throws SQLException: : In case of query failure due to external issues in DB or if
     *                         someone changed one of the column names in the DB
     * @author David Sasse
     */
    public void addUserToGroup(String personalID, int groupID) throws  SQLException {
        String sql = "UPDATE " + USER_TABLE + " SET groupID = ? WHERE personalID = ?";
        PreparedStatement intermediate = dbConnection.prepareStatement(sql);
        intermediate.setInt(1, groupID);
        intermediate.setString(2, personalID);
        intermediate.executeUpdate();
    }

    /** Deletes a user from a group in the database
     *
     * @param personalID The personalID of the user which has to be deleted
     * @throws SQLException: : In case of query failure due to external issues in DB or if
     *                         someone changed one of the column names in the DB
     * @author David Sasse
     */
    public void deleteUserFromGroup(String personalID) throws  SQLException {
        String sql = "UPDATE " + USER_TABLE + " SET groupID = null WHERE personalID = ?";
        PreparedStatement intermediate = dbConnection.prepareStatement(sql);
        intermediate.setString(1, personalID);
        intermediate.executeUpdate();
    }

    /* room specific methods */

    /** Creates a list with all rooms
     *
     * @return the ArrayList with all rooms
     * @throws SQLException: : In case of query failure due to external issues in DB or if
     *                         someone changed one of the column names in the DB
     * @author Meikel Kokowski, David Sasse
     */
    public ArrayList<Room> getAllRooms() throws SQLException {
        ArrayList<Room> res = new ArrayList<>();
        String sql = "SELECT * FROM " + ROOM_TABLE + ";";
        ResultSet rooms = executeQuery(sql);
        while(rooms.next()) {
            if(rooms.getString("type").equals("office")){
                res.add(new Office(
                        rooms.getInt("roomID"),
                        rooms.getInt("capacity"),
                        this));
            } else if(rooms.getString("type").equals("conference")){
                res.add(new Conference(
                        rooms.getInt("roomID"),
                        rooms.getInt("capacity"),
                        this));
            } else if(rooms.getString("type").equals("hall")){
                res.add(new Hall(
                        rooms.getInt("roomID"),
                        rooms.getInt("capacity"),
                        this));
            }
        }
        return res;
    }

    /** Creates a list with all groups in the hall
     *
     * @return the ArrayList with all groups
     * @throws SQLException: : In case of query failure due to external issues in DB or if
     *                         someone changed one of the column names in the DB
     * @author Meikel Kokowski
     */
    public ArrayList<HallGroup> getAllHallGroups() throws SQLException {
        ArrayList<HallGroup> res = new ArrayList<>();
        String sql = "SELECT * FROM " + GROUP_TABLE + ";";
        ResultSet groups = executeQuery(sql);
        while(groups.next()) {
            res.add(new HallGroup(
                    groups.getInt("groupID")));
        }
        return res;
    }

    /** Method to add a room to the DB.
     * Note: The roomID is generated automatically and returned by this function!
     *
     * @param size : Size of the room; the capacity of people to fit inside
     * @param type : type of the room where type element of ["conference", "office", "hall"]
     * @return the ID of the created room.
     * @throws SQLException: : In case of query failure due to external issues in DB or if
     *                         someone changed one of the column names in the DB
     *                         If none of the above cases, auto_increment may not be able to create a new value
     * @author Meikel Kokowski
     * */
    private int createRoom(int size, String type) throws SQLException {
        String sql = "INSERT INTO " + ROOM_TABLE + " (capacity, type) VALUES (?, ?)";
        PreparedStatement intermediate = dbConnection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        intermediate.setInt(1, size);
        intermediate.setString(2, type);
        intermediate.executeUpdate();
        ResultSet result = intermediate.getGeneratedKeys();
        result.next();
        return result.getInt(1);
    }

    /** Method to create a Conference in the DB.
     * Note: The roomID is generated automatically and returned by this function!
     *
     * @param capacity : max size of the conference; the capacity of people to fit inside
     * @param meeting_url : a meeting url to be assigned to the room; null if there is none
     * @return the ID of the created room.
     * @throws SQLException: : In case of query failure due to external issues in DB or if
     *                         someone changed one of the column names in the DB
     *                         If none of the above cases, auto_increment may not be able to create a new value
     * @author Meikel Kokowski
     * */
    public int createConference(int capacity, String meeting_url) throws SQLException  {
        int new_room_id = this.createRoom(capacity, "conference");
        String sql = "INSERT INTO " + CONFERENCE_TABLE + " (roomID) VALUES (?)";
        PreparedStatement intermediate = dbConnection.prepareStatement(sql);
        intermediate.setInt(1, new_room_id);
        intermediate.executeUpdate();
        return new_room_id;
    }

    /** Method to create a Office in the DB.
     * Note: The roomID is generated automatically and returned by this function!
     *
     * @param capacity : max size of the office; the capacity of people to fit inside
     * @return the ID of the created room.
     * @throws SQLException: : In case of query failure due to external issues in DB or if
     *                         someone changed one of the column names in the DB
     *                         If none of the above cases, auto_increment may not be able to create a new value
     * @author Meikel Kokowski
     * */
    public int createOffice(int capacity) throws SQLException  {
        int new_room_id = this.createRoom(capacity, "office");
        String sql = "INSERT INTO " + OFFICE_TABLE + " (roomID) VALUES (?)";
        PreparedStatement intermediate = dbConnection.prepareStatement(sql);
        intermediate.setInt(1, new_room_id);
        intermediate.executeUpdate();
        return new_room_id;
    }

    /** Method to create a Hall in the DB.
     * Note: The roomID is generated automatically and returned by this function!
     *
     * @param capacity : max size of the conference; the capacity of people to fit inside
     * @return the ID of the created room.
     * @throws SQLException: : In case of query failure due to external issues in DB or if
     *                         someone changed one of the column names in the DB
     *                         If none of the above cases, auto_increment may not be able to create a new value
     * @author Meikel Kokowski
     * */
    public int createHall(int capacity) throws SQLException  {
        int new_room_id = this.createRoom(capacity, "hall");
        String sql = "INSERT INTO " + HALL_TABLE + " (roomID) VALUES (?)";
        PreparedStatement intermediate = dbConnection.prepareStatement(sql);
        intermediate.setInt(1, new_room_id);
        intermediate.executeUpdate();
        return new_room_id;
    }

    /** Method to create the start hall in DB. It's the hall with ID=0
     *
     * @param capacity : max size of the conference; the capacity of people to fit inside
     * @return the ID of the created room.
     * @throws SQLException: : In case of query failure due to external issues in DB or if
     *                         someone changed one of the column names in the DB
     *                         a room with ID=0 may already exist
     * @author Meikel Kokowski
     * */
    public int createStartHall(int capacity) throws SQLException  {
        /* Has to be set so DB does not interpret '0' as a command to calculate the next auto_increment */
        String constraint = "SET SESSION sql_mode='NO_AUTO_VALUE_ON_ZERO'";
        this.executeQuery(constraint);
        /* Create normal room */
        String sql = "INSERT INTO " + ROOM_TABLE + " (roomID, capacity, type) VALUES (?, ?, ?)";
        PreparedStatement intermediate = dbConnection.prepareStatement(sql);
        intermediate.setInt(1, 0);
        intermediate.setInt(2, capacity);
        intermediate.setString(3, "hall");
        intermediate.executeUpdate();
        /* Create hall */
        sql = "INSERT INTO " + HALL_TABLE + " (roomID) VALUES (?)";
        intermediate = dbConnection.prepareStatement(sql);
        intermediate.setInt(1, 0);
        intermediate.executeUpdate();
        return 0;
    }

    /** Method to create a Group for the hall in the DB.
     * Note: The roomID is generated automatically and returned by this function!
     *
     * @param capacity : max size of the conference; the capacity of people to fit inside
     * @return the ID of the created room.
     * @throws SQLException: : Thrown:
     *                         if roomID does not exist in the room Table (foreign key)
     *                         In case of query failure due to external issues in DB or if
     *                         someone changed one of the column names in the DB
     *                         If none of the above cases, auto_increment may not be able to create a new value
     * @author Meikel Kokowski, David Sasse
     * */
    public int createGroup(int hall_id) throws SQLException  {
        String sql = "INSERT INTO " + GROUP_TABLE + " (roomID) VALUES (?)";
        PreparedStatement intermediate = dbConnection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        intermediate.setInt(1, hall_id);
        intermediate.executeUpdate();
        ResultSet result = intermediate.getGeneratedKeys();
        result.next();
        return result.getInt(1);
    }

    /** This methode deletes a group in the hall.
     *
     * @param id the id of the group that has to be deleted
     * @throws SQLException
     */
    public void deleteGroup(int id) throws SQLException {
        String query = "DELETE FROM " + GROUP_TABLE + " WHERE groupID = ?";
        PreparedStatement intermediate = dbConnection.prepareStatement(query);
        intermediate.setInt(1, id);
        intermediate.executeUpdate();
    }

    /** Getter for the corresponding hallID of  a hallgroup
     *
     * @param groupID the ID of the group
     * @return the id of the hall
     * @throws SQLException In case of query failure due to external issues in DB or if
     *                      someone changed one of the column names in the DB
     */
    public int getCorrespondingHalltoHallGroup(int groupID) throws SQLException {
        String sql = "SELECT roomID FROM " + GROUP_TABLE + " WHERE groupID = ?";
        PreparedStatement intermediate = dbConnection.prepareStatement(sql);
        intermediate.setInt(1, groupID);
        ResultSet room = intermediate.executeQuery();
        if (!room.next()){
            return 0;
        }
        return room.getInt("roomID");
    }

    /** Method to check whether a meeting exists for a room.
     *
     * @param roomID : roomID to check if a meeting exists
     * @return true iff a meeting exists in the DB, else false
     * @throws SQLException: : In case of query failure due to external issues in DB or if
     *                         someone changed one of the column names in the DB
     * @author Meikel Kokowski
     * */
    public boolean hasMeeting(int roomID) throws SQLException {
        String rooms_with_meeting = "SELECT * FROM " + ROOM_TABLE + " JOIN ( SELECT roomID, meeting_id FROM " + CONFERENCE_TABLE + " UNION SELECT roomID, meeting_id FROM " + OFFICE_TABLE + " ) as offic_and_conference using(roomID) WHERE roomID = ?;";
        PreparedStatement intermediate = dbConnection.prepareStatement(rooms_with_meeting);
        intermediate.setInt(1, roomID);
        ResultSet result = intermediate.executeQuery();
        if(!result.next()) return false;
        return result.getString("meeting_id") != null;
    }

    /** Method to get a meeting for a room.
     *
     * @param roomID : roomID to get the meeting from
     * @return the meeting id as a string or null if there is no meeting
     * @throws SQLException: : In case of query failure due to external issues in DB or if
     *                         someone changed one of the column names in the DB
     * @author Meikel Kokowski
     * */
    public String getMeeting(int roomID) throws SQLException {
        String rooms_with_meeting = "SELECT * FROM " + ROOM_TABLE + " JOIN ( SELECT roomID, meeting_id FROM " + CONFERENCE_TABLE + " UNION SELECT roomID, meeting_id FROM " + OFFICE_TABLE + " ) as offic_and_conference using(roomID) WHERE roomID = ?;";
        PreparedStatement intermediate = dbConnection.prepareStatement(rooms_with_meeting);
        intermediate.setInt(1, roomID);
        ResultSet result = intermediate.executeQuery();
        if(!result.next()) return null;
        return result.getString("meeting_id");
    }

    /** Method to attach meeting to a room.
     *
     * @param roomID : room to be attached to the meeting url
     * @param meeting_id : id of the BBB conference
     * @throws SQLException: : In case of query failure due to external issues in DB or if
     *                         someone changed one of the column names in the DB
     * @author Meikel Kokowski
     * */
    public void setMeeting(int roomID, String meeting_id) throws SQLException {
        String update = "UPDATE " + ROOM_TABLE + " JOIN " + OFFICE_TABLE + " using(roomID) SET meeting_id = ?" + " WHERE roomID = ?;";
        PreparedStatement intermediate = dbConnection.prepareStatement(update);
        intermediate.setString(1, meeting_id);
        intermediate.setInt(2, roomID);
        if(intermediate.executeUpdate() < 1) { // less than one row means the roomID wasn't found
            update = "UPDATE " + ROOM_TABLE + " JOIN " + CONFERENCE_TABLE + " using(roomID) SET meeting_id = ?" + " WHERE roomID = ?;";
            intermediate = dbConnection.prepareStatement(update);
            intermediate.setString(1, meeting_id);
            intermediate.setInt(2, roomID);
            intermediate.executeUpdate();
        }
    }

    /** Method to get a meeting for a group.
     *
     * @param groupID : groupID to get the meeting from
     * @return the meeting id as a string or null if there is no meeting
     * @throws SQLException: : In case of query failure due to external issues in DB or if
     *                         someone changed one of the column names in the DB
     * @author David Sasse
     * */
    public String getMeetingGroup(int groupID) throws SQLException {
        String rooms_with_meeting = "SELECT * FROM " + GROUP_TABLE + " WHERE groupID = ?";
        PreparedStatement intermediate = dbConnection.prepareStatement(rooms_with_meeting);
        intermediate.setInt(1, groupID);
        ResultSet result = intermediate.executeQuery();
        if(!result.next()) return null;
        return result.getString("meeting_id");
    }

    /** Method to attach meeting to a group.
     *
     * @param groupID : group to be attached to the meeting url
     * @param meeting_id : id of the BBB conference
     * @throws SQLException: : In case of query failure due to external issues in DB or if
     *                         someone changed one of the column names in the DB
     * @author David Sasse
     * */
    public void setMeetingGroup(int groupID, String meeting_id) throws SQLException {
        String update = "UPDATE " + GROUP_TABLE + " SET meeting_id = ?" + " WHERE groupID = ?";
        PreparedStatement intermediate = dbConnection.prepareStatement(update);
        intermediate.setString(1, meeting_id);
        intermediate.setInt(2, groupID);
        intermediate.executeUpdate();
    }

    /* --------------------------------- requests ------------------------------------------ */
    /** Method to send a request. Basically you only update the Request table which will be checked each second
     * from each user to see if he's in there.
     *
     * @param senderID : ID of the sender of the request (the one who "invites" the other one)
     * @param receiverID : ID of the one that will receive the request
     * @param type : Type of the request such as "join"
     * @throws SQLException: : Thrown:
     *                         if any request from senderID to receiverID already exists
     *                         In case of query failure due to external issues in DB or if
     *                         someone changed one of the column names in the DB
     * @author Meikel Kokowski
     * */
    public void sendRequest(String senderID, String receiverID, String type) throws SQLException {
        /* Type may be:
        * - join
        * - ...
        * */
        String request = "INSERT INTO " + REQUEST_TABLE + " (senderID, receiverID, type) VALUES (?, ?, ?);";
        PreparedStatement intermediate = dbConnection.prepareStatement(request);
        intermediate.setString(1, senderID);
        intermediate.setString(2, receiverID);
        intermediate.setString(3, type);
        intermediate.executeUpdate();
    }

    /** Method to check if a user has a pending request.
     *
     * @param personalID : ID of the person who might have a meeting
     * @return true if the user has a meeting, else false.
     * @throws SQLException: : In case of query failure due to external issues in DB or if
     *                         someone changed one of the column names in the DB
     * @author Meikel Kokowski
     * */
    public boolean hasRequest(String personalID) throws SQLException {
        String sql = "SELECT * FROM " + REQUEST_TABLE + " WHERE receiverID = ?";
        PreparedStatement intermediate = dbConnection.prepareStatement(sql);
        intermediate.setString(1, personalID);
        ResultSet result = intermediate.executeQuery();
        return result.next();
    }

    /** Method to get all Requests a user has as list sorted by time the request was sent.
     *
     * @param receiver : the person to get all the meetings for
     * @return a list with all the requests for the user
     * @throws SQLException: : In case of query failure due to external issues in DB or if
     *                         someone changed one of the column names in the DB
     * @author Meikel Kokowski, David Sasse
     * */
    public ArrayList<Request> getRequests(User receiver) throws SQLException {
        String requests = "SELECT * FROM " + REQUEST_TABLE + " WHERE receiverID = ? ORDER BY `timestamp`";
        PreparedStatement intermediate = dbConnection.prepareStatement(requests);
        intermediate.setString(1, receiver.getId());
        ResultSet result = intermediate.executeQuery();
        ArrayList<Request> requests_list = new ArrayList<>();
        while(result.next()) {
            // Each sender will appear only once, sorted by time the request was sent
            requests_list.add(new Request(this, receiver, result.getString("senderID"), result.getString("type")));
        }
        return requests_list;
    }

    /** Method to remove a request.
     *
     * @param senderID : ID of the person who sent the request
     * @param receiverID : ID of the person that received the Request
     * @throws SQLException: : In case of query failure due to external issues in DB or if
     *                         someone changed one of the column names in the DB
     * @author Meikel Kokowski
     * */
    public void removeRequest(String senderID, String receiverID) throws SQLException {
        String query = "DELETE FROM " + REQUEST_TABLE + " WHERE senderID = ? and receiverID = ?";
        PreparedStatement intermediate = dbConnection.prepareStatement(query);
        intermediate.setString(1, senderID);
        intermediate.setString(2, receiverID);
        intermediate.executeUpdate();
    }

    /* Document specific */
    /** Method to add a document to the database.
     *
     * @param document_url : url of the document
     * @param officeID : ID of the office-room
     * @throws SQLException: : If document_url in combination with officeID already exists (Primary Key constraint)
     *                         If officeID is not inside the room table
     *                         In case of query failure due to external issues in DB or if
     *                         someone changed one of the column names in the DB
     * @author Meikel Kokowski
     * */
    public void addDocument(String document_url, int officeID) throws SQLException {
        String add_document = "INSERT INTO " + DOCUMENT_TABLE + " (document_url, officeID) VALUES (?, ?);";
        PreparedStatement intermediate = dbConnection.prepareStatement(add_document);
        intermediate.setString(1, document_url);
        intermediate.setInt(2, officeID);
        intermediate.executeUpdate();
    }

    /** Method to get a list of all documents inside the specified office.
     *
     * @param officeID : ID of the office-room.
     * @return a list of all document_urls in the specified room.
     * @throws SQLException thrown in case of failure from the DB.
     * @author Meikel Kokowski
     * */
    public ArrayList<String> getDocumentsIn(int officeID) throws SQLException {
        String documents_in = "SELECT document_url FROM " + DOCUMENT_TABLE + " WHERE officeID = ?;";
        PreparedStatement intermediate = dbConnection.prepareStatement(documents_in);
        intermediate.setInt(1, officeID);
        ResultSet documents = intermediate.executeQuery();
        ArrayList<String> result = new ArrayList<>();
        while(documents.next()) {
            result.add(documents.getString("document_url"));
        }
        return result;
    }

    /** Method to add a user to the access-table.
     *
     * @param personalID : ID of the user to add.
     * @param document_url : url of the document to be added.
     * @throws SQLException thrown if (personalID, document_url):
     *      - do already exist in this combination (Primary key constrain)
     *      - either of these two values does not exist as primary key in their respective table
     * @author Meikel Kokowski
     * */
    private void createUserAccess(String personalID, String document_url) throws SQLException {
        String add_user_access = "INSERT INTO " + ACCESSES_TABLE + " (personalID, document_url) VALUES (?, ?)";
        PreparedStatement intermediate = dbConnection.prepareStatement(add_user_access);
        intermediate.setString(1, personalID);
        intermediate.setString(2, document_url);
        intermediate.executeUpdate();
    }

    /** Method to increment the access counter of a user for a specified document by one.
     *
     * @param personalID : ID of the user.
     * @param document_url : url of the document that was accessed.
     * @throws SQLException could be thrown if "Create User Access" was called or any external errors in the DB occur.
     * @author Meikel Kokowski
     * */
    public void incrementAccessCounter(String personalID, String document_url) throws SQLException {
        /* First check if user has already accessed a file */
        String increment_access_times = "UPDATE " + ACCESSES_TABLE + " SET times = times+1 WHERE personalID = ? AND document_url = ?;";
        PreparedStatement intermediate = dbConnection.prepareStatement(increment_access_times);
        intermediate.setString(1, personalID);
        intermediate.setString(2, document_url);
        // If no row was affected, create new entry for user as it was his first access to the document
        if(intermediate.executeUpdate() < 1) {
            this.createUserAccess(personalID, document_url);
        }
    }

    /** Method to see how many times a user accessed a document.
     *
     * @param personalID : ID of the user.
     * @param document_url : url of the document that was accessed.
     * @throws SQLException could be thrown if "Create User Access" was called or any external errors in the DB occur.
     * @author Meikel Kokowski
     * */
    public int getAccessTimes(String personalID, String document_url) throws SQLException {
        /* First check if user has already accessed a file */
        String increment_access_times = "SELECT times FROM " + ACCESSES_TABLE + " WHERE personalID = ? AND document_url = ?;";
        PreparedStatement intermediate = dbConnection.prepareStatement(increment_access_times);
        intermediate.setString(1, personalID);
        intermediate.setString(2, document_url);
        ResultSet res = intermediate.executeQuery();
        return res.next() ? res.getInt("times") : 0;
    }

    public boolean getComFailed(){
        return this.comFailed;
    }

    public void setComFailed(boolean comFailed) {
        this.comFailed = comFailed;
    }

    public static void main(String[] args) {
        TestDB db = new TestDB();
        try {
            // Loops endlessly; uncomment if you want to check out the other examples
            //User user = new User("mei.kokowski", "sobin", null);
            //db.update_data(user, null);

            /* Some example calls */
            System.out.println(db.getAllUserInDB());

            /* Set online status and check if user is online */
            db.setOnlineStatus("mei.kokowski", true);
            db.setOnlineStatus("mei.kokowski", false);
            //System.out.println(db.userIsOnline("mei.kokowski"));
            //System.out.println(db.userIsOnline("mar.deichsel"));

            /* Add and delete user */
            db.addUser("max.müller", false);
            db.deleteUser("max.müller");

            /* Check if user is known to DB */
            //System.out.println(db.userIsInDB("mei.kokowski"));

            /* Set and get ping */
            //db.setPingFor("dav.sasse", true);
            //db.setPingFor("dav.sebode", true);
            //System.out.println(db.userHasToJoinMeeting("dav.sasse"));
            //System.out.println(db.userHasToJoinMeeting("mei.kokowski"));

            /* delete ping */
            //db.setPingFor("dav.sasse", false);
            //db.setPingFor("dav.sebode", false);

            /* Each time you make a call a new room will definitely be added, so be careful uncommenting */
            // Create new room
            //System.out.println("The room ID of the new room is: ");
            // room ID is yield:
            // System.out.println(db.createHall(200));
            // System.out.println(db.createConference(6));
            // System.out.println(db.createOffice(6));

            /* Set meeting & get meeting */
            db.setMeeting(4, "https://bbb04.elearning.uni-hannover.de/html5client/join?sessionToken=test_url");
            //System.out.println("Room with ID 1 has meeting: " + Boolean.toString(db.hasMeeting(1)));
            //System.out.println(db.getMeeting(1));
            System.out.print("Has meeting: ");
            System.out.print(db.hasMeeting(4));
            System.out.println(" should be true");

            /* detach meeting from room */
            db.setMeeting(4, null);
            System.out.print("Has meeting: ");
            System.out.print(db.hasMeeting(4));
            System.out.println(" should be false");
            //System.out.println("Room with ID 1 has meeting: " + Boolean.toString(db.hasMeeting(1)));
            //System.out.println(db.getMeeting(1));

            /*for(Room room : db.getAllRooms()) {
                System.out.print("Room with ID: ");
                System.out.print(room.getId());
                System.out.print(" Capacity: ");
                System.out.print(room.getCapacity());
                System.out.print(" Type: ");
                System.out.print(room.getType());
                System.out.println();
            }*/

            //db.createConference(10, null);

            /* Check if a user is inside a meeting */
            System.out.print("Inside meeting: ");
            System.out.println(db.userIsInMeeting("mar.deichsel"));

            /* Send a request */
            db.sendRequest("mei.kokowski", "jos.berger", "join");
            /* Get all requests for a person */
            System.out.print("User that sent a request: ");
            //System.out.println(db.getRequests("jos.berger"));
            /* User has pending requests */
            System.out.println(db.hasRequest("jos.berger"));
            /* remove request */
            db.removeRequest("mei.kokowski", "jos.berger");

            /* Increment access counter for a file / create access-instance */
            //db.addDocument("www.test_document_aaa.de", 5);
            //db.incrementAccessCounter("mei.kokowski", "www.test_document_aaa.de");

            //db.createStartHall(200);

            System.out.println(db.getRoomWithRoomID(0, db));

            db.createConference(2, null);

        } catch(SQLException ex) {
            ex.printStackTrace();
        } /*catch(InterruptedException ex) {
            System.out.println("An error occurred with the Threads");
        }*/

    }

    public void closeConnection() throws SQLException{
        this.dbConnection.close();
    }
}