package definitions;

import javafx.scene.canvas.Canvas;
import javafx.scene.layout.StackPane;

public class ResizeableCanvas extends Canvas {
    public ResizeableCanvas(double width, double height){
        super(width,height);
    }

    @Override
    public boolean isResizable() {
        return true;
    }

    @Override
    public double prefWidth(double height) {
        return getWidth();
    }

    @Override
    public double prefHeight(double width) {
        return getHeight();
    }

    public void extendWidth(double width) {
        this.setWidth(this.getWidth() + width);
    }

    public void extendHeight(double height) {
        this.setHeight(this.getHeight() + height);
    }

    public void decreaseWidth(double width) {
        this.setWidth(this.getWidth() - width);
    }

    public void decreaseHeight(double height) {
        this.setHeight(this.getHeight() - height);
    }
}