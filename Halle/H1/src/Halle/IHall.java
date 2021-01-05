package Halle;

import Server.Client;
import User.Layer;
import User.MouseGestures;
import User.Person;
import javafx.scene.Group;
import javafx.scene.control.CheckBox;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface IHall {
    /**
     *
     * @param algorithm
     */
    public void setAlgorithm(placingAlgorithm algorithm) ;

    /**
     *
     * @return
     */
    public int getSize();

    /**
     *
     * @param size
     */
    public void setSize(int size) ;
    public SplitMenuButton getPlatzierungBox() ;
    /**
     *
     * @param loadTest
     */
    public void setLoadTest(boolean loadTest);


    /**
     *
     * @return
     */
    public CheckBox getShowPower();
    public Person getperson(int id );
    public void  setClient(Client c);
    public Pane getPaneHalle() ;

    /**
     *
     * @return
     */
    public Test getTest();

    public ArrayList<Grp> getGroups();

    /**
     *
     * @return
     */
    public List<Person> getAllPersonsAtOffice();

    /**
     *
     * @return
     */
    public Map<Integer, List<Integer>> getUsersData();

    /**
     *
     * @return
     */
    public Map<Integer, Integer> getPersonsInHall();

    /**
     *
     * @return
     */
    public List<Person> getAllPersonsInHall();

    /**
     *
     * @return
     */
    public List<Person> getAllPersonsInLobby();

    /**
     *
     * @return
     */
    public placingAlgorithm getAlgorithm();

    /**
     *
     * @return
     */
    public Layer getPlayfield();

    /**
     *
     * @return
     */
    public MouseGestures getMouseGestures();
    public Layer getLobbyHalle();
    public void prepareHalle(Group dc, Pane paneHalle, CheckBox showPower, SplitMenuButton platzierungBox, ToggleButton alone,SplitMenuButton user);
    public Person addPerson(String name, int id, String position,double x , double y);
    public void addPersonInHall(Person person);
    public void addListeners(Person p);
    public void addListeners();
    public SplitMenuButton getselectmenu();
    public void leftHall();
    public void left();
    public void left(int id);
    public Client getClient();
    public void enterHall(int id);
    public void removeStrength();
    public void showStrength(int value);
    public void setFriendValue(Integer id1, Integer id2, double value);
    public void setFriendValueRandom(Integer id1, Integer id2, double value);
    public Double getFriendValue(Person user, Integer friend);
    public Person getFriend(Person user, Integer friend);

    public void setUsersData(Map<Integer, List<Integer>> usersData);
    public void setFriendsValue(Map<Integer, Map<Integer, Double>> friendsValue);
    public Map<Integer, Map<Integer, Double>> getFriendsValue();
    public double getFriendValueRandom(Person user,int friend);
    public Map<Integer, Map<Integer, Double>> getFriendsValueRandom();
    public void setFriendsValueRandom(Map<Integer, Map<Integer, Double>> friendsValueRandom);
    public void loadUsers();
    public void leftHall(int id );
    public void setGroups(ArrayList<Grp> groups);
}
