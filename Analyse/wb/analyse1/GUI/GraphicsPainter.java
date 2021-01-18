package wb.analyse1.GUI;

import definitions.DatabaseCommunication;
import definitions.Person;
import javafx.util.Pair;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.sql.SQLOutput;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

/**
 * creates the View of the application
 * The Network (Nodes and Edges)
 */

public class GraphicsPainter extends JPanel {

    Calculation calc;
    int[] positionsOnline;
    //String building;
    Color[] CliqueColors = {Color.BLUE, Color.GREEN, Color.RED, Color.PINK, Color.CYAN, Color.MAGENTA};
    private ArrayList<Pair<String, Pair<Integer,Integer>>> arrayList = new ArrayList<>();

    boolean switchOnline = false;


    int strategy = 0; // 0 for edges and 1 for nodes

    public void setSwitchOnline(boolean switchOnline) {
        this.switchOnline = switchOnline;
    }

    /**
     * Create the panel.
     */


    public List<VertexPoint> verticies;


    public List<String> getIDSinBuilding(){
        List<String> ids = new ArrayList<>();
        List<Person> persons =  DatabaseCommunication.getPersons(this.calc.getBuilding());
        System.out.println(this.calc.getBuilding());
        System.out.println(persons.size());
        System.out.println("Persons: ");
        for (Person person : persons){
            ids.add(person.getId());
            System.out.println(person);
        }
        return ids;
    }

