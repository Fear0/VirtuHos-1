package GUI;

import GUI.Controllers.ShowController;
import definitions.Text;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainMenu extends Application {
    public static Scene mainMenu;
    public static Scene editor;
    public static Scene show;
    public static Stage primaryStage;

    private static ShowController showController;

    @Override
    public void start(Stage primaryStage) throws Exception {
        MainMenu.primaryStage = primaryStage;

        mainMenu = new Scene(FXMLLoader.load(getClass().getResource("MainMenu.fxml")), 700, 600);
        editor = new Scene(FXMLLoader.load(getClass().getResource("Editor.fxml")), 862, 706);

        FXMLLoader showLoader = new FXMLLoader(getClass().getResource("Show.fxml"));
        Parent parent = showLoader.load();
        showController = showLoader.getController();
        show = new Scene(parent);

        primaryStage.setTitle(Text.TITLE);
        primaryStage.setWidth(845);
        primaryStage.setHeight(590);
        primaryStage.setScene(mainMenu);
        primaryStage.show();
    }

    @Override
    public void stop() {
        if (MainMenu.primaryStage.getScene().equals(show))
            showController.removeUser();
    }

    public static ShowController getShowController() {
        return showController;
    }
}