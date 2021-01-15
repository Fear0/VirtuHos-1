package wb.analyse1.GUI;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import javax.swing.JFrame;
import javax.swing.JRadioButton;
import java.awt.*;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JButton;

/**
 * the Model class that contains all the GUI objects
 * 
 * @author Frikha
 *
 */
public class View extends JFrame {
	public JRadioButton Interaktionshaufigkeit;// JRadioButton that represents Interaktionshaufigkeit
	public JRadioButton Degree_Centrality;// JRadioButton that Degree Centrality
	public JRadioButton Betweenness;// JRadioButton that represents Betweenness Centrality
	public JRadioButton EigenVektor;// JRadioButton that represents Eigen Vektor
	public JRadioButton Closeness;// JRadioButton that represents Closeness Centrality
	public JRadioButton Clique;// JRadioButton that represents CliqueID Analyse
	public JButton btnNewButton;// JButton that represents new Matrix
	public JButton toggleOnline;
	private GraphicsPainter GP;


	public GraphicsPainter getGraphicsPainter(){
		return GP;
	}

	public void setGraphicsPainter(GraphicsPainter GP){
		this.GP = GP;
	}
    /**
     * The intraktionsListner class that implements the actions of the corresponding JButton
     * @param action : the action that will take part
     */
	public void InteraktionsListener(ActionListener action) {
		this.Interaktionshaufigkeit.addActionListener(action);
	}
	 /**
     * The DegreeListener class that implements the actions of the corresponding JButton
     * @param action : that will take part
     */
	public void DegreeListener(ActionListener action) {
		this.Degree_Centrality.addActionListener(action);
	}
	 /**
     * The BetweennessListener class that implements the actions of the corresponding JButton
     * @param action : that will take part
     */
	public void BetweennessListener(ActionListener action) {
		this.Betweenness.addActionListener(action);
	}
	/**
	 * The EigenvektorListener class that implements the actions of the corresponding JButton
	 * @param action : that will take part
	 */
	public void EigenVektorListener(ActionListener action) { this.EigenVektor.addActionListener(action); }
	/**
	 * The ClosenessListener class that implements the actions of the corresponding JButton
	 * @param action : that will take part
	 */
	public void ClosenessListener(ActionListener action) { this.Closeness.addActionListener(action); }
	/**
	 * The CliqueListener class that implements the actions of the corresponding JButton
	 * @param action : that will take part
	 */
	public void CliqueListener(ActionListener action) { this.Clique.addActionListener(action); }

	 /**
    * The ChangeMatListener class that implements the actions of the corresponding JButton
    * @param action : that will take part
    */
	public void ChangeMatListener(ActionListener action) {
		this.btnNewButton.addActionListener(action);
	}
	/**
	 * The toggleonLineListener class that implements the actions of the corresponding JButton
	 * @param action : that will take part
	 */
	public void toggleOnlineListener(ActionListener action) {
		this.toggleOnline.addActionListener(action);
	}


	/**
	 * Create the frame.
	 */
	public View() {

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(500, 200, 1080, 720);
		getContentPane().setLayout(null);

		//Side menu (new Panel)

		JPanel middlePanel = new JPanel();
		middlePanel.setBorder(new LineBorder(Color.BLACK, 3));
		middlePanel.setLayout(new FlowLayout(4,4,4));
		middlePanel.setBackground(Color.BLACK);

		JPanel gridPanel = new JPanel();
		gridPanel.setBorder(new LineBorder(Color.BLACK, 3));
		gridPanel.setLayout(new GridLayout(9,2,5,5));
		gridPanel.setBackground(Color.BLACK);

		JLabel Algorithmen = new JLabel("  Algorithmen : ", SwingConstants.CENTER);
		Algorithmen.setFont(new Font("Tahoma", Font.BOLD, 13));
		Algorithmen.setForeground(Color.WHITE);
		gridPanel.add(Algorithmen);

		Interaktionshaufigkeit = new JRadioButton("Interaction frequency");
		Degree_Centrality = new JRadioButton("Degree Centrality");
		Betweenness = new JRadioButton("Betweenness Centrality");
		EigenVektor = new JRadioButton("Eigenvector Centrality");
		Closeness = new JRadioButton("Closeness Centrality");
		Clique = new JRadioButton("Clique analysis");

		btnNewButton = new JButton("show all users");
		btnNewButton.setBounds(1022,1,160,57);
		btnNewButton.setFont(new Font("Georgia",Font.BOLD,15));
		btnNewButton.setBackground(Color.lightGray);
		btnNewButton.setBorder(new LineBorder(Color.BLACK));
		btnNewButton.setForeground(Color.black);

		toggleOnline = new JButton("show online users");
		toggleOnline.setBounds(1022,71,160,57);
		toggleOnline.setFont(new Font("Georgia",Font.BOLD,15));
		toggleOnline.setBackground(Color.lightGray);
		toggleOnline.setBorder(new LineBorder(Color.BLACK));
		toggleOnline.setForeground(Color.black);

		Container mainContainer = this.getContentPane();
		mainContainer.setLayout(new BorderLayout(8,6));

		gridPanel.add(Interaktionshaufigkeit);
		//getContentPane().add(Interaktionshaufigkeit);
		gridPanel.add(Degree_Centrality);
		gridPanel.add(Betweenness);
		gridPanel.add(EigenVektor);
		gridPanel.add(Closeness);
		gridPanel.add(Clique);
		gridPanel.add(btnNewButton);
		gridPanel.add(toggleOnline);

		middlePanel.add(gridPanel, BorderLayout.CENTER);
		mainContainer.add(middlePanel,BorderLayout.WEST);
		mainContainer.isShowing();
	}

}