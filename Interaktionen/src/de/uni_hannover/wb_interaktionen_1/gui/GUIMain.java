package de.uni_hannover.wb_interaktionen_1.gui;

import de.uni_hannover.wb_interaktionen_1.Main;
import de.uni_hannover.wb_interaktionen_1.logic.Login;
import de.uni_hannover.wb_interaktionen_1.logic.ReadConfig;
import de.uni_hannover.wb_interaktionen_1.logic.User;
import de.uni_hannover.wb_interaktionen_1.rooms.*;
import de.uni_hannover.wb_interaktionen_1.test_db.TestDB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;


import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/** This class builds the GUI for the main page of the application.
 *
 * @author David Sasse and Joshua Berger
 */
public class GUIMain {
    ArrayList<Room> existent_rooms;
    ArrayList<HallGroup> hall_groups;
    Login login;
    TestDB db;
    Main main;
    static final DataFormat USER_LIST = new DataFormat("UserList");
    private ArrayList<ListView<String>> all_listviews_room;
    private ArrayList<ListView<String>> all_listviews_group;
    private ArrayList<Label> all_capacity_labels;
    private ReadConfig rc;


    /** Creates the GUIMain Object.
     *
     * Is creates a list with the existent rooms and with the hall groups.
     *
     * @param log The login
     * @param db The database
     */
    public GUIMain(Login log, TestDB db, Main main){
        try{
            rc = new ReadConfig();
            this.existent_rooms = db.getAllRooms(rc.Building);
            this.hall_groups = db.getAllHallGroups(rc.Building);
            this.main = main;
        } catch (SQLException e){
            e.printStackTrace();
        } catch (IOException ie){
            ie.printStackTrace();
        }
        this.db = db;
        this.login = log;
        all_listviews_room = new ArrayList<>();
        all_listviews_group = new ArrayList<>();
        all_capacity_labels = new ArrayList<>();
    }

    /** The getter for the ArrayList with all ListViews for the rooms.
     *
     * @return the ArrayList with all ListViews
     */
    public ArrayList<ListView<String>> getAll_listviews_room(){
        return all_listviews_room;
    }

    /** The getter for the ArrayList with all ListViews for the groups.
     *
     * @return the ArrayList with all ListViews
     */
    public ArrayList<ListView<String>> getAll_listviews_group(){
        return all_listviews_group;
    }

    /** The getter for the ArrayList with all Labels withe the Capacity.
     *
     * @return the ArrayList with all Labels
     */
    public ArrayList<Label> getAll_capacity_labels(){
        return all_capacity_labels;
    }

    /** This method creates the button used to log out of the system
     * @author Joshua Berger
     * @param window: the stage that is needed in nested methods
     * @param scene: the scene that is needed in nested methods
     * @return The logout button
     */
    public Button createLogoutButton(Stage window, Scene scene) {
        Button logout = new Button("Logout");
        logout.setOnAction(e -> createLogoutPopUp(window, scene));
        return logout;
    }

    /** Temporary test button so you can activate a given users webcam when the browser window is open
     * @author David Sebode
     * @param user The user you are activating the webcam for
     * @return The webcam button
     */
    public Button createWebcamButton(User user){
        Button webcam = new Button("Activate Webcam");
        webcam.setOnAction(e-> user.activateWebcam());
        return webcam;
    }

    /** Switch the window of the GUI login scene
     * @param window Stage of the GUI
     * @param next Next scene that will be displayed
     * @author Joshua Berger
     */
    public void confirmLogout(Stage window, Scene next){
        System.out.println("Setting current user in database to offline.");
        if(login.getCurrentUser().getCurrent_room() != null){
            login.getCurrentUser().getCurrent_room().leave_Room(login.currentUser, db, login.getCurrentUser().getCurrent_room().getId());
        }
        login.setCurrentUserOffline();
        login.resetCurrentUser();
        window.setScene(next);
    }
    /** Close the given stage
     * @param stage: The stage to close
     * @author Joshua Berger
     */
    public void closeStage(Stage stage){
        stage.close();
    }

