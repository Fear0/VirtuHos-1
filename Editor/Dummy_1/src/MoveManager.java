import javafx.scene.input.MouseEvent;

public class MoveManager extends Manager {
    private VhosScreenObj temp;
    private int offsetX;
    private int offsetY;

    @Override
    public void setActive(boolean active) {
        super.setActive(active);
        temp = null;
        offsetX = 0;
        offsetY = 0;
    }

    @Override
    public void handle(MouseEvent e) {
        if(!getActive())
            return;

        if(e.getEventType() == MouseEvent.MOUSE_DRAGGED)
            mouseDragged(e);
        if(e.getEventType() == MouseEvent.MOUSE_PRESSED)
            mousePressed(e);
        if(e.getEventType() == MouseEvent.MOUSE_RELEASED)
            mouseReleased(e);
    }

    public void mouseDragged(MouseEvent e) {
        if(temp == null)
            return;

        int newX = (int)e.getX()-offsetX;
        int newY = (int)e.getY()-offsetY;
        temp.setX(newX);
        temp.setY(newY);

        getEditor().modifyObject(temp);
    }
    public void mousePressed(MouseEvent e) {
        VhosScreenObj o = getEditor().getObject((int)e.getX(), (int)e.getY());
        if(o == null)
            return;
        offsetX = (int)e.getX() - o.getX();
        offsetY = (int)e.getY() - o.getY();

        temp = o;
    }
    public void mouseReleased(MouseEvent e) {
        temp = null;
        offsetX = 0;
        offsetY = 0;
    }
}
