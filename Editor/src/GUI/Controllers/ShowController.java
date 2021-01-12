package GUI.Controllers;

import GUI.MainMenu;
import de.uni_hannover.wb_interaktionen_1.i_face.InteraktionControl;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import definitions.*;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

import java.awt.*;
import java.net.URI;
import java.sql.SQLException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;


public class ShowController {

    private @FXML TextField userField;
    private Canvas showCanvas;
    private @FXML StackPane showStackPane;
    private Building building;
    private String username;
    private String buildingName;
    private Person transferPerson;
    private BuildingThread thread;
    private InteraktionControl IC;
    private String ID;


    public void setUsername(String n) {
        this.username = n;
    }

    //Wechselt aus dem Anzeigemodus ins Hauptmenü
    public void onBackClicked() {
        removeUser();
        building = null;
        new Building().redraw(showCanvas, true);
        //stop the thread for building updates
        if(this.thread != null) this.thread.end();
        MainMenu.primaryStage.setScene(MainMenu.mainMenu);
    }

    public void removeUser() {
        DatabaseCommunication.deletePerson(username);
    }

    //Lädt ein Gebäude und zeigt es an
    public void onLoadClicked() throws SQLException {
        //get the name of the building in the database and save it in buildingname
        building = DatabaseCommunication.loadDialog(IC);
        if (building != null){
            removeUser();
            new Building().redraw(showCanvas, true);
            buildingName = building.getName();
            showStackPane.setPrefWidth(building.highestXValue() + building.getGridSize());
            showStackPane.setPrefHeight(building.highestYValue() + building.getGridSize());
            showCanvas = new Canvas(building.highestXValue() + building.getGridSize(), building.highestYValue() + building.getGridSize() );
            showStackPane.getChildren().add(showCanvas);
            StackPane.setAlignment(showCanvas, Pos.TOP_LEFT);
            showCanvas.setOnMouseClicked(this::onCanvasClicked);
            building.redraw(showCanvas, true);

            //giving thread new building info if we switch buildings
            if(this.thread == null) {
                //start thread for updating the building
                this.thread = new BuildingThread(this);
                this.thread.setBuildingname(buildingName);
                Thread t1 = new Thread(thread, "building updates");
                t1.setDaemon(true);
                t1.start();
            } else{
                this.thread.setBuildingname(buildingName);
            }

        }
    }

    public void onGridClicked(){
        if (building != null) {
            building.setGridState(!building.getGridState());
            building.redraw(showCanvas, true);
        }
    }

    public void onCanvasClicked(MouseEvent mouseEvent) {
        try {
            building = DatabaseCommunication.loadDialog(buildingName,IC);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (building != null){
            building.redraw(showCanvas, true);
        }

        if (building != null) {
            Room room = building.getRoomAtCoordinates(mouseEvent.getX(), mouseEvent.getY());
            if (room == null) return;

            //check if it was clicked on a lock and change the locks state
            if ((room.getType() != RoomType.HALL) &&
                    room.clickedOnLock(mouseEvent.getX() - room.getCoordinateX(),
                            mouseEvent.getY() - room.getCoordinateY())) {
                room.setLocked(!room.getLocked());
                building.redraw(showCanvas, true);
                building.setGridState(false);
                try {
                    DatabaseCommunication.replace(building, buildingName);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return;
            }

            //check if the user clicked on a document and open it in default browser
            Table table = room.getTableAtCoordinates(mouseEvent.getX(), mouseEvent.getY());
            if (table != null){
                if (table.getDoc() != null) {
                    if (table.hasDocAtTableCoordinates(mouseEvent.getX()- room.getCoordinateX() -table.getCoordinateX(), mouseEvent.getY()-room.getCoordinateY() -table.getCoordinateY() )) {
                        try {
                            Desktop desktop = java.awt.Desktop.getDesktop();
                            URI oURL = new URI(table.getDoc().getLink());
                            desktop.browse(oURL);
                        } catch (Exception e) {
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setTitle(Text.TITLE);
                            alert.setHeaderText("");
                            alert.setContentText(Text.SHOW_CONTROLLER_LINK_ERROR);
                            alert.showAndWait();
                        }
                        return;
                    }
                }
            }

            //check if the room you want to enter is currently locked
            if (room.getLocked()) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle(Text.TITLE);
                alert.setHeaderText("");
                alert.setContentText(Text.SHOW_CONTROLLER_ROOM_LOCKED);
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() != ButtonType.OK) {
                    return;
                }
            }

            //move person
            if (room.getType() == RoomType.HALL) {
                Person tempPerson = new Person(this.username, mouseEvent.getX() - (0.5 * building.getGridSize()),
                        mouseEvent.getY() - (0.5 * building.getGridSize()));
                DatabaseCommunication.updatePerson(tempPerson, buildingName);
                //TODO hier hallen gruppenerstellung
                InteraktionMovePerson(room, false);
                building.redraw(showCanvas, true);
            } else {
                Chair chair = room.getChairAtRoomCoordinates(mouseEvent.getX() - room.getCoordinateX(),
                        mouseEvent.getY() - room.getCoordinateY());
                if (chair != null) {
                    if (chair.isFree(room.getCoordinateX(), room.getCoordinateY(), buildingName)) {
                        Person tempPerson = new Person(this.username, chair.getCoordinateX() + room.getCoordinateX(),
                                chair.getCoordinateY() + room.getCoordinateY());
                        DatabaseCommunication.updatePerson(tempPerson, buildingName);
                        InteraktionMovePerson(room, false);
                        building.redraw(showCanvas, true);
                    } else {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle(Text.TITLE);
                        alert.setHeaderText(null);
                        alert.setContentText(Text.SHOW_CONTROLLER_CHAIR_NOT_FREE);
                        alert.showAndWait();
                    }
                } else {
                    Chair tempChair = room.freeChair(buildingName);
                    if (tempChair != null) {
                        Person tempPerson = new Person(this.username,
                                tempChair.getCoordinateX() + room.getCoordinateX(),
                                tempChair.getCoordinateY() + room.getCoordinateY());
                        DatabaseCommunication.updatePerson(tempPerson, buildingName);
                        InteraktionMovePerson(room, false);
                        building.redraw(showCanvas, true);
                    } else {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle(Text.TITLE);
                        alert.setHeaderText(null);
                        alert.setContentText(Text.SHOW_CONTROLLER_NO_FREE_CHAIR);
                        alert.showAndWait();
                    }
                }
            }
        }
    }

