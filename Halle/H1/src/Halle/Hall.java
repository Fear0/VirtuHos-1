package Halle;


import BigBlueButton.api.BBBException;
import Server.Client;
import User.*;
import com.google.gson.reflect.TypeToken;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;


import java.util.*;

public class Hall extends  Area implements IHall {

    int size=33;

    public Map<Integer, Map<Integer, Double>> getFriendsValue() {
        return friendsValue;
    }

    public void setFriendsValue(Map<Integer, Map<Integer, Double>> friendsValue) {
        this.friendsValue = friendsValue;
    }

    public Map<Integer, Map<Integer, Double>> getFriendsValueRandom() {
        return friendsValueRandom;
    }

    public void setFriendsValueRandom(Map<Integer, Map<Integer, Double>> friendsValueRandom) {
        this.friendsValueRandom = friendsValueRandom;
    }

    Random random = new Random();
    private Group dc;

    public void setUsersData(Map<Integer, List<Integer>> usersData) {
        this.usersData = usersData;

    }

    private Pane paneHalle;
    private CheckBox showPower;
    private SplitMenuButton platzierungBox;
    private placingAlgorithm Algorithm= new placingAlgorithm(this);

    Alert errorAlert = new Alert(Alert.AlertType.ERROR);

    private Map<Integer , List<Integer> > usersData = new HashMap< >(); //person#s friend data

    public Client getClient() {
        return client;
    }


    private Map<Integer , Integer > personsInHall = new HashMap<>(); // person index in our little database will be change with a right database connection
    private Map<Integer , Map<Integer , Double> > friendsValue = new HashMap<>();
    private Map<Integer , Map<Integer , Double> > friendsValueRandom = new HashMap<>();

    private Layer playfield =new Layer( Settings.SCENE_WIDTH, Settings.SCENE_HEIGHT);
    private Layer lobbyHalle =new Layer( Settings.Lobby_WIDTH, Settings.Lobby_HEIGHT);
    private ArrayList<Grp> groups = new ArrayList<Grp>();
    private MouseGestures mouseGestures;
    private Vector2D mouseLocation = new Vector2D( 0, 0);

    private List<Person> allPersonsAtOffice  = new ArrayList<>();
    private List<Person> allPersonsInHall = new ArrayList<>();
    private List<Person> allPersonsInLobby = new ArrayList<>();
    private List<Meeting> meetings = new ArrayList<>();
    private List<Line> lines = new ArrayList<>();
    private Test test =new Test(this);
    private boolean loadTest =false;
     private Client client;
    SplitMenuButton user;

    private  ToggleButton alone;


    public SplitMenuButton getselectmenu(){return user;}
    public void setAlgorithm(placingAlgorithm algorithm) {
        Algorithm = algorithm;
    }
    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setLoadTest(boolean loadTest) { this.loadTest = loadTest; }

    public CheckBox getShowPower() {return showPower;}

    public ToggleButton getAlone() { return alone; }
    public Person getperson(int id ){

        for(Person x : allPersonsInHall)
            if(x.getMyId()==id)
                return x;
        return null;
    }
    public void  setClient(Client c)
    {
        client=c;
        Algorithm.setClient(c);

    }


    public Pane getPaneHalle() { return paneHalle; }

    public Test getTest() { return  test; }

    public ArrayList<Grp> getGroups() { return this.groups; }

    public List<Person> getAllPersonsAtOffice() { return allPersonsAtOffice; }

    public Map<Integer, List<Integer>> getUsersData() { return usersData; }

    public Map<Integer, Integer> getPersonsInHall() { return personsInHall; }

    public List<Person> getAllPersonsInHall() { return allPersonsInHall; }

    public List<Person> getAllPersonsInLobby() { return allPersonsInLobby; }

    public placingAlgorithm getAlgorithm() { return Algorithm; }



    public Layer getPlayfield() { return playfield; }

    public Layer getLobbyHalle() { return lobbyHalle; }

    public MouseGestures getMouseGestures() {
        return mouseGestures;
    }

