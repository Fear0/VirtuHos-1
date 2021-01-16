package GUI.Controllers;

import definitions.*;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.ArrayList;

public class SelectionController implements EventHandler<MouseEvent> {
    private enum Mode {ROOMS, OBJECTS}
    private static final Paint okColor = Color.MEDIUMSEAGREEN;
    private static final Paint badColor = Color.TOMATO;

    private Building building;
    private final Canvas canvas;
    private final EditorController editorController;
    private Mode mode;

    private Point moveStart;

    private Point start;
    private Room currentRoom;
    private SelectionArea selectionArea;

    private ArrayList<GhostObject> ghostObjects = new ArrayList<>();

    public SelectionController(Building building, Canvas canvas, EditorController editorController) {
        this.building = building;
        this.canvas = canvas;
        this.editorController = editorController;
    }

    public boolean isActive() {
        return editorController.getEditorMode() == EditorMode.SELECT ||
                editorController.getEditorMode() == EditorMode.DELETE_SELECTION ||
                editorController.getEditorMode() == EditorMode.MOVE_SELECTION ||
                editorController.getEditorMode() == EditorMode.SELECTION_TO_OFFICE ||
                editorController.getEditorMode() == EditorMode.SELECTION_TO_MEETING_ROOM ||
                editorController.getEditorMode() == EditorMode.SELECTION_TO_HALL;
    }

    public boolean selectionExists() {
        return ghostObjects.size() > 0;
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        if (!isActive())
            return;

        if (mouseEvent.getEventType() == MouseEvent.MOUSE_DRAGGED)
            mouseDragged(mouseEvent);
        if (mouseEvent.getEventType() == MouseEvent.MOUSE_PRESSED)
            mousePressed(mouseEvent);
        if (mouseEvent.getEventType() == MouseEvent.MOUSE_RELEASED)
            mouseReleased();
        if (mouseEvent.getEventType() == MouseEvent.MOUSE_CLICKED)
            mouseClicked(mouseEvent);
    }

    public void mouseClicked(MouseEvent mouseEvent) {
        if (editorController.getEditorMode() != EditorMode.SELECT &&
                editorController.getEditorMode() != EditorMode.MOVE_SELECTION &&
                ghostClicked(mouseEvent) == null) {
            editorController.setEditorMode(switch (editorController.getEditorMode()) {
                case DELETE_SELECTION -> EditorMode.DELETE;
                case SELECTION_TO_OFFICE -> EditorMode.OFFICE;
                case SELECTION_TO_MEETING_ROOM -> EditorMode.MEETING_ROOM;
                case SELECTION_TO_HALL -> EditorMode.HALL;
                default -> editorController.getEditorMode();
            });
            clearSelection();
        }

        switch (editorController.getEditorMode()) {
            case DELETE_SELECTION -> {
                deleteSelectedObjects();
                editorController.setEditorMode(EditorMode.DELETE);
            }
            case SELECTION_TO_OFFICE -> assignRoomType(ghostObjects, RoomType.OFFICE);
            case SELECTION_TO_MEETING_ROOM -> assignRoomType(ghostObjects, RoomType.MEETING_ROOM);
            case SELECTION_TO_HALL -> assignRoomType(ghostObjects, RoomType.HALL);
        }
    }

    private void assignRoomType(ArrayList<GhostObject> ghostObjects, RoomType roomType) {
        if (mode != Mode.ROOMS)
            return;

        if (roomType == RoomType.HALL && haveChairs(ghostObjects)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(Text.TITLE);
            alert.setHeaderText(null);
            alert.setContentText(Text.SELECTION_CONTROLLER_CHAIRS_IN_HALL);
            alert.showAndWait();
            return;
        }

        for (GhostObject ghostObject : ghostObjects)
            ((Room) ghostObject.getOriginal()).setType(roomType);

        redraw(canvas, building);
    }

    private boolean haveChairs(ArrayList<GhostObject> ghostObjects) {
        for (GhostObject ghostObject : ghostObjects) {
            if (!((Room)ghostObject.getOriginal()).getChairs().isEmpty())
                return true;
        }
        return false;
    }

    private GhostObject ghostClicked(MouseEvent mouseEvent) {
        double x = mouseEvent.getX();
        double y = mouseEvent.getY();
        for (GhostObject ghostObject : ghostObjects) {
            if (coordinatesInside(ghostObject, x, y))
                return ghostObject;
        }
        return null;
    }

