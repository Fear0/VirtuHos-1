package wb.analyse1.GUI;

import wb.analyse1.Database.Database;
import wb.analyse1.analyse.Analyse;
import wb.analyse1.analyse.User;

import javax.xml.crypto.Data;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Random;

public class Client {

    public static void waiting(int time) {
        Random r = new Random();
        try {
            Thread.sleep(r.nextInt(1000) + time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args){

        Database db = new Database();


        /*LinkedHashSet<User> users = db.fetchUsers();
        LinkedHashSet<User> onlineUsers = db.fetchOnlineUsers();
        int[][] networkMatrix = db.fetchNetworkMatrix(users);*/

        LinkedHashSet<User> users = new LinkedHashSet<>();
        LinkedHashSet<User> onlineUsers = new LinkedHashSet<>();
        int[][] networkMatrix = new int[0][];

        GUIThread guiThread = new GUIThread(networkMatrix,users,onlineUsers);
        guiThread.run();
        LinkedHashSet<User> auxiliaryForUsers = users;
        LinkedHashSet<User> auxiliaryForOnlineUsers = onlineUsers;
        int[][] auxiliaryForMatrix = networkMatrix;
        while(true){

            waiting(3000);
            //Analyse.printSet(users);
            //Analyse.printSet(onlineUsers);
            users = db.fetchUsers();
            onlineUsers = db.fetchOnlineUsers();
            networkMatrix = db.fetchNetworkMatrix(users);
            System.out.println("Matrix in Client:");
            //System.out.println(networkMatrix);
            /*if (users.size() == 0 ||onlineUsers.size() == 0 || networkMatrix[0].length ==0) {
                waiting(2000);
                continue;
            }*/
            Analyse.printMatrix(networkMatrix);
            Analyse.printSet(users);
            if (users.containsAll(auxiliaryForUsers) && auxiliaryForUsers.containsAll(users)){
                continue;
            }
            if (onlineUsers.containsAll(auxiliaryForOnlineUsers) && auxiliaryForOnlineUsers.containsAll(onlineUsers)){
                continue;
            }

            if (Arrays.deepEquals(auxiliaryForMatrix, networkMatrix)) {
                //waiting(5000);
                continue;
            }
            auxiliaryForUsers = users;
            auxiliaryForOnlineUsers = onlineUsers;
            auxiliaryForMatrix = networkMatrix;
            guiThread.setModel(networkMatrix, users, onlineUsers);
          //  waiting(7000000);
        }
    }
}
