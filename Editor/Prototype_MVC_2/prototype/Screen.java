import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Screen extends JPanel {

	Editor editor;
	ArrayList<VhosScreenObj> objects;

	public void setEditor(Editor editor) {
		this.editor = editor;
		objects = editor.getObjects();
	}

	private void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;

		RenderingHints rh = new RenderingHints(
			RenderingHints.KEY_ANTIALIASING,
			RenderingHints.VALUE_ANTIALIAS_ON);

		rh.put(RenderingHints.KEY_RENDERING,
			RenderingHints.VALUE_RENDER_QUALITY);

		g2d.setRenderingHints(rh);

		drawObjects(objects, g2d);

		g2d.dispose();
	}

	private void drawObjects(ArrayList<VhosScreenObj> objects, Graphics2D g) {
		for(int i = 0; i < objects.size(); i++)
			drawObject(objects.get(i), g);
	}

	private void drawObject(VhosScreenObj obj, Graphics2D g) {
		if(obj instanceof Building)
			g.setPaint(((Building)obj).getColor());
		if(obj instanceof Room)
			g.setPaint(((Room)obj).getColor());

		BasicStroke bs1 = new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL);
		g.setStroke(bs1);
		g.drawRect(obj.getX(), obj.getY(), obj.getWidth(), obj.getHeight());

	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}

	public void update() {
		objects = editor.getObjects();
		repaint();
	}

}
