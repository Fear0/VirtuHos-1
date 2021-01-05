package hallserver;

public class Main {
    public static  void main(String args[]){

        Server server=new Server(5555);
        server.runserver();
        server.listentoclients();


    }
}
