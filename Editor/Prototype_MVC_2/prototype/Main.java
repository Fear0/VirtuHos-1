import javax.swing.*;
import java.awt.*;

public class Main {
	public static void main(String[] args) {
        
		Editor editor = new Editor();
		Controller controller = new Controller();
		Screen screen = new Screen();
		GUI gui = new GUI(controller, screen);
		RoomDrawManager roomDrawManager = new RoomDrawManager();

		controller.setEditor(editor);
		screen.setEditor(editor);
		editor.setScreen(screen);
		roomDrawManager.setEditor(editor);
		controller.setRoomDrawManager(roomDrawManager);

		screen.addMouseListener(roomDrawManager);
		screen.addMouseMotionListener(roomDrawManager);

		startGUI(gui);
	}

	public static void startGUI(GUI gui) {
		JFrame frame = new JFrame();
		frame.setContentPane(gui.getBasePanel());
		frame.setTitle("VirtuHoS Editor");
		frame.setSize(500, 350);
		frame.setLocation(100, 100);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(true);
		frame.setVisible(true);
	}
}