    public Hall(Rectangle areas[], Pane Halle){
        super("Hall");
        this.client=client;

        this.Algorithm = new placingAlgorithm(this);
        playfield.setLayoutX(Halle.localToScene(Halle.getLayoutBounds()).getCenterX()-230);
        playfield.setLayoutY(Halle.localToScene(Halle.getLayoutBounds()).getCenterY());
        playfield.setTranslateX(0);
        playfield.setLayoutY(0);
        /*playfield.setPrefHeight(422);
        playfield.setPrefWidth(695);*/

        mouseGestures = new MouseGestures(areas);

        if(!loadTest) {

            /*loadUsers();
            setFriendValue(1000,1001,1.4);
            setFriendValue(1001,1002,1.6);
            setFriendValue(1002,1003,1.9);
            setFriendValue(1004,1005,1.0);
            setFriendValue(1004,1008,1.8);
            setFriendValue(1006,1007,1.6);
            System.out.println( friendsValue.get(1000).get(usersData.get(1000).get(0)));
            System.out.println( friendsValue.get(1001).get(usersData.get(1001).get(2)));
            System.out.println( friendsValue.get(1002).get(usersData.get(1001).get(4)));
             */
        }

    }

    /**
     *
     * @param dc
     * @param paneHalle
     * @param showPower
     * @param platzierungBox
     */
    public void prepareHalle(Group dc, Pane paneHalle, CheckBox showPower, SplitMenuButton platzierungBox, ToggleButton alone,SplitMenuButton user) {
        this.dc=dc;
        this.paneHalle=paneHalle;
        this.showPower=showPower;
        this.platzierungBox=platzierungBox;
        this.alone=alone;
        this.user=user;
    }



    /**
     *
     * @param name
     * @param id
     * @param position
     * @return
     * changed from void to Person in order to reach the Person's elements in other functions
     */
    public Person addPerson(String name, int id, String position,double realx ,double realy ) {


        // random location
        double x = realx;
        double y = realy;
        if(x==0&&y==0)
        while (x < 234 || y < 320) {
            x = random.nextDouble() * 600;
            y = random.nextDouble() * 358;
        }

        // dimensions
        double width = 50;
        double height = width / 2.0;
        // create vehicle data
        Vector2D location = new Vector2D( x,y);
        Vector2D velocity = new Vector2D( 0,0);
        Vector2D acceleration = new Vector2D( 0,0);

        // create Person and add to layer
        Person person = new Person( playfield,location, velocity, acceleration, width, height,name,id);

        if (position.equals("Rhalle")||  (!position.equals("lobby")))
        {
            person.setRoom(position);
            if (position.equals("Rhalle")) {
                allPersonsInHall.add(person);
                allPersonsInHall.forEach(Sprite::display);

                personsInHall.put(person.getMyId(), allPersonsInHall.size() - 1);
                allPersonsInHall.get(allPersonsInHall.size() - 1).setVirtualLocation("Rhalle");
                person.setLocation(location.x,location.y);
                //showStrength();



            }
            else {

                allPersonsAtOffice.add(person);
                allPersonsAtOffice.forEach(Sprite::display);

                allPersonsAtOffice.get(allPersonsAtOffice.size() - 1).setVirtualLocation("Office1");

                //showStrength();

            }
        }

        else {
            person.setLocation(323,489);
            allPersonsInLobby.add(person);
            allPersonsInLobby.get(allPersonsInLobby.size() - 1).setVirtualLocation("lobbyHalle");
            allPersonsInLobby.forEach(Sprite::display);

        }

        usersData.put(id,new ArrayList<Integer>());
        friendsValue.put(id,new HashMap<Integer, Double>());
        friendsValueRandom.put(id,new HashMap<Integer, Double>());

        for (int i=0;i<getAllPersonsInHall().size();i++)
        {
            setFriendValue(person.getMyId(), getAllPersonsInHall().get(i).getMyId(),0.0 );
        }
        for (int i=0;i<getAllPersonsInHall().size();i++)
        {
            setFriendValueRandom(person.getMyId(), getAllPersonsInHall().get(i).getMyId(),5 );
        }
        return person;

    }


