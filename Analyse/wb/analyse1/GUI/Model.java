package wb.analyse1.GUI;

import wb.analyse1.analyse.User;

import java.util.LinkedHashSet;
import java.util.Observable;

public class Model extends Observable {

    private  Calculation calc;
    private String building;

    public Model(Calculation calc){
        this.calc = calc;

    }

    public Model(int[][] networkMatrix, LinkedHashSet<User> users,LinkedHashSet<User> onlineUsers,String building) {
        this.calc = new Calculation(networkMatrix,  users, onlineUsers);
        this.calc.setBuilding(building);
    }


    public void setCalc(Calculation calc, String building) {

        this.calc = calc;
        System.out.println("set Calc building: "+ building);
        calc.setBuilding(building);
        setChanged();
        notifyObservers(calc);

    }

    public Calculation getCalc() {
        return calc;
    }
    public String getBuilding() { return building;}

}
