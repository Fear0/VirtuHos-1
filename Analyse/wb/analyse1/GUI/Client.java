package wb.analyse1.GUI;

import wb.analyse1.Database.Database;
import wb.analyse1.analyse.Analyse;
import wb.analyse1.analyse.User;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Random;



public class Client implements Runnable{

    private String buildingName;

    private volatile boolean exit = false;

    public Client(String buildingName){
        this.buildingName = buildingName;
    }

    public static void waiting(int time) {
        Random r = new Random();
        try {
            Thread.sleep(r.nextInt(1000) + time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String getBuildingName(){
        return buildingName;
    }

    public void run(){

        Database db = new Database();
        //db.deleteAllInteractions();
       // db.deleteAllUsers();

        /*LinkedHashSet<User> users = db.fetchUsers();
        LinkedHashSet<User> onlineUsers = db.fetchOnlineUsers();
        int[][] networkMatrix = db.fetchNetworkMatrix(users);*/

        LinkedHashSet<User> users = new LinkedHashSet<>();
        LinkedHashSet<User> onlineUsers = new LinkedHashSet<>();
        int[][] networkMatrix = new int[0][];

        GUIThread guiThread = new GUIThread(networkMatrix,users,onlineUsers,this);
        //Thread t1 = new Thread(guiThread, "AnalyseGUI");
        //t1.setDaemon(true);
        //t1.start();
        guiThread.run();

        LinkedHashSet<User> auxiliaryForUsers = users;
        LinkedHashSet<User> auxiliaryForOnlineUsers = onlineUsers;
        int[][] auxiliaryForMatrix = networkMatrix;
        while(!exit){

            waiting(3000);
            //Analyse.printSet(users);
            //Analyse.printSet(onlineUsers);
            users = db.fetchUsers();
            onlineUsers = db.fetchOnlineUsers();
            networkMatrix = db.fetchNetworkMatrix(users);
            if ( networkMatrix == null || users == null || users.size() == 0) {
                continue;
            }
            System.out.println("Matrix in Client:");
            //System.out.println(networkMatrix);
            /*if (users.size() == 0 ||onlineUsers.size() == 0 || networkMatrix[0].length ==0) {
                waiting(2000);
                continue;
            }*/
            System.out.println("Buildingname in loop:" + this.getBuildingName());
            //Analyse.printMatrix(networkMatrix);
            //Analyse.printSet(users);
            /*if (users.containsAll(auxiliaryForUsers) && auxiliaryForUsers.containsAll(users)){
                continue;
            }
            if (onlineUsers.containsAll(auxiliaryForOnlineUsers) && auxiliaryForOnlineUsers.containsAll(onlineUsers)){
                continue;
            }

            if (Arrays.deepEquals(auxiliaryForMatrix, networkMatrix)) {
                //waiting(5000);
                continue;
            }*/
            auxiliaryForUsers = users;
            auxiliaryForOnlineUsers = onlineUsers;
            auxiliaryForMatrix = networkMatrix;
            //System.out.println("Buildingname in loop:" + this.getBuildingName());
            guiThread.setModel(networkMatrix, users, onlineUsers,this.getBuildingName());
          //  waiting(7000000);
        }
        System.out.println("ende while");
    }

    public void end(){
        this.exit = true;
    }
}
