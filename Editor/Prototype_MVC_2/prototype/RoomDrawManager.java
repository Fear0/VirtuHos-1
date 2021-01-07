import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class RoomDrawManager implements MouseListener, MouseMotionListener {
    private boolean active;
    private int startX;
    private int startY;
    Room temp;

    private Editor editor;

    public RoomDrawManager() {
        setActive(false);

    }

    public void setEditor(Editor editor) {
        this.editor = editor;
    }

    public void setActive(boolean state) {
        this.active = state;
        startX = 0;
        startY = 0;
    }

    public boolean isActive() {
        return active;
    }

    public void mousePressed(MouseEvent e) {
        if(!isActive())
            return;

        startX = e.getX();
        startY = e.getY();
    }

    public void mouseReleased(MouseEvent e) {
        if(!isActive())
            return;

        editor.addRoom(Room.makeRoom(startX, startY, e.getX(), e.getY()));
    }

    public void mouseDragged(MouseEvent e) {
        if(!isActive())
            return;

        editor.removeObject(temp);
        temp = Room.makeRoom(startX, startY, e.getX(), e.getY());
        editor.addRoom(temp);
    }

    public void mouseMoved(MouseEvent e) {

    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {

    }

    public void mouseClicked(MouseEvent e) {

    }


}
