import java.util.ArrayList;

public class Editor {
	Screen screen;
	ArrayList<VhosScreenObj> objects = new ArrayList<>();

	public void setScreen(Screen screen) {
		this.screen = screen;
	}
 
	public void addBuilding(Building building) {
		objects.add(building);
		screen.update();
	}

	public void addRoom(VhosScreenObj o) {
		objects.add(o);
		screen.update();
	}

	public ArrayList<VhosScreenObj> getObjects() {
		return objects;
	}

	public void removeObject(VhosScreenObj obj) {
		if(obj == null)
			return;

		objects.remove(obj);
	}

}