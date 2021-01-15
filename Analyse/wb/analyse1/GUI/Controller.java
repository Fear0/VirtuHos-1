package wb.analyse1.GUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

import javax.swing.*;

/**
 * the Controller class that will control the entire GUI
 *
 * @author Frikha
 */
public class Controller implements Observer {
    private Model Graph;//the model as an attribute
    private View View;
    private static boolean isFirstTime = true;
    private static boolean onlineOnly = false;
    private ArrayList<AbstractMap.SimpleEntry<Integer, Integer>> arrayList = new ArrayList<AbstractMap.SimpleEntry<Integer, Integer>>(); //pair for positions

    /**
     * the Controller constructor
     *
     * @param Graph :the Graph that will be controlled
     */
    public Controller(Model Graph, View view) {
        this.Graph = Graph;
        this.View = view;
        this.View.setGraphicsPainter(new GraphicsPainter(Graph.getCalc()));
        this.View.InteraktionsListener(new IntListener());
        this.View.DegreeListener(new DegListener());
        this.View.BetweennessListener(new BetweenListener());
        this.View.EigenVektorListener(new EigenListener());
        this.View.ClosenessListener(new ClosenessListener());
        this.View.CliqueListener(new CliqueListener());
        this.View.ChangeMatListener(new MatrixListener());
        this.View.toggleOnlineListener(new toggleListener());
    }

