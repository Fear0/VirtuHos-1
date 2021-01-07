import javafx.scene.paint.Color;

public class Room extends VhosScreenObj {

    public Room(String name, int x, int y, int width, int height) {
        super(name, x, y, width, height, Color.LIGHTGREY);
    }
    public Room (int x, int y, int width, int height) {
        this("anon_room", x, y, width, height);
    }
    public Room() {
        this("anon_room", 0, 0, 0, 0);
    }

    public static Room makeRoom(int startX, int startY, int endX, int endY) {
        int width = Math.abs(endX - startX);
        int height = Math.abs(endY - startY);
        int x = Math.min(startX, endX);
        int y = Math.min(startY, endY);

        return new Room(x, y, width, height);
    }
}