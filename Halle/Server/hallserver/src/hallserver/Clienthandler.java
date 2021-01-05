package hallserver;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Semaphore;
public  class Clienthandler implements Runnable {
    private final static Semaphore sem = new Semaphore(1);

    String clientname;
    Socket client;
    BufferedReader b;
    BufferedReader reader;
    private List<Meeting> meetings;
    PrintWriter writer;
    ArrayList<PrintWriter> list_client_writer;
    Hallinformation hall;

    public Clienthandler(Socket client, String name, PrintWriter writer,Hallinformation hall,ArrayList<PrintWriter> list_client_writer, List<Meeting> meetings) {
        this.hall=hall;
        this.list_client_writer=list_client_writer;
        this.meetings=meetings;
        this.client = client;
        this.clientname = name;
        this.writer = writer;
        sendmsgtoclient(hall.currentalgo());
        initilize();

        int i = 0;
        while (i <= 0) {
            try {
                reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                i++;
            } catch (IOException e) {
            }
        }


    }

    //todo this function initilze the client , it should be extended,,friendvaklue random should be implemnted in client
    public void  initilize(){
        if(hall.getGroups()!=null){
        JsonObject statues= new JsonObject();
        statues.addProperty("command","addgroups");
        statues.addProperty("groups",hall.getGroups().size());
        sendmsgtoclient(statues.toString());}
        for(Person temp: hall.getAllPersonsInHall())
        {
            if(temp.isOnline())
            {
                JsonObject person= new JsonObject();
                person.addProperty("command","initpersons");
                person.addProperty("name",temp.getName());
                person.addProperty("x",temp.getPos().x);
                person.addProperty("y",temp.getPos().y);
                person.addProperty("id",temp.getid());
                person.addProperty("admin",temp.admin);
                person.addProperty("position",temp.getVirpos());
                person.addProperty("groupid",temp.getGroupid());
                sendmsgtoclient(person.toString());
            }

        }
        for(Person temp: hall.getAllPersonsAtOffice())
        {
            if(temp.isOnline())
            {
                JsonObject person= new JsonObject();
                person.addProperty("command","initpersons");
                person.addProperty("name",temp.getName());
                person.addProperty("x",temp.getPos().x);
                person.addProperty("y",temp.getPos().y);
                person.addProperty("id",temp.getid());
                person.addProperty("admin",temp.admin);
                person.addProperty("groupid",temp.getGroupid());
                person.addProperty("position",temp.getVirpos());
                sendmsgtoclient(person.toString());
            }

        }
        for(Person temp: hall.getAllPersonsInLobby())
        {
            if(temp.isOnline())
            {
                JsonObject person= new JsonObject();
                person.addProperty("command","initpersons");
                person.addProperty("name",temp.getName());
                person.addProperty("x",temp.getPos().x);
                person.addProperty("y",temp.getPos().y);
                person.addProperty("id",temp.getid());
                person.addProperty("admin",temp.admin);
                person.addProperty("groupid",temp.getGroupid());
                person.addProperty("position",temp.getVirpos());
                sendmsgtoclient(person.toString());
            }

        }
        Gson gson = new Gson();
        JsonObject person= new JsonObject();
        String x=gson.toJson(hall.getUsersData());
        String y=gson.toJson(hall.getFriendsValue());

        person.addProperty("command","initilize");
        person.addProperty("Userdata",x);

        person.addProperty("friendvalue", y);
        String z=gson.toJson(hall.getFriendsValueRandom());
        person.addProperty("friendvaluerandom", z);
        sendmsgtoclient(person.toString());

    }
    @Override
    public void run() {

        String recievedmsg = null;
        try {
            while ((recievedmsg = reader.readLine()) != null) {

                JsonParser parser = new JsonParser();
                JsonObject obj = parser.parse(recievedmsg).getAsJsonObject();
                if (obj.get("command").getAsString().equals("signin")) {


                    Person temp=  hall.getperson(Integer.parseInt(obj.get("id").getAsString()));
                    temp.setOnline(true);
                    System.out.println("client "+ Integer.parseInt(obj.get("id").getAsString()) + " is connected");
                    JsonObject statues= new JsonObject();
                    statues.addProperty("command","statues");
                    if(temp==null)
                        statues.addProperty("state","failed");
                    else
                    {

                        Gson gson = new Gson();
                        String x=gson.toJson(hall.getUsersData());
                        String y=gson.toJson(hall.getFriendsValue());
                        String z=gson.toJson(hall.getFriendsValueRandom());
                        statues.addProperty("state","sucess");
                        statues.addProperty("name",temp.getName());
                        statues.addProperty("x",temp.getPos().x);
                        statues.addProperty("y",temp.getPos().y);
                        statues.addProperty("id",temp.getid());
                        statues.addProperty("position",temp.getVirpos());
                        statues.addProperty("admin",temp.admin);
                        statues.addProperty("Userdata", x);
                        statues.addProperty("friendvalue", y);
                        statues.addProperty("friendvaluerandom", z);
                        statues.addProperty("groupid",temp.getGroupid());
                        sendmsgtoclient(statues.toString());
                        JsonObject person= new JsonObject();
                        person.addProperty("command","addperson");
                        person.addProperty("name",temp.getName());
                        person.addProperty("x",temp.getPos().x);
                        person.addProperty("y",temp.getPos().y);
                        person.addProperty("id",temp.getid());
                        person.addProperty("groupid",temp.getGroupid());

                        person.addProperty("admin",temp.admin);
                        person.addProperty("Userdata",x);
                        person.addProperty("friendvalue", y);
                        person.addProperty("friendvaluerandom", z);
                        person.addProperty("position",temp.getVirpos());

                        sendmsg(person.toString());


                    }

                    continue;

                } else if (obj.get("command").getAsString().equals("move")) {





                }
                else if (obj.get("command").getAsString().equals("enterhall")) {







                }
                else if (obj.get("command").getAsString().equals("entergroup")) {

                    Person current=hall.getpersoninhall(Integer.parseInt(obj.get("id").getAsString()));
                    Person friend=hall.getpersoninhall(Integer.parseInt(obj.get("personid").getAsString()));

                    if(hall.getGroups().size()!=0&&friend.getGroupid()!=-1&&hall.getGroups().get(friend.getGroupid()).getPersons().size()==16&&friend.getGroupid()!=current.getGroupid())
                    {
                        JsonObject statues= new JsonObject();
                        statues.addProperty("command", "stop");
                        statues.addProperty("id", friend.getid());
                        sendmsgtoclient(statues.toString());
                        sendmsg(statues.toString());

                        continue;

                    }
                    else{
                        if(current.getGroupid()==-1&&friend.getGroupid()==-1)
                        {
                            try {



                                sem.acquire();
                                if(current.getGroupid()!=-1||friend.getGroupid()!=-1)
                                    continue;
                                JsonObject statues = new JsonObject();
                                Grp temp = new Grp();

                                temp.add(current);
                                temp.add(friend);
                                try {


                                    Meeting meeting = new Meeting();
                                    temp.setMeetingId(meeting.getMeetingId());
                                    meetings.add(meeting);
                                    friend.setMeetingid(meeting.getMeetingId());
                                    current.setMeetingid(meeting.getMeetingId());
                                    statues.addProperty("pw", meeting.getAttendeePW());
                                    statues.addProperty("meetingid", meeting.getMeetingId());
                                    statues.addProperty("mpw", meeting.getModeratorPW());

                                } catch (Exception t) {

                                    System.out.println("error : " + t);
                                }
                                hall.getGroups().add(temp);
                                friend.setGroupid(hall.getGroups().size() - 1);
                                current.setGroupid(hall.getGroups().size() - 1);

                                statues.addProperty("command", "entergroup1");
                                statues.addProperty("id", current.getid());
                                statues.addProperty("personid", friend.getid());

                                sendmsgtoclient(statues.toString());
                                sendmsg(statues.toString());
                                sem.release();
                                continue;

                                //meeting.join(block.getName());
                                //meeting.join( hall.getAllPersonsInHall().get(friend).getName());


                            }
                            catch (Exception x){
                                System.out.println("errpr");}
                        }
                        else
                        if(current.getGroupid()==-1&&friend.getGroupid()!=-1 && !current.isAlone())
                        {

                            hall.getGroups().get(friend.getGroupid()).getPersons().add(current);
                            current.setMeetingid(friend.getMeetingid());
                               /* for (Meeting meeting :meetings){
                                    if(meeting.getMeetingId()==groups.get(hall.getAllPersonsInHall().get(friend).getGroupid()).getMeetingId())
                                        meeting.join(block.getName());
                                }*/
                            current.setGroupid(friend.getGroupid());
                            JsonObject statues= new JsonObject();
                            statues.addProperty("command", "entergroup2");
                            statues.addProperty("meetingid", current.getMeetingid());
                            statues.addProperty("id", current.getid());
                            statues.addProperty("groupid", friend.getGroupid());

                            sendmsgtoclient(statues.toString());
                            sendmsg(statues.toString());
                            continue;


                        }

                        else
                        if(current.getGroupid()!=-1&&friend.getGroupid()!=-1 && current.getGroupid()!=friend.getGroupid() &&!current.isAlone())
                        {

                            hall.removepersonfromgroup(friend);//Here we must make user left the other meeting

                            hall.getGroups().get(current.getGroupid()).add(friend);
                            friend.setGroupid(current.getGroupid());
                            JsonObject statues= new JsonObject();
                            statues.addProperty("command", "entergroup3");
                            statues.addProperty("id", current.getid());
                            statues.addProperty("personid", friend.getid());
                            statues.addProperty("meetingid", current.getMeetingid());
                            statues.addProperty("groupid", current.getGroupid());
                            sendmsgtoclient(statues.toString());
                            sendmsg(statues.toString());



                        }}


                    continue;



                }
                else if (obj.get("command").getAsString().equals("disconnect")){

                    Person temp=  hall.getperson(Integer.parseInt(obj.get("personid").getAsString()));
                    temp.setOnline(false);
                    writer.close();
                    list_client_writer.remove(writer);

                    System.out.println("client "+ obj.get("personid").getAsString()+ " has disconnected");




                }
                else if (obj.get("command").getAsString().equals("currentalgorithm")){

                    hall.setGroups(new ArrayList<Grp>());
                    if(obj.get("algo").getAsString().equals("Zufällig"))
                    {

                        hall.prepareRandom();
                        Gson gson=new Gson();
                        String z=gson.toJson(hall.getFriendsValueRandom());

                        obj.addProperty("friendvaluerandom", z);
                        sendmsgtoclient(obj.toString());
                        sendmsg(obj.toString());
                        continue;

                    }




                }

                sendmsg(recievedmsg);
            }

        } catch (IOException e) {
            System.out.println(e+ "er");

        }

    }

    public void sendmsg(String msg) {
        // System.out.println("enter prin: "+list_client_writer.size());
        Iterator it = list_client_writer.iterator();
        while (it.hasNext()) {

            PrintWriter listwriter = (PrintWriter) it.next();
            JsonParser parser = new JsonParser();
            JsonObject obj = parser.parse(msg).getAsJsonObject();
            if(listwriter==writer)
                continue;
            try {
                listwriter.println(msg);

                listwriter.flush();

            } catch (Exception e) {
            }
            //   listwriter.close();
        }

    }

    public void sendmsgtoclient(String msg) {
        // System.out.println("enter prin: "+list_client_writer.size());


        try {
            writer.println(msg);

            writer.flush();

        } catch (Exception e) {
        }



    }


}