    public ArrayList<AbstractMap.SimpleEntry<Integer, Integer>> getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList<AbstractMap.SimpleEntry<Integer, Integer>> arrayList) {
        this.arrayList = arrayList;
    }
    /*public void changeModel(Model model) {
        this.Graph = model;
        this.View.setGraphicsPainter(new GraphicsPainter(Graph.getCalc()));
    }*/

    /**
     * this method make the right calculation for the corresponding mode
     *
     * @param mode :Interaktionshaufigkeit, degree centrality , betweenness centrality
     */
    public void calculate(int mode) {
        if (Graph.getCalc().Matrix_getter().length == 0){
            JOptionPane.showMessageDialog(this.View, "No users. Please Wait for new users, the interface will update itself automatically.");
            return;
        }
        if (Graph.getCalc().Matrix_getter()[0].length == 0) {
            JOptionPane.showMessageDialog(this.View, "No users on Server. Please Wait for new users, the interface will update itself automatically.");
            return;
        }
        //this.View.setGraphicsPainter(new GraphicsPainter(Graph.getCalc()));
        if (!isFirstTime) {
            View.remove(View.getGraphicsPainter());
            /*System.out.println("ok");
            View.revalidate();
            View.repaint();*/

            int k = arrayList.size();
            for (int b = k; b < Graph.getCalc().Size_getter(); b++) {
                arrayList.add(new AbstractMap.SimpleEntry<>(Graph.getCalc().Employee_Getter()[b].getX(), Graph.getCalc().Employee_Getter()[b].getY()));
            }
            GraphicsPainter gr = new GraphicsPainter(Graph.getCalc(), this.arrayList);
            if (onlineOnly) {
                gr.setSwitchOnline(true);

            }
                View.setGraphicsPainter(gr);
            /*for (int i = 0; i < Graph.getCalc().arrNodeWeight.length; i++) {
                System.out.println(Graph.getCalc().arrNodeWeight[i]);
            }*/

        }
            if (mode == 0) {
                View.getGraphicsPainter().calc.calculateInteraction();
                View.getGraphicsPainter().strategy = 0;
            }
            if (mode == 1) {
                View.getGraphicsPainter().calc.calculateDegree();
                View.getGraphicsPainter().strategy = 1;
            }
            if (mode == 2) {
                View.getGraphicsPainter().calc.calculateBetweeniss();
                View.getGraphicsPainter().strategy = 2;
            }
            if (mode == 3) {
                View.getGraphicsPainter().calc.calculateEigenVektor();
                View.getGraphicsPainter().strategy = 3;
            }
            if (mode == 4) {
                View.getGraphicsPainter().calc.calculateCloseness();
                for (int i = 0; i < Graph.getCalc().arrNodeWeight.length; i++) {
                    //System.out.println(Graph.getCalc().arrNodeWeight[i]);
                }
                View.getGraphicsPainter().strategy = 4;
            }
            if (mode == 5) {
                View.getGraphicsPainter().calc.calculateClique();
                View.getGraphicsPainter().strategy = 5;
            }
        /*} else {

                for (int b = 0; b < Graph.getCalc().Size_getter(); b++) {
                    arrayList.add(new AbstractMap.SimpleEntry<>(Graph.getCalc().Employee_Getter()[b].getX(), Graph.getCalc().Employee_Getter()[b].getY()));
                }
                GraphicsPainter gr = new GraphicsPainter(Graph.getCalc(), this.arrayList);
                this.View.setGraphicsPainter(gr);
            GraphicsPainter GP = new GraphicsPainter(Graph.getCalc());
            this.View.setGraphicsPainter(GP);
            if (mode == 0) {
                View.getGraphicsPainter().calc.calculateInteraction();
                View.getGraphicsPainter().strategy = 0;
            }
            if (mode == 1) {
                View.getGraphicsPainter().calc.calculateDegree();
                View.getGraphicsPainter().strategy = 1;
            }
            if (mode == 2) {
                View.getGraphicsPainter().calc.calculateBetweeniss();
                View.getGraphicsPainter().strategy = 2;
            }
            if (mode == 3) {
                View.getGraphicsPainter().calc.calculateEigenVektor();
                View.getGraphicsPainter().strategy = 3;
            }
            if (mode == 4) {
                View.getGraphicsPainter().calc.calculateCloseness();
                for (int i = 0; i < Graph.getCalc().arrNodeWeight.length; i++) {
                    //System.out.println(Graph.getCalc().arrNodeWeight[i]);
                }
                View.getGraphicsPainter().strategy = 4;
            }
            if (mode == 5) {
                View.getGraphicsPainter().calc.calculateClique();
                View.getGraphicsPainter().strategy = 5;
            }

            /*isFirstTime = false;
            View.revalidate();
            View.repaint();
        }*/
        View.getGraphicsPainter().setBounds(10, 10, 1465, 1000);
        View.add(View.getGraphicsPainter());
        View.revalidate();
        View.repaint();
        isFirstTime = false;
    }

    public Calculation ChangeDistance(Calculation calc) {
        GUIUser[] User = calc.Employee_Getter();
        int k = User.length;
        int[] x = new int[k];
        int[] y = new int[k];

        for (int i = 0; i < User.length; i++) {
            x[i] = User[i].getX();
            y[i] = User[i].getY();
        }
        boolean spaced = false;
        while (!spaced) {
            int a = 0;
            for (int j = 0; j < k; j++) {
                for (int z = j + 1; z < k; z++) {
                    int a1 = x[j] - x[z];
                    int a2 = y[j] - y[z];
                    a1 = Math.abs(a1);
                    a2 = Math.abs(a2);
                    if (a1 < 70 && a2 < 70) a++;
                }
            }
            if (a == 0) spaced = true;
            else {
                for (int w = 0; w < k; w++) {
                    x[w] = (new Random().nextInt(1000));
                    y[w] = (new Random().nextInt(400));
                }
            }
        }
        for (int g = 0; g < User.length; g++) {
            User[g].setXY(x[g], y[g]);
        }
        calc.setEmployee(User);
        return calc;
    }

    public Calculation changeCoordinates(Calculation calc) {
        GUIUser[] User = calc.Employee_Getter();
        for (int g = 0; g < User.length; g++) {
            User[g].setXY(new Random().nextInt(1000), new Random().nextInt(400));
        }
        calc.setEmployee(User);
        return calc;
    }


    @Override
    public void update(Observable o, Object arg) {
        Calculation calc = (Calculation) arg;
        calc = ChangeDistance(calc);
        boolean spacedGraph = false;
        if (arrayList.size() > 0) {
            int k = arrayList.size();
            while (!spacedGraph) {
                int t = 0;
                GUIUser[] users = calc.Employee_Getter();
                System.out.println(users.toString());
                for (int pp = 0; pp < k; pp++) {
                    users[pp].setXY(arrayList.get(pp).getKey(), arrayList.get(pp).getValue());
                }
                System.out.println(users.toString());


                calc.setEmployee(users);
                for (int u = 0; u < calc.Size_getter(); u++) {
                    for (int w = u + 1; w < calc.Size_getter(); w++) {
                        int x0 = Math.abs(calc.Employee_Getter()[w].getX() - calc.Employee_Getter()[u].getX());
                        int y0 = Math.abs(calc.Employee_Getter()[w].getY() - calc.Employee_Getter()[u].getY());
                        if (x0 < 70 && y0 < 70) t++;
                    }
                }
                if (t > 0) {
                    calc = changeCoordinates(calc);
                    calc = ChangeDistance(calc);
                } else {
                    spacedGraph = true;
                }
            }
        }
        this.Graph = new Model(calc);
        if (!isFirstTime) {
            View.remove(View.getGraphicsPainter());
            /*System.out.println("ok");
            View.revalidate();
            View.repaint();*/
            int k = arrayList.size();

            for (int b = k; b < calc.Size_getter(); b++) {
                arrayList.add(new AbstractMap.SimpleEntry<>(calc.Employee_Getter()[b].getX(), calc.Employee_Getter()[b].getY()));
            }
            GraphicsPainter gr = new GraphicsPainter(Graph.getCalc(), this.arrayList);
            View.setGraphicsPainter(gr);
            if (View.Interaktionshaufigkeit.isSelected()) {
                calculate(0);
            }
            if (View.Degree_Centrality.isSelected()) {
                calculate(1);
                System.out.println(Arrays.deepToString(Graph.getCalc().Matrix_getter()));
            }
            if (View.Betweenness.isSelected()) {
                calculate(2);
            }
            if (View.EigenVektor.isSelected()) {
                calculate(3);
            }
            if (View.Closeness.isSelected()) {
                calculate(4);
            }
            if (View.Clique.isSelected()) {
                calculate(5);
            }
            /*for (int i = 0; i < Graph.getCalc().arrNodeWeight.length; i++) {
                System.out.println(Graph.getCalc().arrNodeWeight[i]);
            }*/
        } else {

            for (int b = 0; b < Graph.getCalc().Size_getter(); b++) {
                arrayList.add(new AbstractMap.SimpleEntry<>(Graph.getCalc().Employee_Getter()[b].getX(), Graph.getCalc().Employee_Getter()[b].getY()));
            }
            GraphicsPainter gr = new GraphicsPainter(Graph.getCalc(), this.arrayList);
            this.View.setGraphicsPainter(gr);
            /*GraphicsPainter GP = new GraphicsPainter(Graph.getCalc());
            this.View.setGraphicsPainter(GP);*/
            if (View.Interaktionshaufigkeit.isSelected()) {
                calculate(0);
            }
            if (View.Degree_Centrality.isSelected()) {
                calculate(1);
                System.out.println(Arrays.deepToString(Graph.getCalc().Matrix_getter()));
            }
            if (View.Betweenness.isSelected()) {
                calculate(2);
            }
            if (View.EigenVektor.isSelected()) {
                calculate(3);
            }
            if (View.Closeness.isSelected()) {
                calculate(4);
            }
            if (View.Clique.isSelected()) {
                calculate(5);
            }
        }
        isFirstTime = false;
        View.revalidate();
        View.repaint();
    }

    /**
     * the Intlistener class that will implement the ActionsListener for Interaktionshaufigkeit JRadioButton
     *
     * @author Frikha
     */
    class IntListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            View.Degree_Centrality.setSelected(false);
            View.Betweenness.setSelected(false);
            View.EigenVektor.setSelected(false);
            View.Closeness.setSelected(false);
            View.Clique.setSelected(false);
            View.Interaktionshaufigkeit.setForeground(Color.blue);
            View.Degree_Centrality.setForeground(Color.BLACK);
            View.Betweenness.setForeground(Color.BLACK);
            View.EigenVektor.setForeground(Color.BLACK);
            View.Clique.setForeground(Color.BLACK);
            View.Closeness.setForeground(Color.BLACK);
            calculate(0);
        }

    }

    /**
     * the DegListener class that will implement the ActionsListener for Degree centrality JRadioButton
     *
     * @author Frikha
     */
    class DegListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            View.Interaktionshaufigkeit.setSelected(false);
            View.Betweenness.setSelected(false);
            View.EigenVektor.setSelected(false);
            View.Closeness.setSelected(false);
            View.Clique.setSelected(false);
            View.Interaktionshaufigkeit.setForeground(Color.BLACK);
            View.Degree_Centrality.setForeground(Color.blue);
            View.Betweenness.setForeground(Color.BLACK);
            View.EigenVektor.setForeground(Color.BLACK);
            View.Clique.setForeground(Color.BLACK);
            View.Closeness.setForeground(Color.BLACK);
            calculate(1);
        }

    }

    /**
     * the BetweenListener class that will implement the ActionsListener for Betweenness centrality JRadioButton
     *
     * @author Frikha
     */
    class BetweenListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            View.Interaktionshaufigkeit.setSelected(false);
            View.Degree_Centrality.setSelected(false);
            View.EigenVektor.setSelected(false);
            View.Closeness.setSelected(false);
            View.Clique.setSelected(false);
            View.Interaktionshaufigkeit.setForeground(Color.BLACK);
            View.Degree_Centrality.setForeground(Color.BLACK);
            View.Betweenness.setForeground(Color.blue);
            View.EigenVektor.setForeground(Color.BLACK);
            View.Clique.setForeground(Color.BLACK);
            View.Closeness.setForeground(Color.BLACK);
            calculate(2);
        }

    }

    class EigenListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            View.Interaktionshaufigkeit.setSelected(false);
            View.Degree_Centrality.setSelected(false);
            View.Betweenness.setSelected(false);
            View.Closeness.setSelected(false);
            View.Clique.setSelected(false);
            View.Interaktionshaufigkeit.setForeground(Color.BLACK);
            View.Degree_Centrality.setForeground(Color.BLACK);
            View.Betweenness.setForeground(Color.BLACK);
            View.EigenVektor.setForeground(Color.blue);
            View.Clique.setForeground(Color.BLACK);
            View.Closeness.setForeground(Color.BLACK);
            calculate(3);
        }
    }

    class ClosenessListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            View.Interaktionshaufigkeit.setSelected(false);
            View.Degree_Centrality.setSelected(false);
            View.Betweenness.setSelected(false);
            View.EigenVektor.setSelected(false);
            View.Clique.setSelected(false);
            View.Interaktionshaufigkeit.setForeground(Color.BLACK);
            View.Degree_Centrality.setForeground(Color.BLACK);
            View.Betweenness.setForeground(Color.BLACK);
            View.EigenVektor.setForeground(Color.BLACK);
            View.Clique.setForeground(Color.BLACK);
            View.Closeness.setForeground(Color.blue);

            calculate(4);
        }
    }

    class CliqueListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            View.Interaktionshaufigkeit.setSelected(false);
            View.Degree_Centrality.setSelected(false);
            View.Betweenness.setSelected(false);
            View.Closeness.setSelected(false);
            View.EigenVektor.setSelected(false);
            View.Interaktionshaufigkeit.setForeground(Color.BLACK);
            View.Degree_Centrality.setForeground(Color.BLACK);
            View.Betweenness.setForeground(Color.BLACK);
            View.EigenVektor.setForeground(Color.BLACK);
            View.Clique.setForeground(Color.blue);
            View.Closeness.setForeground(Color.BLACK);
            calculate(5);
        }
    }

    class toggleListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

            onlineOnly = true;

            if (View.Interaktionshaufigkeit.isSelected()) {
                calculate(0);
            }
            if (View.Degree_Centrality.isSelected()) {
                calculate(1);
                //System.out.println(Arrays.deepToString(Graph.getCalc().Matrix_getter()));
            }
            if (View.Betweenness.isSelected()) {
                calculate(2);
            }
            if (View.EigenVektor.isSelected()) {
                calculate(3);
            }
            if (View.Closeness.isSelected()) {
                calculate(4);
            }
            if (View.Clique.isSelected()) {
                calculate(5);
            }
        }
    }

    /**
     * the MatrixListener class that will implement the ActionsListener for New Matrix JButton
     *
     * @author Frikha
     */
    class MatrixListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {


            onlineOnly = false;
            if (View.Interaktionshaufigkeit.isSelected()) {
                calculate(0);
            }
            if (View.Degree_Centrality.isSelected()) {
                calculate(1);
                System.out.println(Arrays.deepToString(Graph.getCalc().Matrix_getter()));
            }
            if (View.Betweenness.isSelected()) {
                calculate(2);
            }
            if (View.EigenVektor.isSelected()) {
                calculate(3);
            }
            if (View.Closeness.isSelected()) {
                calculate(4);
            }
            if (View.Clique.isSelected()) {
                calculate(5);
            }

        }
    }

}
