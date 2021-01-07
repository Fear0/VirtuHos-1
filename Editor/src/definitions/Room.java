package definitions;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import javax.imageio.ImageIO;

import static java.lang.Math.min;
import static java.lang.Math.abs;

public class Room extends EditorObject {
    private String name;
    private RoomType roomtype = RoomType.NONE;
    private ArrayList<Table> tables;
    private ArrayList<Chair> chairs;
    private double gridSize = 30;
    private boolean nameState = false;
    private boolean isLocked = false;

    public Room(double x, double y, double w, double h) {
        super(x, y, w, h);
        tables = new ArrayList<>();
        chairs = new ArrayList<>();
    }

    public Room(String name, double x, double y, double w, double h) {
        super(x, y, w, h);
        this.name = name;
        tables = new ArrayList<>();
        chairs = new ArrayList<>();
    }

    public Room(String name, double x, double y, double w, double h, double gridSize) {
        super(x, y, w, h);
        this.name = name;
        tables = new ArrayList<>();
        chairs = new ArrayList<>();
        this.gridSize = gridSize;
    }

    public ArrayList<Table> getTables() {
        return tables;
    }
    public void setTables(ArrayList<Table> t) {
        tables = t;
    }

    public ArrayList<Chair> getChairs() {
        return chairs;
    }
    public void setChairs(ArrayList<Chair> c) {
        chairs = c;
    }

    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public void setLocked(boolean b) {
        this.isLocked = b;
    }
    public boolean getLocked() {
        return this.isLocked;
    }


    //Zeichnet den Raum
    @Override
    public void draw(Canvas c) {
        GraphicsContext gc = c.getGraphicsContext2D();

        double x1 = getCoordinateX();
        double x2 = x1 + getWidth();
        double y1 = getCoordinateY();
        double y2 = y1 + getHeight();
        double x = Double.min(x1, x2);
        double y = Double.min(y1, y2);
        double w = abs(getWidth());
        double h = abs(getHeight());
        gc.setFill(Color.rgb(0, 0, 0, 1));
        if (y == y1)
            gc.fillText(this.name, x,y + this.getHeight() -3);
        if (y == y2)
            gc.fillText(this.name, x,y - this.getHeight() -3);
        switch (roomtype) {
            case OFFICE -> gc.setFill(Color.rgb(238, 232, 170, 0.7));
            case MEETING_ROOM -> gc.setFill(Color.rgb(188, 143, 143, 0.7));
            case HALL -> gc.setFill(Color.rgb(148, 0, 211, 0.7));
            default -> gc.setFill(Color.rgb(200,200,200,0.7));
        }
        gc.strokeRect(x, y, w, h);
        gc.fillRect(x, y, w, h);

        if (tables != null) {
            for (Table table : tables) {
                table.draw(c, getCoordinateX(), getCoordinateY());
            }
        }

        if (chairs != null) {
            for (Chair chair : chairs) {
                chair.draw(c,this.getCoordinateX(), this.getCoordinateY());
            }
        }
    }

