package de.uni_hannover.wb_interaktionen_1.gui;

import de.uni_hannover.wb_interaktionen_1.logic.User;
import de.uni_hannover.wb_interaktionen_1.rooms.Room;
import de.uni_hannover.wb_interaktionen_1.test_db.TestDB;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/** This class manages the requests which are send when a user invites another user in his room.
 *
 * @author David Sasse
 * @version 1.0
 */
public class Request {
    private TestDB db;
    private User current_user;
    private String sender;
    private String sender_name;
    private String type;



    /** The constructor for a request.
     *
     * @param db The database
     * @param receiver The receiver of the request
     * @param sender The sender of the request
     * @param type The type of the request (join or reject)
     */
    public Request(TestDB db, User receiver, String sender, String type){
        this.db = db;
        this.current_user = receiver;
        this.sender = sender;
        this.type = type;
        try {
            this.sender_name = db.getUserName(sender);
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    /** The getter for the sender.
     *
     * @return the personalID of the sender
     */
    public String getSender(){
        return sender;
    }

    /** The getter for the type of the request.
     *
     * @return The type of the request (join or reject)
     */
    public String getType(){
        return type;
    }

    /** Creates a popup with the invitation. The user can accept or reject the invitation.
     *
     * @param room The room to which the user is invited.
     * @param rooms The list with all rooms.
     */
    public boolean createRequest(Room room, ArrayList<Room> rooms){
        /*Stage popup = new Stage();
        VBox vBox = new VBox();
        Label infotext = new Label(sender_name + " hat sie in den Raum " + room.getType() + "_" + room.getId() + " eingeladen.");
        Label timer_l = new Label();

        Timer timer = new Timer();
        final int[] interval = {30};
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        if (interval[0] > 0) {
                            Platform.runLater(() -> timer_l.setText("Die Einladung wird in " + interval[0] + " Sekunden automatisch abgelehnt."));
                            interval[0]--;
                        } else {
                            timer.cancel();
                            popup.close();
                            rejectRequest();
                        }
                    }
                });
            }
        }, 1000, 1000);

        HBox hbox = new HBox();
        Button accept = new Button("Annehmen");
        Button reject = new Button("Ablehnen");
        accept.setOnAction(e -> {
            acceptRequest(room, rooms);
            timer.cancel();
            popup.close();
        });
        reject.setOnAction(e -> {
            rejectRequest();
            timer.cancel();
            popup.close();
        });
        Scene stageScene = new Scene(vBox, 300, 150);
        hbox.getChildren().addAll(accept, reject);
        vBox.getChildren().addAll(infotext, timer_l, hbox);
        popup.setScene(stageScene);
        popup.show();*/
        final FutureTask query =new FutureTask(new Callable() {
            public final ButtonType buttonTypeYes = new ButtonType("Ablehnen", ButtonBar.ButtonData.YES);
            public final ButtonType buttonTypeNo = new ButtonType("Annehmen", ButtonBar.ButtonData.NO);

            @Override
            public Boolean call() throws Exception {
                String conf = sender_name + " hat dich in seinen Raum eingeladen, möchtest du die Einladung annehmen?";
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Einladung");
                alert.setHeaderText(null);
                alert.setContentText(conf);
                alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);
                Optional<ButtonType> result = alert.showAndWait();
                if(result.orElse(null) != buttonTypeYes) {
                    return true;
                }
                return false;
            }
        });


        Platform.runLater(query);
        try {
            return (boolean) query.get();
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Creates a popup for the inviting user when his invitation is rejected.
     */
    public void createRejectMessage(String type){
        ErrorMessage m = new ErrorMessage();
        if (type.equals("join")) {
            m.createError("Die Einladung wurde abgelehnt oder nicht beantwortet.");
        } else if (type.equals("webcam")) {
            m.createError(sender_name + " hat ihre Anfrage zur Aktivierung der Webcam abgelehnt oder ihre Anfrage ist abgelaufen.");
        }
    }

    /** Accepts the invitation and joins the user to the room.
     *
     * @param room The room to which the user is invited.
     * @param rooms The list with all rooms.
     */
    private void acceptRequest(Room room, ArrayList<Room> rooms){
        if (current_user.getCurrent_room() != null) {
            System.out.println(current_user.getCurrent_room().getId());
            current_user.getCurrent_room().leave_Room(current_user, db, current_user.getCurrent_room().getId());
        }
        try{
            for(Room r : rooms) {
                if (r.getId() == room.getId()) {
                    r.addUser(current_user);
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     *  Creates a request to the inviting user when his invitation is rejected.
     */
    private void rejectRequest(){
        try {
            db.sendRequest(current_user.getId(), sender, "reject");
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return this.sender + " sent you a " + this.type + " request";
    }

    public void createWebcamRequest(){
        Stage popup = new Stage();
        VBox vBox = new VBox();
        Label infotext = new Label(sender_name + " hat sie aufgefordert, ihre Webcam anzuschalten.");
        Label timer_l = new Label();

        Timer timer = new Timer();
        final int[] interval = {30};
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        if(interval[0] > 0) {
                            Platform.runLater(() -> timer_l.setText("Die Anfrage wird in "+ interval[0] + " Sekunden automatisch abgelehnt."));
                            interval[0]--;
                        } else {
                            try{
                                db.sendRequest(current_user.getId(), sender, "rejectwebcam");
                            } catch (SQLException s){
                                s.printStackTrace();
                            }
                            popup.close();
                            timer.cancel();
                        }
                    }
                });
            }
        }, 1000, 1000);

        HBox hbox = new HBox();
        Button accept = new Button("Annehmen");
        Button reject = new Button("Ablehnen");
        accept.setOnAction(e -> {
            current_user.activateWebcam();
            popup.close();
            timer.cancel();
        });
        reject.setOnAction(e -> {
            try{
                db.sendRequest(current_user.getId(), sender, "rejectwebcam");
            } catch (SQLException s){
                s.printStackTrace();
            }
            popup.close();
            timer.cancel();
        });

        Scene stageScene = new Scene(vBox, 300, 150);
        hbox.getChildren().addAll(accept, reject);
        vBox.getChildren().addAll(infotext, timer_l, hbox);
        popup.setScene(stageScene);
        popup.show();
    }
}
