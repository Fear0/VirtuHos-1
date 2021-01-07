import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class SelectionManager extends Manager {
    VhosScreenObj temp;

    public SelectionManager() {}

    @Override
    public void handle(MouseEvent e) {
        if(!getActive())
            return;

        if(e.getEventType() == MouseEvent.MOUSE_MOVED)
            mouseMoved(e);
        if(e.getEventType() == MouseEvent.MOUSE_CLICKED)
            mouseClicked(e);
    }

    @Override
    public void setActive(boolean active) {
        super.setActive(active);

        temp = null;
    }

    public void mouseMoved(MouseEvent e) {
        if(temp != null) {
            unmark(temp);
            getEditor().modifyObject(temp);
        }

        VhosScreenObj o = getEditor().getObject((int)e.getX(), (int)e.getY());
        if(o == null)
            return;

        temp = o;
        mark(temp);
        getEditor().modifyObject(temp);
    }

    private void mark(VhosScreenObj obj) {
        if(obj.getSelected())
            return;
        obj.setColor(Color.DARKGRAY);
    }
    private void unmark(VhosScreenObj obj) {
        if(obj.getSelected())
            return;
        obj.setColor(Color.LIGHTGREY);
    }

    public void mouseClicked(MouseEvent e) {
        VhosScreenObj clicked = getEditor().getObject((int)e.getX(), (int)e.getY());
        VhosScreenObj selected = getEditor().getSelectedObject();

        if(clicked == null || (clicked == selected)) {
            if(selected != null) {
                selected.setSelected(false);
                getEditor().modifyObject(selected);
            }
            return;
        }
        
        if(selected != null)
            selected.setSelected(false);
        clicked.setSelected(true);
        getEditor().modifyObject(clicked);

    }

}
