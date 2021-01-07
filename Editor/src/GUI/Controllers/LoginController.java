package GUI.Controllers;

import definitions.DatabaseCommunication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    private @FXML TextField userField;
    private @FXML PasswordField passwordField;
    private TextField mainUsrField;

    public void onConfirm(ActionEvent event) {
        if (checkPassword(userField.getText(), passwordField.getText())) {
            String user = userField.getText().trim();
            mainUsrField.setText(user);
            DatabaseCommunication.setUsername(user);
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
}
