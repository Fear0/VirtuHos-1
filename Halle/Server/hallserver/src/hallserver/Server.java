package hallserver;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

public class Server  implements Runnable {

    ServerSocket serverSocket;
    int port;
    Hallinformation hall=new Hallinformation();
    ArrayList<PrintWriter> list_client_writer;
    private List<Meeting> meetings = new ArrayList<>();



    public  Server(int port)
    {

        this.port=port;


    }
    public boolean runserver()
    {

        try {
            serverSocket=new ServerSocket(port);
            list_client_writer=new ArrayList<PrintWriter>();

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return  false;
        }
    }
    public void run(){

        listentoclients();

    }
    public  void listentoclients()
    {

        int i= 0;

        while(true)
        {
            try {
                Socket client=  serverSocket.accept();

                System.out.println("client  is connected");
                PrintWriter writer=new PrintWriter((client.getOutputStream()));
                list_client_writer.add(writer);



                Thread clientthread=new Thread(new Clienthandler(client,"client"+i,writer,hall,list_client_writer,meetings));
                clientthread.start();

            }
            catch (IOException e)
            {}

            i++;
        }

    }

}