    public void setRealPositions(){

        List<Person> persons = DatabaseCommunication.getPersons(this.calc.getBuilding());
        System.out.println("getting positions");
        for (int i = 0; i < this.arrayList.size(); i++){
            for (Person person : persons){
                if ( person.getId().equals(this.arrayList.get(i).getKey())){
                    Integer x = Math.toIntExact(Math.round(person.getX()));
                    Integer y = Math.toIntExact(Math.round(person.getY()));
                    Pair<String, Pair<Integer,Integer>> pair = new Pair<String,Pair<Integer,Integer>>(person.getId(),new Pair<Integer,Integer>(x,y));
                    this.arrayList.set(i,pair);
                    System.out.println(this.arrayList.get(i).getValue().getKey() +", " + pair.getValue().getValue());
                }
            }

        }

    }
    @Override
    public String getToolTipText(MouseEvent event) { // names will be displayed by mouse hover
        for (VertexPoint vp : verticies) {
            if (vp.contains(event.getPoint())) {
                int k=arrayList.size();
                for(int i=0;i<k;i++){
                    if(arrayList.get(i).getValue().getKey() + 120 == vp.getVertex().getPoint().getX()){
                        return (i+ 1) + ". " + calc.Employee_Getter()[i].getName();
                    }
                }
            }
        }
        return null;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        System.out.println("Drawing:");
        Graphics2D g2d = (Graphics2D) g.create();
        GUIUser[] Employee = calc.Employee_Getter();
        System.out.println("server users id:");
        for (GUIUser guiUser : Employee) {
            System.out.println(guiUser.getUser().getId());
        }
        int[][] Matrix_EXAMPLE1 = calc.Matrix_getter();
        List<String> ids = getIDSinBuilding();
        List<String> ids2 = new ArrayList<>();
        ids2.add("w_tciwatgsppoa");
        ids2.add("0002");
        setRealPositions();
        System.out.println("ids from database: ");
        ids.forEach(n -> System.out.println(n));
        for (VertexPoint vertex : verticies) {
            g2d.fill(vertex);
            g2d.setColor(Color.white);
        }

        if (this.strategy == 5) {
            // String[] CliqueIds= {"1", "1,2" ,"1,2" ,"2", "3","3" ,"3"};
            String[] CliqueIds = new String[calc.Size_getter()];
            for (int k = 0; k < calc.Size_getter(); k++) {
                CliqueIds[k] = calc.Employee_Getter()[k].CliqueGetter();
            }

           /* int[][] Matrix_EXAMPLE1 = calc.Matrix_getter();
            GUIUser[] Employee = calc.Employee_Getter();*/


            for (int j = 0; j < Matrix_EXAMPLE1.length; j++) { //draw edges
                if (!ids2.contains(Employee[j].getUser().getId())) {
                    continue;
                }
                for (int v = 0; v < Matrix_EXAMPLE1.length; v++) {
                    int h = Matrix_EXAMPLE1[v][j];
                    if (h > 0) {
                        Graphics2D gg = (Graphics2D) g;
                        Stroke stroke;
                        stroke = new BasicStroke(3.0f);
                        gg.setStroke(stroke);
                        if (!switchOnline) {
                            gg.drawLine(arrayList.get(v).getValue().getKey() + 120, arrayList.get(v).getValue().getValue() + 120,
                                    arrayList.get(j).getValue().getKey() + 120, arrayList.get(j).getValue().getValue() + 120);
                        }
                        if (switchOnline && this.occurence(positionsOnline, j) && this.occurence(positionsOnline, v)) {
                            gg.drawLine(arrayList.get(v).getValue().getKey() + 120, arrayList.get(v).getValue().getValue() + 120,
                                    arrayList.get(j).getValue().getKey() + 120, arrayList.get(j).getValue().getValue() + 120);
                        }

                    }
                }
            }
            for (int i = 0; i < Matrix_EXAMPLE1.length; i++) {
                if (!ids2.contains(Employee[i].getUser().getId())) {
                    continue;
                }
                String[] Clique = Employee[i].CliqueGetter().split(",");
                int No_clique = Clique.length;
                if (No_clique > 0) {
                    int slice = 360 / No_clique;
                    int start = 0;
                    Graphics2D ga = (Graphics2D) g;
                    ga.setPaint(new Color(255, 184, 6, 215));
                    g.setFont(new Font("ZapfDingbats", Font.BOLD, 12));
                    g.setColor(new Color(0, 0, 0));

                /*if (!switchOnline) {
                    if (arrayList.get(i).getValue() > 180) {
                        g.drawString((i + 1) + "." + Employee[i].getName(), arrayList.get(i).getKey() + 100,
                                arrayList.get(i).getValue() + 150);
                    } else {
                        g.drawString((i + 1) + "." + Employee[i].getName(), arrayList.get(i).getKey() + 100,
                                arrayList.get(i).getValue() + 90);
                    }
                }*/
                /*if (switchOnline && this.occurence(positionsOnline, i)) {
                    if (arrayList.get(i).getValue() > 180) {
                        g.drawString((i + 1) + "." + Employee[i].getName(), arrayList.get(i).getKey() + 100,
                                arrayList.get(i).getValue() + 150);
                    } else {
                        g.drawString((i + 1) + "." + Employee[i].getName(), arrayList.get(i).getKey() + 100,
                                arrayList.get(i).getValue() + 90);
                    }

                }*/
                    for (int z = 0; z < No_clique; z++) {
                        int c = (Integer.parseInt(Clique[z])) - 1;
                        ga.setPaint(CliqueColors[c]);
                        Arc2D arc2D4 = new Arc2D.Double(arrayList.get(i).getValue().getKey() + 100, arrayList.get(i).getValue().getValue() + 100, 30.0f, 30.0f, start, slice, Arc2D.PIE);
                        if (!switchOnline) {
                            ga.fill(arc2D4);
                            ga.draw(arc2D4);
                            start += slice;
                        }
                        if (switchOnline && this.occurence(positionsOnline, i)) {
                            ga.fill(arc2D4);
                            ga.draw(arc2D4);
                            start += slice;
                        }


                    }


                }
            }
        } else {
            for (int j = 0; j < calc.arrEdgeWeight.length; j++) { //draw edges
                if (!ids2.contains(Employee[j].getUser().getId())) {
                    continue;
                }
                for (int v = 0; v < calc.arrEdgeWeight.length; v++) {
                    int h = calc.Matrix_getter()[j][v];
                    if (h > 0) {
                        Graphics2D gg = (Graphics2D) g;
                        Stroke stroke;
                        if (this.strategy == 0) {
                            float factor = calculateFacotrForEdges();
                            stroke = new BasicStroke(h * factor);
                        } else {
                            stroke = new BasicStroke(3.0f);
                        }
                        gg.setStroke(stroke);
                        if (!switchOnline) {
                            gg.drawLine(arrayList.get(v).getValue().getKey() + 120, arrayList.get(v).getValue().getValue() + 120,
                                    arrayList.get(j).getValue().getKey() + 120, arrayList.get(j).getValue().getValue() + 120);
                        }
                        if (switchOnline && this.occurence(positionsOnline, j) && this.occurence(positionsOnline, v)) {
                            gg.drawLine(arrayList.get(v).getValue().getKey() + 120, arrayList.get(v).getValue().getValue() + 120,
                                    arrayList.get(j).getValue().getKey() + 120, arrayList.get(j).getValue().getValue() + 120);
                        }

                    }

                }
            }


            for (int i = 0; i < calc.Size_getter(); i++) {
                if (! ids2.contains(Employee[i].getUser().getId())) {
                    continue;
                }
                //draw nodes
                if (this.strategy == 0) {
                    g.setColor(new Color(76, 111, 111, 255));
                } else if (this.strategy == 1) {
                    normalizeDouble(calc.arrNodeWeight);
                    float weight = 0.0f + (float) calc.arrNodeWeight[i];
                    g.setColor(Color.getHSBColor((0.0f + 255 - weight * 50) / 360, 1.0f, 1.0f));
                    //System.out.println(weight);
                } else if (this.strategy == 2) {
                    float between = 0.0f + (float) calc.Employee_Getter()[i].BetweenGetter();
                    //System.out.println("Color:" + between);
                    g.setColor(Color.getHSBColor((255 - between * 250) / 360, 1.0f, 1.0f));
                    // System.out.println(between);
                } else if (this.strategy == 3) {
                    float eigen = 0.0f + (float) calc.Employee_Getter()[i].EigenGetter();
                    g.setColor(Color.getHSBColor((255 - eigen * 250) / 360, 1.0f, 1.0f));
                    //System.out.println(eigen);
                } else if (this.strategy == 4) {
                    float closeness = 0.0f + (float) calc.Employee_Getter()[i].ClosenessGetter();
                    g.setColor(Color.getHSBColor((255 - closeness * 250) / 360, 1.0f, 1.0f));
                   // System.out.println(closeness);
                    //System.out.println(Color.getHSBColor(250/360,1.0f,1.0f));
                    //System.out.println(closeness +"nikni");
                }
                if (!switchOnline) {
                    g.drawOval(arrayList.get(i).getValue().getKey() + 100, arrayList.get(i).getValue().getValue() + 100, 30, 30);
                    g.fillOval(arrayList.get(i).getValue().getKey() + 100, arrayList.get(i).getValue().getValue() + 100, 30, 30);
                }
                if (switchOnline && this.occurence(positionsOnline, i)) {
                    g.drawOval(arrayList.get(i).getValue().getKey() + 100, arrayList.get(i).getValue().getValue() + 100, 30, 30);
                    g.fillOval(arrayList.get(i).getValue().getKey() + 100, arrayList.get(i).getValue().getValue() + 100, 30, 30);
                }
            /*g.setColor(Color.black);


            g.drawOval(arrayList.get(i).getKey() + 100, arrayList.get(i).getValue() + 100, 30, 30);
            g.fillOval(arrayList.get(i).getKey() + 100, arrayList.get(i).getValue() + 100, 30, 30);*/
                g.setColor(new Color(255, 184, 6, 205));
                g.setFont(new Font("ZapfDingbats", Font.BOLD, 12));
    		/*if(arrayList.get(i).getValue()>180) {


            g.drawString((i + 1) + "." + calc.Employee_Getter()[i].getName(), arrayList.get(i).getKey() + 100,
                    arrayList.get(i).getValue() +150);
            //System.out.println(calc.Employee_Getter()[i].getName());
        }else {
        	g.drawString((i + 1) + "." + calc.Employee_Getter()[i].getName(), arrayList.get(i).getKey() + 100,
                    arrayList.get(i).getValue() +90);
                //System.out.println(calc.Employee_Getter()[i].getName());
        }*/


             /*   if (!switchOnline) {
                    if (calc.Employee_Getter()[i].getY() > 180) {


                        g.drawString((i + 1) + "." + calc.Employee_Getter()[i].getName(), arrayList.get(i).getKey() + 100,
                                arrayList.get(i).getValue() + 150);
                        //System.out.println(calc.Employee_Getter()[i].getName());
                    } else {
                        g.drawString((i + 1) + "." + calc.Employee_Getter()[i].getName(), arrayList.get(i).getKey() + 100,
                                arrayList.get(i).getValue() + 90);
                        //System.out.println(calc.Employee_Getter()[i].getName());
                    }
                }*/
                /** if (switchOnline && this.occurence(positionsOnline, i)) {
                    if (calc.Employee_Getter()[i].getY() > 180) {


                        g.drawString((i + 1) + "." + calc.Employee_Getter()[i].getName(), arrayList.get(i).getKey() + 100,
                                arrayList.get(i).getValue() + 150);
                        //System.out.println(calc.Employee_Getter()[i].getName());
                    } else {
                        g.drawString((i + 1) + "." + calc.Employee_Getter()[i].getName(), arrayList.get(i).getKey() + 100,
                                arrayList.get(i).getValue() + 90);
                        //System.out.println(calc.Employee_Getter()[i].getName());
                    }
                } */
            }

        }


        if (this.strategy == 1 || this.strategy == 2 || this.strategy == 3 || this.strategy == 4) {//draw sacala
            int x = 150;
            g.setColor(new Color(0, 0, 0));
            g.drawString("low", x, 75);

            for (int i = 240; i >= 0; i--) {
                g.setColor(Color.getHSBColor((0.0f + i) / 360, 1.0f, 1.0f));
                g.fillRect(x, 50, 1, 10);
                x++;
            }
            g.setColor(new Color(0, 0, 0));
            g.drawString("high", x, 75);
        }

        //int x = 25;
        //int y = 100;
/*
        g.setColor(new Color(0, 0, 0));


        int x = 10;


        int y = 350;
        for (int i = 0; i < calc.Size_getter(); i++) {
            g.drawString((i + 1) + "." + calc.Employee_Getter()[i].getName(), x, y);
            y = y + 20;
        }

        //frame
        Graphics2D gg = (Graphics2D) g;
        Stroke stroke;
        stroke = new BasicStroke(3.0f);
        gg.setStroke(stroke);
        g.drawRect(x - 10, 135, 150, calc.Size_getter() * 25);*/

        g.dispose();

    }

