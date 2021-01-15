package wb.analyse1.GUI;

import wb.analyse1.analyse.User;

import java.util.LinkedHashSet;
import java.util.Observable;

public class Model extends Observable {

    private  Calculation calc;

    public Model(Calculation calc){
        this.calc = calc;

    }

    public Model(int[][] networkMatrix, LinkedHashSet<User> users,LinkedHashSet<User> onlineUsers) {
        this.calc = new Calculation(networkMatrix,  users, onlineUsers);
    }


    public void setCalc(Calculation calc) {

        this.calc = calc;
        setChanged();
        notifyObservers(calc);

    }

    public Calculation getCalc() {
        return calc;
    }
}