    /** Close the given stage
     * @param window: The stage that is needed in nested methods
     * @param next: the scene that is needed in nested methods
     * @return returns the created popup window
     * @author Joshua Berger
     */
    public Stage createLogoutPopUp(Stage window, Scene next) {
        Stage popup = new Stage();
        VBox comp = new VBox();
        Label infotext = new Label("Wollen Sie sich wirklich ausloggen?");
        Button confirm = new Button("Ja");
        Button cancel = new Button("Nein");
        confirm.setOnAction(e -> {
            confirmLogout(window, next);
            closeStage(popup);
        });
        cancel.setOnAction(e -> closeStage(popup));
        Scene stageScene = new Scene(comp, 300, 150);
        comp.getChildren().addAll(infotext, confirm, cancel);
        popup.setScene(stageScene);
        popup.show();
        return popup;
    }

    /** It creates the control elements for adding and deleting hall groups
     *
     * @param window The window where the controls should by displayed
     * @param main The main class for updating the grid
     * @return The VBox with with the control elements
     */
    public VBox createHallControl(Stage window, Main main){
        //Creating observablelists for the comboboxes
        ObservableList<String> hall_groups_observable = FXCollections.observableArrayList();
        for(HallGroup g : hall_groups){
            hall_groups_observable.add("hall_group_" + g.getId());
        }
        ObservableList<String> halls_observable = FXCollections.observableArrayList();
        for(Room r : existent_rooms){
            if(r.getType().equals("hall")) {
                halls_observable.add("hall_" + r.getId());
            }
        }


        //Creating the control elements
        Label description = new Label("Steuerung für die Gruppen in der Halle:");
        ComboBox<String> add_cb = new ComboBox<>(halls_observable);
        add_cb.getSelectionModel().selectFirst();
        Button add = new Button("Hinzufügen");
        ComboBox<String> delete_cb = new ComboBox<>(hall_groups_observable);
        delete_cb.getSelectionModel().selectFirst();
        Button delete = new Button("Löschen");

        //If no groups exists the delete button and delete combobox should be disabled
        if(hall_groups.size() <= 0){
            delete.setDisable(true);
            delete_cb.setDisable(true);
        }

        //If no hall exists the add button and add combobox should be disabled
        if(halls_observable.size() <= 0){
            add.setDisable(true);
            add_cb.setDisable(true);
        }

        //Setting the elements in boxes together
        HBox hbox = new HBox(add_cb, add, delete_cb, delete);
        HBox.setMargin(add_cb, new Insets(10, 5, 10, 10));
        HBox.setMargin(add, new Insets(10,20,10,5));
        HBox.setMargin(delete_cb, new Insets(10,5,10,20));
        HBox.setMargin(delete, new Insets(10,10,10,5));

        VBox vbox = new VBox();
        VBox.setMargin(description, new Insets(10,10,0,10));
        vbox.getChildren().addAll(description, hbox);

        //Control for the add button
        add.setOnAction(e -> {
            String selectedhall = add_cb.getValue();
            for (Room r : existent_rooms){
                if(selectedhall.equals("hall_" + r.getId())){
                    try{
                        db.createGroup(r.getId());
                    } catch (SQLException s){
                        s.printStackTrace();
                    }
                }
            }
            main.updatemain(window);
        });

        //Control for the delete button
        delete.setOnAction(e -> {
            String selectedgroup = delete_cb.getValue();
            for (HallGroup g : hall_groups){
                if(selectedgroup.equals("hall_group_" + g.getId())){
                    try{
                        if(db.getAllUserInHallGroupObservable(g.getId()).size() == 0) {
                            db.deleteGroup(g.getId());
                        } else {
                            ErrorMessage er = new ErrorMessage();
                            er.createError("Sie können die Gruppe nicht löschen, da sich noch ein oder mehrere Benutzer in ihr befinden.");
                            return;
                        }
                    } catch (SQLException s){
                        s.printStackTrace();
                    }
                }
            }
            main.updatemain(window);
        });
        return vbox;
    }


