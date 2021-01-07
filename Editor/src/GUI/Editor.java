package GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class Editor extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene editorScene = new Scene(loadEditor(), 800, 800);

        primaryStage.setTitle("GUI.Editor");
        primaryStage.setX(100);
        primaryStage.setY(100);
        primaryStage.setScene(editorScene);
        primaryStage.show();
    }

    private Parent loadEditor() throws IOException {
        return FXMLLoader.load(getClass().getResource("Editor.fxml"));
    }
}
