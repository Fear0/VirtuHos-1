package wb.analyse1.parser;

public class Attendee {
    private String id;
    private String name;

    public Attendee() {

    }
    public Attendee(String id , String name){
        this.id = id;
        this.name = name;
    }
    public String getId() {
        return id;
    }

    public void setId(String s) {
        this.id = s;
    }

    public String getName() {
        return name;
    }

    public void setName(String s) {
        this.name = s;
    }

    @Override
    public String toString() {
        return "Attendee ID='" + id + "' Name='" + name + "'";
    }
}