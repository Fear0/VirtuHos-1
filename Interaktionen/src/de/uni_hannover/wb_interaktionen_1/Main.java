package de.uni_hannover.wb_interaktionen_1;

import de.uni_hannover.wb_interaktionen_1.gui.ErrorMessage;
import de.uni_hannover.wb_interaktionen_1.logic.ReadConfig;
import de.uni_hannover.wb_interaktionen_1.test_db.TestDB;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOError;
import java.io.IOException;
import java.lang.*;
import java.sql.SQLException;

import de.uni_hannover.wb_interaktionen_1.gui.GUIMain;
import de.uni_hannover.wb_interaktionen_1.logic.Login;
import de.uni_hannover.wb_interaktionen_1.threads.ThreadUpdateData;

public class Main extends Application{

    Stage window;
    Scene login, mains;
    TestDB database;
    Login log;
    GUIMain g;
    ReadConfig rc;

    public static void main(String[] args) {
        launch(args);
    }

    public TestDB getDB() {
        return database;
    }

    public Stage getWindow(){
        return window;
    }

    public Scene getLoginScene(){
        return login;
    }

    @Override
    public void init() throws Exception {
        super.init();
        database = new TestDB();
        log = new Login(database);
        g = new GUIMain(log, database, this);
        try{
            rc = new ReadConfig();
        } catch (IOException e){
            e.printStackTrace();
        }

    }

    public void start(Stage primaryStage) throws Exception{
        window = primaryStage;
        //Login Form
        Text text = new Text();
        String text_in = "Personalnummer:";
        text.setText(text_in);
        Text text_2 = new Text();
        String text_in_2 = "Gebäude:";
        text_2.setText(text_in_2);
        TextField nameInput = new TextField();
        ComboBox<String> building = new ComboBox<String>(getDB().getAllBuildings());
        building.setValue(rc.Building);
        Button button_login = new Button("Login");
        button_login.setDefaultButton(true);
        button_login.setOnAction(e -> log.switchScene(window, nameInput.getText(), log, this, building.getValue()));

        //Layout
        VBox layout_login = new VBox();
        layout_login.setPadding(new Insets(20, 20, 20, 20));
        layout_login.getChildren().addAll(text, nameInput, text_2, building, button_login);
        login = new Scene(layout_login, 300, 250);

        //Launch
        window.setScene(login);
        window.setTitle("VirtuHoS");
        window.show();
    }

    public void updatemain(Stage window){
        //This is used to avoid a second reload of the GUI when the current user adds or removes a hallgroup
        try {
            log.getThreadUpdate().setHallgroups(getDB().getAllHallGroups(rc.Building));
        } catch (SQLException e){
            e.printStackTrace();
        }

        VBox layout_main = new VBox();
        g = new GUIMain(log, database, this);
        mains = new Scene(layout_main, 700, 450);
        layout_main.getChildren().add(g.createLogoutButton(window, login));
        layout_main.getChildren().add(g.createMainGripPane2(true));
        layout_main.getChildren().add(g.createHallControl(window, this));
        window.setOnCloseRequest(e -> {
            g.confirmLogout(window, null);
            try {
                database.closeConnection();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
        window.setScene(mains);
        window.setTitle("VirtuHoS");
        window.show();
    }


}
