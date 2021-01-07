import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import java.util.ArrayList;

public class Screen {

	private Pane canvas;
	private Editor editor;

	public Screen() {}

	public void initScreen(Pane GUI) {
		canvas = (Pane)GUI.lookup("#Screen");
	}

	public void setEditor(Editor editor) {
		this.editor = editor;
	}

	public void update() {
		canvas.getChildren().clear();
		drawObjects(editor.getObjects());

	}

	public void drawObjects(ArrayList<VhosScreenObj> screenObjects) {
		for(int i = 0; i < screenObjects.size(); i++) {
			VhosScreenObj o = screenObjects.get(i);
			Rectangle rect = new Rectangle(o.getX(), o.getY(), o.getWidth(), o.getHeight());
			rect.setFill(Color.TRANSPARENT);
			rect.setStroke(o.getColor());
			rect.setStrokeWidth(5);

			canvas.getChildren().add(rect);
		}
	}

}