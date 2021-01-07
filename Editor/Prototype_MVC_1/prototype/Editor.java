import java.util.ArrayList;

public class Editor {
	Screen screen;
	ArrayList<VhosScreenObj> objects = new ArrayList<>();

	public void setScreen(Screen screen) {
		this.screen = screen;
	}

	public void addObject(VhosScreenObj obj) {
		objects.add(obj);
		screen.update();
	}

	public ArrayList<VhosScreenObj> getObjects() {
		return objects;
	}

}