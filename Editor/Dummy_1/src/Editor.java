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
		screen.update();
	}

	public void modifyObject(VhosScreenObj obj) {
		if(obj == null)
			return;

		screen.update();
	}

	public VhosScreenObj getObject(int x, int y) {
		for(int i = objects.size()-1; i >= 0; i--) {
			if(objects.get(i).isInside(x, y))
				return objects.get(i);
		}
		return null;
	}

	public VhosScreenObj getSelectedObject() {
		for(int i = objects.size()-1; i >= 0; i--) {
			if(objects.get(i).getSelected())
				return objects.get(i);
		}
		return null;
	}

}