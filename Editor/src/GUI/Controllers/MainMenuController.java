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

import static GUI.MainMenu.show;


public class MainMenuController {


    private InteraktionControl IC;
    private ShowController SC;
    private PersonThread PThread = null;
    private LoginController LC;
    private Parent Login;
    private Stage LoginStage;

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
            MainMenu.primaryStage.setScene(show);
            ShowController ctrl = MainMenu.getShowController();
            ctrl.setIC(IC);
        }
    }

    //Zeigt Login-Dialog an
    public void onLogin() throws IOException {
        LC.setMainUsrField(userField);
        LC.setIC(IC);
        LC.setSC(SC);
        LoginStage.showAndWait();
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

    public void setLC(LoginController LC) {
        this.LC = LC;
    }

    public void setLogin(Parent login) {
        Login = login;
    }

    public void setLoginStage(Stage loginStage) {
        LoginStage = loginStage;
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
            IC.updateUser();

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