package GUI.Controllers;
import de.uni_hannover.wb_interaktionen_1.i_face.InteraktionControl;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import GUI.MainMenu;
import definitions.*;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

import static java.lang.Math.abs;
import static java.lang.Math.min;

public class EditorController {
    private EditorMode editorMode = EditorMode.NONE;
    private Building building = new Building();
    private ResizeableCanvas canvas = null;
    private double tempX;
    private double tempY;
    private Room tempRoom;
    private Table tempTable;
    private double offsetX;
    private double offsetY;

    private Chair ghostChair;
    private Chair tempChair;
    private double chairOffsetX;
    private double chairOffsetY;
    private Room tempChairRoom;

    private @FXML Slider GridSlider;
    private @FXML MenuButton roomTypes;
    private @FXML StackPane myPane;
    private @FXML ToggleButton newRoom;
    private @FXML ToggleButton grid;
    private SelectionController selectionController;

    private InteraktionControl IC;

    public void initialize() {
        if (myPane != null) {
            canvas = new ResizeableCanvas(601, 602);
            myPane.getChildren().add(canvas);
            StackPane.setAlignment(canvas, Pos.TOP_LEFT);
            canvas.widthProperty().bind(myPane.widthProperty());
            canvas.heightProperty().bind(myPane.heightProperty());
            canvas.setOnMouseClicked(this::onMouseClicked);
            canvas.setOnMouseDragged(this::onMouseDragged);
            canvas.setOnMouseExited(this::onMouseExited);
            canvas.setOnMouseMoved(this::onMouseMoved);
            canvas.setOnMousePressed(this::onCanvasPressed);
            canvas.setOnMouseReleased(this::onCanvasReleased);
        }
        initSelectionController();
    }

    //Wechselt vom Editor ins Hauptmenü
    public void onCancelClicked() {
        if (building.getRooms().isEmpty()){
            building = new Building();
            building.redrawBuilding(canvas);
            MainMenu.primaryStage.setScene(MainMenu.mainMenu);
            this.resetBuilding();
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(Text.TITLE);
        alert.setHeaderText("");
        alert.setContentText(Text.EDITOR_CONTROLLER_LEAVE_DIALOG);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK){
            building = new Building();
            building.redrawBuilding(canvas);
            MainMenu.primaryStage.setScene(MainMenu.mainMenu);
            this.resetBuilding();
        }
    }

