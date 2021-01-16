package definitions;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;

import static java.lang.Math.abs;
import static java.lang.Math.min;

public class Building implements java.io.Serializable {
    private final ArrayList<Room> rooms;
    private String name;
    private double gridSize = 30;
    public boolean gridState = false;

    public Building() {
        this.rooms = new ArrayList<>();
    }

    public ArrayList<Room> getRooms() {
        return rooms;
    }

    public double getGridSize() {
        return gridSize;
    }
    public void setGridSize(double gridSize) {
        this.gridSize = gridSize;
    }

    public boolean getGridState() {
        return gridState;
    }
    public void setGridState(boolean gridState) {
        this.gridState = gridState;
    }

    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }


    //Überprüft, ob ein Raum in einer legalen Position ist
    public boolean isLegalRoom(Room r, Canvas canvas) {
        double x1 = r.getCoordinateX();
        double x2 = x1 + r.getWidth();
        double y1 = r.getCoordinateY();
        double y2 = y1 + r.getHeight();
        double w = canvas.getWidth();
        double h = canvas.getHeight();
        if (x1 < 0 || x2 < 0 || y1 < 0 || y2 < 0 || x1 >= w || x2 >= w || y1 >= h || y2 >= h) return false;
        double x = min(x1, x2);
        double y = min(y1, y2);
        w = abs(r.getWidth());
        h = abs(r.getHeight());
        for (Room room : this.rooms) {
            if (room.getCoordinateX() >= x + w)
                continue;
            if (x >= room.getCoordinateX() + room.getWidth())
                continue;
            if (room.getCoordinateY() >= y + h)
                continue;
            if (y >= room.getCoordinateY() + room.getHeight())
                continue;
            if (r == room)
                continue;
            return false;
        }
        return true;
    }

    //Zeichnet das Gebäude neu
    public void redrawBuilding(Canvas canvas) {
        if (canvas == null) return;
        this.fitGrid();
        this.setRoomNames();
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        if (getGridState()) {
            drawGrid(canvas);
        }
        gc.setLineWidth(1);
        for (Room room : rooms) {
            room.draw(canvas);
            }
        }
    public void redrawPersons(Canvas canvas) {
        if (canvas == null) return;
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            for (Person person : DatabaseCommunication.getPersons(name)) {
            person.draw(canvas, gridSize);
        }
    }

    public void redrawLocks(Canvas canvas) {
        if (canvas == null) return;
        List<String> lockedRooms = DatabaseCommunication.getLockedRooms(this.name);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        for (Room room : rooms) {
            room.draw(canvas);
            if (room.getType() != RoomType.HALL) {
                try {
                    BufferedImage lock = ImageIO.read(new File("openlock.png"));
                    for (String roomName : lockedRooms) {
                        if (room.getName().equals(roomName)) {
                            lock = ImageIO.read(new File("closedlock.png"));
                        }
                    }
                    Image image = SwingFXUtils.toFXImage(lock,null);
                    double size = gridSize / 2;
                    gc.clearRect(room.getCoordinateX(), room.getCoordinateY(), size, size);
                    gc.drawImage(image, room.getCoordinateX(), room.getCoordinateY(), size, size);
                } catch (IOException e) {
                    e.printStackTrace();
            }
        }
    }
    }

    public void redrawEverything(Canvas canvas, Canvas personCanvas, Canvas lockCanvas) {
        redrawBuilding(canvas);
        redrawPersons(personCanvas);
        redrawLocks(lockCanvas);
    }

    //returns a Room if coordinates inside the Room liegen
    public Room getRoomAtCoordinates(double x, double y) {
        for (Room room : rooms) {
            if (room.coordinatesInside(x, y))
                return room;
        }
        return null;
    }
    //draws the Grid of the building
    public void drawGrid(Canvas c) {
        GraphicsContext gc = c.getGraphicsContext2D();
        for (int i = 0; i < c.getWidth(); i += this.getGridSize()) {
            gc.beginPath();
            gc.moveTo(i, 0);
            gc.lineTo(i, c.getHeight());
            gc.closePath();
            gc.setLineWidth(0.2);
            gc.stroke();
        }
        for (int i = 0; i < c.getHeight(); i += this.getGridSize()) {
            gc.beginPath();
            gc.moveTo(0, i);
            gc.lineTo(c.getWidth(), i);
            gc.closePath();
            gc.setLineWidth(0.2);
            gc.stroke();
        }
    }

    public void fitGrid() {
        for (Room room: rooms) {
            room.toGridCoordinates(this.gridSize);
        }
    }

    public void setRoomNames() {
        int officeCounter = 1;
        int meetingCounter = 1;
        int hallCounter = 1;
        int unknownCounter = 1;
        for (Room room : rooms) {
            if (!room.getNameState()) {
                switch (room.getType()) {
                    case NONE:
                        room.setName(Text.ROOM + " " + unknownCounter);
                        unknownCounter++;
                        break;
                    case HALL:
                        room.setName(Text.HALL + " " + hallCounter);
                        hallCounter++;
                        break;
                    case OFFICE:
                        room.setName(Text.OFFICE + " " + officeCounter);
                        officeCounter++;
                        break;
                    case MEETING_ROOM:
                        room.setName(Text.MEETING_ROOM + " " + meetingCounter);
                        meetingCounter++;
                        break;
                    default:
                        break;
                }
            }
        }
    }

    /**
     * checks if a building is correct (meaning all rooms touch each other and there is exactly one hall)
     * @return a string which is "legal" if the building is correct or a description of what is wrong otherwise
     * if there is more than one problem the first one found is returned
     */
    public String isLegal(){
        //are there zero or more than 1 halls
        int hallCount = 0;
        for (Room room : this.rooms) {
            if(room.getType() == RoomType.NONE){
                return Text.BUILDING_HAS_ROOM_WITHOUT_TYPE;
            }
            if (room.getType() == RoomType.HALL) {
                hallCount++;
            }
        }
        if (hallCount == 0) {
            return Text.BUILDING_NO_HALL;
        } else if (hallCount > 1) {
            return Text.BUILDING_MULTIPLE_HALLS;
        }

        //does all rooms touch each other (uses BFS)
        ArrayList<Room> explored = new ArrayList<>();
        Queue<Room> q = new LinkedList<>();
        q.add(this.rooms.get(0));
        while (!q.isEmpty()) {
            Room current = q.remove();
            explored.add(current);
            //add all rooms to q that are within grid size distance and not already in q
            for (Room room : this.rooms) {
                if(room == current) {
                    continue;
                }
                if (current.touchesRoomOnGrid(room, this.gridSize)) {
                    if (!q.contains(room) && !explored.contains(room)) {
                        q.add(room);
                    }
                }
            }
        }

        //check if the rooms you can reach from room0 are equal to all rooms
        for (Room room : this.rooms) {
            if (!explored.contains(room)) {
                return Text.BUILDING_ROOMS_DO_NOT_TOUCH;
            }
        }
        return Text.BUILDING_LEGAL;
    }

    public boolean roomNameExists(String s) {
        for (Room room : rooms) {
            if (room.getName().equals(s)) {
                return true;
            }
        }
        return false;
    }

    public double highestXValue() {
        double output = 0;
        double rightEdge;
        for (Room room : rooms) {
            rightEdge = room.getCoordinateX() + room.getWidth();
            if (output < rightEdge) {
                output = rightEdge;
            }
        }
        return output;
    }

    public double highestYValue() {
        double output = 0;
        double lowestEdge;
        for (Room room : rooms) {
            lowestEdge = room.getCoordinateY() + room.getHeight();
            if (output < lowestEdge) {
                output = lowestEdge;
            }
        }
        return output;
    }
    public void clearScreen(Canvas can){
        if(can == null) return;
        GraphicsContext gc = can.getGraphicsContext2D();
        gc.clearRect(0,0,can.getWidth(),can.getHeight());
    }
}