    public void mousePressed(MouseEvent mouseEvent) {
        if (editorController.getEditorMode() == EditorMode.MOVE_SELECTION && ghostClicked(mouseEvent) == null) {
            editorController.setEditorMode(EditorMode.MOVE);
            clearSelection();
            return;
        }

        if (editorController.getEditorMode() == EditorMode.SELECT) {
            clearSelection();
            Room room = building.getRoomAtCoordinates(mouseEvent.getX(), mouseEvent.getY());
            if (room == null)
                mode = Mode.ROOMS;
            else {
                if (room.getChairs().size() == 0 && room.getTables().size() == 0)
                    mode = Mode.ROOMS;
                else {
                    currentRoom = room;
                    mode = Mode.OBJECTS;
                }
            }
            start = new Point(mouseEvent.getX(), mouseEvent.getY());
            selectionArea = new SelectionArea(mouseEvent.getX(), mouseEvent.getY(), 0, 0);
        }

        if (editorController.getEditorMode() == EditorMode.MOVE_SELECTION) {
            moveStart = new Point(mouseEvent.getX(), mouseEvent.getY());
        }
    }

    public void mouseDragged(MouseEvent mouseEvent) {
        if (editorController.getEditorMode() == EditorMode.SELECT) {
            if (selectionArea != null && start != null) {
                selectionArea.setWidth(Math.abs(mouseEvent.getX() - start.x));
                selectionArea.setHeight(Math.abs(mouseEvent.getY() - start.y));
                selectionArea.setCoordinateX(Math.min(mouseEvent.getX(), start.x));
                selectionArea.setCoordinateY(Math.min(mouseEvent.getY(), start.y));
                if (mode == Mode.ROOMS)
                    ghostObjects = getRoomOverlaps(selectionArea, building.getRooms());
                if (mode == Mode.OBJECTS) {
                    ArrayList<EditorObject> objects = new ArrayList<>();
                    objects.addAll(currentRoom.getTables());
                    objects.addAll(currentRoom.getChairs());
                    ghostObjects = getObjectOverlaps(selectionArea, currentRoom, objects);
                }
                redraw(canvas, building);
            }
        }

        if (editorController.getEditorMode() == EditorMode.MOVE_SELECTION) {
            Point moveEnd = new Point(mouseEvent.getX(), mouseEvent.getY());
            for (GhostObject ghostObject : ghostObjects) {
                ghostObject.setCoordinateX(ghostObject.getOriginalPosition().x+(moveEnd.x - moveStart.x));
                ghostObject.setCoordinateY(ghostObject.getOriginalPosition().y+(moveEnd.y - moveStart.y));
                ghostObject.toGridCoordinates(building.getGridSize());
                if (!ghostObjectsLegal(ghostObjects, canvas, building, mode))
                    setGhostObjectColors(SelectionController.badColor);
                else
                    setGhostObjectColors(SelectionController.okColor);
                redraw(canvas, building);
            }
        }
    }

