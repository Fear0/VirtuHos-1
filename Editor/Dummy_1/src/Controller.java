import javafx.scene.Node;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class Controller implements EventHandler<ActionEvent> {
	private RoomDrawManager roomDrawManager;
	private SelectionManager selectionManager;
	private MoveManager moveManager;
	private Editor editor;

	public Controller() {}

	public void setRoomDrawManager(RoomDrawManager roomDrawManager) {
		this.roomDrawManager = roomDrawManager;
	}

	public void setSelectionManager(SelectionManager selectionManager) {
		this.selectionManager = selectionManager;
	}

	public void setMoveManager(MoveManager moveManager) {
		this.moveManager = moveManager;
	}

	public void setEditor(Editor editor) {
		this.editor = editor;
	}

	@Override
	public void handle(ActionEvent e) {
		Object src = e.getSource();

		if(((Node)src).getId().equals("bCreate"))
			bCreateHandle();

		if(((Node)src).getId().equals("bSelect"))
			bSelectHandle();

		if(((Node)src).getId().equals("bMove"))
			bMoveHandle();

		if(((Node)src).getId().equals("bDelete"))
			bDeleteHandle();
	}

	private void bCreateHandle() {
		flip(roomDrawManager);
	}

	private void bSelectHandle() {
		flip(selectionManager);
	}

	private void bMoveHandle() {
		flip(moveManager);
	}

	private void bDeleteHandle() {
		VhosScreenObj o = editor.getSelectedObject();
		if(o == null)
			return;
		editor.removeObject(o);

	}

	private void deactivateAll() {
		roomDrawManager.setActive(false);
		selectionManager.setActive(false);
		moveManager.setActive(false);
	}

	private void flip(Manager m) {
		if(!m.getActive()) {
			deactivateAll();
			m.setActive(true);
			return;
		}
		m.setActive(false);
	}
}