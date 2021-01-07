import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Controller implements ActionListener {

	private Editor editor;

	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("New Building"))
			editor.addObject(new Building(5, 5, 100, 50));

	}

	public void setEditor(Editor editor) {
		this.editor = editor;
	}

}
