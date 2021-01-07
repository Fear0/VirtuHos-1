import javax.swing.*;

public class GUI {
	private JPanel pBase;
	private JPanel pNorth;
	private JButton bNewBuilding;
	private JButton bNewRoom;
	private Screen screen;

	private Controller controller;

	public GUI(Controller controller, Screen screen) {
		this.controller = controller;
		this.screen = screen;
		initGUI();
	}

	private void initGUI() {
		pBase = new JPanel();
		pNorth = new JPanel();
		bNewRoom = new JButton("New Room");
		bNewBuilding = new JButton("New Building");
		pNorth.add(bNewBuilding);
		pNorth.add(bNewRoom);
		pBase.setLayout(new java.awt.BorderLayout());
		pBase.add(pNorth, java.awt.BorderLayout.NORTH);
		bNewRoom.addActionListener(controller);
		bNewBuilding.addActionListener(controller);
		pBase.add(screen, java.awt.BorderLayout.CENTER);
	
	}

	public JPanel getBasePanel() {
		return pBase;
	}

}