    public boolean occurence(int[] arr, int x) {

        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == x) {
                return true;
            }
        }
        return false;
    }

    private float calculateFacotrForEdges() {
        int max = 0;
        for (int i = 0; i < calc.arrEdgeWeight.length; i++) {
            for (int j = 0; j < calc.arrEdgeWeight.length; j++) {
                if (calc.arrEdgeWeight[i][j] > max) {
                    max = calc.arrEdgeWeight[i][j];
                }
            }
        }
        return 12.0f / max;
    }

    public void setArrayList(ArrayList<Pair<String, Pair<Integer,Integer>>> arrayList) {
        this.arrayList = arrayList;
    }

    public GraphicsPainter(Calculation calc, ArrayList<Pair<String, Pair<Integer,Integer>>> arrayList, String building) {

        this.calc = calc;
        this.arrayList = arrayList;
        //this.building = building;
        GUIUser[] onlineUsers = calc.getOnlineUsers();
        int i = 0;
        verticies = new ArrayList<>(25);
        if (onlineUsers != null) {
            positionsOnline = new int[onlineUsers.length];
            for (GUIUser guiuser : onlineUsers) {
                positionsOnline[i] = guiuser.getUser().getPositionMatrix();
                System.out.print(positionsOnline[i] + ", ");
                i++;
            }
            System.out.println();
        }
        for(int index=0;index< calc.Size_getter();index++){
            int x=arrayList.get(index).getValue().getKey() + 120;
            int y=arrayList.get(index).getValue().getValue() + 120;
            add(new Vertex(new Point(x,y)));

        }
        setToolTipText("");
    }
    public void add(Vertex vertex) {
        verticies.add(new VertexPoint(vertex));
    }

    /**
     * @param
     */
    public class VertexPoint extends Ellipse2D.Double {
        private Vertex vertex;

        public VertexPoint(Vertex vertex) {
            super(vertex.getPoint().x - 5, vertex.getPoint().y - 5, 10, 10);
            this.vertex = vertex;
        }

        public Vertex getVertex() {
            return vertex;
        }

    }

    public GraphicsPainter(Calculation calc) {
        this.calc = calc;
        GUIUser[] onlineUsers = calc.getOnlineUsers();
        int i = 0;
        verticies = new ArrayList<>(25);
        if (onlineUsers != null) {
            positionsOnline = new int[onlineUsers.length];
            for (GUIUser guiuser : onlineUsers) {
                positionsOnline[i] = guiuser.getUser().getPositionMatrix();
                System.out.print(positionsOnline[i] + ", ");
                i++;
            }
            System.out.println();
        }
        for(int index=0;index< calc.Size_getter();index++){
            if(arrayList.size()>0){
            int x=arrayList.get(index).getValue().getKey() + 100;
            int y=arrayList.get(index).getValue().getValue() + 100;
            add(new Vertex(new Point(x,y)));}

        }}

    public static  double[][] normalizeDouble(double[][] arr){


      double  min = Double.MAX_VALUE;
       double max = Double.MIN_VALUE;


        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {

                max = Math.max(arr[i][j],max);
                min = Math.min(arr[i][j],min);


            }
        }

        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {

                arr[i][j] = (arr[i][j] - min)/(max-min);

            }
        }

        return arr;

    }

    public static double norm(double[] arr) {
        assert arr != null;
        double s = 0;
        for (double i : arr) {
            s += Math.pow(i, 2);
           // s += i;

            return Math.sqrt(s);
        }
        return 0;
    }
    public static  double[] normalizeDouble(double[] arr){
        double norm = norm(arr);
        for (int i = 0; i < arr.length ; i++){
            arr[i] = arr[i] / norm;
        }
        return arr;
    }




    public ArrayList<Pair<String, Pair<Integer,Integer>>> getArrayList() {
        return arrayList;
    }


}





