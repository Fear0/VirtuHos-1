package Server;


import com.mysql.cj.jdbc.exceptions.CommunicationsException;

import java.sql.*;
import java.util.Properties;

/** Server class. The Server checks the DB values on validity.
 * He has to run on a seperated Server to guarantee the functions on wb_interaktionen_1.
 * @author Alan Dryaev, Meikel Kokowski
 * */
public class Server {
    final private int check_ms = 30000;
    private Connection dbConnection;
    private Properties info;

    //attempts of reconnection
    private int timeout_counter = 0;
    //time per reconnect attempt + 1000ms
    final private int timeout_ms = 3000;
    //maximum reconnect attempts
    final private int TIMEOUT_LIMIT = 3;

    final private String DATABASE_NAME = "VirtuHoS_1.";
    final private String USER_TABLE = DATABASE_NAME + "i1_user";
    final private String url = "jdbc:mysql://goethe.se.uni-hannover.de:3306/?user=Interaktion_1";
    final private String user = "Interaktion_1";
    final private String password = "JVx;2brXZzFq";


    /** Server Constructor calling initialize function
     * @exception SQLException : In case of query failure.
     * @throws SQLException
     * @author Alan Dryaev
     */
    public Server() {
        info = new Properties();
        info.put("user", user);
        info.put("password", password);
        this.connect();
    }

    /** initializes the Connection to the DB
     * @exception SQLException : In case of query failure.
     * @author Alan Dryaev
     */
    public void connect(){
        this.dbConnection = null;
        try {
            this.dbConnection = DriverManager.getConnection(url, info);
            if(this.dbConnection!=null) {
                timeout_counter = 0;
                System.out.println("Successfully connected to DB");
            }else{
                System.out.println("dbConnection is invalid");
                System.exit(1);
            }

        }catch(CommunicationsException ce){
            try {
                System.out.println("Failed to connect to DB");
                if(timeout_counter == 0){
                    System.out.println("Check your Internet Connection Error 403");
                    System.exit(403);
                }
                if(timeout_counter < TIMEOUT_LIMIT) {
                    Thread.sleep(timeout_ms);

                } else {
                    System.out.println("Connection Timeout Server Error 404");
                    System.exit(404);
                }
            }catch(InterruptedException ie){
                System.out.println("Interrupt Communication Server");
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
            System.out.println("Interrupt Communication Server");
        }
    }



    /*------------------------------- User specific Methods ------------------------------------- */

    /** checks if user is connected and sets the user offline if he doesn't react after 30s
     * @throws SQLException
     * @author Alan Dryaev
     */
    public void setOffline() throws  SQLException {
        String sql = "UPDATE " + USER_TABLE + " SET online_status = false, roomID = NULL, groupID = NULL WHERE online_status XOR online_status_2";
        PreparedStatement intermediate = dbConnection.prepareStatement(sql);
        intermediate.executeUpdate();

        sql = "UPDATE " + USER_TABLE + " SET online_status_2 = false WHERE online_status_2 = true";
        intermediate = dbConnection.prepareStatement(sql);
        intermediate.executeUpdate();
    }

    private void setInactiveUsersOffline() {
        int counter = 0;
        while(true) {
            try {
                System.out.printf("Setting all inactive Users offline %d\n", counter);
                this.setOffline();
                Thread.sleep(this.check_ms);
                counter++;
            } catch (CommunicationsException ce) {
                System.out.println("Disconnected from DB");
                //Reconnects to DB if Server loses connection to DB
                this.reconnect();

            } catch (SQLException ex) {
                System.out.println("Problem with SQL");
                System.exit(1);

            } catch (InterruptedException ex) {
                System.out.println("Interrupt Server");
                break;
            }
        }
    }


    public static void main(String[] args) {
        Server db = new Server();
        db.setInactiveUsersOffline();
    }

}