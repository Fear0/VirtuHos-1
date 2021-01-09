package de.uni_hannover.wb_interaktionen_1.gui;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/** This class creates GUI error messages.
 *
 * @author Joshua Berger
 */
public class ErrorMessage {

    /** This method creates a popup window containing an error message
     * @author Joshua Berger
     * @param message: The message to display in the error window
     * @return The created popup window
     */
    public Stage createError(String message) {
        Stage popup = new Stage();
        VBox comp = new VBox();
        Label infotext = new Label(message);
        Button confirm = new Button("Ok");
        confirm.setOnAction(e -> popup.close());
        Scene stageScene = new Scene(comp, 300, 150);
        comp.getChildren().addAll(infotext, confirm);
        popup.setScene(stageScene);
        popup.show();
        return popup;
    }
}