    public void setBuilding(Building building){
        this.building = building;
        this.building.redraw(showCanvas, true);
    }

    //Event für die Analyse Gruppe
    public void AnalyseClicked() {
        //call function of analyse
    }

    public void onCanvasMoved(MouseEvent mouseEvent) {
        //show lock symbol when hovering over a room
        //maybe in polish
    }

    /*public void DragEnter(DragEvent dragEvent) {
        Room room = building.getRoomAtCoordinates(dragEvent.getX(), dragEvent.getY());
        if(room != null){
            if(room.getType() == RoomType.HALL){
                transferPerson.setX(dragEvent.getX());
                transferPerson.setY(dragEvent.getY());
                room.setPersonsForHall(transferPerson);
            }else{
                Chair chair = room.getChairAtRoomCoordinates(dragEvent.getX(), dragEvent.getY());
                if(chair != null){
                    if(transferPerson != null){
                        chair.setPerson(transferPerson);
                    }
                }
            }
        }
    }

    public void DragExit(DragEvent dragEvent) {
        Room room = building.getRoomAtCoordinates(dragEvent.getX(), dragEvent.getY());
        if(room != null){
            if(room.getType() == RoomType.HALL){
                transferPerson = room.getPersonAtHallCoordinates(dragEvent.getX(), dragEvent.getY());
            }else{
               Chair chair = room.getChairAtRoomCoordinates(dragEvent.getX(), dragEvent.getY());
               if(chair != null){
                   transferPerson = chair.getPerson();
               }
            }
        }
    }*/

    public void setIC(InteraktionControl IC) {
        this.IC = IC;
    }

    public InteraktionControl getIC(){
        return this.IC;
    }

    private void InteraktionMovePerson(Room room, boolean halle){
        //System.out.println(room.getInteraktionsRoomID());
        IC.moveUser(this.ID,room.getInteraktionsRoomID(),halle);
    }

    public void setID(String ID){
        this.ID = ID;
    }
}

class BuildingThread implements Runnable{
    private volatile boolean exit = false;
    private volatile String buildingName;
    private ShowController cont;
    private Building newBuilding;

    public BuildingThread(ShowController a){
        this.cont = a;
    }

    @Override
    public void run() {
        InteraktionControl IC = cont.getIC();
        while(!exit){
            //replace this print with logic to update a building
            //System.out.println("in the thread for: " + buildingName);
            try {
                newBuilding = DatabaseCommunication.loadDialog(buildingName, IC);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            cont.setBuilding(newBuilding);

            IC.checkRequest();

            //wait 5 sec
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                System.out.println("thread interrupted");
            }

        }
        //System.out.println("thread ended for: " + buildingName);
    }

    public void end(){
        exit = true;
    }

    public void setBuildingname(String buildingname) {
        this.buildingName = buildingname;
    }
    //Testkommentar
}
