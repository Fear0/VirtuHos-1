package definitions;
import javafx.scene.canvas.Canvas;

public abstract class EditorObject implements java.io.Serializable {
    private double coordinateX;
    private double coordinateY;
    private double width;
    private double height;

    private boolean selected;

    public EditorObject(double x, double y, double w, double h) {
        this.coordinateX = x;
        this.coordinateY = y;
        this.width = w;
        this.height = h;
    }

    public double getCoordinateX() {
        return coordinateX;
    }
    public void setCoordinateX(double x) {
        this.coordinateX = x;
    }
    public double getCoordinateY() {
        return coordinateY;
    }
    public void setCoordinateY(double y) {
        this.coordinateY = y;
    }
    public double getWidth() {
        return width;
    }
    public void setWidth(double w) {
        this.width = w;
    }
    public double getHeight() {
        return height;
    }
    public void setHeight(double h) {
        this.height = h;
    }

    public abstract void draw(Canvas c);
    //public abstract void draw(Canvas c, boolean b);
    //public abstract void draw(Canvas c, double offsetX, double offsetY);

    //Ueberpruefft ob Koordinaten in diesem Objekt drin liegen
    public boolean coordinatesInside(double x, double y) {
        return x > getCoordinateX() && x < getCoordinateX() + getWidth() &&
                y > getCoordinateY() && y < getCoordinateY() + getHeight();
    }

    //Ueberpruefft ob dieses Objekt in ueberbegenen Objekt drin liegt
    public boolean isInside(EditorObject o) {
        return !(getCoordinateX() < o.getCoordinateX()) &&
                !(getCoordinateX() + getWidth() > o.getCoordinateX() + o.getWidth()) &&
                !(getCoordinateY() < o.getCoordinateY()) &&
                !(getCoordinateY() + getHeight() > o.getCoordinateY() + o.getHeight());
    }

    @Override
    public String toString() {
        return "" + getCoordinateY() + " " + getCoordinateY() + " " + getWidth() + " " + getHeight();
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    public boolean getSelected() {
        return selected;
    }
}
