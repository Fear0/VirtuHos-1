package Server;


import com.mysql.cj.jdbc.exceptions.CommunicationsException;
import de.uni_hannover.wb_interaktionen_1.logic.ReadConfig;

import java.sql.*;
import java.util.Properties;

/** Server class. The Server checks the DB values on validity.
 * He has to run on a seperated Server to guarantee the functions on wb_interaktionen_1.
 * @author Alan Dryaev, Meikel Kokowski
 * */
public class Server {
    private Connection dbConnection;
    private Properties info;
    private ReadConfig config;
    //attempts of reconnection
    private int timeout_counter = 0;
    //time per reconnect attempt + 1000ms
    final private int timeout_ms = 3000;
    //maximum reconnect attempts
    final private int TIMEOUT_LIMIT = 3;

    final private String DATABASE_NAME = "VirtuHoS_1.";
    final private String USER_TABLE = DATABASE_NAME + "i1_user";
    /*
    final private String url = "jdbc:mysql://goethe.se.uni-hannover.de:3306/?user=Interaktion_1";
    final private String user = "Interaktion_1";
    final private String password = "JVx;2brXZzFq";
    */



    /** Server Constructor calling initialize function
     * @exception SQLException : In case of query failure.
     * @throws SQLException
     * @author Alan Dryaev
     */
    public Server() {
        try {
            config = new ReadConfig();
        } catch(Exception e) {
            System.out.println("Config kann nicht gelesen werden");
        }
        info = new Properties();
        info.put("user", config.user);
        info.put("password", config.password);
        this.connect();
    }

    /** initializes the Connection to the DB
     * @exception SQLException : In case of query failure.
     * @author Alan Dryaev
     */
    public void connect(){
        this.dbConnection = null;
        try {
            this.dbConnection = DriverManager.getConnection(config.url, info);
            if(this.dbConnection!=null) {
                timeout_counter = 0;
                System.out.println("Erfolgreich zum Server verbunden.");
            }else{
                System.out.println("URL ist ungültig oder konnte keine Verbindung herstellen");
                System.exit(1);
            }

        }catch(CommunicationsException ce){
            try {
                System.out.println("Konnte keine Verbindung zum Server herstellen");
                if(timeout_counter == 0){
                    System.out.println("Überprüfe deine Internetverbindung");
                    System.exit(403);
                }
                if(timeout_counter < TIMEOUT_LIMIT) {
                    Thread.sleep(timeout_ms);

                } else {
                    System.out.println("Timeout, Verbindung zum Server abgebrochen");
                    System.exit(404);
                }
            }catch(InterruptedException ie){
                System.out.println("Interrupt Communication Server");
            }
        }catch(SQLException sqe){

            System.out.println("Problem mit SQL");
            System.exit(1);
        }
    }

    public void reconnect() {
        try {
            do {
                this.timeout_counter++;
                System.out.printf("Versuche erneut zum Server zu verbinden...%d/%d\n", timeout_counter, TIMEOUT_LIMIT);
                Thread.sleep(1000);
                this.connect();
            } while (this.dbConnection == null);
        }catch(InterruptedException ie){
            System.out.println("Probleme mit der Verbidnung zum Server");
        }
    }


    private int checkUserStatus(boolean on_stat) throws SQLException{
        String sql = "SELECT COUNT(*) FROM " + USER_TABLE + " WHERE online_status = ?";
        PreparedStatement intermediate = dbConnection.prepareStatement(sql);
        intermediate.setBoolean(1, on_stat);
        ResultSet res = intermediate.executeQuery();
        res.next();
        return res.getInt(1);
    }

    private int getUserCount() throws SQLException{
        String sql = "SELECT COUNT(*) FROM " + USER_TABLE ;
        PreparedStatement intermediate = dbConnection.prepareStatement(sql);
        ResultSet res = intermediate.executeQuery();
        res.next();
        return res.getInt(1);
    }

    /*------------------------------- User specific Methods ------------------------------------- */

    /** checks if user is connected and sets the user offline if he doesn't react after 30s
     * @throws SQLException
     * @author Alan Dryaev
     */
    public void setOffline() throws  SQLException {

        int on_before = checkUserStatus(true);

        String sql = "UPDATE " + USER_TABLE + " SET online_status = false, roomID = NULL, groupID = NULL WHERE online_status XOR online_status_2";
        PreparedStatement intermediate = dbConnection.prepareStatement(sql);
        intermediate.executeUpdate();

        sql = "UPDATE " + USER_TABLE + " SET online_status_2 = false WHERE online_status_2 = true";
        intermediate = dbConnection.prepareStatement(sql);
        intermediate.executeUpdate();

        int on_users = checkUserStatus(true);

        System.out.printf("Aktive Nutzer: %d / %d, %d Nutzer wurden abgemeldet\r", on_users, getUserCount(),on_before - on_users);

    }

    private void setInactiveUsersOffline() {
        int on_users = 0;
        System.out.printf("Setze alle inaktiven Nutzer auf dem Server offline...\n");
        while(true) {
            try {
                int timer = 0;
                this.setOffline();
                while(timer < config.check_ms) {
                    timer += config.check_ms / 5;
                    Thread.sleep(config.check_ms / 5);
                    on_users = checkUserStatus(true);
                    System.out.printf("Aktive Nutzer: %d / %d\r", on_users, getUserCount());
                }
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