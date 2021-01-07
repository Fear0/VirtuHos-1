package definitions;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static java.lang.Double.min;
import static java.lang.Math.abs;

public class Table extends EditorObject {
    private Document doc;
    public Table(double x, double y, double w, double h) {
        super(x, y, w, h);
    }

    public void setDoc(Document doc) {
        this.doc = doc;
    }
    public Document getDoc() {
        return this.doc;
    }

    //draws a table with the offset of his rooms coordinates
    public void draw(Canvas c, double offsetX, double offsetY) {
        GraphicsContext gc = c.getGraphicsContext2D();
        gc.setFill(Color.rgb(139, 69, 19, 0.5));
        double x1 = getCoordinateX();
        double x2 = x1 + getWidth();
        double y1 = getCoordinateY();
        double y2 = y1 + getHeight();
        double x = min(x1, x2);
        double y = min(y1, y2);
        double w = abs(getWidth());
        double h = abs(getHeight());
        gc.setStroke(Color.BLACK);
        gc.strokeRect(x + offsetX, y + offsetY, w, h);
        gc.fillRect(x + offsetX, y + offsetY, w, h);
        if (this.doc != null) {
            //draw the document as a white rectangle
            double documentW = (30 < w) ? 30: (15 < w) ? 15: 9;
            double documentH = (40 < h) ? 40: (20 < h) ? 20: 12;
            double docX = x+ offsetX + w/2 - documentW/2;
            double docY = y + offsetY + h/2 - documentH/2;
            //gc.strokeRoundRect(docX, docY, documentW, documentH,10,10);
            //gc.setFill(Color.rgb(255,255,255, 1));
            //gc.fillRoundRect(docX, docY, documentW, documentH, 10, 10);
            try {
                BufferedImage myPicture = ImageIO.read(new File("document.png"));
                Image image = SwingFXUtils.toFXImage(myPicture,null);
                gc.drawImage(image, docX, docY, documentW, documentH);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //draw lines into document maybe in polish
            //gc.setFill(Color.rgb(0,0,0, 1));
            //gc.strokeLine(docX,docY, docX + documentW, docY + documentH);
        }
    }

    //Zeichnet einen Tisch
    @Override
    public void draw(Canvas c) {
        GraphicsContext gc = c.getGraphicsContext2D();
        gc.setFill(Color.rgb(139, 69, 19, 0.5));
        double x1 = getCoordinateX();
        double x2 = x1 + getWidth();
        double y1 = getCoordinateY();
        double y2 = y1 + getHeight();
        double x = min(x1, x2);
        double y = min(y1, y2);
        double w = abs(getWidth());
        double h = abs(getHeight());
        gc.strokeRect(x, y, w, h);
        gc.fillRect(x, y, w, h);
    }

    //passt tables ans Grid an
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

    //returns true if the table has a document at the given position. the position must be in coordinates of the table!
    //so (0,0) is the top left corner of the table
    public boolean hasDocAtTableCoordinates(double clickX, double clickY) {
        if (this.doc == null){
            return false;
        }
        double w = abs(getWidth());
        double h = abs(getHeight());
        double documentW = (30 < w) ? 30: (15 < w) ? 15: 9;
        double documentH = (40 < h) ? 40: (20 < h) ? 20: 12;
        double x1pos = w/2 - documentW/2;
        double y1pos = h/2 - documentH/2;
        double x2pos = w/2 + documentW/2;
        double y2pos = h/2 + documentH/2;
        return clickX <= x2pos && clickX >= x1pos && clickY <= y2pos && clickY >= y1pos;
    }
}
