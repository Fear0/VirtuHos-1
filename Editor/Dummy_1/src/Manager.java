import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

abstract public class Manager implements EventHandler<MouseEvent> {
    private boolean active;
    private Editor editor;

    public Manager() {
        setActive(false);
    }

    public void setEditor(Editor editor) {
        this.editor = editor;
    }

    public Editor getEditor() {
        return editor;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean state) {
        this.active = state;
    }

}
