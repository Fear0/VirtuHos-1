package GUI.Controllers;

import de.uni_hannover.wb_interaktionen_1.i_face.InteraktionControl;
import definitions.DatabaseCommunication;
import definitions.Text;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Optional;

public class LoginController {

    private @FXML TextField userField;
    private @FXML PasswordField passwordField;
    private TextField mainUsrField;
    private InteraktionControl IC;
    private ShowController SC;

    public void onConfirm(ActionEvent event) {
        /*
        if (checkPassword(userField.getText(), passwordField.getText())) {
            String user = userField.getText().trim();
            mainUsrField.setText(user);
            DatabaseCommunication.setUsername(user);
        }
        onCancel(event);*/
        String user = userField.getText().trim();
        if(IC.login(user)){
            SC.setID(user);
            String name = IC.getUserName(user);
            mainUsrField.setText(name);
            DatabaseCommunication.setUsername(name);
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(Text.TITLE);
            alert.setHeaderText("");
            alert.setContentText(Text.LOGIN_CONTROLLER_LOGIN_ERROR);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK){
                return;
            }
        }
        onCancel(event);
    }


    public void onCancel(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    private boolean checkPassword(String user, String password) {
        //TODO
        return true;
    }

    public void setMainUsrField(TextField mainUsrField) {
        this.mainUsrField = mainUsrField;
    }

    public void setIC(InteraktionControl IC) {
        this.IC = IC;
    }

    public void setSC(ShowController SC){
        this.SC =SC;
    }
}
