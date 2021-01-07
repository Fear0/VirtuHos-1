import javafx.scene.paint.Color;

public class Building extends VhosScreenObj {

	public Building(String name, int x, int y, int width, int height) {
		super(name, x, y, width, height, Color.DARKGRAY);
	}
	public Building (int x, int y, int width, int height) {
		this("anon_building", x, y, width, height);
	}
	public Building() {
		this("anon_building", 0, 0, 0, 0);
	}

}
