package definitions;


public class Document implements java.io.Serializable {
    private String Link;

    //maybe create a google docs via api
    public Document(String link) {
        this.Link = link;
    }

    public String getLink() {
        return this.Link;
    }

    public void setLink(String link) {
        this.Link = link;
    }
}