    /**
     *
     * @param person
     */
    public void addPersonInHall(Person person) {

        allPersonsInHall.add(person);
        allPersonsInHall.forEach(Sprite::display);
        personsInHall.put(person.getMyId(), allPersonsInHall.size()-1);
        allPersonsInHall.get(allPersonsInHall.size()-1).setVirtualLocation("Rhalle");
        //showStrength();

    }


    /**
     *
     */
    public void addListeners(Person p) {


        mouseGestures.makeDraggable(p);
        /*// capture mouse position
        dc.addEventFilter(MouseEvent.ANY, e -> {
            mouseLocation.set(e.getX(), e.getY());
        });

        // move Person via mouse
        for (Person user : allPersonsInHall) {
            if(user.getMyId()==id)
            { mouseGestures.makeDraggable(user);
            break;}

        }
        for (Person user : allPersonsInLobby) {
            if(user.getMyId()==id)
            {  mouseGestures.makeDraggable(user);
            break;}
        }
        for (Person user : allPersonsAtOffice) {
            if (user.getMyId() == id)
            {   mouseGestures.makeDraggable(user);
            break;}*
        }*/
    }
    public void addListeners() {

        // capture mouse position
        dc.addEventFilter(MouseEvent.ANY, e -> {
            mouseLocation.set(e.getX(), e.getY());
        });

        // move Person via mouse
        for (Person user : allPersonsInHall) {
            mouseGestures.makeDraggable(user);

        }
        for (Person user : allPersonsInLobby) {
            mouseGestures.makeDraggable(user);
        }
        for (Person user : allPersonsAtOffice) {
            mouseGestures.makeDraggable(user);
        }
    }

    public void leftHall(int id ){

        Person temp=getperson(id);

        allPersonsAtOffice.add(temp);
        personsInHall.remove( temp.getMyId());
        allPersonsInHall.remove(temp);
        for (int i = 0; i< allPersonsInHall.size(); i++)
        {
            personsInHall.put(allPersonsInHall.get(i).getMyId(),i);
        }

        //showStrength();

    }
    public void leftHall(){

        {


            allPersonsAtOffice.add(client.getPerson());
            personsInHall.remove(client.getPerson().getMyId());
            allPersonsInHall.remove(client.getPerson());
            for (int i = 0; i < allPersonsInHall.size(); i++) {
                personsInHall.put(allPersonsInHall.get(i).getMyId(), i);
            }
        }
        //showStrength();

    }

    public void left(int id){

        personsInHall.remove( id);
        for(int i =0;i<allPersonsInHall.size();i++)
            if(allPersonsInHall.get(i).getMyId()==id)
            {
                allPersonsInHall.get(i).setVisible(false);
                allPersonsInHall.remove(i);
                for (int j = 0; j< allPersonsInHall.size(); j++)
                {
                    personsInHall.put(allPersonsInHall.get(j).getMyId(),j);
                }
            break;}


        //showStrength();

    }

    public void setGroups(ArrayList<Grp> groups) {
        this.groups = groups;
    }

    public void left(){
        client.getPerson().setVisible(false);
        personsInHall.remove( client.getPerson().getMyId());
        allPersonsInHall.remove(client.getPerson());
        for (int i = 0; i< allPersonsInHall.size(); i++)
        {
            personsInHall.put(allPersonsInHall.get(i).getMyId(),i);
        }
        client.disconnect();
        Map<Integer , Integer > personsInHall = new HashMap<>(); // person index in our little database will be change with a right database connection
        Map<Integer , Map<Integer , Double> > friendsValue = new HashMap<>();
         Map<Integer , Map<Integer , Double> > friendsValueRandom = new HashMap<>();

         ArrayList<Grp> groups = new ArrayList<Grp>();
         MouseGestures mouseGestures;
         Vector2D mouseLocation = new Vector2D( 0, 0);

        List<Person> allPersonsAtOffice  = new ArrayList<>();
         List<Person> allPersonsInHall = new ArrayList<>();
        List<Person> allPersonsInLobby = new ArrayList<>();
        List<Meeting> meetings = new ArrayList<>();
         List<Line> lines = new ArrayList<>();

        //showStrength();

    }

