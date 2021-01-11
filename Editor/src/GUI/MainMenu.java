package GUI;

import GUI.Controllers.EditorController;
import GUI.Controllers.MainMenuController;
import GUI.Controllers.ShowController;
import de.uni_hannover.wb_interaktionen_1.i_face.InteraktionControl;
import de.uni_hannover.wb_interaktionen_1.logic.Login;
import de.uni_hannover.wb_interaktionen_1.test_db.TestDB;
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

    public static TestDB InterDB = new TestDB();
    public static InteraktionControl IC = new InteraktionControl(new Login(InterDB), InterDB);

    @Override
    public void start(Stage primaryStage) throws Exception {
        MainMenu.primaryStage = primaryStage;

        FXMLLoader MMLoader = new FXMLLoader(getClass().getResource("MainMenu.fxml"));
        mainMenu = new Scene(MMLoader.load(), 700, 600);
        FXMLLoader ELoader = new FXMLLoader(getClass().getResource("Editor.fxml"));
        editor = new Scene(ELoader.load(), 862, 706);

        FXMLLoader showLoader = new FXMLLoader(getClass().getResource("Show.fxml"));
        Parent parent = showLoader.load();
        showController = showLoader.getController();
        show = new Scene(parent);

        MainMenuController ctrl = MMLoader.getController();
        ctrl.setIC(IC);
        ctrl.setSC(showController);
        EditorController ctrl2 = ELoader.getController();
        ctrl2.setIC(IC);

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
        IC.logout();
    }

    public static ShowController getShowController() {
        return showController;
    }
}