package Controller;

import BigBlueButton.api.BBBException;
import Halle.Hall;
import Halle.IHall;
import User.MouseGestures;

import User.Person;
import User.Vector2D;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import Server.*;

public class Controller {

    @FXML private Group dc;
    @FXML private TextField Name;
    @FXML private TextField ID;
    @FXML private Button add,test07;
    @FXML private SplitMenuButton user;
    @FXML private SplitMenuButton platzierungBox;
    @FXML private Pane Halle, lobby;
    @FXML private Rectangle office1,office2,office3,office4,Rhalle,lobbyHalle;
    @FXML private CheckBox showPower;
    @FXML private ComboBox tests;
    @FXML private ToggleButton alone;
    @FXML private  Text hallsize;
    private Rectangle areas[];
    private IHall hall;

    private MouseGestures mouseGestures ;
    private Vector2D mouseLocation = new Vector2D( 0, 0);
    private boolean enter=true;

    Client client;
    public void initialize() throws BBBException {
        areas=new Rectangle[6];
        areas[0]=office1;
        areas[1]=office2;
        areas[2]=office3;
        areas[3]=office4;
        areas[4]=Rhalle;

        areas[5]=lobbyHalle;
        hall=new Hall(areas, Halle);
        client=new Client(hall);
        hall.setClient(client);
        mouseGestures = new MouseGestures(areas);
        hall.prepareHalle(dc,Halle,showPower,platzierungBox,alone,user);
        Halle.getChildren().addAll(hall.getPlayfield());
        lobby.getChildren().addAll(hall.getLobbyHalle());
        tests.getItems().addAll(fill_tests());
        selectAngestellter();
        selectHäufigkeit();
        hallsize.setText("max Hall size is : "+hall.getSize()+" Person/s");
        test07.setVisible(false);
        client.startconnection();



    }

    public Controller(){



        // hall.load();


    }
    public  void startserver()
    {





       // server.listentoclients();

    }
    public void connect(){

    }

    public void load() throws BBBException {

        hall.loadUsers();
        hall.addListeners();
        /*hall.setLoadTest(true);
        hall.getTest();//.loadTest01();
        hall.getAlgorithm().enterHall();
        hall.addListeners();
        hall.getAlgorithm().interactionSorting(true);
       hall.addListeners();
        placing.interactionSorting();
        hall.addListeners();*/

       /*for(int i=0; i< 9;i++){
            hall.addPerson("User_"+i,1000+i);
        }

        placing.enterLobby();
        hall.addListeners();
        placing.interactionSorting();
        */

    }
    public  void add() throws BBBException { //add person once he clicked on enter



       // Person person=hall.addPerson(Name.getText(),Integer.parseInt(ID.getText()),"Halle",0,0);
        client.signin(Integer.parseInt(ID.getText()));
      //  hall.addListeners(person);
        /* hall.getAlgorithm().enterHall();*/
        //hall.getAlgorithm().interactionSorting(true);
     /*   if(alone.isSelected()) {
            person.stayAlone();
        }

        hall.addListeners();

        if (platzierungBox.getText().equals("Zufällig")){
            hall.getAlgorithm().randomSorting(true);
        } else if (platzierungBox.getText().equals("Inverse")){
            hall.getAlgorithm().inverseSorting(true);
        } else if(platzierungBox.getText().equals("Interaktionshäufigkeit")){
            hall.getAlgorithm().interactionSorting(true);
        } else if(platzierungBox.getText().equals("Ohne Kräfte")){
            hall.getAlgorithm().withoutPower(true);
        }

        else
            hall.getAlgorithm().interactionSorting(true);*/


    }

    public void selectUser(){

    }
    public void selectAngestellter(){ //SelectStudent action
        user.setText("Benutzer");
        user.setVisible(false);
        platzierungBox.setVisible(true);
        platzierungBox.setDisable(true);
        Name.setVisible(true);
        ID.setVisible(true);
        add.setVisible(true);
        alone.setVisible(true);



    }
    public void selectAdmin(){
        user.setText("Admin");
        platzierungBox.setVisible(true);
        platzierungBox.setDisable(false);
        Name.setVisible(false);
        ID.setVisible(false);
        add.setVisible(false);

    }



    public void groups() throws BBBException {
        //  hall.updateGroups();
        hall.getAlgorithm().pr();
        //placing.changeColor();
    }