    public void mouseReleased() {
        if (editorController.getEditorMode() == EditorMode.SELECT) {
            selectionArea = null;
            start = null;
            currentRoom = null;
            redraw(canvas, building);
        }
        if (editorController.getEditorMode() == EditorMode.MOVE_SELECTION) {
            if (ghostObjectsLegal(ghostObjects, canvas, building, mode)) {
                for (GhostObject ghost : ghostObjects)
                    ghost.setOriginalPosition(ghost.getPosition());
            } else {
                for (GhostObject ghost : ghostObjects)
                    ghost.setPosition(ghost.getOriginalPosition());
            }
            setGhostObjectColors(SelectionController.okColor);
            redraw(canvas, building);
            moveStart = null;
        }
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

    private void setGhostObjectColors(Paint color) {
        for (GhostObject ghostObject : ghostObjects)
            ghostObject.setColor(color);
    }

    private boolean ghostObjectsLegal(ArrayList<GhostObject> ghostObjects, Canvas canvas, Building building, Mode mode) {
        boolean valid = true;

        for (GhostObject ghostObject : ghostObjects)
            ghostObject.getOriginal().setSelected(true);

        if (mode == Mode.ROOMS) {
            ArrayList<Room> rooms = new ArrayList<>();
            for (Room room : building.getRooms()) {
                if (!room.getSelected())
                    rooms.add(room);
            }
            GhostObject screen = new GhostObject(0, 0, canvas.getWidth(), canvas.getHeight());
            for (GhostObject ghostObject : ghostObjects) {
                if (!isInside(ghostObject, screen)) {
                    valid = false;
                    break;
                }
                ArrayList<GhostObject> overlaps = getRoomOverlaps(ghostObject, rooms);
                if (overlaps.size() > 0) {
                    valid = false;
                    break;
                }
            }
        }
        if (mode == Mode.OBJECTS) {
            Room room = ghostObjects.get(0).getRoom();
            ArrayList<EditorObject> objects = new ArrayList<>();
            for (Table table : room.getTables()) {
                if (!table.getSelected())
                    objects.add(table);
            }
            for (Chair chair : room.getChairs()) {
                if (!chair.getSelected())
                    objects.add(chair);
            }
            for (GhostObject ghost : ghostObjects) {
                if (!isInside(ghost, room)) {
                    valid = false;
                    break;
                }
                ArrayList<GhostObject> overlaps = getObjectOverlaps(ghost, room, objects);
                if (overlaps.size() > 0) {
                    valid = false;
                    break;
                }
            }
        }

        for (GhostObject ghost : ghostObjects)
            ghost.getOriginal().setSelected(false);
        return valid;
    }

    private void redraw(Canvas canvas, Building building) {
        building.redrawBuilding(canvas);
        for (GhostObject ghostObject : ghostObjects) {
            ghostObject.draw(canvas);
        }
        if (selectionArea != null)
            selectionArea.draw(canvas);
    }

    private void deleteSelectedObjects() {
        for (GhostObject ghostObject : ghostObjects)
            ghostObject.deleteOrig(building);
        clearSelection();
        redraw(canvas, building);
    }

    private ArrayList<GhostObject> getRoomOverlaps(EditorObject editorObject, ArrayList<Room> rooms) {
        ArrayList<GhostObject> ghosts = new ArrayList<>();
        for (Room room : rooms) {
            if (overlaps(room, editorObject))
                ghosts.add(new GhostObject(room));
        }
        return ghosts;
    }
    private ArrayList<GhostObject> getObjectOverlaps(EditorObject editorObject, Room room, ArrayList<EditorObject> editorObjects) {
        ArrayList<GhostObject> ghostObjects = new ArrayList<>();
        for (EditorObject editorObject1 : editorObjects) {
            GhostObject ghostObject;
            if (editorObject1 instanceof Table)
                ghostObject = new GhostObject((Table) editorObject1, room);
            else
                ghostObject = new GhostObject((Chair) editorObject1, room);
            if (overlaps(ghostObject, editorObject))
                ghostObjects.add(ghostObject);
        }
        return ghostObjects;
    }

    private static class SelectionArea extends EditorObject {
        public SelectionArea(double x, double y, double w, double h) {
            super(x, y, w, h);
        }

        @Override
        public void draw(Canvas canvas) {
            GraphicsContext graphicsContext2D = canvas.getGraphicsContext2D();
            graphicsContext2D.setFill(Color.TRANSPARENT);
            graphicsContext2D.setStroke(Color.BLACK);
            graphicsContext2D.strokeRect(getCoordinateX(), getCoordinateY(), getWidth(), getHeight());
            graphicsContext2D.fillRect(getCoordinateX(), getCoordinateY(), getWidth(), getHeight());
        }
    }

    public void clearSelection() {
        ghostObjects = new ArrayList<>();
        redraw(canvas, building);
    }

    private static class GhostObject extends EditorObject {
        private EditorObject original;
        private Room room;
        private Paint color = SelectionController.okColor;

        public GhostObject(double x, double y, double w, double h) {
            super(x, y, w, h);
        }
        public GhostObject(EditorObject original) {
            super(original.getCoordinateX(), original.getCoordinateY(),
                    original.getWidth(), original.getHeight());
            this.original = original;
        }
        public GhostObject(Table table, Room room) {
            super(table.getCoordinateX() + room.getCoordinateX(),
                    table.getCoordinateY() + room.getCoordinateY(),
                    table.getWidth(), table.getHeight());
            this.original = table;
            this.room = room;
        }
        public GhostObject(Chair chair, Room room) {
            super(chair.getCoordinateX() + room.getCoordinateX(),
                    chair.getCoordinateY() + room.getCoordinateY(),
                    chair.getWidth(), chair.getHeight());
            this.original = chair;
            this.room = room;
        }

        public void setColor(Paint color) {
            this.color = color;
        }

        public EditorObject getOriginal() {
            return original;
        }

        public Room getRoom() {
            return room;
        }

        public Point getPosition() {
            return new Point(getCoordinateX(), getCoordinateY());
        }

        public void setPosition(Point point) {
            setCoordinateX(point.x);
            setCoordinateY(point.y);
        }

        public Point getOriginalPosition() {
            if (original instanceof Room)
                return new Point(original.getCoordinateX(), original.getCoordinateY());
            if (original instanceof Table || original instanceof Chair)
                return new Point(original.getCoordinateX() + room.getCoordinateX(),
                                    original.getCoordinateY() + room.getCoordinateY());
            return null;
        }

        public void setOriginalPosition(Point point) {
            if (original instanceof Room) {
                original.setCoordinateX(point.x);
                original.setCoordinateY(point.y);
            }
            if (original instanceof Table || original instanceof Chair) {
                original.setCoordinateX(point.x - room.getCoordinateX());
                original.setCoordinateY(point.y - room.getCoordinateY());
            }
        }

        public void deleteOrig(Building building) {
            if (original != null && original instanceof Room) {
                building.getRooms().remove(original);
            }
            if (original != null && original instanceof Table && room != null) {
                room.getTables().remove(original);
            }
            if (original != null && original instanceof Chair && room != null) {
                room.getChairs().remove(original);
            }
        }

        @Override
        public void draw(Canvas canvas) {
            GraphicsContext graphicsContext2D = canvas.getGraphicsContext2D();
            graphicsContext2D.setFill(Color.TRANSPARENT);
            Paint oldColor = graphicsContext2D.getStroke();
            double oldLineWidth = graphicsContext2D.getLineWidth();
            graphicsContext2D.setStroke(this.color);
            graphicsContext2D.setLineWidth(4);
            graphicsContext2D.strokeRect(getCoordinateX(), getCoordinateY(), getWidth(), getHeight());
            graphicsContext2D.fillRect(getCoordinateX(), getCoordinateY(), getWidth(), getHeight());
            graphicsContext2D.setStroke(oldColor);
            graphicsContext2D.setLineWidth(oldLineWidth);
        }

        public void toGridCoordinates(double gridSize) {
            double oldX = this.getCoordinateX();
            double oldY = this.getCoordinateY();
            double diffX = Math.abs(oldX) % gridSize;
            double diffY = Math.abs(oldY) % gridSize;
            if (diffX < gridSize / 2) {
                if (oldX < 0)
                    this.setCoordinateX(oldX + diffX);
                else
                    this.setCoordinateX(oldX - diffX);
            } else if (diffX >= gridSize / 2) {
                if (oldX < 0)
                    this.setCoordinateX(oldX - (gridSize - diffX));
                else
                    this.setCoordinateX(oldX + (gridSize - diffX));
            }
            if (diffY < gridSize / 2) {
                if (oldY < 0)
                    this.setCoordinateY(oldY + diffY);
                else
                    this.setCoordinateY(oldY - diffY);
            } else if (diffY >= gridSize / 2) {
                if (oldY < 0)
                    this.setCoordinateY(oldY - (gridSize - diffY));
                else
                    this.setCoordinateY(oldY + (gridSize - diffY));
            }
            double oldHeight = this.getHeight();
            double oldWidth = this.getWidth();
            double diffHeight = oldHeight % gridSize;
            double diffWidth = oldWidth % gridSize;
            if (diffHeight < gridSize / 2) {
                this.setHeight(oldHeight - diffHeight);
            } else if (diffHeight >= gridSize / 2) {
                this.setHeight(oldHeight + (gridSize - diffHeight));
            }
            if (diffWidth < gridSize / 2) {
                this.setWidth(oldWidth - diffWidth);
            } else if (diffWidth >= gridSize / 2) {
                this.setWidth(oldWidth + (gridSize - diffWidth));
            }
        }
    }

    //Ueberpruefft ob Koordinaten in diesem Objekt drin liegen
    public boolean coordinatesInside(EditorObject editorObject, double x, double y) {
        return x > editorObject.getCoordinateX() && x < editorObject.getCoordinateX() + editorObject.getWidth() &&
                y > editorObject.getCoordinateY() && y < editorObject.getCoordinateY() + editorObject.getHeight();
    }

    //Ueberpruefft ob dieses Objekt in ueberbegenen Objekt drin liegt
    public boolean isInside(EditorObject editorObject1, EditorObject editorObject2) {
        return !(editorObject1.getCoordinateX() < editorObject2.getCoordinateX()) &&
                !(editorObject1.getCoordinateX() + editorObject1.getWidth() >
                        editorObject2.getCoordinateX() + editorObject2.getWidth()) &&
                !(editorObject1.getCoordinateY() < editorObject2.getCoordinateY()) &&
                !(editorObject1.getCoordinateY() + editorObject1.getHeight() >
                        editorObject2.getCoordinateY() + editorObject2.getHeight());
    }

    public boolean overlaps(EditorObject editorObject1, EditorObject editorObject2) {
        if (editorObject1 == editorObject2)
            return false;

        if (editorObject1.isInside(editorObject2) || editorObject2.isInside(editorObject1))
            return true;

        Point o1_p1 = new Point(editorObject1.getCoordinateX(), editorObject1.getCoordinateY());
        Point o1_p2 = new Point(editorObject1.getCoordinateX() + editorObject1.getWidth(),
                editorObject1.getCoordinateY());
        Point o1_p3 = new Point(editorObject1.getCoordinateX(),
                editorObject1.getCoordinateY() + editorObject1.getHeight());
        Point o1_p4 = new Point(editorObject1.getCoordinateX() + editorObject1.getWidth(),
                editorObject1.getCoordinateY() + editorObject1.getHeight());

        Point o2_p1 = new Point(editorObject2.getCoordinateX(), editorObject2.getCoordinateY());
        Point o2_p2 = new Point(editorObject2.getCoordinateX() + editorObject2.getWidth(),
                editorObject2.getCoordinateY());
        Point o2_p3 = new Point(editorObject2.getCoordinateX(),
                editorObject2.getCoordinateY() + editorObject2.getHeight());
        Point o2_p4 = new Point(editorObject2.getCoordinateX() + editorObject2.getWidth(),
                editorObject2.getCoordinateY() + editorObject2.getHeight());

        if(linesTouch(o1_p3, o1_p4, o2_p1, o2_p1) || linesTouch(o1_p1, o1_p2, o2_p3, o2_p4) ||
            linesTouch(o1_p2, o1_p4, o2_p1, o2_p3) || linesTouch(o1_p1, o1_p3, o2_p2, o2_p4))
            return false;
        
        if(linesIntersect(o1_p1, o1_p2, o2_p1, o2_p2) || linesIntersect(o1_p1, o1_p2, o2_p1, o2_p3) ||
                linesIntersect(o1_p1, o1_p2, o2_p2, o2_p4) || linesIntersect(o1_p1, o1_p2, o2_p3, o2_p4))
            return true;

        if(linesIntersect(o1_p1, o1_p3, o2_p1, o2_p2) || linesIntersect(o1_p1, o1_p3, o2_p1, o2_p3) ||
                linesIntersect(o1_p1, o1_p3, o2_p2, o2_p4) || linesIntersect(o1_p1, o1_p3, o2_p3, o2_p4))
            return true;

        if(linesIntersect(o1_p2, o1_p4, o2_p1, o2_p2) || linesIntersect(o1_p2, o1_p4, o2_p1, o2_p3) ||
                linesIntersect(o1_p2, o1_p4, o2_p2, o2_p4) || linesIntersect(o1_p2, o1_p4, o2_p3, o2_p4))
            return true;

        return linesIntersect(o1_p3, o1_p4, o2_p1, o2_p2) || linesIntersect(o1_p3, o1_p4, o2_p1, o2_p3) ||
                linesIntersect(o1_p3, o1_p4, o2_p2, o2_p4) || linesIntersect(o1_p3, o1_p4, o2_p3, o2_p4);
    }

    private boolean linesTouch(Point p1, Point p2, Point p3, Point p4) {
        if (p1.y == p2.y && p3.y == p4.y && p1.y == p3.y)
            return true;
        return p1.x == p2.x && p3.x == p4.x && p1.x == p3.x;
    }
    
    //Ueberpruefft ob zwei Linien sich schneiden
    private boolean linesIntersect(Point p1, Point p2, Point p3, Point p4) {
        if (p1.x >= p3.x && p1.x <= p4.x && p1.y <= p3.y && p2.y >= p3.y)
            return true;
        return p3.x >= p1.x && p3.x <= p2.x && p3.y <= p1.y && p4.y >= p1.y;
    }

    private static class Point {
        public double x;
        public double y;

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    public boolean roomsSelected() {
        return selectionExists() && mode == Mode.ROOMS;
    }
    public boolean chairLegal(Chair chair, Room room) {
        GhostObject ghost = new GhostObject(chair, room);
        this.ghostObjects.add(ghost);
        boolean legal = ghostObjectsLegal(ghostObjects, canvas, building, Mode.OBJECTS);
        this.ghostObjects.clear();
        return legal;
    }
}
