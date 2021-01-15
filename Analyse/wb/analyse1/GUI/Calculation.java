package wb.analyse1.GUI;

import wb.analyse1.analyse.User;

import java.util.LinkedHashSet;
import java.util.Random;

/**
 * Create a Matrix containing User names , matrix size and the weight of every Edge between Users (will be representedt in knote)
 *
 * @author Amine Abdelmaksoud
 */
public class Calculation {
    private int[][] networkMatrix;
    private int Matrix_size;
    private GUIUser[] Employee;
    private GUIUser[] onlineUsers;
    public double[] arrNodeWeight;
    public int[][] arrEdgeWeight;



    public void setEmployee(GUIUser[] employee) {
        Employee = employee;
    }

    public Calculation() {
        Matrix Example = new Matrix();
        networkMatrix = Example.Matrix_getter();
        Matrix_size = Example.Matrix_getter().length;
        this.arrNodeWeight = new double[Matrix_size];
        calculateDegree();
        //System.out.println(Matrix_size);
        GUIUser[] Employee1 = new GUIUser[Matrix_size];

        for (int i = 0; i < Matrix_size; i++) {
            Employee1[i] = new GUIUser(new User());
            //while(!Employee1[i].DisEmp(Employee1)) {
            //		Employee1[i] = new GUIUser(NAME_EXAMPLES[i]);
            //}
            //	for(int j=0; j<=i;j++) {
            //	while(Math.abs((Employee1[i].getX() - Employee1[j].getX())) < 10  || Math.abs((Employee1[i].getY() - Employee1[j].getY())) < 10 ) {
            //		Employee1[i]= new GUIUser(NAME_EXAMPLES[8]);}

        }


        this.Employee = Employee1;
    }

    /**
     * function to create the matrix.
     */
    public Calculation(int[][] networkMatrix, LinkedHashSet<User> users, LinkedHashSet<User> onlineUsers) {

        this.networkMatrix = networkMatrix;
        Matrix_size = this.networkMatrix.length;
        arrEdgeWeight = networkMatrix;
        this.Employee = new GUIUser[networkMatrix.length];
        int i = 0;
        for (User guiuser : users){
            this.Employee[i] = new GUIUser( guiuser);
           // System.out.println(Employee[i]);
            i++;
        }
        i = 0;
        this.onlineUsers = new GUIUser[onlineUsers.size()];
        for (User user : onlineUsers){
            this.onlineUsers[i] = new GUIUser(user);
            i++;
        }


    }

    public GUIUser[] getOnlineUsers(){
        return onlineUsers;
    }

    //Getter to read the Matrix
    public int[][] Matrix_getter() {
        return this.networkMatrix;

    }

    //Getter to read the value of the size of the matrix
    public int Size_getter() {
        return this.Matrix_size;
    }

    //Getter to read the value of the Employee
    public GUIUser[] Employee_Getter() {
        return this.Employee;
    }

    public void calculateInteraction() {
        arrEdgeWeight = networkMatrix;
    }

    /**
     * function to calculate the degree of interactions for every GUIUser.
     */
    public void calculateDegree() {
        arrNodeWeight = new double[Matrix_size];
        int degree = 0;
        for (int i = 0; i < Matrix_size; i++) {
            for (int j = 0; j < Matrix_size; j++) {
                degree = degree + this.networkMatrix[j][i];
            }
            //System.out.println(degree);
            arrNodeWeight[i] = degree;
            degree = 0;
        }
    }


    /**
     * function to calculate 'the betweeness centrality'.
     * gives a randoom value ( betweeen 0 and 255 : we will use it to colour the knote )
     * to test the GUI of the 'the betweeness centrality'
     */
    public void calculateBetweeniss() {
        arrNodeWeight = new double[Matrix_size];
        for (int i = 0; i < Matrix_size; i++) {
            arrNodeWeight[i] = this.Employee[i].BetweenGetter();
        }

    }

    public void calculateEigenVektor() {
        arrNodeWeight = new double[Matrix_size];
        for (int i = 0; i < Matrix_size; i++) {
            arrNodeWeight[i] = this.Employee[i].EigenGetter();
        }
    }

    public void calculateCloseness() {
        double[] arrNodeWeight1 = new double[Matrix_size];
        for (int i = 0; i < Matrix_size; i++) {
            //System.out.println(this.Employee[i] + " zab");
            arrNodeWeight1[i] = this.Employee[i].ClosenessGetter();
            //System.out.println(arrNodeWeight1[i]);
        }
        arrNodeWeight = arrNodeWeight1;
    }

    public void calculateClique() {
        arrNodeWeight = new double[Matrix_size];
        for (int i = 0; i < Matrix_size; i++) {
            arrNodeWeight[i] = new Random().nextInt(255);
        }
    }
}
