package Halle;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {


    Scene scene;


    @Override
    public void start(Stage primaryStage) throws Exception{
        //creat the GUI
        Parent root = FXMLLoader.load(getClass().getResource("../View/Halle.fxml"));
        primaryStage.setTitle("Halle");
        primaryStage.setScene(new Scene(root, 1133, 750));
        primaryStage.setResizable(false);
        primaryStage.show();
        primaryStage.setAlwaysOnTop(false);



    }



    /*private void addAttractors() {

        Layer layer = playfield;

        // center attractor
        double x = layer.getWidth() / 2;
        double y = layer.getHeight() / 2;

        // dimensions
        double width = Settings.ATTRACTOR_SIZE;
        double height = Settings.ATTRACTOR_SIZE;

        // create attractor data
        Vector2D location = new Vector2D( x,y);
        Vector2D velocity = new Vector2D( 0,0);
        Vector2D acceleration = new Vector2D( 0,0);

        // create attractor and add to layer
        Attractor attractor = new Attractor( layer, location, velocity, acceleration, width, height);

        // register sprite
        allAttractors.add(attractor);

    }*/

    /*private void addListeners() {

        // capture mouse position
        scene.addEventFilter(MouseEvent.ANY, e -> {
            mouseLocation.set(e.getX(), e.getY());
        });*/

        // move attractors via mouse
       /* for( Attractor attractor: allAttractors) {
            mouseGestures.makeDraggable(attractor);
        }*/
    //}
    public static void main(String[] args) {
        launch(args);
    }
}
