package wb.analyse1.GUI;

import java.awt.Color;
import java.util.Random;

import wb.analyse1.analyse.User;

/**
 * every user is represented with a color , name and coordinates. (and a random betweeness number (nur zu testen))
 *
 * @author Amine Abdelmaksoud.
 */

public class GUIUser {
    private Color color;
    private User user;
    // private String name;
    private int x;
    private int y;
 /*private int betweenness =new Random().nextInt(255) ;
 private int eigen =new Random().nextInt(255) ;
 private int closeness =new Random().nextInt(255) ;
 private String clique =null;*/


 public GUIUser (User user) {
     this.user= user;
	 this.color=new Color(0,0,0);
	 //this.name= name;
	 this.x= (new Random().nextInt(1000) ) ;
	 this.y= (new Random().nextInt(400) ) ;
	// int k= new Random().nextInt(7);
	/* switch(k) {
	 case 0:
		 clique="1";
	     break;
	 case 1:
		 clique="2";
		 break;
	 case 2:
		 clique="1,2";
		 break;
	 case 3:
		 clique="3";
		 break;
	 case 4:
		 clique="2,3";
		 break;
	 case 5:
		 clique="1,3";
	     break;
	 case 6:
		 clique="1,2,3";
		 break;
	 default:
		 clique="1";
		 break;
	
	 }*/
 }

    public User getUser(){
        return this.user;
    }

    public String getName() {
        return this.user.getName();
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public void clique_setter(String clique) {
        this.user.setCliqueIDs(clique);
    }

    /**
     *
     * @param employee the users.
     * @return check if the distance between the employee is bigger then 5.
     * ( make sure that the nodes won't be represented over each other)
     */
    public boolean DisEmp(GUIUser[] employee) {
        int l = employee.length;
        for (int i = 0; i < l; i++) {
            if ((employee[i].getX() - this.x) < 5 || (employee[i].getY() - this.y) < 5) {
                return false;
            } else {
                return true;
            }

        }
        return false;
    }

    /**
     * Random number to test the 'Betweenness Centrality'.
     * @return betweeneness.
     */
    public double BetweenGetter() {
        return this.user.getBetweenness();
    }
    //Getter to read the value of the color

    public double EigenGetter() {
        return this.user.getEigenvector();
    }

    public double ClosenessGetter() {
        return this.user.getCloseness();
    }

    public String CliqueGetter() {
        return this.user.getCliqueIDs();
    }

    public Color getColor() {
        return this.color;
    }

    //setter to update the value of the x, y positions
    public void setXY(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return this.user.toString();
    }
}
