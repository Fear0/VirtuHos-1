package GUI.Controllers;

import GUI.MainMenu;
import definitions.DatabaseCommunication;
import definitions.Text;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;

public class MainMenuController {

    private @FXML TextField userField;

    //Wechselt aus dem Hauptmenü in den Editor
    public void onEditorClicked() {
        MainMenu.primaryStage.setScene(MainMenu.editor);
        MainMenu.primaryStage.setWidth(882);
        MainMenu.primaryStage.setHeight(746);
    }

    //Beendet das Programm
    public void onEndClick() {
        System.exit(0);
    }

    //Wechselt aus dem Hauptmenü in den Anzeigemodus
    public void onShowClicked() {
        String username = DatabaseCommunication.getUsername();
        if (username == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, Text.MAIN_MENU_CONTROLLER_NO_USERNAME);
            alert.getButtonTypes().setAll(Text.buttonTypeOk);
            alert.setTitle(Text.TITLE);
            alert.setHeaderText(null);
            alert.showAndWait();
        } else {
            MainMenu.getShowController().setUsername(username);
            MainMenu.primaryStage.setScene(MainMenu.show);
        }
    }

    //Zeigt Login-Dialog an
    public void onLogin() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../LoginDialog.fxml"));
        Parent parent = fxmlLoader.load();
        LoginController ctrl = fxmlLoader.getController();
        ctrl.setMainUsrField(userField);
        Scene scene = new Scene(parent, 400, 200);
        Stage stage = new Stage();
        stage.setTitle("VirtuHoS Login");
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.showAndWait();
    }
}
