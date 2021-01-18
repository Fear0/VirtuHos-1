package wb.analyse1.Database;

import wb.analyse1.analyse.Analyse;
import wb.analyse1.analyse.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

/* Somehow running this class using Gradle causes an error because it doesn't save the classpath of the
mysql- connector: mysql-connector-java-8.0.21.jar that I downloaded as a driver to connect to the database, eventhough
I added its path to the libraries!!.
Running manually with explicit adding of the file classpath works perfectly: java -cp pathToTheFile\mysql-connector-java-8.0.21.jar; wb.analyse1.analyse.Main
 */

/**
 * connects to the database server and performs queries to save the results of the analysis
 */

public class Database {

    private final String db_url = "jdbc:mysql://goethe.se.uni-hannover.de/VirtuHoS_1";
    private final String user = "Analyse_1";
    private final String password = "M2Rg4Yc,=#b_";
    private Analyse analyse;
    private Connection conn = null;

    public Database() {
        try {
            conn = DriverManager.getConnection(db_url, user, password);
            if (conn != null) {
                System.out.println("SUCCESS");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Analyse analyse = new Analyse();
        Database db = new Database();
        db.fetchUsers();
    }

    /**
     * @param analyse
     */
    public void setAnalyse(Analyse analyse) {
        this.analyse = analyse;
    }

    /**
     * get Data from A1_User table and build Users list
     * This is the initial Userslist of the analysis object at each start of the application.
     * Null if database is empty.
     *
     * @return userSet
     */
    public LinkedHashSet<User> fetchUsers() {
        /*TODO: get Data from A1_User table and build Users list. This is the initial Userslist of the analysis object
           at each start of the application. Null if database is empty.*/

        LinkedHashSet<User> userSet = new LinkedHashSet<>();
        String sqlUsers = "Select * FROM A1_User";
        try {
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(sqlUsers);
            if (!result.isBeforeFirst() && result.getRow() == 0) { // checken ob zeilen gibt, weil if (!result.next())
                return userSet;
            }
            while (result.next()) {
                String UserID = result.getString("UserID");
                String UserName = result.getString("UserName");
                int postMatrix = result.getInt("PosMatrix");
                int Degree = result.getInt("Degree");
                int online = result.getInt("Online");
                //get all atrributes
                double Betweenness = result.getDouble("Betweenness");
                double Closeness = result.getDouble("Closeness");
                double Eigenvector = result.getDouble("Eigenvector");
                String CliqueID = result.getString("CliqueID");


                User user = new User(UserID, UserName, postMatrix, online);
                user.setDegree(Degree);
                user.setBetweenness(Betweenness);
                user.setCloseness(Closeness);
                user.setEigenvector(Eigenvector);
                user.setCliqueIDs(CliqueID);
                //get all atrributes

               /* System.out.print(result.getString("UserID")+",");
                System.out.print(result.getString("UserName")+",");
                System.out.print(result.getInt("Degree")+",");
                System.out.print();*/
                userSet.add(user);

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return userSet;
    }

    public LinkedHashSet<User> fetchOnlineUsers(){

        LinkedHashSet<User> userSet = new LinkedHashSet<>();
        String sqlOnlineUsers = "Select * FROM A1_User WHERE Online = 1";
        try{
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(sqlOnlineUsers);
            if (!result.isBeforeFirst() && result.getRow() == 0) { // checken ob zeilen gibt, weil if (!result.next())
                return userSet;
            }

            while (result.next()) {
                String UserID = result.getString("UserID");
                String UserName = result.getString("UserName");
                int postMatrix = result.getInt("PosMatrix");
                int Degree = result.getInt("Degree");
                int online = result.getInt("Online");
                //get all atrributes
                double Betweenness = result.getDouble("Betweenness");
                double Closeness = result.getDouble("Closeness");
                double Eigenvector = result.getDouble("Eigenvector");
                String CliqueID = result.getString("CliqueID");


                User user = new User(UserID, UserName, postMatrix, online);
                user.setDegree(Degree);
                user.setBetweenness(Betweenness);
                user.setCloseness(Closeness);
                user.setEigenvector(Eigenvector);
                user.setCliqueIDs(CliqueID);
                //get all atrributes

               /* System.out.print(result.getString("UserID")+",");
                System.out.print(result.getString("UserName")+",");
                System.out.print(result.getInt("Degree")+",");
                System.out.print();*/
                userSet.add(user);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return userSet;
    }

    /**
     *  find the position in the matrix of the user with the given ID
     * @param users
     * @param ID
     * @return
     */
    public static int getIndexforID(LinkedHashSet<User> users, String ID) {
        for (User user : users) {
            if (user.getId().equals(ID)) {
                return user.getPositionMatrix();
            }
        }
        return -1;
    }

    /**
     * get Data from A1_Interaction table and build network matrix.
     * This is the initial network matrix of the analysis object  at each start of the application.
     * Null if database is empty.
     *
     * @return matrix
     */
    public int[][] fetchNetworkMatrix(LinkedHashSet<User> users) {
        /* TODO:
         *   */
        if (users == null || users.size() == 0){
            return new int[0][];
        }
        int[][] matrix = new int[0][];
        List<String> interactions = new ArrayList<>();
        String sqlInteraction = "Select * FROM A1_Interaction";
        try {
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(sqlInteraction);

            ArrayList<String> ids = new ArrayList<>();
            if (!result.isBeforeFirst() && result.getRow() == 0) { //
                return new int[0][];
            }
            //System.out.println("new results:");
            while (result.next()) {
                //System.out.println("results:");
                //System.out.println(result.getString("UserID1"));
                //System.out.println(result.getString("UserID2"));
                String UserID1 = result.getString("UserID1");
                String UserID2 = result.getString("UserID2");
                int interaction = result.getInt(("Interaction"));
                int index1 = getIndexforID(users,UserID1);
                int index2 = getIndexforID(users,UserID2);
                interactions.add(index1 + "," + index2 + "," + interaction);
                if (!ids.contains(UserID1)) {
                    ids.add(UserID1);
                }
                if (!ids.contains(UserID2)) {
                    ids.add(UserID2);
                }
            }
            int n = ids.size();
            //System.out.println(n);
            matrix = new int[n][n];
            for (String interaction : interactions) {
                try {
                    String[] parts = interaction.split(",");
                    /*System.out.println("\t" + matrix.length + " " + matrix[0].length);
                    System.out.println("\t" + parts.length);
                    System.out.println(Arrays.toString(parts));*/
                    matrix[Integer.parseInt(parts[0])][Integer.parseInt(parts[1])] = Integer.parseInt(parts[2]);
                    matrix[Integer.parseInt(parts[1])][Integer.parseInt(parts[0])] = Integer.parseInt(parts[2]);
                } catch (Exception e) {
                    System.out.println("Refetching...");
                    e.printStackTrace();
                    return fetchNetworkMatrix(users);
                }
            }
            //System.out.println(Arrays.deepToString(matrix).replaceAll("],", "]," + System.getProperty("line.separator")));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return matrix;
    }


    /**
     * get Data from A1_Interaction table and build network matrix.
     * This is the initial network matrix of the analysis object  at each start of the application.
     * Null if database is empty.
     *
     * @return matrix
     */
    public int[][] fetchNetworkMatrix() {
        /* TODO:
         *   */

        int[][] matrix = new int[0][];
        List<String> interactions = new ArrayList<>();
        String sqlInteraction = "Select * FROM A1_Interaction";
        try {
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(sqlInteraction);

            ArrayList<String> ids = new ArrayList<>();
            if (!result.isBeforeFirst() && result.getRow() == 0) { //
                return new int[0][];
            }
            System.out.println("new results:");
            while (result.next()) {
                System.out.println("results:");
                System.out.println(result.getString("UserID1"));
                System.out.println(result.getString("UserID2"));
                String UserID1 = result.getString("UserID1");
                String UserID2 = result.getString("UserID2");
                int interaction = result.getInt(("Interaction"));
                int index1 = analyse.getIndexforID(UserID1);
                int index2 = analyse.getIndexforID(UserID2);
                interactions.add(index1 + "," + index2 + "," + interaction);
                if (!ids.contains(UserID1)) {
                    ids.add(UserID1);
                }
                if (!ids.contains(UserID2)) {
                    ids.add(UserID2);
                }
            }
            int n = ids.size();
            System.out.println(n);
            matrix = new int[n][n];
            for (String interaction : interactions) {
                try {
                    String[] parts = interaction.split(",");
                    /*System.out.println("\t" + matrix.length + " " + matrix[0].length);
                    System.out.println("\t" + parts.length);
                    System.out.println(Arrays.toString(parts));*/
                    matrix[Integer.parseInt(parts[0])][Integer.parseInt(parts[1])] = Integer.parseInt(parts[2]);
                    matrix[Integer.parseInt(parts[1])][Integer.parseInt(parts[0])] = Integer.parseInt(parts[2]);
                } catch (Exception e) {
                    System.out.println("Refetching...");
                    return fetchNetworkMatrix();
                }
            }
            //System.out.println(Arrays.deepToString(matrix).replaceAll("],", "]," + System.getProperty("line.separator")));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return matrix;
    }

    /**
     * Inserts in or Updates Interaction table from the network matrix values.
     */

    // TODO: don't use 'this.analyse', use parameters instead
    public void insertInOrUpdateInteractionTable() {

        try {
            String selectString = "SELECT * from A1_Interaction where UserID1=? AND UserID2=?";
            String insertString = "Insert INTO A1_Interaction (UserID1,UserID2,Interaction) values (?,?,?)";
            String updateString = "UPDATE A1_Interaction SET Interaction = ? WHERE UserID1=? AND UserID2=?";

            PreparedStatement selectInteraction1 = conn.prepareStatement(selectString);
            PreparedStatement selectInteraction2 = conn.prepareStatement(selectString);
            PreparedStatement insertInteraction = conn.prepareStatement(insertString);
            PreparedStatement updateInteraction = conn.prepareStatement(updateString);
            if (analyse.getNetworkMatrix() == null) {
                return;
            }
            int[][] networkMatrix = analyse.getNetworkMatrix();
            for (int i = 0; i < networkMatrix.length; i++) {
                for (int j = i + 1; j < networkMatrix.length; j++) {
                    User user1 = analyse.getUserforIndex(i);
                    User user2 = analyse.getUserforIndex(j);
                    String idUser1 = user1.getId();
                    String idUser2 = user2.getId();
                    selectInteraction1.setString(1, idUser1);
                    selectInteraction1.setString(2, idUser2);
                    ResultSet result1 = selectInteraction1.executeQuery();
                    selectInteraction2.setString(1, idUser2);
                    selectInteraction2.setString(2, idUser1);
                    ResultSet result2 = selectInteraction2.executeQuery();
                    if (result1.next() || result2.next()) {
                        updateInteraction.setInt(1, networkMatrix[i][j]);
                        updateInteraction.setString(2, idUser1);
                        updateInteraction.setString(3, idUser2);
                        updateInteraction.addBatch();
                    } else {
                        insertInteraction.setString(1, idUser1);
                        insertInteraction.setString(2, idUser2);
                        insertInteraction.setInt(3, networkMatrix[i][j]);
                        insertInteraction.addBatch();
                    }

                }
            }
            try {
                updateInteraction.executeBatch();
                insertInteraction.executeBatch();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Inserts in or updates the Users table from the users list. Update new values for each user.
     */
    // TODO: don't use 'this.analyse', use parameters instead
    public void insertInOrUpdateUsersTable() {

        if (analyse.getUsers().size() == 0) {
            return;
        }
        LinkedHashSet<User> users = analyse.getUsers();

        try {

            String selectString = "SELECT * from A1_User WHERE UserID=?";
            String insertString = "INSERT INTO A1_User (UserID,UserName,Degree,Betweenness,Closeness,Eigenvector,CliqueID,PosMatrix,Online) values (?,?,?,?,?,?,?,?,?)";
            String updateString = "UPDATE A1_User SET UserID = ?, UserName = ?, Degree = ?, Betweenness = ?, Closeness = ?, Eigenvector = ?, CliqueID = ?, PosMatrix = ?, Online = ? WHERE UserID = ?";
            PreparedStatement updateUser = conn.prepareStatement(updateString);
            PreparedStatement insertUser = conn.prepareStatement(insertString);
            PreparedStatement selectUser = conn.prepareStatement(selectString);

            //System.out.println(users);
            for (User user : users) {

                selectUser.setString(1, user.getId());
                ResultSet result = selectUser.executeQuery();

                if (result.next()) {
                    String userName = result.getString("UserName");
                    if (!user.getName().equals(userName)) {
                        continue;
                    }
                    updateUser.setString(1, user.getId());
                    updateUser.setString(2, user.getName());
                    updateUser.setInt(3, user.getDegree());
                    if(Double.isNaN(user.getBetweenness())){
                        //System.out.println("Lahiri 9a7ba kbira");
                        updateUser.setDouble(4,0.0);
                    } else {
                        updateUser.setDouble(4, user.getBetweenness());
                    }
                    if (Double.isNaN(user.getCloseness())){
                        updateUser.setDouble(5, 0.0);
                    } else {
                        updateUser.setDouble(5, user.getCloseness());
                    }
                    updateUser.setDouble(6, user.getEigenvector());
                    updateUser.setString(7, user.getCliqueIDs());
                    updateUser.setInt(8, user.getPositionMatrix());
                    updateUser.setInt(9, user.getOnline());
                    updateUser.setString(10,user.getId());
                    updateUser.addBatch();
                } else {
                    insertUser.setString(1, user.getId());
                    insertUser.setString(2, user.getName());
                    insertUser.setInt(3, user.getDegree());
                    if(Double.isNaN(user.getBetweenness())){
                        insertUser.setDouble(4,0.0);
                    } else {
                        insertUser.setDouble(4, user.getBetweenness());
                    }
                    if (Double.isNaN(user.getCloseness())){
                        insertUser.setDouble(5, 0.0);
                    } else {
                        insertUser.setDouble(5, user.getCloseness());
                    }
                    insertUser.setDouble(6, user.getEigenvector());
                    insertUser.setString(7, user.getCliqueIDs());
                    insertUser.setInt(8, user.getPositionMatrix());
                    insertUser.setInt(9, user.getOnline());
                    insertUser.addBatch();
                }
            }
            try {
                updateUser.executeBatch();
                insertUser.executeBatch();
            } catch (BatchUpdateException e) {
                System.out.println(e);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    // just for test!!
    public void deleteAllUsers() {

        try {
            Statement stmt = conn.createStatement();
            String delete = "DELETE from A1_User";
            stmt.executeUpdate(delete);
            System.out.println("All rows in table A1_User deleted");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    // just for test!!
    public void deleteAllInteractions() {

        try {
            Statement stmt = conn.createStatement();
            String delete = "DELETE from A1_Interaction";
            stmt.executeUpdate(delete);
            System.out.println("All rows in table A1_Interaction deleted");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public void close() {
        if (this.conn != null) {
            try {
                this.conn.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
}