    //draws lock symbols for the room indicating if the room is open or closed
    public void drawWithLock(Canvas c) {
        GraphicsContext gc = c.getGraphicsContext2D();

        double x1 = getCoordinateX();
        double x2 = x1 + getWidth();
        double y1 = getCoordinateY();
        double y2 = y1 + getHeight();
        double x = Double.min(x1, x2);
        double y = Double.min(y1, y2);
        double w = abs(getWidth());
        double h = abs(getHeight());
        gc.setFill(Color.rgb(0, 0, 0, 1));
        if (y == y1)gc.fillText(this.name, x,y + this.getHeight() -3);
        if (y == y2)gc.fillText(this.name, x,y - this.getHeight() -3);

        switch (roomtype) {
            case OFFICE -> gc.setFill(Color.rgb(238, 232, 170, 0.7));
            case MEETING_ROOM -> gc.setFill(Color.rgb(188, 143, 143, 0.7));
            case HALL -> gc.setFill(Color.rgb(148, 0, 211, 0.7));
            default -> gc.setFill(Color.rgb(200,200,200,0.7));
        }
        gc.strokeRect(x, y, w, h);
        gc.fillRect(x, y, w, h);

        //draw lock symbols here
        if (this.getType() != RoomType.HALL) {
            try {
                BufferedImage lock = ImageIO.read(new File("openlock.png"));
                if (this.isLocked) {
                    lock = ImageIO.read(new File("closedlock.png"));
                }
                Image image = SwingFXUtils.toFXImage(lock,null);
                double size = min(w/8, h/8);
                gc.drawImage(image, x, y, size, size);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        if (tables != null) {
            for (Table table : tables) {
                table.draw(c, getCoordinateX(), getCoordinateY());
            }
        }

        if (chairs != null) {
            for (Chair chair : chairs) {
                chair.draw(c,this.getCoordinateX(), this.getCoordinateY());
            }
        }
    }

    public void setType(RoomType roomtype) {
        this.roomtype = roomtype;
    }
    public RoomType getType() {
        return roomtype;
    }

    //Überprüft, ob ein Tisch legal platziert ist
    public boolean isLegalTable(Table t) {
        double x1 = t.getCoordinateX();
        double x2 = x1 + t.getWidth();
        double y1 = t.getCoordinateY();
        double y2 = y1 + t.getHeight();
        double w = getWidth();
        double h = getHeight();
        if (x1 < 0 || x2 < 0 || y1 < 0 || y2 < 0 || x1 > w || x2 > w || y1 > h || y2 > h) return false;
        double x = min(x1, x2);
        double y = min(y1, y2);
        w = abs(t.getWidth());
        h = abs(t.getHeight());
        for (Table table : this.tables) {
            if (table.getCoordinateX() >= x + w)
                continue;
            if (x >= table.getCoordinateX() + table.getWidth())
                continue;
            if (table.getCoordinateY() >= y + h)
                continue;
            if (y >= table.getCoordinateY() + table.getHeight())
                continue;
            if (t == table)
                continue;
            return false;
        }
        //kontrolliert, ob ein Stuhl im Weg ist
        for (Chair chair : this.chairs){
            if (chair.getCoordinateX() >= x + w)
                continue;
            if (x >= chair.getCoordinateX() + chair.getWidth())
                continue;
            if (chair.getCoordinateY() >= y + h)
                continue;
            if (y >= chair.getCoordinateY() + chair.getHeight())
                continue;
            return false;
        }
        return true;
    }

    //Überprüft, ob ein Stuhl legal platziert ist
    public boolean isLegalChair(Chair c) {
        double x1 = c.getCoordinateX();
        double x2 = x1 + c.getWidth();
        double y1 = c.getCoordinateY();
        double y2 = y1 + c.getHeight();
        double w = getWidth();
        double h = getHeight();
        if (x1 < 0 || x2 < 0 || y1 < 0 || y2 < 0 || x1 > w || x2 > w || y1 > h || y2 > h) return false;
        double x = min(x1, x2);
        double y = min(y1, y2);
        w = abs(c.getWidth());
        h = abs(c.getHeight());
        for (Table table : this.tables) {
            if (table.getCoordinateX() >= x + w)
                continue;
            if (x >= table.getCoordinateX() + table.getWidth())
                continue;
            if (table.getCoordinateY() >= y + h)
                continue;
            if (y >= table.getCoordinateY() + table.getHeight())
                continue;
            return false;
        }
        //kontrolliert, ob ein Stuhl im Weg ist
        for (Chair chair : this.chairs){
            if (chair.getCoordinateX() >= x + w)
                continue;
            if (x >= chair.getCoordinateX() + chair.getWidth())
                continue;
            if (chair.getCoordinateY() >= y + h)
                continue;
            if (y >= chair.getCoordinateY() + chair.getHeight())
                continue;
            if (c == chair)
                continue;
            return false;
        }
        return true;

        /*if(!c.isInside(this))
            return false;

        for (Chair chair : this.chairs) {
            if(c.overlaps(chair)) {
                return false;
            }
        }

        //Funktioniert noch nicht(?)

        for (Table table : this.tables) {
            if(c.overlaps(table)) {
                return false;
            }
        }
        return true;*/
    }
    //x and y need to be coordinates relativ to the this room object NOT THE CANVAS
    public Chair getChairAtRoomCoordinates(double x, double y) {
        for (Chair chair : chairs) {
            if (chair.coordinatesInside(x, y))
                return chair;
        }
        return null;
    }

    //x and y are coordinates on the canvas
    public Table getTableAtCoordinates(double x, double y) {
        double roomX = x - this.getCoordinateX();
        double roomY = y - this.getCoordinateY();
        for (Table table : tables) {
            if (table.coordinatesInside(roomX, roomY))
                return table;
        }
        return null;
    }

    public void adjustChairs(double oldX, double oldY, double newX, double newY) {
        for (Chair chair : chairs) {
            chair.setCoordinateX(chair.getCoordinateX() + newX - oldX);
            chair.setCoordinateY(chair.getCoordinateY() + newY - oldY);
        }
    }

    //Errechnet für räume die Koordinaten für das Grid passend.
    //Ruft die Funktion für chairs und tables auf
    public void toGridCoordinates(double s) {
        double oldX = this.getCoordinateX();
        double oldY = this.getCoordinateY();
        double diffX = oldX % s;
        double diffY = oldY % s;
        if (diffX < s/2) {
            this.setCoordinateX(oldX-diffX);
        } else if(diffX >= s/2) {
            this.setCoordinateX(oldX+(s-diffX));
        }
        if (diffY < s/2) {
            this.setCoordinateY(oldY-diffY);
        } else if(diffY >= s/2) {
            this.setCoordinateY(oldY+(s-diffY));
        }
        double oldHeight = this.getHeight();
        double oldWidth = this.getWidth();
        double diffHeight = oldHeight % s;
        double diffWidth =  oldWidth % s;
        if (diffHeight < s/2) {
            this.setHeight(oldHeight - diffHeight);
        } else if(diffHeight >= s/2) {
            this.setHeight(oldHeight + (s - diffHeight));
        }
        if (diffWidth < s/2) {
            this.setWidth(oldWidth - diffWidth);
        } else if (diffWidth >= s/2) {
            this.setWidth(oldWidth+(s-diffWidth));
        }

        for (Table table : tables) {
            table.toGridCoordinates(s);
        }

        for (Chair chair : chairs) {
            chair.toGridCoordinates(s);
        }
    }

    public double getGridSize() {
        return gridSize;
    }
    public void setGridSize(double gridSize) {
        this.gridSize = gridSize;
    }

    //returns true if the given room touches this room at any wall
    //two corners touching each other counts as a touch
    public boolean touchesRoomOnGrid(Room r, double gridSize) {
        double leftToRight = abs(this.getCoordinateX() - (r.getCoordinateX() + r.getWidth()));
        double rightToLeft = abs(this.getCoordinateX() + (this.getWidth() - r.getCoordinateX()));
        double topToBottom = abs(this.getCoordinateY() - (r.getCoordinateY() + r.getHeight()));
        double bottomToTop = abs(this.getCoordinateY() + (this.getHeight() - r.getCoordinateY()));
        //walls are within gridSize distance on x or y coordinate
        if ((topToBottom <= gridSize) || (bottomToTop <= gridSize)) {
            //if left side of this more right than right side of r or vice versa the rooms don't touch
            return !((this.getCoordinateX() + this.getWidth()) < r.getCoordinateX()) && !((r.getCoordinateX() + r.getWidth()) < this.getCoordinateX());
        }
        if ((leftToRight <= gridSize) || (rightToLeft <= gridSize)) {
            //if bottom side of this is higher than top side of r or vice versa the rooms don't touch
            return !((this.getCoordinateY() + this.getHeight()) < r.getCoordinateY()) && !((r.getCoordinateY() + r.getHeight()) < this.getCoordinateY());
        }
        return false;
    }

    public Chair freeChair(String buildingName) {
        List<Person> persons = DatabaseCommunication.getPersons(buildingName);
        outerLoop: for (Chair chair : chairs) {
            double x = chair.getCoordinateX() + this.getCoordinateX();
            double y = chair.getCoordinateY() + this.getCoordinateY();
            for (Person person : persons) {
                if (person.getX() == x && person.getY() == y) {
                    continue outerLoop;
                }
            }
            return chair;
        }
        return null;
    }

    public boolean getNameState() {
        return nameState;
    }
    public void setNameState(boolean nameState) {
        this.nameState = nameState;
    }
    
    public boolean clickedOnLock(double x, double y) {
        double size = min(this.getWidth()/8, this.getHeight()/8);
        return x < size && y < size;
    }
}
