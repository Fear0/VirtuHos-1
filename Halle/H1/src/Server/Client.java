package Server;


import Halle.Grp;
import Halle.Hall;
import Halle.IHall;

import Halle.Meeting;
import User.Person;
import User.Vector2D;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.paint.Color;
import netscape.javascript.JSObject;

import java.io.*;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Client  {
    protected Socket client;
    protected DataInputStream datain;
    protected DataOutputStream dataout;
    protected String messageout, massagein;
    protected IHall hall;
    private int id;
    Person person;
    PrintWriter w;
    OutputStream outputStream;
    DataOutputStream dataOutputStream;
    BufferedReader b;
    boolean isinmeeting=false;
    public boolean isconnected(){
        if(client==null)
            return false;
        return client.isConnected();
    }
    public  Client(IHall hall){


        this.hall=hall;
        this.hall.getMouseGestures().setClient(this);
    };
    public  void startconnection()
    {
        try {
            //client = new Socket("2.tcp.ngrok.io", 18545);
            client = new Socket("localhost", 5555);
            System.out.println("client started");
             outputStream = client.getOutputStream();
            // create a data output stream from the output stream so we can send data through it
             dataOutputStream = new DataOutputStream(outputStream);

            System.out.println("Sending string to the ServerSocket");

            // write the message we want to send

            //dataOutputStream.close(); // close the output stream when we're done
            dataout = new DataOutputStream(client.getOutputStream());
            datain =new DataInputStream(client.getInputStream());
            //senddata("sdfsfdsf");
             w=new PrintWriter((client.getOutputStream()));
             b=new BufferedReader(new InputStreamReader(client.getInputStream()));
            Thread t=new Thread(new connectionmangment());
            t.start();

        }
        catch (IOException e)
        {
            System.out.println(e+ " EEEE");
        }



    }

    public void senddata(String msg) {
      if(!client.isClosed()){


            w.println(msg);
        //    w.println();
            w.flush();
            //dataOutputStream.writeUTF(msg);
            //dataOutputStream.flush(); // send the message
            // dataout.close();

    }}

    //make the group to
    public void entergroup(int id){
        JsonObject temp= new JsonObject();
        temp.addProperty("command","entergroup");
        temp.addProperty("id",person.getMyId());
        temp.addProperty("personid",id);
        senddata(temp.toString());

    }

    public void movesprite(){
    if(person!=null) {
        JsonObject temp = new JsonObject();
        temp.addProperty("command", "movesprite");
        Gson gson = new Gson();
        temp.addProperty("id", person.getMyId());
        String x = gson.toJson(getPerson().getLocation());
        temp.addProperty("location", x);

        temp.addProperty("x", person.getCenterX());
        temp.addProperty("y", person.getCenterY());
        x = gson.toJson(getPerson().getVelocity());
        temp.addProperty("velocity", x);
        x = gson.toJson(getPerson().getAcceleration());
        temp.addProperty("accelration", x);
        senddata(temp.toString());

    }

        //TODO this function should try to sign in and send the password and username , if it is sucessful it should add the person to hall,but currently it checks if the user exist in DB

    }
    public void signin(int id){
        JsonObject temp= new JsonObject();
        temp.addProperty("command","signin");
        temp.addProperty("id",id);
        senddata(temp.toString());


    }

    public void lefthall(){
        JsonObject temp= new JsonObject();
        temp.addProperty("command","lefthall");
        temp.addProperty("id",id);
        senddata(temp.toString());


    }
    public void addperson(Person person ){
        JsonObject temp= new JsonObject();
        temp.addProperty("command","addperson");
        temp.addProperty("name",person.getName());
        temp.addProperty("x",person.getLocation().x);
        temp.addProperty("y",person.getLocation().y);
        temp.addProperty("id",person.getMyId());

        temp.addProperty("position","Rhalle");
        System.out.println(temp.toString());
        senddata(temp.toString());
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public void moveperson(double x, double y , double dragx, double dragy,int id){
        JsonObject temp= new JsonObject();
        temp.addProperty("command","move");
        temp.addProperty("x",x);
        temp.addProperty("y",y);
        temp.addProperty("dragx",dragx);
        temp.addProperty("dragy",dragy);
        temp.addProperty("personid",id);
        senddata(temp.toString());

    }
    public void disconnect(){
        JsonObject temp= new JsonObject();
        temp.addProperty("command","disconnect");
        temp.addProperty("personid",person.getMyId());
        senddata(temp.toString());

        try {
            person=null;
            dataOutputStream.close();
            client.close();



        }

        catch (Exception x){System.out.println(x+ "  rtt");}



    }
    //TODO implement the full function , i just implemented the color function
    public void enterhall(int id ){
        JsonObject temp= new JsonObject();
        temp.addProperty("command","enterhall");

        temp.addProperty("personid",id);
        senddata(temp.toString());

    }
    public void changealgo(String algo ){
        JsonObject temp= new JsonObject();
        temp.addProperty("command","currentalgorithm");

        temp.addProperty("algo",algo);
        senddata(temp.toString());

    }

    class connectionmangment implements   Runnable {
        public void run() {
            try {
                while((massagein = b.readLine())!=null) {
                  //  massagein = b.readLine();
                    JsonParser parser = new JsonParser();
                    JsonObject obj = parser.parse(massagein).getAsJsonObject();
                    if (obj.get("command").getAsString().equals("statues")) {

                        Platform.runLater(() -> {
                            try {
                                if(obj.get("state").getAsString().equals("failed"))
                                {

                                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);

                                    errorAlert.setHeaderText("no such a user");
                                    errorAlert.setContentText("there is no user please check user name or pass");
                                    errorAlert.show();

                                }
                                else {
                                    Person p = hall.addPerson(obj.get("name").getAsString(), Integer.parseInt(obj.get("id").getAsString()), obj.get("position").getAsString(), Double.parseDouble(obj.get("x").getAsString()), Double.parseDouble(obj.get("y").getAsString()));

                                    person=p;
                                    id=Integer.parseInt(obj.get("id").getAsString());
                                    hall.addListeners(p);


                                    Gson gson = new Gson();
                                    String t=obj.get("Userdata").getAsString();
                                    hall.setUsersData(  gson.fromJson(t, new TypeToken<Map<Integer , List<Integer>>>(){}.getType()));
                                    t=obj.get("friendvalue").getAsString();
                                    hall.setFriendsValue( gson.fromJson(t, new TypeToken<Map<Integer , Map<Integer , Double> >>(){}.getType()));
                                    if (obj.get("admin").getAsString().equals("true"))
                                        hall.getselectmenu().setVisible(true);
                                    else
                                        hall.getselectmenu().setVisible(false);
                                    p.display();
                                }

                            } catch (Exception x) {
                                System.out.println(x);


                            }
                        });
                    }
                    else
                    if (obj.get("command").getAsString().equals("movesprite")) {

                        Gson gson = new Gson();
                        String t=obj.get("location").getAsString();
                        Vector2D location = gson.fromJson(t, new TypeToken<Vector2D>(){}.getType());
                        double x=Double.parseDouble(obj.get("x").getAsString());
                        double y=Double.parseDouble(obj.get("y").getAsString());
                        t=obj.get("accelration").getAsString();
                        Vector2D acc=gson.fromJson(t, new TypeToken<Vector2D>(){}.getType());
                        t=obj.get("velocity").getAsString();
                        Vector2D velo=gson.fromJson(t, new TypeToken<Vector2D>(){}.getType());
                        Person temp=hall.getperson(Integer.parseInt(obj.get("id").getAsString()));
                        if(temp!=null){
                        Platform.runLater(() -> {
                            try {

                                temp.setnewinfo(location,acc,velo,x,y);
                                new AnimationTimer() {


                                    @Override
                                    public void handle(long now) {
                                        temp.move();

                                    }

                                };
                                temp.display();

                            } catch (Exception c) {
                                System.out.println(c);


                            }
                        });
                    }}
                    else if (obj.get("command").getAsString().equals("addperson")) {
                        Platform.runLater(() -> {
                            try {
                                Person p = hall.addPerson(obj.get("name").getAsString(), Integer.parseInt(obj.get("id").getAsString()), obj.get("position").getAsString(), Double.parseDouble(obj.get("x").getAsString()), Double.parseDouble(obj.get("y").getAsString()));
                                p.display();
                                Gson gson = new Gson();
                                String t=obj.get("Userdata").getAsString();
                                hall.setUsersData(  gson.fromJson(t, new TypeToken<Map<Integer , List<Integer>>>(){}.getType()));
                                t=obj.get("friendvalue").getAsString();
                                hall.setFriendsValue( gson.fromJson(t, new TypeToken<Map<Integer , Map<Integer , Double> >>(){}.getType()));


                            } catch (Exception x) {
                                System.out.println(111111);
                            }
                        });


                    }
                    else if (obj.get("command").getAsString().equals("stop")) {
                        Platform.runLater(() -> {
                            try {
                               int id=Integer.parseInt(obj.get("stop").getAsString());
                               hall.getperson(id).stop();


                            } catch (Exception x) {
                                System.out.println(x +" 111v");

                            }
                        });


                    }
                    else if (obj.get("command").getAsString().equals("lefthall")) {
                        Platform.runLater(() -> {
                            try {
                                int id=Integer.parseInt(obj.get("id").getAsString());
                                hall.leftHall(id);


                            } catch (Exception x) {
                                System.out.println(x +" 111v");

                            }
                        });


                    } else if (obj.get("command").getAsString().equals("entergroup1")) {

                        Person current =hall.getperson( Integer.parseInt(obj.get("id").getAsString()));
                        Person friend=hall.getperson( Integer.parseInt(obj.get("personid").getAsString()));
                        try{
                        if(person.getMyId()==current.getMyId()&&!isinmeeting) {
                            person.setGroupid(hall.getAlgorithm().getGroups().size() - 1);
                            Meeting meeting = new Meeting(Integer.parseInt(obj.get("meetingid").getAsString()),Integer.parseInt(obj.get("pw").getAsString()),Integer.parseInt(obj.get("mpw").getAsString()));
                            isinmeeting=true;
                            meeting.join(person.getName());
                            System.out.println(obj.get("meetingid").getAsString() +"  + 2  " + obj.get("pw").getAsString());
                        }
                        else
                        if(person.getMyId()==friend.getMyId()&&!isinmeeting)
                        {
                            Meeting meeting = new Meeting(Integer.parseInt(obj.get("meetingid").getAsString()),Integer.parseInt(obj.get("pw").getAsString()),Integer.parseInt(obj.get("mpw").getAsString()));
                            while(!meeting.isrunning()){}
                                meeting.join(person.getName());
                            isinmeeting=true;
                         //   System.out.println(obj.get("meetingid").getAsString() +"  + 2  " + obj.get("pw").getAsString());
                        }}
                        catch (Exception e){}
                        Platform.runLater(() -> {
                            try {
                               // System.out.println("im sexy and i know it ");
                                Grp temp=new Grp();

                                friend.setGroupid(hall.getGroups().size()-1);
                                temp.add(current);
                                temp.add(friend);
                                hall.getAlgorithm().getGroups().add(temp);
                                current.setGroupid(hall.getAlgorithm().getGroups().size()-1);
                                hall.getperson( Integer.parseInt(obj.get("id").getAsString())).setGroupid(hall.getAlgorithm().getGroups().size()-1);
                                hall.getperson( Integer.parseInt(obj.get("personid").getAsString())).setGroupid(hall.getAlgorithm().getGroups().size()-1);
                                hall.setGroups(hall.getAlgorithm().getGroups());

                                hall.getAlgorithm().changeColor();




                            } catch (Exception x) {
                                System.out.println(x);

                            }
                        });


                    }
                    else if (obj.get("command").getAsString().equals("entergroup2")) {
                        Platform.runLater(() -> {
                            try {

                                Grp temp = new Grp();

                                Person current = hall.getperson(Integer.parseInt(obj.get("id").getAsString()));
                                int groupid = Integer.parseInt(obj.get("groupid").getAsString());

                                hall.getAlgorithm().getGroups().get(groupid).add(current);
                                if(person.getMyId()==current.getMyId()&&!isinmeeting) {
                                    person.setGroupid(hall.getAlgorithm().getGroups().size() - 1);
                                    Meeting meeting = new Meeting(Integer.parseInt(obj.get("meetingid").getAsString()),0,0);
                                    isinmeeting=true;
                                    while(!meeting.isrunning()){}
                                    meeting.join(person.getName());
                                    System.out.println(obj.get("meetingid").getAsString() +"  + 2  " + 0);
                                }
                                current.setGroupid(groupid);
                                hall.setGroups(hall.getAlgorithm().getGroups());
                                hall.getAlgorithm().changeColor();

                            } catch (Exception x) {
                                System.out.println(x+"13331131"); //+ " this execption is normal because the random hashmap not initiziled //todo initilazie it  ");


                            }
                        });

                    }
                    else if (obj.get("command").getAsString().equals("entergroup3")) {
                        Platform.runLater(() -> {
                            try {
                                Grp temp = new Grp();
                                Person current = hall.getperson(Integer.parseInt(obj.get("id").getAsString()));
                                Person friend=hall.getperson(Integer.parseInt(obj.get("personid").getAsString()));
                                hall.getAlgorithm().removeperson(friend);
                                hall.getAlgorithm().getGroups().get(current.getGroupid()).add(friend);
                                hall.setGroups(hall.getAlgorithm().getGroups());
                                int groupid = Integer.parseInt(obj.get("groupid").getAsString());
                                friend.setGroupid(groupid);
                                hall.getperson(Integer.parseInt(obj.get("personid").getAsString())).setGroupid(groupid);
                                if(friend.getMyId()==id) {
                                    person.setGroupid(groupid);
                                    Meeting meeting = new Meeting(Integer.parseInt(obj.get("meetingid").getAsString()),0,0);
                                    isinmeeting=true;
                                    while(!meeting.isrunning()){}
                                    meeting.join(person.getName());
                                    System.out.println(obj.get("meetingid").getAsString() +"  + 2  " );

                                }



                                hall.getAlgorithm().changeColor();

                            } catch (Exception x) {
                                System.out.println(x+ "2333"); //+ " this execption is normal because the random hashmap not initiziled //todo initilazie it  ");


                            }
                        });

                    }
                    else if (obj.get("command").getAsString().equals("initpersons")) {
                        Platform.runLater(() -> {
                            try {
                                Person p = hall.addPerson(obj.get("name").getAsString(), Integer.parseInt(obj.get("id").getAsString()), obj.get("position").getAsString(), Double.parseDouble(obj.get("x").getAsString()), Double.parseDouble(obj.get("y").getAsString()));
                                int groupid=Integer.parseInt(obj.get("groupid").getAsString());
                                    p.setGroupid(-1);
                                    if(groupid!=-1){
                                        hall.getAlgorithm().getGroups().get(groupid).add(p);
                                        p.setGroupid(groupid);
                                        hall.getAlgorithm().changeColor();
                                    }
                                    p.display();






                            } catch (Exception x) {
                                System.out.println(133331); //+ " this execption is normal because the random hashmap not initiziled //todo initilazie it  ");


                            }
                        });


                    }
                    else if (obj.get("command").getAsString().equals("addgroups")) {
                        Platform.runLater(() -> {
                            try {
                                int groupsize=Integer.parseInt(obj.get("groups").getAsString());
                                for(int i = 0;i<groupsize;i++)
                                    hall.getAlgorithm().getGroups().add(new Grp());
                                hall.setGroups(hall.getAlgorithm().getGroups());







                            } catch (Exception x) {
                                System.out.println(133331); //+ " this execption is normal because the random hashmap not initiziled //todo initilazie it  ");


                            }
                        });


                    }

                    else
                    if (obj.get("command").getAsString().equals("initilize")) {
                        Platform.runLater(() -> {
                            try {

                                Gson gson = new Gson();
                                String t=obj.get("Userdata").getAsString();
                                hall.setUsersData(  gson.fromJson(t, new TypeToken<Map<Integer , List<Integer>>>(){}.getType()));
                                t=obj.get("friendvalue").getAsString();
                                hall.setFriendsValue( gson.fromJson(t, new TypeToken<Map<Integer , Map<Integer , Double> >>(){}.getType()));


                            } catch (Exception x) {
                                System.out.println(1112211);


                            }
                        });


                    }
                    else if (obj.get("command").getAsString().equals("currentalgorithm")) {
                        Platform.runLater(() -> {
                            try {
                                hall.getAlgorithm().setGroups(new ArrayList<Grp>());
                                hall.setGroups(new ArrayList<Grp>());

                                if(obj.get("algo").getAsString().equals("interaction"))
                                {
                                    hall.getPlatzierungBox().setText("Interaktionhäufigkeit");
                                    hall.getAlgorithm().interactionSorting(true);

                                }
                                else if(obj.get("algo").getAsString().equals("inverse"))
                                {
                                    hall.getPlatzierungBox().setText("inverse");
                                    hall.getAlgorithm().inverseSorting(true);
                                }
                                else if(obj.get("algo").getAsString().equals("Zufällig"))
                                {
                                    Gson gson = new Gson();

                                    String t=obj.get("friendvaluerandom").getAsString();
                                    hall.setFriendsValueRandom( gson.fromJson(t, new TypeToken<Map<Integer , Map<Integer , Double> >>(){}.getType()));
                                    hall.getPlatzierungBox().setText("Zufällig");
                                    hall.getAlgorithm().randomSorting(true);

                                }
                                else
                                {
                                    hall.getPlatzierungBox().setText("Ohne Kräfte");
                                    hall.getAlgorithm().withoutPower(true);
                                }
                                hall.getAlgorithm().enterHall();


                            } catch (Exception x) {
                                System.out.println(x);


                            }
                        });


                    }
                    else if (obj.get("command").getAsString().equals("move")) {
                        Platform.runLater(() -> {
                            try {
                                Person temp=hall.getperson(Integer.parseInt(obj.get("personid").getAsString()));

                               temp.setLocationOffset(Double.parseDouble(obj.get("x").getAsString()), Double.parseDouble(obj.get("y").getAsString()));
                                temp.display();
                                // temp.setLocation(0,0);
                            } catch (Exception x) {
                                System.out.println(x);


                            }
                        });


                    }
                    else
                    if(obj.get("command").getAsString().equals("enterhall")) {
                        Platform.runLater(() -> {
                            try {
                                Person temp=hall.getperson(Integer.parseInt(obj.get("personid").getAsString()));

                                temp.getCircle().setFill(Color.RED);
                                temp.display();
                                // temp.setLocation(0,0);
                            } catch (Exception x) {
                                System.out.println(x);


                            }
                        });
                    }
                    else
                    if(obj.get("command").getAsString().equals("disconnect")) {
                        Platform.runLater(() -> {
                            try {
                                //TODO implemntation of disconnection notification

                                hall.left(Integer.parseInt(obj.get("personid").getAsString()));
                            } catch (Exception x) {
                                System.out.println(x);


                            }
                        });
                    }

                }
            } catch (IOException e) {
                System.out.println(e+ " 11");



            }
        }
    }



}