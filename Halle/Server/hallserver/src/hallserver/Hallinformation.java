package hallserver;

import com.google.gson.JsonObject;
import netscape.javascript.JSObject;

import java.util.*;

public class Hallinformation {
    private Map<Integer , List<Integer>> usersData = new HashMap< >(); //person#s friend data
    private Map<Integer , Integer > personsInHall = new HashMap<>(); // person index in our little database will be change with a right database connection
    private Map<Integer , Map<Integer , Double> > friendsValue = new HashMap<>();
    private Map<Integer , Map<Integer , Double> > friendsValueRandom = new HashMap<>();
    private String currentalgorithm="interaction";
    private ArrayList<Grp> groups = new ArrayList<Grp>();



    private List<Person> allPersonsAtOffice  = new ArrayList<>();
    private List<Person> allPersonsInHall = new ArrayList<>();
    private List<Person> allPersonsInLobby = new ArrayList<>();

    private boolean loadTest =false;

    public  Hallinformation(){

        addPerson("abd",1,"Rhalle",0,0,false,true);
        addPerson("kiswani",2,"Rhalle",0,0,false,false);
        addPerson("tom",3,"Rhalle",0,0,false,false);
        setFriendValue(3,2,1);
        setFriendValue(1,3,0.5);
       // setFriendValue(3,2,1);
        addPerson("elisa",4,"Rhalle",0,0,false,false);
        addPerson("jendrik",5,"Rhalle",0,0,false,false);
         setFriendValue(4,5,1);
        setFriendValue(4,1,1);
        setFriendValue(1,5,1);
    }
    public String currentalgo(){
        JsonObject temp= new JsonObject();
        temp.addProperty("command","currentalgorithm");
        temp.addProperty("algo",currentalgorithm);
        return temp.toString();

    }
    //todo this function should be extended to search in  DB
    public Person getperson(int id){
        for(Person x : allPersonsInHall)
            if(x.getid()==id)
                return x;
        for(Person x : allPersonsInLobby)
            if(x.getid()==id)
                return x;
        for(Person x : allPersonsAtOffice)
            if(x.getid()==id)
                return x;
        return null;
    }
    public Person getpersoninhall(int id){
        for(Person x : allPersonsInHall)
            if(x.getid()==id)
                return x;
        return null;
    }
    public Map<Integer, List<Integer>> getUsersData() {
        return usersData;
    }

    public void setUsersData(Map<Integer, List<Integer>> usersData) {
        this.usersData = usersData;
    }

    public Map<Integer, Integer> getPersonsInHall() {
        return personsInHall;
    }

    public void setPersonsInHall(Map<Integer, Integer> personsInHall) {
        this.personsInHall = personsInHall;
    }

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

    public ArrayList<Grp> getGroups() {
        return groups;
    }

    public void setGroups(ArrayList<Grp> groups) {
        this.groups = groups;
    }

   public boolean removepersonfromgroup(Person person){
        Person p = person;
        for(int i=0;i<groups.size();i++)
        {
            if(groups.get(i).persons.remove(person))
            {
                person.resetgroup();
                if(groups.get(i).persons.size()<=1) {

                    if(groups.get(i).persons.size()==1) {

                        groups.get(i).persons.get(0).resetgroup();
                    }
                    groups.remove(groups.get(i));
                    for(int j=0;j<groups.size();j++)
                    {
                        for(int k=0;k<groups.get(j).getPersons().size();k++)
                            groups.get(j).getPersons().get(k).setGroupid(j);
                    }
                }

                return true;

            }

        }
        return false;


    }

    public List<Person> getAllPersonsAtOffice() {
        return allPersonsAtOffice;
    }

    public void setAllPersonsAtOffice(List<Person> allPersonsAtOffice) {
        this.allPersonsAtOffice = allPersonsAtOffice;
    }

    public List<Person> getAllPersonsInHall() {
        return allPersonsInHall;
    }

    public void setAllPersonsInHall(List<Person> allPersonsInHall) {
        this.allPersonsInHall = allPersonsInHall;
    }

    public List<Person> getAllPersonsInLobby() {
        return allPersonsInLobby;
    }

    public void setAllPersonsInLobby(List<Person> allPersonsInLobby) {
        this.allPersonsInLobby = allPersonsInLobby;
    }

    public boolean isLoadTest() {
        return loadTest;
    }

    public void setLoadTest(boolean loadTest) {
        this.loadTest = loadTest;
    }


