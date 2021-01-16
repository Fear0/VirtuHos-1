package definitions;

import de.uni_hannover.wb_interaktionen_1.i_face.InteraktionControl;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextInputDialog;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class DatabaseCommunication {
    private static Connection connection = null;
    private static String username = null;
    private static boolean initialized = false;

    private static final String SAVE_BUILDING = "INSERT INTO E1_Buildings(Name, Building, Author) VALUES (?, ?, ?)";
    private static final String REPLACE_BUILDING = "UPDATE E1_Buildings SET Building=?, Author=?, Date=CURRENT_TIMESTAMP() WHERE Name=?";
    private static final String GET_INFORMATION = "SELECT Name, Author, Date FROM E1_Buildings ORDER BY Date DESC";
    private static final String LOAD_BUILDING = "SELECT Building, Author, Date FROM E1_Buildings WHERE Name = ?";
    private static final String DELETE_BUILDING = "DELETE FROM E1_Buildings WHERE Name = ?";

    private static final String UPDATE_PERSON = "INSERT INTO E1_Persons(ID, Name, Building, X, Y) VALUES (?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE Name = ?, Building = ?, X = ?, Y = ?";
    private static final String DELETE_PERSON = "DELETE FROM E1_Persons WHERE ID = ?";
    private static final String GET_PERSONS = "SELECT ID, Name, X, Y FROM E1_Persons WHERE Building = ?";
    private static final String GET_PERSON = "SELECT ID, Name, X, Y FROM E1_Persons WHERE ID = ? AND Building = ?";
    private static final String DELETE_PERSONS = "DELETE FROM E1_Persons WHERE Building = ?";

    private static final String LOCK_ROOM = "INSERT INTO E1_Locks(Building, Room) VALUES (?, ?) ON DUPLICATE KEY UPDATE Building = Building";
    private static final String UNLOCK_ROOM = "DELETE FROM E1_Locks WHERE Building = ? AND Room = ?";
    private static final String IS_LOCKED_ROOM = "SELECT 1 FROM E1_Locks WHERE Building = ? AND Room = ?";
    private static final String GET_LOCKED_ROOMS = "SELECT Room FROM E1_Locks WHERE Building = ?";
    private static final String DELETE_LOCKS = "DELETE FROM E1_Locks WHERE Building = ?";

    private static final String COLUMN_NAME = "Name";
    private static final String COLUMN_BUILDING = "Building";
    private static final String COLUMN_AUTHOR = "Author";
    private static final String COLUMN_DATE = "Date";
    private static final String COLUMN_ID = "ID";
    private static final String COLUMN_X = "X";
    private static final String COLUMN_Y = "Y";
    private static final String COLUMN_ROOM = "Room";

    //Stellt eine Verbindung zur Datenbank her
    public static void initialize() {
        if (initialized) return;
        initialized = true;
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://goethe.se.uni-hannover.de/VirtuHoS_1";
        String username = "Editor_1";
        String password = "pU:_d(cX4G%E";
        try {
            Class.forName(driver);
            DatabaseCommunication.connection = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            System.err.println(Text.DATABASE_CONNECTION_NO_CONNECTION);
        }
    }

    //Dialog zum Speichern eines Gebäudes
    public static void saveDialog(Building building) throws SQLException {
        TextInputDialog textInputDialog = new TextInputDialog("");
        textInputDialog.setTitle(Text.TITLE);
        textInputDialog.setHeaderText(Text.DATABASE_CONNECTION_NAME_DIALOG);
        textInputDialog.setContentText(Text.DATABASE_CONNECTION_NAME);
        Optional<String> result = textInputDialog.showAndWait();
        String name = result.orElse("");
        building.setName(name);
        if (name.equals("")) return;
        try (PreparedStatement preparedStatement = connection.prepareStatement(SAVE_BUILDING)) {
            preparedStatement.setString(1, name);
            preparedStatement.setObject(2, building);
            preparedStatement.setString(3, username);
            try {
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                replaceDialog(building, name);
            }
        }
    }

    //save with a given name instead of asking for one
    public static void replace(Building building, String name) throws SQLException {
        building.setName(name);
        if (name.equals("")) return;
        try (PreparedStatement preparedStatement = connection.prepareStatement(REPLACE_BUILDING)) {
            preparedStatement.setObject(1, building);
            preparedStatement.setString(2, username);
            preparedStatement.setString(3, name);
            preparedStatement.executeUpdate();
            DatabaseCommunication.deletePersons(building.getName());
            DatabaseCommunication.deleteLocks(building.getName());
        }
    }

    //Dialog zum Ersetzen eines bereits vorhandenen Gebäudes
    private static void replaceDialog(Building building, String name) throws SQLException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(Text.TITLE);
        alert.setHeaderText(null);
        alert.setContentText(Text.DATABASE_CONNECTION_DUPLICATE_NAME_1);
        alert.getButtonTypes().setAll(Text.buttonTypeYes, Text.buttonTypeNo);
        try (PreparedStatement preparedStatement = connection.prepareStatement(LOAD_BUILDING)) {
            preparedStatement.setString(1, name);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                resultSet.next();
                byte[] buffer = resultSet.getBytes(COLUMN_AUTHOR);
                String author = (buffer != null) ? new String(buffer, StandardCharsets.UTF_8) : Text.DATABASE_CONNECTION_NULL;
                buffer = resultSet.getBytes(COLUMN_DATE);
                String date = new String(buffer, StandardCharsets.UTF_8);
                alert.setContentText(String.format(Text.DATABASE_CONNECTION_DUPLICATE_NAME_2, author, date));
            }
        }
        Optional<ButtonType> result = alert.showAndWait();
        if (result.orElse(null) != Text.buttonTypeYes) return;
        DatabaseCommunication.replace(building, name);
    }

    //Dialog zum Laden eines Gebäudes
    public static Building loadDialog(InteraktionControl IC) throws SQLException {
        String name = DatabaseCommunication.selectNameDialog();
        return DatabaseCommunication.loadDialog(name, IC);
    }

    //load with a given name instead of asking
    public static Building loadDialog(String name, InteraktionControl IC) throws SQLException {
        if (name == null || name.equals("")) return null;
        Building building = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement(LOAD_BUILDING)) {
            preparedStatement.setString(1, name);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                resultSet.next();
                byte[] buffer = resultSet.getBytes(COLUMN_BUILDING);
                if (buffer != null) {
                    ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(buffer));
                    building = (Building) objectInputStream.readObject();
                }
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, Text.DATABASE_CONNECTION_LOAD_EXCEPTION);
                alert.getButtonTypes().setAll(Text.buttonTypeYes, Text.buttonTypeNo);
                alert.setTitle(Text.TITLE);
                Optional<ButtonType> result = alert.showAndWait();
                if (result.orElse(null) == Text.buttonTypeYes) {
                    DatabaseCommunication.deletePersons(name);
                    DatabaseCommunication.deleteLocks(name);
                    try (PreparedStatement preparedStatement1 = connection.prepareStatement(DELETE_BUILDING)) {
                        preparedStatement1.setString(1, name);
                        preparedStatement1.executeUpdate();
                    }
                    IC.deleteBuilding(name);
                }
            }
        }
        return building;
    }

    //Dialog zum Auswählen eines in der Datenbank gespeicherten Gebäudes
    private static String selectNameDialog() throws SQLException {
        List<String> choices = new ArrayList<>();
        try (
                PreparedStatement preparedStatement = connection.prepareStatement(GET_INFORMATION);
                ResultSet resultSet = preparedStatement.executeQuery()
        ) {
            while (resultSet.next()) {
                byte[] buffer = resultSet.getBytes(COLUMN_NAME);
                String string = new String(buffer, StandardCharsets.UTF_8);
                choices.add(string);
            }
        }
        if (choices.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, Text.DATABASE_CONNECTION_NO_BUILDINGS);
            alert.setTitle(Text.TITLE);
            alert.setHeaderText(null);
            alert.showAndWait();
            return null;
        }
        ChoiceDialog<String> choiceDialog = new ChoiceDialog<>(choices.get(0), choices);
        choiceDialog.setTitle(Text.TITLE);
        choiceDialog.setHeaderText(null);
        choiceDialog.setContentText(Text.DATABASE_CONNECTION_SELECTION_DIALOG);
        Optional<String> result = choiceDialog.showAndWait();
        return result.orElse(null);
    }
    
    public static void setUsername(String username) {
        DatabaseCommunication.username = username;
    }

    public static String getUsername() {
        return DatabaseCommunication.username;
    }

    public static void updatePerson(Person person, String buildingName) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_PERSON)) {
            preparedStatement.setString(1, person.getId());
            preparedStatement.setString(2, person.getName());
            preparedStatement.setString(3, buildingName);
            preparedStatement.setDouble(4, person.getX());
            preparedStatement.setDouble(5, person.getY());
            preparedStatement.setString(6, person.getName());
            preparedStatement.setString(7, buildingName);
            preparedStatement.setDouble(8, person.getX());
            preparedStatement.setDouble(9, person.getY());
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            System.err.println("updatePerson Error");
        }
    }

    public static void deletePerson(String id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_PERSON)) {
            preparedStatement.setString(1, id);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            System.err.println("deletePerson Error");
        }
    }

    public static List<Person> getPersons(String buildingName) {
        List<Person> persons = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_PERSONS)) {
            preparedStatement.setString(1, buildingName);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    byte[] buffer = resultSet.getBytes(COLUMN_ID);
                    String id = new String(buffer, StandardCharsets.UTF_8);
                    buffer = resultSet.getBytes(COLUMN_NAME);
                    String name = new String(buffer, StandardCharsets.UTF_8);
                    double x = resultSet.getDouble(COLUMN_X);
                    double y = resultSet.getDouble(COLUMN_Y);
                    persons.add(new Person(id, name, x, y));
                }
            }
        } catch (Exception e) {
            System.err.println("getPersons Error");
        }
        return persons;
    }
    public static Person getPerson(String id, String buildingName) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_PERSON)) {
            preparedStatement.setString(1, id);
            preparedStatement.setString(2, buildingName);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                resultSet.next();
                byte[] buffer = resultSet.getBytes(COLUMN_ID);
                String temp_id = new String(buffer, StandardCharsets.UTF_8);
                buffer = resultSet.getBytes(COLUMN_NAME);
                String name = new String(buffer, StandardCharsets.UTF_8);
                double x = resultSet.getDouble(COLUMN_X);
                double y = resultSet.getDouble(COLUMN_Y);
                return new Person(temp_id, name, x, y);
            }
        } catch (Exception e) {
            System.err.println("getPersons Error");
        }
        return null;
    }
    public static void lockRoom(String buildingName, String roomName) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(LOCK_ROOM)) {
            preparedStatement.setString(1, buildingName);
            preparedStatement.setString(2, roomName);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            System.err.println("lockRoom Error");

        }
    }

    public static void unlockRoom(String buildingName, String roomName) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(UNLOCK_ROOM)) {
            preparedStatement.setString(1, buildingName);
            preparedStatement.setString(2, roomName);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            System.err.println("unlockRoom Error");
        }
    }

    public static void toggleLockRoom(String buildingName, String roomName) {
        if (isLocked(buildingName, roomName))
            unlockRoom(buildingName, roomName);
        else
            lockRoom(buildingName, roomName);
    }

    public static boolean isLocked(String buildingName, String roomName) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(IS_LOCKED_ROOM)) {
            preparedStatement.setString(1, buildingName);
            preparedStatement.setString(2, roomName);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.isBeforeFirst();
            }
        } catch (Exception e) {
            System.err.println("toggleLockRoom Error");
        }
        return false;
    }

    public static List<String> getLockedRooms(String buildingName) {
        List<String> roomNames = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_LOCKED_ROOMS)) {
            preparedStatement.setString(1, buildingName);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    byte[] buffer = resultSet.getBytes(COLUMN_ROOM);
                    String roomName = new String(buffer, StandardCharsets.UTF_8);
                    roomNames.add(roomName);
                }
            }
        } catch (Exception e) {
            System.err.println("getLockedRooms Error");
        }
        return roomNames;
    }

    public static void deletePersons(String buildingName) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_PERSONS)) {
            preparedStatement.setString(1, buildingName);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            System.err.println("deletePersons Error");
        }
    }

    public static void deleteLocks(String buildingName) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_LOCKS)) {
            preparedStatement.setString(1, buildingName);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            System.err.println("deleteLocks Error");
        }
    }
}
