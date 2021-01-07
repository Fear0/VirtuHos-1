package definitions;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Person implements java.io.Serializable {

    //ersetzen/erweitern durch id von interaktionen
    private final String name;
    private final double x;
    private final double y;

    public Person(String name, double x, double y) {
        this.name = name;
        this.x = x;
        this.y = y;
    }

    public String getName() {
        return this.name;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void draw(Canvas canvas, double gridSize) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.rgb(61, 176, 187, 0.7));
        gc.strokeOval(this.x, this.y, gridSize, gridSize);
        gc.fillOval(this.x, this.y, gridSize, gridSize);
        gc.setFill(Color.rgb(0, 0, 0, 1));
        gc.fillText(this.getName(), this.x, this.y);
    }

    public boolean coordinatesInside(double roomX, double roomY, double gridSize) {
        //TODO Diese Methode returnt immer false.
        return x > getX() && x < getX() + gridSize && y > getY() && y < getY() + gridSize;
    }
}