    /**Creates GridPane
     *
     * This methode creates the layout with all elements for the main page.
     *
     * @return The GridPane with the layout
     */
    /*public GridPane createMainGripPane(){

        //Initialize the GridPane
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setGridLinesVisible(true);

        Insets in = new Insets(10,10,10,10);

        //Create the rooms
        for (int i = 0; i < existent_rooms.size() + hall_groups.size(); i++){
            Label l;
            if(i < existent_rooms.size()){
                l = new Label(existent_rooms.get(i).getType() + "_" + existent_rooms.get(i).getId());
            } else {
                l = new Label("hall_group_" + hall_groups.get(i - existent_rooms.size()).getId());
            }
            GridPane.setMargin(l, in);
            grid.add(l, i + 1, 0);
        }

        //Create the persons
        for (int i = 0; i < login.getNumberOfUsers(); i++){
            Label l = new Label(login.getUserByListIndex(i).getName());
            GridPane.setMargin(l, in);
            grid.add(l, 0, i + 1);
        }


        //Create the RadioButtons for the persons
        ArrayList<ToggleGroup> toggles = new ArrayList<>();
        for (int i = 0; i < login.getNumberOfUsers(); i++){
            toggles.add(new ToggleGroup());

            for (int j = 0; j < existent_rooms.size() + hall_groups.size(); j++){
                RadioButton rb = new RadioButton();
                rb.setToggleGroup(toggles.get(i));
                if(j < existent_rooms.size()){
                    rb.setUserData(existent_rooms.get(j).getId());
                    if(login.userList.get(i).getCurrent_room() != null && login.userList.get(i).getCurrent_room().getId() == existent_rooms.get(j).getId()){
                        rb.setSelected(true);
                    }
                } else {
                    rb.setUserData(hall_groups.get(j - existent_rooms.size()).getId());
                }
                rb.setToggleGroup(toggles.get(i));

                GridPane.setMargin(rb, in);
                GridPane.setHalignment(rb, HPos.CENTER);
                grid.add(rb, j + 1, i + 1);
            }
        }

        //Create the webcam activation buttons
        int last_column = existent_rooms.size() + hall_groups.size() + 1;
        for(int i = 0; i < login.getNumberOfUsers(); i++){
            Button wb = createWebcamButton(login.getUserByListIndex(i));
            GridPane.setMargin(wb, in);
            GridPane.setHalignment(wb, HPos.CENTER);
            grid.add(wb, last_column, i + 1);
        }

        //Listener für die RadioButtons
        for(ToggleGroup t : toggles){
            t.selectedToggleProperty().addListener((ObservableValue<? extends Toggle> observableValue, Toggle toggle, Toggle t1) -> {
                if(t.getSelectedToggle() != null){

                    int currentroom =  Integer.parseInt(t.getSelectedToggle().getUserData().toString());

                    // Leaving rooms later implemented as for loop over all rooms?
                    // Would mean a room parent class
                    // Leaving hall implemented right now
                    // leaving other rooms is not
                    User user_in_question = login.getUserByListIndex(toggles.indexOf(t));

                    for (Room room : existent_rooms) {
                        if (room.getId() == currentroom && user_in_question.getCurrent_room() != null) {
                            user_in_question.getCurrent_room().leave_Room(user_in_question, db, user_in_question.getCurrent_room().getId());
                            try {
                                room.addUser(user_in_question);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    for (HallGroup g : hall_groups){
                        if(g.getId() == currentroom){
                            if(!user_in_question.getCurrent_room().getType().equals("hall")){
                                System.out.println("Please enter the hall first! (Fehlerfall nur relevant im Mockup tbh)");
                            } else {
                                g.joinMeetingAs(user_in_question, db, g.getId());
                            }
                        }
                    }
                }
            });
        }
        return grid;
    }*/