    //Speichert das aktuelle Gebäude ab
    public void onSaveClicked() throws SQLException {
        String buildingState = building.isLegal();
        if (!buildingState.equals(Text.BUILDING_LEGAL)) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle(Text.TITLE);
            alert.setHeaderText("");
            if(buildingState.equals(Text.BUILDING_HAS_ROOMTYPELESS_ROOM)){
                alert.setContentText(String.format(Text.EDITOR_CONTROLLER_ROOMTYPE_ERROR_DIALOG, buildingState));
            }else{
            alert.setContentText(String.format(Text.EDITOR_CONTROLLER_SAVE_WARNING, buildingState));
            }
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK && !buildingState.equals(Text.BUILDING_HAS_ROOMTYPELESS_ROOM)){
                building.setGridState(false);
                DatabaseCommunication.saveDialog(building);
            } else if(buildingState.equals(Text.BUILDING_HAS_ROOMTYPELESS_ROOM) && result.get() == ButtonType.OK) {
                for(Room room : building.getRooms()){
                    if(room.getType() == RoomType.NONE){
                        room.setType(RoomType.OFFICE);
                    }
                }
                building.redrawBuilding(canvas);
            } else {
                return;
            }
        }

        building.setGridState(false);
        DatabaseCommunication.saveDialog(building);
        //TODO Hier in der Interaktion DB die Räume hinterlegen
        //Mit einer Schleife alle Räume des Gebäudes (Gebäudename, Raumname, Typ) in der I1_DB hinterlegen)
        IC.deleteBuilding(building.getName());
        for (Room room : building.getRooms()){
            if(room.getType() == RoomType.HALL) {
                room.setInteraktionsRoomID(IC.addRoom(160,"hall",building.getName()));
            }else if(room.getType() == RoomType.MEETING_ROOM){
                room.setInteraktionsRoomID(IC.addRoom(room.getChairs().size(),"conference",building.getName()));
            }else{
                room.setInteraktionsRoomID(IC.addRoom(room.getChairs().size(),"office",building.getName()));
            }
        }
        DatabaseCommunication.replace(building,building.getName());
    }

    //Lädt ein zuvor gespeichertes Gebäude
    public void onLoadClicked() throws SQLException {
        Building tempBuilding = DatabaseCommunication.loadDialog(IC);
        if (tempBuilding != null) building = tempBuilding;
        building.redrawBuilding(canvas);
    }

    //Wechselt den aktiven Modus des Editors
    private void changeMode(EditorMode editorMode) {
        resetSelection(editorMode);

        if (this.editorMode == editorMode) {
            this.editorMode = EditorMode.NONE;
        } else {
            this.editorMode = editorMode;
        }
    }

    public void onNewRoomClicked() {
        changeMode(EditorMode.ROOM);
    }

    public void selectOffice() {
        if (selectionController.roomsSelected())
            changeMode(EditorMode.SELECTION_TO_OFFICE);
        else
            changeMode(EditorMode.OFFICE);
        roomTypes.setText(Text.OFFICE);
    }

    public void selectMeetingRoom() {
        if (selectionController.roomsSelected())
            changeMode(EditorMode.SELECTION_TO_MEETING_ROOM);
        else
            changeMode(EditorMode.MEETING_ROOM);
        roomTypes.setText(Text.MEETING_ROOM);
    }

    public void selectHalle() {
        if (selectionController.roomsSelected())
            changeMode(EditorMode.SELECTION_TO_HALL);
        else
            changeMode(EditorMode.HALL);
        roomTypes.setText(Text.HALL);
    }

    public void onChairClicked() {
        changeMode(EditorMode.CHAIR);
    }

    public void onTableClicked() {
        changeMode(EditorMode.TABLE);
    }

    public void onDeleteClicked() {
        if (selectionController.selectionExists())
            changeMode(EditorMode.DELETE_SELECTION);
        else
            changeMode(EditorMode.DELETE);
    }

    public void onMoveClicked() {
        if (selectionController.selectionExists())
            changeMode(EditorMode.MOVE_SELECTION);
        else
            changeMode(EditorMode.MOVE);
    }

    public void onDocumentClicked() {
        changeMode(EditorMode.DOCUMENT);
    }

    public void onRoomNameClicked() {
        changeMode(EditorMode.ROOM_NAME);
    }

    public void onSelectClicked() {
        changeMode(EditorMode.SELECT);
    }

    public void onMouseClicked(MouseEvent mouseEvent) {
        switch (editorMode) {
            case CHAIR -> {
                Room room = building.getRoomAtCoordinates(mouseEvent.getX(), mouseEvent.getY());
                if (room != null) {
                    if (room.getType() != RoomType.HALL) {
                        Chair chair = new Chair(mouseEvent.getX() - room.getCoordinateX(), mouseEvent.getY() - room.getCoordinateY(), building.getGridSize(), building.getGridSize());
                        chair.toGridCoordinates(building.getGridSize());
                        if (room.isLegalChair(chair)) {
                            room.getChairs().add(chair);
                            building.redrawBuilding(canvas);
                        }
                    } else {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle(Text.TITLE);
                        alert.setHeaderText("");
                        alert.setContentText(Text.EDITOR_CONTROLLER_CHAIRS_IN_HALL);
                        alert.showAndWait();

                    }
                }
            }
            case DOCUMENT -> {
                Room room = building.getRoomAtCoordinates(mouseEvent.getX(), mouseEvent.getY());
                if (room != null) {
                    Table table = room.getTableAtCoordinates(mouseEvent.getX(), mouseEvent.getY());
                    if (table != null){
                        if (table.getDoc() == null) {
                            TextInputDialog textInputDialog = new TextInputDialog(Text.EDITOR_CONTROLLER_LINK_1);
                            textInputDialog.setTitle(Text.TITLE);
                            textInputDialog.setHeaderText(Text.EDITOR_CONTROLLER_LINK_DIALOG);
                            textInputDialog.setContentText(Text.EDITOR_CONTROLLER_LINK_2);
                            Optional<String> result = textInputDialog.showAndWait();
                            if (result.isPresent()){
                                Document doc = new Document(result.get());
                                table.setDoc(doc);
                                building.redrawBuilding(canvas);
                            }
                        } else {
                            //tisch hat ein dokoment an dieser stelle könnte man es bearbeiten
                            TextInputDialog textInputDialog = new TextInputDialog(table.getDoc().getLink());
                            textInputDialog.setTitle(Text.TITLE);
                            textInputDialog.setHeaderText(Text.EDITOR_CONTROLLER_LINK_NEW);
                            textInputDialog.setContentText(Text.EDITOR_CONTROLLER_LINK_2);
                            Optional<String> result = textInputDialog.showAndWait();
                            if (result.isPresent()){
                                table.getDoc().setLink((result.get()));
                                building.redrawBuilding(canvas);
                            }
                        }
                    }
                }
            }
        }
        checkForGridSlider();
    }

    public void onMouseMoved(MouseEvent mouseEvent) {
        if (editorMode == EditorMode.CHAIR) {
            if (ghostChair == null)
                ghostChair = new Chair(mouseEvent.getX(), mouseEvent.getY(), building.getGridSize(), building.getGridSize());
            else {
                ghostChair.setCoordinateX(mouseEvent.getX());
                ghostChair.setCoordinateY(mouseEvent.getY());
                ghostChair.toGridCoordinates(building.getGridSize());
                building.redrawBuilding(canvas);
                ghostChair.draw(canvas);
            }
        }
    }
    public void onMouseExited(MouseEvent mouseEvent) {
        if (editorMode == EditorMode.CHAIR) {
            building.redrawBuilding(canvas);
        }
    }

    //Ruft die passende Methode bei Ziehen der Maus aus
    public void onMouseDragged(MouseEvent mouseEvent) {
        if (mouseEvent.getX() >= myPane.getWidth() - building.getGridSize()) {
            myPane.setPrefWidth(myPane.getWidth() + building.getGridSize());
            //canvas.extendWidth(building.getGridSize());
            building.redrawBuilding(canvas);
        }
        if (mouseEvent.getY() >= myPane.getHeight() - building.getGridSize()) {
            myPane.setPrefHeight(myPane.getHeight() + building.getGridSize());
            //canvas.extendHeight(building.getGridSize());
            building.redrawBuilding(canvas);
        }
        switch (editorMode) {
            case MOVE -> {
                if(tempChair != null && tempChairRoom != null) {
                    moveChair(mouseEvent);
                    break;
                }
                if (tempTable != null) {
                    moveTable(mouseEvent);
                    break;
                }
                if (tempRoom != null) {
                    moveRoom(mouseEvent);
                }
            }
            case TABLE -> previewTable(mouseEvent);
            case ROOM -> previewRoom(mouseEvent);
        }

    }

    //Speichert bei einem Mausklick die Koordinaten und Objekte, auf die geklickt wurde
    //Erstellt in einigen Modi ggf. auch Objekte
    public void onCanvasPressed(MouseEvent mouseEvent) {
        tempX = mouseEvent.getX();
        tempY = mouseEvent.getY();
        for (Room room : building.getRooms()) {
            //Es wurde auf einen Raum geklickt
            if (tempX > room.getCoordinateX() && tempX < (room.getCoordinateX() + room.getWidth()) && tempY > room.getCoordinateY() && tempY < (room.getCoordinateY() + room.getHeight())) {
                tempRoom = room;
                offsetX = tempX - room.getCoordinateX();
                offsetY = tempY - room.getCoordinateY();
                for (Table table : room.getTables()) {
                    //Es wurde auf einen Tisch geklickt
                    if (table.getCoordinateX() <= offsetX && table.getCoordinateX() + table.getWidth() >= offsetX && table.getCoordinateY() <= offsetY && table.getCoordinateY() + table.getHeight() >= offsetY) {
                        tempTable = table;
                        offsetX = tempX - table.getCoordinateX();
                        offsetY = tempY - table.getCoordinateY();
                    }
                }
            }
        }

        //Es wurde im Move-Modus auf ein Stuhl im Raum gelickt
        if (editorMode == EditorMode.MOVE) {
            Room room = building.getRoomAtCoordinates(mouseEvent.getX(), mouseEvent.getY());
            if (room != null) {
                Chair chair = room.getChairAtRoomCoordinates(mouseEvent.getX()- room.getCoordinateX(), mouseEvent.getY()- room.getCoordinateY());
                if (chair != null) {
                    tempChair = chair;
                    tempChairRoom = room;
                    chairOffsetX = mouseEvent.getX() - chair.getCoordinateX();
                    chairOffsetY = mouseEvent.getY() - chair.getCoordinateY();
                }
            }
        }

        //Erstellt einen Raum
        if (editorMode == EditorMode.ROOM) {
            tempRoom = new Room(tempX, tempY, 0.0, 0.0, building.getGridSize());
            building.getRooms().add(tempRoom);
        }
        //Erstellt einen Tisch
        if (editorMode == EditorMode.TABLE && tempRoom != null) {
            tempTable = new Table(offsetX, offsetY, 0.0, 0.0);
            tempRoom.getTables().add(tempTable);
        }
    }

    //Führt verschiedene Aktionen bei Loslassen der Maus je nach Modus aus
    public void onCanvasReleased(MouseEvent mouseEvent) {
        switch (editorMode) {
            case ROOM:
                //Speichert einen erstellten Raum ab
                if (tempRoom == null) break;
                if (Math.abs(tempRoom.getWidth()) * Math.abs(tempRoom.getHeight()) < (10 * building.getGridSize() * building.getGridSize())){
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle(Text.TITLE);
                    alert.setHeaderText("");
                    alert.setContentText(Text.EDITOR_CONTROLLER_ROOM_SIZE_ERROR);
                    alert.showAndWait();
                    building.getRooms().remove(tempRoom);
                    building.redrawBuilding(canvas);
                }
                else {
                    tempRoom.setCoordinateX(min(tempRoom.getCoordinateX(), tempRoom.getCoordinateX() + tempRoom.getWidth()));
                    tempRoom.setCoordinateY(min(tempRoom.getCoordinateY(), tempRoom.getCoordinateY() + tempRoom.getHeight()));
                    tempRoom.setWidth(abs(tempRoom.getWidth()));
                    tempRoom.setHeight(abs(tempRoom.getHeight()));
                }
                break;
            case MOVE:
                tempChairRoom = null;
                tempChair = null;
                break;
            case CHAIR:
                //To Do
                break;
            case TABLE:
                //Speichert einen erstellten Tisch ab
                if (tempTable == null) break;
                if (tempTable.getWidth() == 0.0 || tempTable.getHeight() == 0.0)
                    tempRoom.getTables().remove(tempTable);
                else {
                    tempTable.setCoordinateX(min(tempTable.getCoordinateX(), tempTable.getCoordinateX() + tempTable.getWidth()));
                    tempTable.setCoordinateY(min(tempTable.getCoordinateY(), tempTable.getCoordinateY() + tempTable.getHeight()));
                    tempTable.setWidth(abs(tempTable.getWidth()));
                    tempTable.setHeight(abs(tempTable.getHeight()));
                }
                break;
            case DELETE:
                //Löscht ein angeklicktes Objekt

                //loescht einen Stuhl
                Room room = building.getRoomAtCoordinates(mouseEvent.getX(), mouseEvent.getY());
                if (room != null) {
                    Chair chair = room.getChairAtRoomCoordinates(mouseEvent.getX()- room.getCoordinateX(), mouseEvent.getY()- room.getCoordinateY());
                    if (chair != null) {
                        room.getChairs().remove(chair);
                        building.redrawBuilding(canvas);
                        break;
                    }
                }

                //löscht einen Tisch
                if (tempTable != null) {
                    if (tempTable.getDoc() != null) {
                        if (tempTable.hasDocAtTableCoordinates(mouseEvent.getX()- tempRoom.getCoordinateX() -tempTable.getCoordinateX(), mouseEvent.getY()-tempRoom.getCoordinateY() -tempTable.getCoordinateY() )) {
                            tempTable.setDoc(null);
                            building.redrawBuilding(canvas);
                            break;
                        }
                    }
                    tempRoom.getTables().remove(tempTable);
                    building.redrawBuilding(canvas);
                    break;
                }
                //Löscht einen Raum
                if (tempRoom != null) {
                    if (!tempRoom.getTables().isEmpty() || !tempRoom.getChairs().isEmpty()) {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle(Text.TITLE);
                        alert.setHeaderText("");
                        alert.setContentText(Text.EDITOR_CONTROLLER_DELETE_DIALOG);
                        Optional<ButtonType> result = alert.showAndWait();
                        if (result.isPresent() && result.get() == ButtonType.OK) {
                            building.getRooms().remove(tempRoom);
                            building.redrawBuilding(canvas);
                        } else {
                            break;
                        }
                    }
                    building.getRooms().remove(tempRoom);
                    building.redrawBuilding(canvas);
                    break;
                }
            case OFFICE:
                //Verändert den Raumtyp eines Raums zu Büro
                if (tempRoom != null) {
                    tempRoom.setType(RoomType.OFFICE);
                    building.redrawBuilding(canvas);
                }
                break;
            case MEETING_ROOM:
                //Verändert den Raumtyp eines Raums zu Besprechungsraum
                if (tempRoom != null) {
                    tempRoom.setType(RoomType.MEETING_ROOM);
                    building.redrawBuilding(canvas);
                }
                break;
            case HALL:
                //Verändert den Raumtyp eines Raums zu Halle
                if (tempRoom != null) {
                    if (!tempRoom.getChairs().isEmpty()){
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle(Text.TITLE);
                        alert.setHeaderText("");
                        alert.setContentText(Text.EDITOR_CONTROLLER_CHAIRS_IN_HALL_DIALOG);
                        Optional<ButtonType> result = alert.showAndWait();
                        if (result.isPresent() && result.get() == ButtonType.OK) {
                            tempRoom.setChairs(new ArrayList<>());
                            tempRoom.setType(RoomType.HALL);
                            building.redrawBuilding(canvas);
                        } else {
                            break;
                        }
                    }
                    tempRoom.setType(RoomType.HALL);
                    building.redrawBuilding(canvas);
                }
                break;
            case ROOM_NAME:
                if (tempRoom != null) {
                    TextInputDialog textInputDialog = new TextInputDialog("");
                    textInputDialog.setTitle(Text.TITLE);
                    textInputDialog.setHeaderText(Text.EDITOR_CONTROLLER_NAME_DIALOG_1);
                    textInputDialog.setContentText(Text.EDITOR_CONTROLLER_NAME);
                    Optional<String> result = textInputDialog.showAndWait();
                    String name = result.orElse("");
                    if (!building.roomNameExists(name)) {
                        tempRoom.setName(name);
                        tempRoom.setNameState(true);
                        building.redrawBuilding(canvas);
                    } else {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle(Text.TITLE);
                        alert.setHeaderText("");
                        alert.setContentText(Text.EDITOR_CONTROLLER_NAME_DIALOG_2);
                        alert.showAndWait();
                    }
                }
                break;
            default:
                break;
        }
        tempRoom = null;
        tempTable = null;
        checkForGridSlider();
    }

    //Bewegt einen Raum
    private void moveRoom(MouseEvent mouseEvent) {
        if (tempRoom == null) return;
        double oldX = tempRoom.getCoordinateX();
        tempRoom.setCoordinateX(mouseEvent.getX() - offsetX);
        if (!building.isLegalRoom(tempRoom, canvas)) {
            tempRoom.setCoordinateX(oldX);
        }
        double oldY = tempRoom.getCoordinateY();
        tempRoom.setCoordinateY(mouseEvent.getY() - offsetY);
        if (!building.isLegalRoom(tempRoom, canvas)) {
            tempRoom.setCoordinateY(oldY);
        }
        //Koordinaten von Stuehlen anpassen, so dass sie sich mitbewegen
        building.redrawBuilding(canvas);
    }

    //Bewegt einen Tisch
    private void moveTable(MouseEvent mouseEvent) {
        double oldX = tempTable.getCoordinateX();
        tempTable.setCoordinateX(mouseEvent.getX() - offsetX);
        if (!tempRoom.isLegalTable(tempTable)) {
            tempTable.setCoordinateX(oldX);
        }
        double oldY = tempTable.getCoordinateY();
        tempTable.setCoordinateY(mouseEvent.getY() - offsetY);
        if (!tempRoom.isLegalTable(tempTable)) {
            tempTable.setCoordinateY(oldY);
        }
        building.redrawBuilding(canvas);
    }

    //Stellt einen Raum dar, während er erstellt wird
    public void previewRoom(MouseEvent mouseEvent) {
        if (tempRoom == null) return;
        double oldWidth = tempRoom.getWidth();
        tempRoom.setWidth(mouseEvent.getX() - tempX);
        if (!building.isLegalRoom(tempRoom, canvas)) {
            tempRoom.setWidth(oldWidth);
        }
        double oldHeight = tempRoom.getHeight();
        tempRoom.setHeight(mouseEvent.getY() - tempY);
        if (!building.isLegalRoom(tempRoom, canvas)) {
            tempRoom.setHeight(oldHeight);
        }
        building.redrawBuilding(canvas);
    }

    //Stellt einen Tisch dar, während er erstellt wird
    public void previewTable(MouseEvent mouseEvent) {
        if (tempTable == null) return;
        double oldWidth = tempTable.getWidth();
        tempTable.setWidth(mouseEvent.getX() - tempX);
        if (!tempRoom.isLegalTable(tempTable)) {
            tempTable.setWidth(oldWidth);
        }
        double oldHeight = tempTable.getHeight();
        tempTable.setHeight(mouseEvent.getY() - tempY);
        if (!tempRoom.isLegalTable(tempTable)) {
            tempTable.setHeight(oldHeight);
        }
        building.redrawBuilding(canvas);
    }

    //Bewegt ein Stuhl in Raum
    public void moveChair(MouseEvent mouseEvent) {
        double oldX = tempChair.getCoordinateX();
        double oldY = tempChair.getCoordinateY();
        tempChair.setCoordinateX(mouseEvent.getX() - chairOffsetX);
        tempChair.setCoordinateY(mouseEvent.getY() - chairOffsetY);
        if (!tempChairRoom.isLegalChair(tempChair)) {
            tempChair.setCoordinateX(oldX);
            tempChair.setCoordinateY(oldY);
        }
        building.redrawBuilding(canvas);
    }
    //Zeigt bzw. versteckt das Grid für den nutzer
    public void onGridClicked() {
        building.setGridState(!building.getGridState());
        building.redrawBuilding(canvas);
    }

    public void onSliderDragged() {
        if (!GridSlider.isDisabled()) {
            building.setGridSize(GridSlider.getValue());
            building.redrawBuilding(canvas);
        }
    }

    private void checkForGridSlider() {
        GridSlider.setDisable(!building.getRooms().isEmpty());
    }

    public EditorMode getEditorMode() {
        return editorMode;
    }

    public void initSelectionController() {
        if (selectionController != null) {
            selectionController.clearSelection();
            selectionController.setBuilding(building);
            building.redrawBuilding(canvas);
            return;
        }
        selectionController = new SelectionController(building, canvas, this);
        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, selectionController);
        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, selectionController);
        canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, selectionController);
        canvas.addEventHandler(MouseEvent.MOUSE_CLICKED, selectionController);
    }

    public void setEditorMode(EditorMode mode) {
        changeMode(mode);
    }

    public void resetBuilding() {
        initSelectionController();
        newRoom.setSelected(true);
        newRoom.setSelected(false);
        this.editorMode = EditorMode.NONE;
        grid.setSelected(false);
        building.setGridState(false);
        GridSlider.setValue(30);
        checkForGridSlider();
    }

    private void resetSelection(EditorMode mode) {
        if (mode == EditorMode.MOVE_SELECTION ||
                mode == EditorMode.DELETE_SELECTION ||
                mode == EditorMode.SELECTION_TO_OFFICE ||
                mode == EditorMode.SELECTION_TO_MEETING_ROOM ||
                mode == EditorMode.SELECTION_TO_HALL)
            return;
        selectionController.clearSelection();
    }

    public void setIC(InteraktionControl IC) {
        this.IC = IC;
    }
}