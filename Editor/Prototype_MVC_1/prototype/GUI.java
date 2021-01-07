import javax.swing.*;

public class GUI {
	private JPanel pBase;
	private JButton bNewBuilding;
	private Screen screen;

	private Controller controller;

	public GUI(Controller controller, Screen screen) {
		this.controller = controller;
		this.screen = screen;
		initGUI();
	}

	private void initGUI() {
		pBase = new JPanel();
		bNewBuilding = new JButton("New Building");
		pBase.setLayout(new java.awt.BorderLayout());
		pBase.add(bNewBuilding, java.awt.BorderLayout.NORTH);
		bNewBuilding.addActionListener(controller);
		pBase.add(screen, java.awt.BorderLayout.CENTER);
	
	}

	public JPanel getBasePanel() {
		return pBase;
	}

}
