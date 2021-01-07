package wb.analyse1.analyse;

import wb.analyse1.parser.*;

public class User extends Attendee {
    private int Degree;
    private double Betweenness;
    private double Closeness;
    private double Eigenvector;
    private String CliqueIDs = "";
    private int positionMatrix;

    public User(){
        super();
    }

    public User(int positionMatrix){
        super();
        this.Degree = 0;
        this.Betweenness = 0;
        this.Closeness = 0;
        this.positionMatrix = positionMatrix;
    }
    public User(String ID, String name, int positionMatrix) {
        super(ID,name);
        this.Degree = 0;
        this.Betweenness = 0;
        this.Closeness = 0;
        this.positionMatrix = positionMatrix;
    }

    public String getCliqueIDs(){
        return CliqueIDs;
    }
    public void setCliqueIDs(String ids){
        this.CliqueIDs = ids;
    }
    public double getCloseness() {
        return Closeness;
    }

    public void setPositionMatrix(int pos) {this.positionMatrix = pos;}

    public double getEigenvector() {
        return Eigenvector;
    }

    public int getDegree() {
        return Degree;
    }

    public int getPositionMatrix() {
        return positionMatrix;
    }

    public double getBetweenness() {
        return Betweenness;
    }

    public void setEigenvector(double eigenvector) {
        this.Eigenvector = eigenvector;
    }
    public void setBetweenness(double betweenness) {
        Betweenness = betweenness;
    }
    public void setCloseness(double closeness) { Closeness = closeness;}

    public void setDegree(int degree) {
        Degree = degree;
    }
    public void setCloseness(int closeness) {
        closeness = closeness;
    }


    @Override
    public String toString() {
        return "{" + super.toString() + " Degree=" + Degree + " Eigenvector=" + Eigenvector + " Betweenness=" + Betweenness +
            " Closeness=" + Closeness + "CliqueIDs=" + CliqueIDs + " Index=" + positionMatrix + "}";
    }
}
