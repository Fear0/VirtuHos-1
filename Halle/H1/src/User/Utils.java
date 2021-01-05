package User;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;


public class Utils {

    public static double map(double value, double currentRangeStart, double currentRangeStop, double targetRangeStart, double targetRangeStop) {
        return targetRangeStart + (targetRangeStop - targetRangeStart) * ((value - currentRangeStart) / (currentRangeStop - currentRangeStart));
    }

    /**
     * Create an imageview of a right facing arrow.
     * @param size The width. The height is calculated as width / 2.0.
     * @return height
     *//*
    public static ImageView createArrowImageView( double size) {

        return createArrowImageView(size, size / 2.0, Color.BLUE, Color.BLUE.deriveColor(1, 1, 1, 0.3), 1);

    }*/

    public static StackPane createCircleImageView(double size , int iid, String name, StackPane pane) {
        //ImageView circle= createArrowImageView(size, size / 2.0, Color.BLUE, Color.BLUE.deriveColor(1, 1, 1, 0.3), 1);
        Circle circle= new Circle(Settings.PERSON_SIZE, Color.BLUE.deriveColor(1, 1, 1, 0.3));
        pane = new StackPane();
        Text text = new Text(name);
         pane = new StackPane();
        pane.getChildren().add(circle);
        pane.getChildren().add(text);
       text.setX(circle.getCenterX());
        text.setY(circle.getCenterY());


        return pane;
    }

    /**
     * Create an imageview of a right facing arrow.
     * @param width
     * @param height
     * @return
     */
   /* public static ImageView createArrowImageView( double width, double height, Paint stroke, Paint fill, double strokeWidth) {

        return new ImageView( createArrowImage(width, height, stroke, fill, strokeWidth));

    }
*/
    /**
     * Create an image of a right facing arrow.
     * @param width
     * @param height
     * @return
     */
   /* public static Image createArrowImage( double width, double height, Paint stroke, Paint fill, double strokeWidth) {

        WritableImage wi;
        Circle circle = new Circle(height/2);

        circle.setStrokeLineJoin(StrokeLineJoin.MITER);
        circle.setStrokeLineCap(StrokeLineCap.SQUARE);

        circle.setStroke(stroke);
        circle.setFill(fill);
        circle.setStrokeWidth(strokeWidth);
        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);


        wi = new WritableImage((int) height*3, (int)height*3);
        circle.snapshot(parameters, wi);
        return wi;
        *//*WritableImage wi;

        double arrowWidth = width - strokeWidth * 2;
        double arrowHeight = height - strokeWidth * 2;

        Polygon arrow = new Polygon( 0, 0, arrowWidth, arrowHeight / 2, 0, arrowHeight); // left/right lines of the arrow
        arrow.setStrokeLineJoin(StrokeLineJoin.MITER);
        arrow.setStrokeLineCap(StrokeLineCap.SQUARE);
        arrow.setStroke(stroke);
        arrow.setFill(fill);
        arrow.setStrokeWidth(strokeWidth);

        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);

        int imageWidth = (int) width;
        int imageHeight = (int) height;

        wi = new WritableImage( imageWidth, imageHeight);
        arrow.snapshot(parameters, wi);

        return wi;*//*

    }*/


}