    public GridPane createMainGripPane2(boolean update_thread){
        try {
            this.existent_rooms = db.getAllRooms(rc.Building);
            this.hall_groups = db.getAllHallGroups(rc.Building);
        } catch (SQLException e){
            e.printStackTrace();
        }

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setHgap(10);

        // Only used to display all users. Later only the online users have to be displayed. Then it can be removed
        Label startl = new Label("Offline Nutzer");
        ObservableList<String> o = FXCollections.observableArrayList();

        try {
            o = db.getAllUserWithoutRoomObservable(); // The list of the users
        } catch (SQLException e){
            e.printStackTrace();
        }

        Label start_capacity = new Label("Kapazität: " + o.size());
        all_capacity_labels.add(start_capacity);

        ListView<String> startlv = new ListView<>(o);
        startlv.setPrefSize(200, 200);
        startlv.setOnDragDetected(mouseEvent -> dragDetected(mouseEvent, startlv, 0));
        startlv.setOnDragOver(dragEvent -> dragOver(dragEvent, startlv, 0));
        startlv.setOnDragDropped(dragEvent -> dragDropped(dragEvent, startlv, 0, false));
        startlv.setOnDragDone(dragEvent -> dragDone(dragEvent,startlv, 0));

        all_listviews_room.add(startlv);
        grid.addColumn(0, startl, start_capacity, startlv);

        //Create the rooms
        for (int i = 0; i < existent_rooms.size() + hall_groups.size(); i++){
            Label l;
            ObservableList<String> user_list = FXCollections.observableArrayList();

            if(i < existent_rooms.size()){
                int roomID = existent_rooms.get(i).getId();
                switch (existent_rooms.get(i).getType()) {
                    case "conference":
                        l = new Label("Besprechungsraum " + roomID);
                        break;
                    case "hall":
                        l = new Label("Halle " + roomID);
                        break;
                    case "office":
                        l = new Label("Büro " + roomID);
                        break;
                    case "hall_group":
                        l = new Label("HallenGruppe " + roomID);
                        break;
                    default:
                        l = new Label(existent_rooms.get(i).getType() + "_" + roomID);
                }
                try {
                    user_list = db.getAllUserInRoomObservable(roomID);
                } catch (SQLException e){
                    e.printStackTrace();
                }

                Label capacityl = new Label("Kapazität: " + user_list.size() + "/" + existent_rooms.get(i).getCapacity());
                all_capacity_labels.add(capacityl);

                ListView<String> lv = new ListView<>(user_list);
                lv.setPrefSize(200, 200);
                lv.setOnDragDetected(mouseEvent -> dragDetected(mouseEvent, lv, roomID));
                lv.setOnDragOver(dragEvent -> dragOver(dragEvent, lv, roomID));
                lv.setOnDragDropped(dragEvent -> dragDropped(dragEvent, lv, roomID, false));
                lv.setOnDragDone(dragEvent -> dragDone(dragEvent, lv, roomID));
                grid.addColumn(i + 1, l, capacityl, lv);



                lv.setOnMouseClicked(click -> {
                    try { //ToDo Vielleicht schöner fixen?
                        String[] s = lv.getSelectionModel().getSelectedItem().split("[\\(\\)]");
                        String username = s[0];
                        String userid = s[1];
                        if (click.getClickCount() == 2 && login.currentUser.getCurrent_room().isOffice()) {
                            try {
                                ArrayList<String> a = db.getAllUserInRoom(login.currentUser.getCurrent_room().getId());
                                if (db.getAllUserInRoom(login.currentUser.getCurrent_room().getId()).contains(userid)) {
                                    System.out.println(lv.getSelectionModel().getSelectedItem());
                                    System.out.println("-----------------------------------------------------");
                                    db.sendRequest(login.currentUser.getId(), userid, "webcam");
                                    ErrorMessage e = new ErrorMessage();
                                    e.createError("Die Anfrage zur Aktivierung der Webcam wurde erfolgreich an " + username + " verschickt.");
                                }
                            } catch (SQLException ex) {
                                ex.printStackTrace();
                            }
                        }
                    } catch (NullPointerException ex){
                        System.out.println("You clicked on an empty object");
                    }
                });

                all_listviews_room.add(lv);

            } else {
                int groupID = hall_groups.get(i - existent_rooms.size()).getId();
                l = new Label();
                try {
                    l.setText("HallenGruppe " + groupID + " (Halle: " + db.getCorrespondingHalltoHallGroup(groupID) + ")");

                    user_list = db.getAllUserInHallGroupObservable(groupID);
                } catch (SQLException e){
                    e.printStackTrace();
                }

                Label capacityl = new Label("Kapazität: " + user_list.size());
                all_capacity_labels.add(capacityl);

                ListView<String> lv = new ListView<>(user_list);
                lv.setPrefSize(200, 200);
                lv.setOnDragDetected(mouseEvent -> dragDetected(mouseEvent, lv, groupID));
                lv.setOnDragOver(dragEvent -> dragOver(dragEvent, lv, groupID));
                lv.setOnDragDropped(dragEvent -> dragDropped(dragEvent, lv, groupID, true));
                lv.setOnDragDone(dragEvent -> dragDone(dragEvent, lv, groupID));
                grid.addColumn(i + 1, l, capacityl, lv);

                all_listviews_group.add(lv);
            }
        }
        if(update_thread) {
            login.getThreadUpdate().setList_room(all_listviews_room);
            login.getThreadUpdate().setList_group(all_listviews_group);
        }

        return grid;
    }

