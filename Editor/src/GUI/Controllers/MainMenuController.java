package GUI.Controllers;

import GUI.MainMenu;
import de.uni_hannover.wb_interaktionen_1.i_face.InteraktionControl;
import de.uni_hannover.wb_interaktionen_1.logic.Login;
import de.uni_hannover.wb_interaktionen_1.test_db.TestDB;
import definitions.Building;
import definitions.DatabaseCommunication;
import definitions.Text;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.concurrent.TimeUnit;


public class MainMenuController {


    private InteraktionControl IC;
    private ShowController SC;
    private PersonThread PThread;

    private @FXML TextField userField;

    //Wechselt aus dem Hauptmenü in den Editor
    public void onEditorClicked() {
        MainMenu.primaryStage.setScene(MainMenu.editor);
        MainMenu.primaryStage.setWidth(882);
        MainMenu.primaryStage.setHeight(746);
    }

    //Beendet das Programm
    public void onEndClick() {
        IC.logout();
        System.exit(0);
    }

    //Wechselt aus dem Hauptmenü in den Anzeigemodus
    public void onShowClicked() {
        String username = DatabaseCommunication.getUsername();
        if (username == null || username.equals("")) {
            Alert alert = new Alert(Alert.AlertType.ERROR, Text.MAIN_MENU_CONTROLLER_NO_USERNAME);
            alert.getButtonTypes().setAll(Text.buttonTypeOk);
            alert.setTitle(Text.TITLE);
            alert.setHeaderText(null);
            alert.showAndWait();
        } else {
            MainMenu.getShowController().setUsername(username);
            MainMenu.primaryStage.setScene(MainMenu.show);
            ShowController ctrl = MainMenu.getShowController();
            ctrl.setIC(IC);
        }
    }

    //Zeigt Login-Dialog an
    public void onLogin() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../LoginDialog.fxml"));
        Parent parent = fxmlLoader.load();
        LoginController ctrl = fxmlLoader.getController();
        ctrl.setMainUsrField(userField);
        ctrl.setIC(IC);
        ctrl.setSC(SC);
        Scene scene = new Scene(parent, 400, 200);
        Stage stage = new Stage();
        stage.setTitle("VirtuHoS Login");
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.showAndWait();
    }

    public void setIC(InteraktionControl IC) {
        this.IC = IC;
    }

    public void setSC(ShowController SC){
        this.SC =SC;
    }

    public void setPThread(PersonThread p){
        this.PThread = p;
    }
}

class PersonThread implements Runnable{
    private volatile boolean exit = false;
    private InteraktionControl IC;

    public PersonThread(InteraktionControl IC){
        this.IC = IC;

    }

    @Override
    public void run() {

        while(!exit){

            IC.checkRequest();


            //wait 5 sec
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                System.err.println("thread interrupted");
            }

        }
    }

    public void end(){
        exit = true;
    }

}