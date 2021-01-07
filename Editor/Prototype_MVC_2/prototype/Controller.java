import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Controller implements ActionListener {

	private Editor editor;
	private RoomDrawManager roomDrawManager;

	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("New Room") && !roomDrawManager.isActive()) {
            		roomDrawManager.setActive(true);
		} else if(e.getActionCommand().equals("New Room") && roomDrawManager.isActive()) {
            		roomDrawManager.setActive(false);
		}

		if(e.getActionCommand().equals("New Building")) {
			editor.addBuilding(new Building(10, 10, 1000, 800));
		}

	}

	public void setRoomDrawManager(RoomDrawManager rdm) {
		this.roomDrawManager = rdm;
	}

	public void setEditor(Editor editor) {
		this.editor = editor;
	}

}
