import javafx.scene.input.MouseEvent;

public class RoomDrawManager extends Manager {
    private int startX;
    private int startY;
    Room temp;

    public RoomDrawManager() {}

    @Override
    public void setActive(boolean state) {
        super.setActive(state);
        startX = 0;
        startY = 0;
    }

    @Override
    public void handle(MouseEvent e) {
        if(!getActive())
            return;

        if(e.getEventType() == MouseEvent.MOUSE_PRESSED)
            mousePressed(e);

        if(e.getEventType() == MouseEvent.MOUSE_RELEASED)
            mouseReleased(e);

        if(e.getEventType() == MouseEvent.MOUSE_DRAGGED)
            mouseDragged(e);
    }

    public void mousePressed(MouseEvent e) {
        startX = (int)e.getX();
        startY = (int)e.getY();
    }

    public void mouseReleased(MouseEvent e) {
        getEditor().removeObject(temp);
        getEditor().addRoom(Room.makeRoom(startX, startY, (int)e.getX(), (int)e.getY()));
    }

    public void mouseDragged(MouseEvent e) {
        getEditor().removeObject(temp);
        temp = Room.makeRoom(startX, startY, (int)e.getX(), (int)e.getY());
        getEditor().addRoom(temp);
    }

}
