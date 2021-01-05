package hallserver;
import  hallserver.Vector2D;
public class Person {
    boolean admin=false;
    private int groupid;
    private int id ;
    private  String name;
    private  String virpos;
    private  boolean online;
    Vector2D pos;

    public int getMeetingid() {
        return meetingid;
    }

    public void setMeetingid(int meetingid) {
        this.meetingid = meetingid;
    }

    int meetingid;

    public int getId() {
        return id;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAlone(boolean alone) {
        this.alone = alone;
    }

    public  Person(double x , double  y, boolean alone, int id , String name, String virpos,boolean admin )
    {
        this.name=name;
        groupid=-1;
        pos=new Vector2D(x,y);
        this.id=id;
        this.alone=alone;
        this.virpos=virpos;
        this.admin=admin;


    }

    public Vector2D getPos() {
        return pos;
    }

    public void setPos(Vector2D pos) {
        this.pos = pos;
    }

    public String getVirpos() {
        return virpos;
    }

    public void setVirpos(String virpos) {
        this.virpos = virpos;
    }

    boolean alone;
    public void resetgroup(){ groupid=-1;}
    public int getGroupid() {
        return groupid;
    }
    public int getid(){return  id;}
    public void setGroupid(int groupid) {
        this.groupid = groupid;
    }
    public void stayAlone(){

        alone = true;
    }
    public void notAlone(){

        alone = false;
    }

    public boolean isAlone() {
        return alone;
    }

}