    /**
     *
     * @param id
     */
    public void enterHall(int id){

       /* if(size==allPersonsInHall.size())
        {
            errorAlert.setHeaderText("no place");
            errorAlert.setContentText("there is no place , please wait in your office");
            errorAlert.show();
            allPersonsAtOffice.get(id).setVirtualLocation("Office");
        }
        else */{
            Person person = allPersonsAtOffice.get(id);

            person.getCircle().setFill(Color.RED);
            client.enterhall(person.getMyId());
            person.setVirtualLocation("Rhalle");
            allPersonsInHall.add(person);

            allPersonsAtOffice.remove(id);

            personsInHall.put(person.getMyId(), allPersonsInHall.size() - 1);
            friendsValueRandom.put(person.getMyId(), new HashMap<Integer, Double>());
            Random r = new Random();
            int i=0;
          //  Collections.shuffle(allPersonsInHall);
            for (Person per : allPersonsInHall) {
                double x = r.nextDouble() *2;
                setFriendValueRandom(person.getMyId(), per.getMyId(), x);
                i++;
                if(i==2)
                    break;

            }

        }

        //showStrength();

    }

    /**
     *
     * @throws BBBException
     */
    /*void updateGroups() throws BBBException {
        groups = new ArrayList<Grp>();
        for (Person current : allPersonsInHall) {
            // find group with current
            Grp currentGroup = new Grp(playfield);
            boolean foundGroup = false;
            for (Grp possibleGroup : groups) {
                if (possibleGroup.getPersons().contains(current)) {
                    currentGroup = possibleGroup;
                    foundGroup = true;
                    break;
                }
            }
            // if person is not in a group already, set group id and add group to hall
            if (!foundGroup && !current.isAlone()) {
                currentGroup.setGroupid(groups.size());
                currentGroup.add(current);
                groups.add(currentGroup);
            }
            else {

            }

            // check for neighbours
            for (Person possibleNeighbour : allPersonsInHall) {
                // euclidean distance

                if (Vector2D.subtract(current.getLocation(), possibleNeighbour.getLocation()).magnitude() < Settings.GROUP_RADIUS) {
                    if (!currentGroup.persons.contains(possibleNeighbour) && !possibleNeighbour.isAlone()) {
                        currentGroup.add(possibleNeighbour);



                    }
                }
            }

        }
    }
*/
    /**
     *
     */
    public void removeStrength(){
        paneHalle.getChildren().removeAll(lines);
    }

    public SplitMenuButton getPlatzierungBox() {
        return platzierungBox;
    }

