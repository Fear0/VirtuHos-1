import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Button;

public class Main extends Application { 
	@Override
	public void start(Stage stage) throws Exception {
		Pane GUI = loadGUI();
		Controller controller = new Controller();
		Editor editor = new Editor();
		Screen screen = new Screen();
		RoomDrawManager roomDrawManager = new RoomDrawManager();
		SelectionManager selectionManager = new SelectionManager();
		MoveManager moveManager = new MoveManager();

		initControls(GUI, controller, roomDrawManager, selectionManager, moveManager);

		controller.setRoomDrawManager(roomDrawManager);
		controller.setSelectionManager(selectionManager);
		controller.setMoveManager(moveManager);
		controller.setEditor(editor);
		editor.setScreen(screen);
		screen.initScreen(GUI);
		screen.setEditor(editor);
		roomDrawManager.setEditor(editor);
		selectionManager.setEditor(editor);
		moveManager.setEditor(editor);

		startGUI(GUI, stage);
	}

	private void initControls(Pane GUI, Controller controller,
							  RoomDrawManager roomDrawManager,
							  SelectionManager selectionManager,
							  MoveManager moveManager) {
		ToggleButton bCreate = (ToggleButton)GUI.lookup("#bCreate");
		ToggleButton bSelect = (ToggleButton)GUI.lookup("#bSelect");
		ToggleButton bMove = (ToggleButton)GUI.lookup("#bMove");
		Button bDelete = (Button)GUI.lookup("#bDelete");
		bCreate.setOnAction(controller);
		bSelect.setOnAction(controller);
		bMove.setOnAction(controller);
		bDelete.setOnAction(controller);

		ToggleGroup toggleGroup = new ToggleGroup();
		bCreate.setToggleGroup(toggleGroup);
		bSelect.setToggleGroup(toggleGroup);
		bMove.setToggleGroup(toggleGroup);

		Pane screen = (Pane)GUI.lookup("#Screen");
		screen.setOnMousePressed(roomDrawManager);
		screen.setOnMouseReleased(roomDrawManager);
		screen.setOnMouseDragged(roomDrawManager);
		screen.setOnMouseMoved(selectionManager);
		screen.setOnMouseClicked(selectionManager);
		screen.addEventHandler(MouseEvent.MOUSE_DRAGGED, moveManager);
		screen.addEventHandler(MouseEvent.MOUSE_PRESSED, moveManager);
		screen.addEventHandler(MouseEvent.MOUSE_RELEASED, moveManager);
	}

	private void startGUI(Pane GUI, Stage stage) {
		stage.setTitle("VirtuHoS Editor");
		stage.setX(100);
		stage.setY(200);
		stage.setScene(new Scene(GUI, 700, 600));
		stage.show();
	}

	private Pane loadGUI() throws IOException {
		return FXMLLoader.load(getClass().getResource("GUI.fxml"));
	}

	public static void main(String[] args) {
		Application.launch(Main.class, args);
	}
}