    public Person addPerson(String name, int id, String position,double realx ,double realy,boolean alone,boolean admin ) {


        // random location
        double x = realx;
        double y = realy;
        Random random=new Random();
        if(x==0&&y==0)
            while (x < 234 || y < 320) {
                x = random.nextDouble() * 600;
                y = random.nextDouble() * 358;
            }

        // dimensions
        double width = 50;
        double height = width / 2.0;
        // create vehicle data


        // create Person and add to layer
        Person person = new Person( x,y, alone,id, name,position,admin);

        if (position.equals("Rhalle")||  (!position.equals("lobby")))
        {
            person.setVirpos(position);
            if (position.equals("Rhalle")) {
                allPersonsInHall.add(person);

                personsInHall.put(person.getid(), allPersonsInHall.size() - 1);





            }
            else {

                allPersonsAtOffice.add(person);




            }
        }

        else {
            person.setPos(new Vector2D(323,489));
            allPersonsInLobby.add(person);


        }

        usersData.put(id,new ArrayList<Integer>());
        friendsValue.put(id,new HashMap<Integer, Double>());
        friendsValueRandom.put(id,new HashMap<Integer, Double>());

        for (int i=0;i<getAllPersonsInHall().size();i++)
        {
            setFriendValue(person.getid(), getAllPersonsInHall().get(i).getid(),0.0 );
        }
        for (int i=0;i<getAllPersonsInHall().size();i++)
        {
            setFriendValueRandom(person.getid(), getAllPersonsInHall().get(i).getid(),5 );
        }
        return person;

    }

   /* public Person addPerson(double x , double  y,boolean alone,int id ,String name,String position ) {




        // create Person and add to layer
        Person person = new Person(x, y, alone, id,name,position);

        if (position.equals("Halle")||  (!position.equals("lobby")))
        {
            person.setVirpos (position);
            if (position.equals("Halle")) {
                allPersonsInHall.add(person);


                personsInHall.put(person.getid(), allPersonsInHall.size() - 1);
                allPersonsInHall.get(allPersonsInHall.size() - 1).setVirpos("Rhalle");



            }
            else {

                allPersonsAtOffice.add(person);

                allPersonsAtOffice.get(allPersonsAtOffice.size() - 1).setVirpos("Office1");
                //showStrength();

            }
        }

        else {

            allPersonsInLobby.add(person);
            allPersonsInLobby.get(allPersonsInLobby.size() - 1).setVirpos("lobbyHalle");

        }

        usersData.put(id,new ArrayList<Integer>());
        friendsValue.put(id,new HashMap<Integer, Double>());
        friendsValueRandom.put(id,new HashMap<Integer, Double>());

        for (int i=0;i<getAllPersonsInHall().size();i++)
        {
            setFriendValue(person.getid(), getAllPersonsInHall().get(i).getid(),0.0 );
        }
        for (int i=0;i<getAllPersonsInHall().size();i++)
        {
            setFriendValueRandom(person.getid(), getAllPersonsInHall().get(i).getid(),5 );
        }
        return person;

    }*/


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

    public void prepareRandom(){
        List<Integer> iDs= new ArrayList<>();
        for(Person p : getAllPersonsInHall())
            iDs.add(p.getId());

        friendsValueRandom  = new HashMap<Integer , Map<Integer , Double>>();
        for(Map.Entry<Integer,Map<Integer, Double>> entry : friendsValue.entrySet())
        {
            for (Map.Entry<Integer,Double> ent : entry.getValue().entrySet())
                friendsValueRandom.put(entry.getKey(), new HashMap<Integer,Double>());
        }
        Random ran = new Random();


        for (int i=0;i<getAllPersonsInHall().size();i++) {
            friendsValueRandom.put(allPersonsInHall.get(i).getid(), new HashMap<Integer, Double>());


        }

        Collections.shuffle(iDs);
        while(!iDs.isEmpty())
        {
            int k=0;
            int temp = ran.nextInt(iDs.size());

            int size = ran.nextInt(13)+3;
            int tempId = iDs.get(temp);
            iDs.remove(temp);
            if(iDs.isEmpty())
            {
                temp = ran.nextInt(allPersonsInHall.size());
                setFriendValueRandom(tempId,allPersonsInHall.get(temp).getid(),1.0);
            }
            else
                for(int i=0;i<iDs.size();i++)
                {
                    setFriendValueRandom(tempId,iDs.get(i),1.0);
                    iDs.remove(i);
                    k++;
                    if(k>=size) break;
                }


        }


    }

}
