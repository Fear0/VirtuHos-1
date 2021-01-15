package wb.analyse1.GUI;

import javax.swing.border.LineBorder;
import java.awt.*;
import javax.swing.JFrame;
import javax.swing.JRadioButton;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
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
     * The intraktionsListner class that implements the actions of the corresponding JRadioButton
     * @param action : the action that will take part
     */
	public void InteraktionsListener(ActionListener action) {
		this.Interaktionshaufigkeit.addActionListener(action);
	}
	 /**
     * The DegreeListener class that implements the actions of the corresponding JRadioButton
     * @param action : that will take part
     */
	public void DegreeListener(ActionListener action) {
		this.Degree_Centrality.addActionListener(action);
	}
	 /**
     * The BetweennessListener class that implements the actions of the corresponding JRadioButton
     * @param action : that will take part
     */
	public void BetweennessListener(ActionListener action) {
		this.Betweenness.addActionListener(action);
	}
	/**
	 * The EigenvektorListener class that implements the actions of the corresponding JRadioButton
	 * @param action : that will take part
	 */
	public void EigenVektorListener(ActionListener action) { this.EigenVektor.addActionListener(action); }
	/**
	 * The ClosenessListener class that implements the actions of the corresponding JRadioButton
	 * @param action : that will take part
	 */
	public void ClosenessListener(ActionListener action) { this.Closeness.addActionListener(action); }
	/**
	 * The CliqueListener class that implements the actions of the corresponding JRadioButton
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

		JLabel Algorithmen = new JLabel("Algorithmen :    ");
		Algorithmen.setBounds(171, 9, 108, 16);
		Algorithmen.setFont(new Font("Tahoma", Font.BOLD, 13));

		getContentPane().add(Algorithmen);

		Interaktionshaufigkeit = new JRadioButton("Interaction frequency");

		Interaktionshaufigkeit.setBounds(284, 5, 177, 25);

		Interaktionshaufigkeit.setFont(new Font("Tahoma", Font.BOLD, 13));
		getContentPane().add(Interaktionshaufigkeit);

		Degree_Centrality = new JRadioButton("Degree Centrality");
		Degree_Centrality.setBounds(466, 5, 145, 25);

		Degree_Centrality.setFont(new Font("Tahoma", Font.BOLD, 13));
		getContentPane().add(Degree_Centrality);

		Betweenness = new JRadioButton("Betweenness Centrality");
		Betweenness.setBounds(616, 5, 185, 25);

		Betweenness.setFont(new Font("Tahoma", Font.BOLD, 13));
		getContentPane().add(Betweenness);

		EigenVektor = new JRadioButton("Eigenvector Centrality");
		EigenVektor.setBounds(805, 5, 185, 25);

		EigenVektor.setFont(new Font("Tahoma", Font.BOLD, 13));
		getContentPane().add(EigenVektor);

		Closeness = new JRadioButton("Closeness Centrality");
		Closeness.setBounds(375, 28, 185, 25);

		Closeness.setFont(new Font("Tahoma", Font.BOLD, 13));
		getContentPane().add(Closeness);

		Clique = new JRadioButton("Clique analysis");
		Clique.setBounds(565, 28, 185, 25);

		Clique.setFont(new Font("Tahoma", Font.BOLD, 13));
		getContentPane().add(Clique);

		btnNewButton = new JButton("show all users");
		btnNewButton.setBounds(1022,1,160,57);
		btnNewButton.setFont(new Font("Georgia",Font.BOLD,15));
		btnNewButton.setBackground(Color.lightGray);
		btnNewButton.setBorder(new LineBorder(Color.BLACK));
		btnNewButton.setForeground(Color.black);
		/*btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});*/
		//btnNewButton.setBounds(922, 200, 200, 57);
		//panel.add(toggleOnline);
		toggleOnline = new JButton("show online users");
		toggleOnline.setBounds(1022,71,160,57);
		toggleOnline.setFont(new Font("Georgia",Font.BOLD,15));
		toggleOnline.setBackground(Color.lightGray);
		toggleOnline.setBorder(new LineBorder(Color.BLACK));
		toggleOnline.setForeground(Color.black);

		getContentPane().add(btnNewButton);
		getContentPane().add(toggleOnline);
		this.setBackground(Color.GRAY);

	}


}