    public ObservableList fill_tests(){
        ObservableList<String> test_list = FXCollections.observableArrayList();
        for(int i = 1; i < 16; i++){
            String testName = "Test " + i;
            test_list.add(testName);
        }
        test_list.add("TestAlone");
        return test_list;
    }




    public void selectHäufigkeit() throws BBBException {
        platzierungBox.setText("Interaktionshäufigkeit");
        if(hall.getClient()!=null&&hall.getClient().isconnected())
        hall.getClient().changealgo("interaction");
        if (hall.getAllPersonsInHall().size() > 0){
            hall.getAlgorithm().interactionSorting(true);
        }
      //  hall.addListeners();



    }
    public void selectInverse(){
        platzierungBox.setText("Inverse");
        if(hall.getClient()!=null&&hall.getClient().isconnected())
        hall.getClient().changealgo("inverse");
        if (hall.getAllPersonsInHall().size() > 0){

            hall.getAlgorithm().inverseSorting(true);
        }
       // hall.addListeners();
        //hall.changeColor();

    }
    public void selectZufällig() throws BBBException {
        platzierungBox.setText("Zufällig");
        if(hall.getClient()!=null&&hall.getClient().isconnected())
       hall.getClient().changealgo("Zufällig");
        if (hall.getAllPersonsInHall().size() > 0){
            hall.getAlgorithm().randomSorting(true);
        }
      //  hall.addListeners();


    }
    @FXML
    public void selectTest() throws BBBException{
       /* //System.out.println("onAction");
        hall.setLoadTest(true);
        String selection = (String) tests.getSelectionModel().getSelectedItem();
        switch (selection) {
            case "Test 1":
                selectZufällig();
                selectAngestellter();
                hall.getAlgorithm().randomSorting(true);
                hall.getTest().loadTest01();
                break;
            case "Test 2":
                selectHäufigkeit();
                selectAngestellter();
                hall.getAlgorithm().interactionSorting(true);
                hall.getTest().loadTest02();
                break;
            case "Test 3":
                selectInverse();
                selectAngestellter();
                hall.getAlgorithm().inverseSorting(true);
                hall.getTest().loadTest03();
                break;
            case "TestAlone":
                hall.getTest().loadTestStayAlone();
                break;
            case "Test 4":
                selectZufällig();
                selectAngestellter();
                hall.getAlgorithm().randomSorting(true);
                hall.getTest().loadTest04();
                break;
            case "Test 5":
                hall.getTest().loadTest05();
                break;
            case "Test 6":
                selectHäufigkeit();
                selectAngestellter();
                hall.getTest().loadTest06();
                break;
            case "Test 7":
                selectHäufigkeit();
                selectAngestellter();
                hall.getTest().loadTest07();
                test07.setVisible(true);
                break;
            case "Test 8":
                selectHäufigkeit();
                selectAngestellter();
                hall.getTest().loadTest08();
                break;
            case "Test 9":
                selectHäufigkeit();
                selectAngestellter();
                hall.getTest().loadTest09();
                break;
            case "Test 10":
                hall.getTest().loadTest10();
                selectHäufigkeit();
                selectAngestellter();
                break;
            case "Test 11":
                hall.getTest().loadTest11();
                selectHäufigkeit();
                break;
            case "Test 12":
                hall.getTest().loadTest12();
                selectHäufigkeit();
                selectAdmin();
                break;
            case "Test 13":
                hall.getTest().loadTest13();
                selectHäufigkeit();
                selectAdmin();
                break;
            case "Test 14":
                hall.getTest().loadTest14();
                selectInverse();
                selectAdmin();
                break;
            default:
                selectHäufigkeit();
                selectAngestellter();
                hall.getTest().loadTest15();
                break;
        }
        hall.getAlgorithm().enterHall();
        hall.addListeners();
        //hall.getAlgorithm().interactionSorting(true);*/
    }

    @FXML
    public void selectCurrentTest(){
        //System.out.println("onMouseClicked");

    }

    public void select_test07(){
      //  hall.left(hall.getPersonsInHall().get(1000));
      //  hall.left(hall.getPersonsInHall().get(1000));
    }

    public void selectWithout() {
        platzierungBox.setText("Ohne Kräfte");
        if(hall.getClient()!=null&&hall.getClient().isconnected())
            hall.getClient().changealgo("Ohne Kräfte");

        hall.getAlgorithm().withoutPower(true);

    }

}

