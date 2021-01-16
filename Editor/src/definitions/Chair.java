package definitions;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.List;

import static java.lang.Double.min;
import static java.lang.Math.abs;

public class Chair extends EditorObject {

    public Chair(double x, double y, double w, double h) {
        super(x, y, w, h);
    }

    public void draw(Canvas c, double offsetX, double offsetY) {
        GraphicsContext gc = c.getGraphicsContext2D();
        gc.setFill(Color.rgb(66, 37, 24, 0.5));
        double x1 = getCoordinateX();
        double x2 = x1 + getWidth();
        double y1 = getCoordinateY();
        double y2 = y1 + getHeight();
        double x = min(x1, x2);
        double y = min(y1, y2);
        double w = abs(getWidth());
        double h = abs(getHeight());
        gc.strokeRect(x + offsetX, y + offsetY, w, h);
        gc.fillRect(x + offsetX, y + offsetY, w, h);
    }

    //Zeichnet einen Stuhl
    @Override
    public void draw(Canvas c) {
        GraphicsContext gc = c.getGraphicsContext2D();
        gc.setFill(Color.TRANSPARENT);
        gc.strokeRect(getCoordinateX(), getCoordinateY(), getWidth(), getHeight());
        gc.fillRect(getCoordinateX(), getCoordinateY(), getWidth(), getHeight());
    }

    //passt chairs an das Grid an.s
    public void toGridCoordinatesOld(double s) {
        double oldX = this.getCoordinateX();
        double oldY = this.getCoordinateY();
        double diffX = oldX % s;
        double diffY = oldY % s;
        setCoordinateX(oldX - diffX);
        setCoordinateY(oldY - diffY);
        this.setWidth(s);
        this.setHeight(s);
    }

    public void toGridCoordinates(double s) {
        double oldX = this.getCoordinateX();
        double oldY = this.getCoordinateY();
        double diffX = oldX % s;
        double diffY = oldY % s;
        if (diffX < s/2) {
            this.setCoordinateX(oldX-diffX);
        } else if (diffX >= s/2) {
            this.setCoordinateX(oldX+(s-diffX));
        }
        if (diffY < s/2) {
            this.setCoordinateY(oldY-diffY);
        } else if (diffY >= s/2) {
            this.setCoordinateY(oldY+(s-diffY));
        }
        double oldHeight = this.getHeight();
        double oldWidth = this.getWidth();
        double diffHeight = oldHeight % s;
        double diffWidth =  oldWidth % s;
        if (diffHeight < s/2) {
            this.setHeight(oldHeight - diffHeight);
        } else if (diffHeight >= s/2) {
            this.setHeight(oldHeight+(s-diffHeight));
        }
        if (diffWidth < s/2) {
            this.setWidth(oldWidth - diffWidth);
        } else if(diffWidth >= s/2) {
            this.setWidth(oldWidth+(s-diffWidth));
        }
    }
    public boolean isFree(double roomX, double roomY, String buildingName) {
        List<Person> persons = DatabaseCommunication.getPersons(buildingName);
        double x = this.getCoordinateX() + roomX;
        double y = this.getCoordinateY() + roomY;
        for (Person person : persons) {
            if (person.getX() == x && person.getY() == y) {
                return false;
            }
        }
        return true;
    }
}