    private void dragDetected(MouseEvent event, ListView<String> listView, int roomID){
        // At least one item has to be selected
        if (listView.getSelectionModel().getSelectedIndices().size() == 0){
            event.consume();
            return;
        }

        //Init drag and drop
        Dragboard drag = listView.startDragAndDrop(TransferMode.COPY_OR_MOVE);

        //put selected item to the dragboard
        ArrayList<String> selectedItem = new ArrayList<>(listView.getSelectionModel().getSelectedItems());
        ClipboardContent content = new ClipboardContent();
        content.put(USER_LIST, selectedItem);
        drag.setContent(content);
        event.consume();
    }

    private void dragOver(DragEvent event, ListView<String> listView, int roomID){
        Dragboard drag = event.getDragboard();

        if(event.getGestureSource() != listView && drag.hasContent(USER_LIST)){
            event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
        }
        event.consume();
    }

    private void dragDropped(DragEvent event, ListView<String> listView, int roomID, boolean is_hallgroup){
        boolean dragCompleted = false;

        Dragboard drag = event.getDragboard();

        String userID = new String();
        String username = new String();
        if (drag.hasContent(USER_LIST)) {
            ArrayList<String> list = (ArrayList<String>) drag.getContent(USER_LIST);
            String[] s = list.get(0).split("[\\(\\)]");
            username = s[0];
            userID = s[1];
        }

        if(userID.equals(login.currentUser.getId())) {
            if(!is_hallgroup) {
                for (Room room : existent_rooms) {
                    if (room.getId() == roomID) {
                        System.out.println(login.currentUser.getId());
                        try {
                            if (db.getRoomWithRoomID(roomID, db).getCapacity() == db.getAllUserInRoom(roomID).size()) {
                                ErrorMessage err = new ErrorMessage();
                                err.createError("Dieser Raum ist bereits voll.");
                            } else {
                                //System.out.println(user_in_question.getCurrent_room().getId());
                                if (login.currentUser.getCurrent_room() != null) {
                                    System.out.println(login.currentUser.getCurrent_room().getId());
                                    login.currentUser.getCurrent_room().leave_Room(login.currentUser, db, login.currentUser.getCurrent_room().getId());
                                }
                                try {
                                    System.out.println(room.getId());
                                    room.addUser(login.currentUser);
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (SQLException sq) {
                            System.out.println(sq);
                        }
                    }
                }
            } else {
                try {
                    for (HallGroup g : hall_groups) {
                        if (g.getId() == roomID) {
                            if (login.currentUser.getCurrent_room().getId() != db.getCorrespondingHalltoHallGroup(g.getId())){
                                ErrorMessage e = new ErrorMessage();
                                e.createError("Please enter the hall first! (Fehlerfall nur relevant im Mockup tbh)");
                                return;
                            } else {
                                /*if (login.currentUser.getCurrent_room() != null) {
                                    login.currentUser.getCurrent_room().leave_Room(login.currentUser, db, login.currentUser.getCurrent_room().getId());
                                }*/
                                g.addUser(login.currentUser);
                            }
                        }
                    }
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }
            if(drag.hasContent(USER_LIST)){
                ArrayList<String> list = (ArrayList<String>)drag.getContent(USER_LIST);
                listView.getItems().addAll(list);
                dragCompleted = true;
            }
        // Invite another user in the same room
        } else if (login.getCurrentUser().getCurrent_room().getId() == roomID) {
            try {
                if (db.getAllUserInRoom(roomID).size() < db.getRoomWithRoomID(roomID,db).getCapacity()) {
                    db.sendRequest(login.currentUser.getId(), userID, "join");
                    ErrorMessage e = new ErrorMessage();
                    e.createError("Einladung verschickt.");
                } else {
                    ErrorMessage e = new ErrorMessage();
                    e.createError("Die Einladung wurde nicht an " + username + " verschickt, weil der Raum bereits voll ist.");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        event.setDropCompleted(dragCompleted);
        event.consume();
    }

    private void dragDone(DragEvent event, ListView<String> listView, int roomID){
        TransferMode tm = event.getTransferMode();
        if (tm == TransferMode.COPY) {
            removeSelectedUser(listView);
        }
        event.consume();
    }

    private void removeSelectedUser(ListView<String> listView){
        List<String> selectedList = new ArrayList<>();

        for (String s : listView.getSelectionModel().getSelectedItems()){
            selectedList.add(s);
        }

        listView.getSelectionModel().clearSelection();
        listView.getItems().removeAll(selectedList);
    }

}
