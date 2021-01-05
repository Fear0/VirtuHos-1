package hallserver;







import java.security.PublicKey;
import java.util.ArrayList;

public class Grp  {

    ArrayList<Person> persons;


    private Integer meetingId;
    //Center of group;
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


    public Integer getMeetingId() { return meetingId; }

    public void setMeetingId(Integer meetingId) { this.meetingId = meetingId; }




    public Grp(){

        persons =new ArrayList<Person>();




    }

    public void add(Person person) { persons.add(person); }


}
