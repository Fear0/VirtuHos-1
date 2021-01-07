import javafx.scene.paint.Color;

public abstract class VhosScreenObj extends VhosObj {
	private int x;
	private int y;
	private int width;
	private int height;
	private Color color;
	private boolean selected;

	public VhosScreenObj(String name, int x, int y, int width, int height, Color color) {
		super(name);
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.color = color;
		setSelected(false);
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public boolean isInside(int x, int y) {
		return 	x > getX() && x < (getX() + getWidth()) &&
				y > getY() && y < (getY() + getHeight());
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
		if(getSelected())
			setColor(Color.DARKSEAGREEN);
		else
			setColor(Color.LIGHTGREY);
	}

	public boolean getSelected() {
		return selected;
	}

	public int getArea() {
		return getWidth() * getHeight();
	}
}
