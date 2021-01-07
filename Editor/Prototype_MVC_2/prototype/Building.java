import java.awt.*;

public class Building extends VhosScreenObj {

	private Color clr = Color.darkGray;

	public Building(String name, int x, int y, int width, int height) {
		super(name, x, y, width, height);
	}
	public Building (int x, int y, int width, int height) {
		this("anon_building", x, y, width, height);
	}
	public Building() {
		this("anon_building", 0, 0, 0, 0);
	}

	public Color getColor() {
		return clr;
	}

}
