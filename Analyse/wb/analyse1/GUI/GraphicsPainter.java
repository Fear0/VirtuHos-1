package wb.analyse1.GUI;

import java.awt.*;
import java.awt.geom.Arc2D;
import java.util.AbstractMap;
import java.util.ArrayList;

import javax.swing.JPanel;


public class GraphicsPainter extends JPanel {

	Calculation calc;
	int[] positionsOnline;
	Color[] CliqueColors = { Color.BLUE, Color.GREEN, Color.RED , Color.PINK, Color.CYAN, Color.MAGENTA};
    private ArrayList<AbstractMap.SimpleEntry<Integer, Integer> > arrayList = new ArrayList<AbstractMap.SimpleEntry<Integer, Integer> >();

	boolean switchOnline = false;





    int strategy = 0; // 0 for edges and 1 for nodes

    public void setSwitchOnline(boolean switchOnline) {
        this.switchOnline = switchOnline;
    }

    /**
     * Create the panel.
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(this.strategy == 5) {
       // String[] CliqueIds= {"1", "1,2" ,"1,2" ,"2", "3","3" ,"3"};
       String[] CliqueIds=new String[calc.Size_getter()];
       for (int k=0;k<calc.Size_getter();k++){
           CliqueIds[k]=calc.Employee_Getter()[k].CliqueGetter();
       }
      /*  int[][] Matrix_EXAMPLE1=	{
        	 {0, 1, 1, 0, 0, 0, 0},
        	 {1, 0, 2, 1, 0, 0, 0},
        	 {1, 2, 0, 1, 0, 0, 0},
        	 {0, 1, 1, 0, 0, 0, 0},
        	 {0, 0, 0, 0, 0, 1, 1},
        	 {0, 0, 0, 0, 1, 0, 1},
        	 {0, 0, 0, 0, 1, 1, 0}};*/
            int[][] Matrix_EXAMPLE1= calc.Matrix_getter();
        GUIUser[] Employee=calc.Employee_Getter();

        
        for (int j = 0; j < Matrix_EXAMPLE1.length; j++) { //draw edges
            for (int v = 0; v < Matrix_EXAMPLE1.length; v++) {
                int h = Matrix_EXAMPLE1[v][j];
                if (h > 0) {
                    Graphics2D gg = (Graphics2D) g;
                    Stroke stroke;
                    stroke = new BasicStroke(3.0f);
                    gg.setStroke(stroke);
                    if(!switchOnline) {
                        gg.drawLine(arrayList.get(v).getKey() + 120, arrayList.get(v).getValue() + 120,
                                arrayList.get(j).getKey() + 120, arrayList.get(j).getValue() + 120);
                    }
                    if (switchOnline && this.occurence(positionsOnline,j) && this.occurence(positionsOnline,v)) {
                        gg.drawLine(arrayList.get(v).getKey() + 120, arrayList.get(v).getValue() + 120,
                                arrayList.get(j).getKey() + 120, arrayList.get(j).getValue() + 120);
                    }

                }
            }}
        	for (int i = 0; i < Matrix_EXAMPLE1.length; i++) {
        		String[] Clique = Employee[i].CliqueGetter().split(",");
        		int No_clique= Clique.length;
        		int slice = 360 / No_clique;
        		int start =0;
        		Graphics2D ga = (Graphics2D) g;
        		ga.setPaint(new Color(255, 184, 6, 215));
        		g.setFont(new Font("ZapfDingbats", Font.BOLD, 12));
                if(!switchOnline) {
                    if (arrayList.get(i).getValue() > 180) {
                        g.drawString((i + 1) + "." + Employee[i].getName(), arrayList.get(i).getKey() + 100,
                                arrayList.get(i).getValue() + 150);
                    } else {
                        g.drawString((i + 1) + "." + Employee[i].getName(), arrayList.get(i).getKey() + 100,
                                arrayList.get(i).getValue() + 90);
                    }
                }
                if (switchOnline && this.occurence(positionsOnline,i)) {
                    if (arrayList.get(i).getValue() > 180) {
                        g.drawString((i + 1) + "." + Employee[i].getName(), arrayList.get(i).getKey() + 100,
                                arrayList.get(i).getValue() + 150);
                    } else {
                        g.drawString((i + 1) + "." + Employee[i].getName(), arrayList.get(i).getKey() + 100,
                                arrayList.get(i).getValue() + 90);
                    }

                }
        		for(int z=0; z< No_clique; z++) {
        			int c=(Integer.parseInt(Clique[z]) )- 1;
        			ga.setPaint(CliqueColors[c]);
        			Arc2D arc2D4 = new Arc2D.Double(arrayList.get(i).getKey() +100, arrayList.get(i).getValue() + 100, 30.0f, 30.0f, start, slice, Arc2D.PIE);
                    if(!switchOnline) {
                        ga.fill(arc2D4);
                        ga.draw(arc2D4);
                        start += slice;
                    }
                    if (switchOnline && this.occurence(positionsOnline,i)) {
                        ga.fill(arc2D4);
                        ga.draw(arc2D4);
                        start += slice;
                    }


        		}
        		
           
        	}}
        	else {
        for (int j = 0; j < calc.arrEdgeWeight.length; j++) { //draw edges
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
                    if(!switchOnline) {
                        gg.drawLine(arrayList.get(v).getKey() + 120, arrayList.get(v).getValue() + 120,
                                arrayList.get(j).getKey() + 120, arrayList.get(j).getValue() + 120);
                    }
                    if (switchOnline && this.occurence(positionsOnline,j) && this.occurence(positionsOnline,v)) {
                        gg.drawLine(arrayList.get(v).getKey() + 120, arrayList.get(v).getValue() + 120,
                                arrayList.get(j).getKey() + 120, arrayList.get(j).getValue() + 120);
                    }

                }

            }
        }
       
        	
        for (int i = 0; i < calc.Size_getter(); i++) { 
        	//draw nodes
            if (this.strategy == 0) {
                g.setColor(new Color(76, 111, 111, 255));
            } else if(this.strategy == 1) {
                float weight = 0.0f + (float) calc.arrNodeWeight[i];
                g.setColor(Color.getHSBColor((0.0f + 255-weight*10)/360,1.0f,1.0f));
                //System.out.println(weight);
            } else if(this.strategy == 2) {
                float between = 0.0f + (float) calc.Employee_Getter()[i].BetweenGetter();
                System.out.println("Color:"+between);
                g.setColor(Color.getHSBColor((255-between*250)/360,1.0f,1.0f));
               // System.out.println(between);
            } else if(this.strategy == 3) {
                float eigen = 0.0f + (float) calc.Employee_Getter()[i].EigenGetter();
                g.setColor(Color.getHSBColor((255-eigen*250)/360,1.0f,1.0f));
                //System.out.println(eigen);
            } else if(this.strategy == 4) {
                float closeness = 0.0f + (float) calc.Employee_Getter()[i].ClosenessGetter();
                g.setColor(Color.getHSBColor((255-closeness*400)/360,1.0f,1.0f));
                //System.out.println(Color.getHSBColor(250/360,1.0f,1.0f));
                //System.out.println(closeness +"nikni");
            }
            if (!switchOnline) {
                g.drawOval(arrayList.get(i).getKey() + 100, arrayList.get(i).getValue() + 100, 30, 30);
                g.fillOval(arrayList.get(i).getKey() + 100, arrayList.get(i).getValue() + 100, 30, 30);
            }
            if (switchOnline && this.occurence(positionsOnline,i)) {
                g.drawOval(arrayList.get(i).getKey() + 100, arrayList.get(i).getValue() + 100, 30, 30);
                g.fillOval(arrayList.get(i).getKey() + 100, arrayList.get(i).getValue() + 100, 30, 30);
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


            if (!switchOnline) {
                if (calc.Employee_Getter()[i].getY() > 180) {


                    g.drawString((i + 1) + "." + calc.Employee_Getter()[i].getName(), arrayList.get(i).getKey() + 100,
                            arrayList.get(i).getValue() +150);
                    //System.out.println(calc.Employee_Getter()[i].getName());
                } else {
                    g.drawString((i + 1) + "." + calc.Employee_Getter()[i].getName(), arrayList.get(i).getKey() + 100,
                            arrayList.get(i).getValue() +90);
                    //System.out.println(calc.Employee_Getter()[i].getName());
                }
            }
            if (switchOnline && this.occurence(positionsOnline,i)) {
                if (calc.Employee_Getter()[i].getY() > 180) {


                    g.drawString((i + 1) + "." + calc.Employee_Getter()[i].getName(), arrayList.get(i).getKey() + 100,
                            arrayList.get(i).getValue() +150);
                    //System.out.println(calc.Employee_Getter()[i].getName());
                } else {
                    g.drawString((i + 1) + "." + calc.Employee_Getter()[i].getName(), arrayList.get(i).getKey() + 100,
                            arrayList.get(i).getValue() +90);
                    //System.out.println(calc.Employee_Getter()[i].getName());
                }
            }
            }

        	}


        if (this.strategy == 1 || this.strategy == 2 || this.strategy == 3 || this.strategy == 4 ) {//draw sacala
            int x = 150;
            g.setColor(new Color(0, 0, 0));
            g.drawString("low", x, 75);

            for (int i = 240; i >=0; i--) {
                g.setColor(Color.getHSBColor((0.0f + i)/360,1.0f,1.0f));
                g.fillRect(x, 50, 1, 10);
                x++;
            }
        g.setColor(new Color(0, 0, 0));
        g.drawString("high", x, 75);
    }

    }

    public boolean occurence(int[] arr, int x){

        for (int i = 0 ; i < arr.length; i++){
            if (arr[i] == x){
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

    public void setArrayList(ArrayList<AbstractMap.SimpleEntry<Integer, Integer>> arrayList) {
        this.arrayList = arrayList;
    }

    public GraphicsPainter(Calculation calc, ArrayList<AbstractMap.SimpleEntry<Integer, Integer>> arrayList) {
        this.calc = calc;
        this.arrayList = arrayList;
        GUIUser[] onlineUsers = calc.getOnlineUsers();
        int i = 0;
        if (onlineUsers != null) {
            positionsOnline = new int[onlineUsers.length];
            for (GUIUser guiuser : onlineUsers) {
                positionsOnline[i] = guiuser.getUser().getPositionMatrix();
                System.out.print(positionsOnline[i]+", ");
                i++;
            }
            System.out.println();
        }
    }

    /**
	 *
	 * @param calc
	 */
	public GraphicsPainter(Calculation calc) {
		this.calc = calc;
		GUIUser[] onlineUsers = calc.getOnlineUsers();
		int i = 0;
		if (onlineUsers != null) {
		    positionsOnline = new int[onlineUsers.length];
            for (GUIUser guiuser : onlineUsers) {
                positionsOnline[i] = guiuser.getUser().getPositionMatrix();
                System.out.print(positionsOnline[i]+", ");
                i++;
            }
            System.out.println();
        }


	}



    public ArrayList<AbstractMap.SimpleEntry<Integer, Integer>> getArrayList() {
        return arrayList;
    }


}
