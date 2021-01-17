package Server;

import com.mysql.cj.jdbc.exceptions.CommunicationsException;
import de.uni_hannover.wb_interaktionen_1.logic.ReadConfig;

import java.sql.*;
import java.util.Properties;

/** Server class. The Server checks the DB values on validity. It checks Online values of Users and invalid Requests.
 * It has to run on a seperate Server to guarantee the functions of wb_interaktionen_1.
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
    final private String REQUEST_TABLE = DATABASE_NAME + "i1_request";


    /** Server Constructor calling connect function
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
                System.out.print("\r");
                System.out.println("Erfolgreich mit dem Server verbunden.");
                if(timeout_counter != 0) {
                    System.out.println("Setze alle inaktiven Nutzer auf dem Server offline...");
                }
                timeout_counter = 0;
            }else{
                System.out.println("URL ist ungültig oder konnte keine Verbindung herstellen");
                System.exit(1);
            }

        }catch(CommunicationsException ce){
            try {
                System.out.print(": Konnte keine Verbindung zum Server herstellen\r");
                Thread.sleep(1000);
                if(timeout_counter == 0){
                    System.out.println("Überprüfe deine Internetverbindung");
                    System.out.println("Programm wird geschlosen");
                    Thread.sleep(5000);
                    System.exit(403);
                }
                if(timeout_counter < TIMEOUT_LIMIT) {
                    Thread.sleep(timeout_ms);

                } else {
                    System.out.print("Timeout, Verbindung zum Server abgebrochen");
                    System.exit(404);
                }
            }catch(InterruptedException ie){
                System.out.print("Interrupt Problem");
            }
        }catch(SQLException sqe){

            System.out.println("Problem mit SQL");
            System.exit(1);
        }
    }

    /** reestablish connection to DB
     * @exception SQLException : In case of query failure.
     * @author Alan Dryaev
     */
    public void reconnect() {
        try {
            do {
                this.timeout_counter++;
                System.out.printf("Versuche erneut mit dem Server zu verbinden %d/%d", timeout_counter, TIMEOUT_LIMIT);
                Thread.sleep(1000);
                this.connect();
            } while (this.dbConnection == null);
        }catch(InterruptedException ie){
            System.out.println("Probleme mit der Verbidnung zum Server");
        }
    }

    /** returns amount of offline or online using depending on the input
     * @exception SQLException : In case of query failure.
     * @param on_stat true means online, false means offline
     * @author Alan Dryaev
     */
    private int checkUserStatus(boolean on_stat) throws SQLException{
        String sql = "SELECT COUNT(*) FROM " + USER_TABLE + " WHERE online_status = ?";
        PreparedStatement intermediate = dbConnection.prepareStatement(sql);
        intermediate.setBoolean(1, on_stat);
        ResultSet res = intermediate.executeQuery();
        res.next();
        return res.getInt(1);
    }

    /** returns number of Users registered in Database
     * @throws SQLException
     * @author Alan Dryaev
     */
    private int getUserCount() throws SQLException{
        String sql = "SELECT COUNT(*) FROM " + USER_TABLE ;
        PreparedStatement intermediate = dbConnection.prepareStatement(sql);
        ResultSet res = intermediate.executeQuery();
        res.next();
        return res.getInt(1);
    }

    /** checks if user is connected and sets the user offline if user isn't connected to DB"
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

        System.out.printf("Aktive Nutzer: %d / %d, %d Nutzer wurde/n abgemeldet", on_users, getUserCount(),on_before - on_users);
    }

    /** deletes invalid entries in Request Table
     * @throws SQLException
     * @author Alan Dryaev
     */
    public void deleteRequests() throws SQLException{
        String del_req = "DELETE " + REQUEST_TABLE + " FROM " + REQUEST_TABLE + " INNER JOIN " + USER_TABLE + " ON (senderID = personalID or receiverID= personalID) WHERE online_status = false";
        PreparedStatement intermediate = dbConnection.prepareStatement(del_req);
        intermediate.executeUpdate();
    }


    public static void main(String[] args) {
        Server db = new Server();
        int on_users = 0;
        System.out.printf("Setze alle inaktiven Nutzer auf dem Server offline...\n");
        while(true) {
            try {
                int timer = 0;
                db.setOffline();
                db.deleteRequests();
                Thread.sleep(5000);
                System.out.print("\r");
                while(timer < db.config.check_ms - 5000) {
                    timer += db.config.check_ms / 3;
                    on_users = db.checkUserStatus(true);
                    System.out.printf("Aktive Nutzer: %d / %d ", on_users, db.getUserCount());
                    Thread.sleep(1000);
                    for(int i = 0; i < 3; i++) {
                        System.out.print(".");
                        Thread.sleep(db.config.check_ms / 9);
                        if(i == 2) System.out.print("\r");
                    }
                }
            } catch (CommunicationsException ce) {
                System.out.println("Verbindung zur Datenbank verloren\n");
                //Reconnects to DB if Server loses connection to DB
                db.reconnect();

            } catch (SQLException ex) {
                ex.printStackTrace();
                System.out.println("Problem mit SQL");

            } catch (InterruptedException ex) {
                System.out.println("Interrupt Problem");
                break;
            }
        }

    }

}