    /**
     * Show strength between persons
     * @param value
     *
     */
    public void showStrength(int value){
        paneHalle.getChildren().removeAll(lines);
        lines= new ArrayList<>();
        Map<Integer,Map<Integer,Double>> temp=null;

        if (value != 0)
            temp=friendsValue;
        else
            temp=friendsValueRandom;


            for (int i = 0; i < allPersonsInHall.size(); i++) {
                for (int j = 0; j < allPersonsInHall.size(); j++) {
                    if (i == j || temp.get(allPersonsInHall.get(i).getMyId()) == null || temp.get(allPersonsInHall.get(i).getMyId()).get(allPersonsInHall.get(j).getMyId()) == null)
                        continue;
                    Line line = new Line();

                    double strength = temp.get(allPersonsInHall.get(i).getMyId()).get(allPersonsInHall.get(j).getMyId());

                    if(strength >=0.0 && strength < 0.5) {
                        line.setStroke(Color.RED);
                        line.setStrokeWidth(0.009);
                        line.setStrokeType(StrokeType.OUTSIDE);
                    }
                    if(strength >= 0.5 && strength<= 0.7)
                        line.setStroke(Color.BLUE);
                    if( strength> 0.7)
                        line.setStroke(Color.GREEN);

                    line.setStartX(allPersonsInHall.get(i).getLocation().x);
                    line.setStartY(allPersonsInHall.get(i).getLocation().y);

                    line.setEndX(allPersonsInHall.get(j).getLocation().x);
                    line.setEndY(allPersonsInHall.get(j).getLocation().y);

                    if (platzierungBox.getText().equals("Inverse")) {
                        // line.setStrokeWidth(1 - friendsValue.get(allPersonsInHall.get(i).getMyId()).get(allPersonsInHall.get(j).getMyId()));
                        if( strength > 0.7 && strength<= 1.0){
                            line.setStroke(Color.RED);
                            line.setStrokeWidth(0.009);
                            line.setStrokeType(StrokeType.OUTSIDE);
                        }
                        if(strength >= 0.5 && strength<= 0.7)
                            line.setStroke(Color.BLUE);

                        if(strength >=0.0 && strength < 0.5){
                            line.setStroke(Color.GREEN);
                            line.setStrokeWidth(0.6);}

                    } else {

                        //line.setStrokeWidth(friendsValue.get(allPersonsInHall.get(i).getMyId()).get(allPersonsInHall.get(j).getMyId()));

                    }


                    //line.setStrokeType(StrokeType.OUTSIDE);
                    // line.getStrokeDashArray().setAll(1.0, 4.0);
                    line.toBack();
                    lines.add(line);
                    paneHalle.getChildren().add(line);

                }
            }
        }





  /* public void loadUsers()
    {

        for (int i=0; i<Settings.USERS_COUNT;i++)
        {
            List<Integer> users= new ArrayList<>();

            for (int k=0; k< Settings.USERS_COUNT;k++)
            {   if((1000+i)!=(1000+k))
                users.add(1000+k);
            }
            usersData.put(1000+i,users);
            for (int j=0; j<usersData.get(1000+i).size();j++)
            {   personValue =new HashMap<>();
                personValue.put(usersData.get(1000+i).get(j),0.0);
            }
            friendsValue.put(1000+i,personValue);
        }
    }*/

    public void loadUsers()
    {
            setFriendValue(1000,1001,1.4);
            setFriendValue(1001,1002,1.6);

    }

    /**
     *
     * @param id1
     * @param id2
     * @param value
     */
    public void setFriendValue(Integer id1, Integer id2, double value)
    {
        usersData.get(id1).add(id2);
        usersData.get(id2).add(id1);
        friendsValue.get(id1).put(id2,value);
        friendsValue.get(id2).put(id1,value);

    }

    public void setFriendValueRandom(Integer id1, Integer id2, double value)
    {

        friendsValueRandom.get(id1).put(id2,value);
        friendsValueRandom.get(id2).put(id1,value);

    }



    /**
     *
     * @param user
     * @param friend
     * @return friendValue
     */
    public Double getFriendValue(Person user, Integer friend){

        if (friendsValue.get(user.getMyId()).get(usersData.get(user.getMyId()).get(friend))!= null)
            return friendsValue.get(user.getMyId()).get(usersData.get(user.getMyId()).get(friend));

        else return 0.0;

    }

    /**
     *
     * @param user
     * @param friend
     * @return friend
     */
    public Person getFriend(Person user, Integer friend){
        if (personsInHall.containsKey(usersData.get(client.getPerson().getMyId()).get(friend)))
            return allPersonsInHall.get(personsInHall.get(usersData.get(client.getPerson().getMyId()).get(friend)));

        else
            return null;
    }
        public double randomGenerator(double min, double max){
        double x=  (Math.random() * ((max-min )+1 )) +min;
        return  x;
        }

    public double getFriendValueRandom(Person user,int friend){

        if (friendsValueRandom.get(user.getMyId()).get(usersData.get(user.getMyId()).get(friend))!= null)
            return friendsValueRandom.get(user.getMyId()).get(usersData.get(user.getMyId()).get(friend));

        else return 0.0;

    }




}

