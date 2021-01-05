package Halle;



import User.Person;
import User.Sprite;
import User.Vector2D;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.ArrayList;

public class Grp  {
    ArrayList<Person> persons;
    //radius
    int r;

    private int groupid;


    private Integer meetingId;
    public int getGroupid() {
        return groupid;
    }
    //Center of group;
    public Vector2D pos;
    public int getSize() {
        return persons.size();
    }

    public ArrayList<Person> getPersons() {
        return persons;
    }

    public void remove(){
        for (int i=0;i< persons.size();i++)
            persons.get(i).resetgroup();
    }

    public  void setGroupid(int id){groupid=id;}
    public Integer getMeetingId() { return meetingId; }

    public void setMeetingId(Integer meetingId) { this.meetingId = meetingId; }


    public Node createView() {
        return new ImageView();
    }

    public Grp(Pane layer){
        persons =new ArrayList<Person>();
        pos = new Vector2D(50,50);
        groupid=0;
        r=50;


    }
    public Grp(){
        persons =new ArrayList<Person>();

        groupid=0;



    }

    public void add(Person person) { persons.add(person); }

    @Override
    public String toString() {
        String s = "\nGroup: " + groupid + " {";
        for (Person current: persons) {
            s += current + "; ";
        }
        return s + "}";
    }

}
