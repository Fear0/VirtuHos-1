package Halle;

import BigBlueButton.api.BBBException;
import Server.Client;
import User.*;
import javafx.animation.AnimationTimer;
import javafx.geometry.Bounds;
import javafx.scene.paint.Color;
import lombok.SneakyThrows;

import java.util.*;

public class placingAlgorithm {
    static int check =0;

    public ArrayList<Grp> getGroups() {
        return groups;
    }

    public void setGroups(ArrayList<Grp> groups) {
        this.groups = groups;
    }

    private AnimationTimer interactionLoop;
    private AnimationTimer inverseLoop;
    private AnimationTimer randomLoop;
    private AnimationTimer lobbyLoop;
    private AnimationTimer withoutLoop;
    private Hall hall;
    private Client client;
    private ArrayList<Grp> groups=new ArrayList<Grp>();

    public Client getClient() {
        return client;
    }



    //ticks per second, Higher values equal more frames = persons move faster.
    private int amountOfTicks = 100;
    //ns should not be cnahged.
    double ns = 1000000000 / amountOfTicks;

    //Vector2D grpV = new Vector2D(50,50);

    /**
     *
     */

    public void pr()
    {

        for(int i=0;i<groups.size();i++) {
            System.out.print("group " +i+"  :" );
            for (int j = 0; j < groups.get(i).getPersons().size(); j++)
                System.out.print( " , "+groups.get(i).getPersons().get(j).getMyId()+"  ");
            System.out.println(" ");
        }

    }

    /**
     * sets a color to each group
     */
    public void changeColor() {

        ArrayList <Color> c =new ArrayList<Color>();
        c.add(Color.BLACK.deriveColor(1, 1, 1, 0.3));
        c.add(Color.LIGHTPINK.deriveColor(1, 1, 1, 0.3));
        c.add(Color.AQUAMARINE.deriveColor(1, 1, 1, 0.3));
        // c.add(Color.BLUE.deriveColor(1, 1, 1, 0.3));
        c.add(Color.GREEN.deriveColor(1, 1, 1, 0.3));
        c.add(Color.YELLOW.deriveColor(1, 1, 1, 0.3));
        c.add(Color.ALICEBLUE.deriveColor(1, 1, 1, 0.3));
        c.add(Color.BLUEVIOLET.deriveColor(1, 1, 1, 0.3));
        c.add(Color.CHOCOLATE.deriveColor(1, 1, 1, 0.3));
        c.add(Color.DODGERBLUE.deriveColor(1, 1, 1, 0.3));
        c.add(Color.DARKBLUE.deriveColor(1, 1, 1, 0.3));
        c.add(Color.BROWN.deriveColor(1, 1, 1, 0.3));
        c.add(Color.ROSYBROWN.deriveColor(1, 1, 1, 0.3));
        c.add(Color.SANDYBROWN.deriveColor(1, 1, 1, 0.3));
        // c.add(Color.AQUAMARINE.deriveColor(1, 1, 1, 0.3));
        c.add(Color.ANTIQUEWHITE.deriveColor(1, 1, 1, 0.3));
        //c.add(Color.LIGHTPINK.deriveColor(1, 1, 1, 0.3));
        Random rand = new Random();
        int j=0;
        for (int i=0;i<groups.size();i++) {
            j=i;
            if(j>=c.size())
                j=0;
            Color cc = c.get(j);
            for (Person person : groups.get(i).getPersons()){
                person.getCircle().setFill(cc);
            }
           /* Circle circle = allPersonsInHall.get(i).getCircle();
            circle.setFill(Color.RED);*/

        }

        for (Person p : hall.getAllPersonsInHall())
        {
            if(p.getGroupid() == -1)
            {
                p.getCircle().setFill(Color.BLUE);
            }

        }

    }

    /**
     * Check if person is too close to a wall and should not move.
     * @param person
     * @return Value if person is too close to a wall
     */

    public static boolean checkCloseToBounds(Person person) {
        /*
        System.out.println("The Hall has these dimensions: " + hall.getPaneHalle().getHeight() +  " * " + hall.getPaneHalle().getWidth());
        System.out.println("Person " + person + " has these coordinates: " + person.getLocation());
        System.out.println("Radius of circle: " + person.getCircle().getRadius());

         */

        double radius = person.getCircle().getRadius();
        Vector2D location = person.getLocation();

        // check x bounds
        if (location.x - radius < 0 || location.x + radius > Settings.SCENE_WIDTH) {
            System.out.println(person + " was too close to x-Bounds.");
            return true;
        }
        // check y bounds
        if (location.y - radius < 0 || location.y + radius > Settings.SCENE_HEIGHT) {
            System.out.println(person + " was too close to y-Bounds.");
            System.out.println(person.getLocation());
            return true;
        }

        return false;

    }


    /**
     * check for collision between persons
     * @param block
     * @param friend
     * @param other
     * @param power
     * @return
     */
    private Boolean checkCollision(Person block, Integer friend, ArrayList<Person>other, double power) throws BBBException {
        boolean collisionDetected = false;
        if(other!=null)
        {


            return  false;

        }

        else
        if(friend!=null ) {


            if (power == 0)

                return true;

            if (hall.getAllPersonsInHall().get(friend) != null) {
                Bounds a = hall.getAllPersonsInHall().get(friend).localToScene(hall.getAllPersonsInHall().get(friend).getBoundsInLocal());
                Bounds b = client.getPerson().localToScene(client.getPerson().getBoundsInLocal());

//
                if (a.intersects(b) && hall.getAllPersonsInHall().get(friend).getMyId() != block.getMyId() && !block.isAlone()) {


                    client.entergroup(hall.getAllPersonsInHall().get(friend).getMyId());
                    if(hall.getGroups().size()!=0&&hall.getAllPersonsInHall().get(friend).getGroupid()!=-1&&groups.get(hall.getAllPersonsInHall().get(friend).getGroupid()).getPersons().size()==16&&hall.getAllPersonsInHall().get(friend).getGroupid()!=block.getGroupid())
                    {



                        return  false;

                    }
                    return true;


                }


            }

        }

        return  false;
    }
/*
    private Boolean checkRandom(Person block, Integer friend) {
        boolean collisionDetected = false;


        if(friend!=null ) {


            if(power==0)

                return true;


            Bounds a = hall.getAllPersonsInHall().get(friend).localToScene(hall.getAllPersonsInHall().get(friend).getBoundsInLocal());
            Bounds b = block.localToScene(block.getBoundsInLocal());
        *//*   if(hall.getAllPersonsInHall().get(friend).getGroupid()!=-1&&groups.size()!=0&&groups.get(block.getGroupid()).getPersons().size()==16)
            {
                return true;

            }*//*

     *//*  if(hall.getAllPersonsInHall().get(friend).getGroupid()!=-1&&groups.size()!=0&&block.getGroupid()!=-1&&hall.getAllPersonsInHall().get(friend).getGroupid()!=block.getGroupid())
            {

                return true;
            }
            if(hall.getAllPersonsInHall().get(friend).getGroupid()!=-1&&groups.size()!=0&&block.getGroupid()!=-1)
            {



                return true;
            }*//*
            if(hall.getAllPersonsInHall().get(friend).getGroupid()==-1&&groups.size()!=0&&block.getGroupid()!=-1&&groups.get(block.getGroupid()).persons.size()==16)
            {


                hall.getAllPersonsInHall().get(friend).stop();
                return true;
            }

         *//*   if(block.getGroupid()!=-1&&groups.size()!=0&&groups.get(block.getGroupid()).getPersons().size()==16)
            {


                return true;
            }
*//*
     *//*if(block.getGroupid()==-1&&groups.size()!=0&&hall.getAllPersonsInHall().get(friend).getGroupid()!=-1&&groups.get(hall.getAllPersonsInHall().get(friend).getGroupid()).getPersons().size()==16)
            {


                return true;
            }*//*

            if (a.intersects(b) && hall.getAllPersonsInHall().get(friend).getMyId()!=block.getMyId() && !block.isAlone()){

                if(block.getGroupid()==-1&&hall.getAllPersonsInHall().get(friend).getGroupid()==-1)
                {
                    Grp temp=new Grp(hall.getPlayfield());
                    if(groups.size()!=0) {
                        temp.pos.x = groups.get(groups.size() - 1).pos.x + 200;
                        temp.pos.y = groups.get(groups.size() - 1).pos.y + 50;
                    }
                    temp.add(block);
                    temp.add(hall.getAllPersonsInHall().get(friend));
                    groups.add(temp);
                    hall.getAllPersonsInHall().get(friend).setGroupid(groups.size()-1);
                    block.setGroupid(groups.size()-1);
                    changeColor();




                }
                else
                if(block.getGroupid()==-1&&hall.getAllPersonsInHall().get(friend).getGroupid()!=-1 && !block.isAlone())
                {
                    groups.get(hall.getAllPersonsInHall().get(friend).getGroupid()).getPersons().add(block);
                    block.setGroupid(hall.getAllPersonsInHall().get(friend).getGroupid());
                    changeColor();

                }
                else
                if(block.getGroupid()!=-1&&hall.getAllPersonsInHall().get(friend).getGroupid()==-1 && !block.isAlone())
                {
                    groups.get(block.getGroupid()).getPersons().add(hall.getAllPersonsInHall().get(friend));
                    hall.getAllPersonsInHall().get(friend).setGroupid(block.getGroupid());

                    changeColor();
                }
                else
                if(block.getGroupid()!=-1&&hall.getAllPersonsInHall().get(friend).getGroupid()!=-1 && block.getGroupid()!=hall.getAllPersonsInHall().get(friend).getGroupid() &&!block.isAlone())
                {
                    // removeperson(hall.getAllPersonsInHall().get(friend));
                    //    groups.get(block.getGroupid()).add(hall.getAllPersonsInHall().get(friend));
                    //System.out.print("wow");
                    removeperson(hall.getAllPersonsInHall().get(friend));
                    groups.get(block.getGroupid()).add(hall.getAllPersonsInHall().get(friend));
                    hall.getAllPersonsInHall().get(friend).setGroupid(block.getGroupid());
                    changeColor();

                }
                //    block.stop();


                return true;



            }


        }



        return  false;
    }*/

    /**
     * clear all groups
     */
    private  void removeGroups(){

        for (int i=0;i<hall.getAllPersonsInHall().size();i++)
            hall.getAllPersonsInHall().get(i).resetgroup();
        groups=new ArrayList<Grp>();


    }

    /**
     * remove person from his group after leaving
     * @param person
     * @return
     */
    public boolean removeperson(Person person){
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

    public  void setClient(Client client) {
        this.client = client;
    }

    /**
     *
     * @param hall
     */
    public placingAlgorithm(Hall hall)
    {
        this.hall=hall;

    }

    /**
     * sorting persons randomly
     * @param move
     */
    public void randomSorting(boolean move)
    {
        if(move){
            removeGroups();
            System.out.println("ran");

        }
        /*ArrayList<Person> personList = new ArrayList<>(hall.getAllPersonsInHall());
        ArrayList<Double> randomList = new ArrayList<>();
        Random r = new Random();
        hall.getAllPersonsInHall().forEach(Sprite::stop);
        Collections.shuffle(personList);
            for(int i = 0; i < personList.size(); i++ ) {
               for(int j=i+1;j<i+5 && j<personList.size();j++)
               {
                  personList.get(i).addFriend(i,j);
               }


        }*/
        if(inverseLoop!= null)
            inverseLoop.stop();
        if(interactionLoop!= null)
            interactionLoop.stop();
        if(randomLoop!=null)
            randomLoop.stop();
        if(withoutLoop != null)
            withoutLoop.stop();

        long end = System.nanoTime() + 5200*(long)Math.pow(10, 9);


        randomLoop = new AnimationTimer() {

            //GameLoop Logic
            long lastTime = System.nanoTime();

            double delta = 0;
            //GameLoop Logic

            @SneakyThrows
            @Override
            public void handle(long now) {
                if(now<end){


                        if( client.getPerson().isClicked()&&hall.getAlone().isSelected()) {
                            client.getPerson().stayAlone();
                        }
                        else  client.getPerson().notAlone();
                        boolean stop = true;

                        if(!checkLeave()) {

                            try{
                                if(client.getPerson() != null){
                                for (int j = 0; j < hall.getUsersData().get(client.getPerson().getMyId()).size(); j++) {

                                    if (hall.getFriend(client.getPerson(), j) != null) {
                                        stop = false;
                                        Vector2D vector = hall.getFriend(client.getPerson(), j).getLocation();
                                        if (!client.getPerson().isAlone() && !hall.getFriend(client.getPerson(), j).isAlone() && !checkCollision(client.getPerson(), hall.getPersonsInHall().get(hall.getFriend(client.getPerson(), j).getMyId()), null, hall.getFriendValueRandom(client.getPerson(), j))) {
                                            {
                                                // hall.getAllPersonsInHall().get(i).maxSpeed *= 4;
                                                client.getPerson().seek(hall.getFriend(client.getPerson(), j).getLocation(), hall.getFriendValueRandom(client.getPerson(), j));
                                            }
                                        }

                                        else if(client.getPerson().getGroupid()==hall.getFriend(client.getPerson(), j).getGroupid() &&client.getPerson().getGroupid()!=-1 ) {
                                            client.getPerson().fleep(hall.getAllPersonsInHall(), 25);

                                        }
                                        else if
                                        (client.getPerson().getGroupid()!=-1  )
                                            client.getPerson().flee(hall.getFriend(client.getPerson(), j));




                                    }

                                }
                                if(stop) client.getPerson().stop();
                                }}
                            catch(BBBException e) {
                                e.printStackTrace();

                            }

                        }
/*
                    for (Grp group: groups)
                        for (int i=0 ; i<group.getSize();i++)
                            for (int j=0; j<group.getSize(); j++)
                            {
                                if (i==j) continue;
                                if (group.getSize()==2)
                                    hall.setFriendValueRandom(group.getPersons().get(0).getMyId(),group.getPersons().get(1).getMyId(),1.0);
                                else
                                    hall.setFriendValueRandom(group.getPersons().get(i).getMyId(),group.getPersons().get(j).getMyId(),1.0);
                            }*/
                }

              /*  try {
                    hall.updateGroups();
                } catch (BBBException e) {
                    e.printStackTrace();
                }*/
             /*   hall.getAllPersonsAtOffice().forEach(Sprite::move);
                hall.getAllPersonsAtOffice().forEach(Sprite::display);
                hall.getAllPersonsInHall().forEach(Sprite::move);
                hall.getAllPersonsInHall().forEach(Sprite::display);*/

                //GameLoop
                long nw = System.nanoTime();
                delta += (nw - lastTime) / ns;
                lastTime = nw;
                if(delta >= 1){
                    //Move sprite "amountOfTicks" per second
                    client.movesprite();
                    client.getPerson().move();
                    client.getPerson().display();
                    delta--;
                }
                //GameLoop


              //  hall.getAllPersonsAtOffice().forEach(Sprite::display);
               // hall.getAllPersonsInHall().forEach(Sprite::display);

                if (hall.getShowPower().isSelected()) {
                    hall.showStrength(0);
                } else {
                    hall.removeStrength();
                }
            }
        };

        randomLoop.start();
    }
    private  boolean temp=true;
    public void inverseSorting(boolean move) {

        if(move){
            removeGroups();
            System.out.println("in");

        }
        // start game
        if(interactionLoop!= null)
            interactionLoop.stop();
        if(randomLoop!= null)
            randomLoop.stop();
        if(inverseLoop!=null)
            inverseLoop.stop();
        if(withoutLoop != null)
            withoutLoop.stop();

        long end = 0;
        inverseLoop = new AnimationTimer() {
            boolean one=true;

            //GameLoop Logic
            long lastTime = System.nanoTime();


            double delta = 0;
            //GameLoop Logic

            @SneakyThrows
            @Override
            public void handle(long now) {
                if(one&&now<end){
                    for (int i = 0; i < hall.getAllPersonsInHall().size(); i++) {
                        if( hall.getAllPersonsInHall().get(i).isClicked()&&hall.getAlone().isSelected()) {
                            hall.getAllPersonsInHall().get(i).stayAlone();
                        }
                        else  hall.getAllPersonsInHall().get(i).notAlone();

                       checkLeave();




                    }

                    //System.out.println("in");

                }

                if (end<now)
                {

                    for (int i=0;i<hall.getAllPersonsInHall().size();i++)
                        hall.getAllPersonsInHall().get(i).stop();
                    one=false;}
                if (client.getPerson()!=null) {
                    if (!one) {
                        // seek attractor location, apply force to get towards it

                        boolean stop = true;
                        if (!checkLeave()) {
                            try {
                                for (int j = 0; j < hall.getUsersData().get(client.getPerson().getMyId()).size(); j++) {
                                    if (hall.getFriend(client.getPerson(), j) != null) {
                                        stop = false;
                                        if (client.getPerson().isAlone() || hall.getFriend(client.getPerson(), j).isAlone())
                                            continue;
                                        //  hall.getAllPersonsInHall().get(i).flee(hall.getAllPersonsInHall().get(hall.getPersonsInHall().get(hall.getUsersData().get(hall.getAllPersonsInHall().get(i).getMyId()).get(j))),hall.getFriendValue().get(hall.getAllPersonsInHall().get(i).getMyId()).get(hall.getUsersData().get(hall.getAllPersonsInHall().get(i).getMyId()).get(j)));
                                        //hall.getAllPersonsInHall().forEach(Sprite::move);
                                        if (!checkCollision(client.getPerson(), hall.getPersonsInHall().get(hall.getFriend(client.getPerson(), j).getMyId()), null, 1 - hall.getFriendValue(client.getPerson(), j))) {
                                            //if ((1 - hall.getFriendValue(i, j)) == 1.0)
                                            client.getPerson().seek(hall.getFriend(client.getPerson(), j).getLocation(), 1 - hall.getFriendValue(client.getPerson(), j));

                                        }
                               /*     else if(hall.getAllPersonsInHall().get(i).getGroupid()==hall.getFriend(i, j).getGroupid() &&hall.getAllPersonsInHall().get(i).getGroupid()!=-1 )
                                        //hall.getAllPersonsInHall().get(i).seek(groups.get(hall.getAllPersonsInHall().get(i).getGroupid()).getNextpos(i), 3);
                                        hall.getAllPersonsInHall().get(i).fleep(hall.getAllPersonsInHall(),30);*/
                                        else if
                                        (client.getPerson().getGroupid() != -1)
                                            client.getPerson().flee(hall.getFriend(client.getPerson(), j));


                                    }
                                    if (stop)
                                        client.getPerson().stop();


                                }
                            } catch (BBBException e) {
                                e.printStackTrace();
                            }

                        }


                    }
                }

                /*try {
                    hall.updateGroups();
                } catch (BBBException e) {
                    e.printStackTrace();
                }*/
              /*  // move sprite
                hall.getAllPersonsInHall().forEach(Sprite::move);
                hall.getAllPersonsAtOffice().forEach(Sprite::move);

                // update in fx scene
                hall.getAllPersonsInHall().forEach(Sprite::display);
                hall.getAllPersonsAtOffice().forEach(Sprite::display);*/

                //GameLoop
                long nw = System.nanoTime();
                delta += (nw - lastTime) / ns;
                lastTime = nw;
                if(delta >= 1){
                    //Move sprite "amountOfTicks" per second
                    //    client.moveperson(client.getPerson().getLocation().x,client.getPerson().getLocation().y,client.getPerson().getLayoutX(),client.getPerson().getLayoutY(),client.getPerson().getMyId());
                    if(client.getPerson()!=null){
                        client.movesprite();
                        client.getPerson().move();
                        client.getPerson().display();
                        delta--;}
                }
                //GameLoop

                // update in fx scene
               // hall.getAllPersonsAtOffice().forEach(Sprite::display);
             //   hall.getAllPersonsInHall().forEach(Sprite::display);



                if (hall.getShowPower().isSelected()) {
                    hall.showStrength(1);
                } else {
                    hall.removeStrength();
                }
            }
        };
        inverseLoop.start();




    }

    /**
     * sorting persons to interaction
     * @param move
     * @throws BBBException
     */
    public void interactionSorting(boolean move)throws BBBException{
        if(move){
            removeGroups();
            //System.out.println("inter");

        }

        hall.getAllPersonsInHall().forEach(Sprite::stop);
        if(inverseLoop!= null)
            inverseLoop.stop();
        if(randomLoop!= null)
            randomLoop.stop();
        if(interactionLoop!= null)
            interactionLoop.stop();
        if(withoutLoop != null)
            withoutLoop.stop();


        interactionLoop = new AnimationTimer() {
            boolean one=true;



            //GameLoop Logic
            long lastTime = System.nanoTime();

            double delta = 0;
            //GameLoop Logic

            @Override
            public void handle(long now) {

         /*       if(one&&now<end) {
                    for (int i = 0; i < hall.getAllPersonsInHall().size(); i++) {

                        if (!checkLeave(i)) {
                            hall.getAllPersonsInHall().get(i).flee(hall.getAllPersonsInHall());

                        }
                    }
                }
                        if (end<now)
                        {
                            one=false;}
              */  if (client.getPerson()!=null) {

                 /*   for (int k=0;k<groups.size();k++)
                        groups.get(k).flee(groups);*/

                        if (client.getPerson().isClicked() && hall.getAlone().isSelected()) {
                            client.getPerson().stayAlone();
                        } else client.getPerson().notAlone();


                        if (!checkLeave()) {

                            try {

                                //the stop must be tested ,im not sure if it is working correctly, it works good on the localhost
                                boolean stop=true;
                                for (int j = 0; j < hall.getUsersData().get(client.getPerson().getMyId()).size(); j++) {
                                    if (hall.getFriend(client.getPerson(), j) != null&&client.getPerson().getMyId()!=hall.getFriend(client.getPerson(), j).getMyId()) {
                                        stop=false;
                                        Vector2D vector = hall.getFriend(client.getPerson(), j).getLocation();
                                        if (!client.getPerson().isAlone() && !hall.getFriend(client.getPerson(), j).isAlone() && !checkCollision(client.getPerson(), hall.getPersonsInHall().get(hall.getFriend(client.getPerson(), j).getMyId()), null, hall.getFriendValue(client.getPerson(), j))) {
                                            client.getPerson().seek(hall.getFriend(client.getPerson(), j).getLocation(), hall.getFriendValue(client.getPerson(), j));

                                        }
                                        else if (client.getPerson().getGroupid() == hall.getFriend(client.getPerson(), j).getGroupid() && client.getPerson().getGroupid() != -1)
                                            //hall.getAllPersonsInHall().get(i).seek(groups.get(hall.getAllPersonsInHall().get(i).getGroupid()).getNextpos(i), 3);
                                            client.getPerson().fleep(hall.getAllPersonsInHall(), 25);
                                        else if (client.getPerson().getGroupid() != hall.getFriend(client.getPerson(), j).getGroupid() && client.getPerson().getGroupid() != -1)
                                            client.getPerson().flee(hall.getFriend(client.getPerson(), j));


                                    }
                                    if(stop)
                                        client.getPerson().stop();



                                }
                            } catch (BBBException e) {
                                e.printStackTrace();
                            }

                        }




              /*  try {
                    hall.updateGroups();
                } catch (BBBException e) {
                    e.printStackTrace();
                }*/
               /* hall.getAllPersonsAtOffice().forEach(Sprite::move);
                hall.getAllPersonsAtOffice().forEach(Sprite::display);
                hall.getAllPersonsInHall().forEach(Sprite::move);
                hall.getAllPersonsInHall().forEach(Sprite::display);*/
                    //GameLoop Logic
                    long nw = System.nanoTime();
                    delta += (nw - lastTime) / ns;
                    lastTime = nw;
                    if(delta >= 1){
                        //Move sprite "amountOfTicks" per second
                        //    client.moveperson(client.getPerson().getLocation().x,client.getPerson().getLocation().y,client.getPerson().getLayoutX(),client.getPerson().getLayoutY(),client.getPerson().getMyId());
                       if(client.getPerson()!=null){
                        client.movesprite();
                        client.getPerson().move();
                        client.getPerson().display();
                           delta--;}
                    }
                    //GameLoop Logic


                    hall.getAllPersonsAtOffice().forEach(Sprite::display);
                 /*   hall.getAllPersonsInHall().forEach(Sprite::display);*/

                    if (hall.getShowPower().isSelected()) {
                        hall.showStrength(1);
                    } else {
                        hall.removeStrength();
                    }

                }
        }};

        interactionLoop.start();

    }

    /**
     * let persons move on their own
     * @param move
     * @throws BBBException
     */
    public void withoutPower(boolean move){
        if(move){
            removeGroups();
        }

        hall.getAllPersonsInHall().forEach(Sprite::stop);
        if(inverseLoop != null)
            inverseLoop.stop();
        if(randomLoop!= null)
            randomLoop.stop();
        if(interactionLoop!= null)
            interactionLoop.stop();
        if(withoutLoop != null)
            withoutLoop.stop();


        withoutLoop = new AnimationTimer() {

            @Override
            @SneakyThrows
            public void handle(long now) {
                if (client.getPerson() != null) {
                    //stayAlone is active
                    if (client.getPerson().isClicked() && hall.getAlone().isSelected()) {
                        client.getPerson().stayAlone();
                    } else client.getPerson().notAlone();

                    if (!checkLeave()) {
                        boolean stop=true;
                        /*!
                        Sollen Personen sich überlappen können, wenn sie sich frei bewegen?
                        */
                        if (!client.getPerson().isAlone()) {
                            client.getPerson().seek(client.getPerson().getLocation(), 0);
                            for (int j = 0; j < hall.getUsersData().get(client.getPerson().getMyId()).size(); j++) {
                                try {
                                    checkCollision(client.getPerson(), hall.getPersonsInHall().get(j), null, 1);
                                } catch (BBBException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                    if(client.getPerson()!=null){
                        client.movesprite();
                        client.getPerson().move();
                        client.getPerson().display();
                    }

                    hall.getAllPersonsAtOffice().forEach(Sprite::display);

                    if (hall.getShowPower().isSelected()) {
                        hall.showStrength(1);
                    } else {
                        hall.removeStrength();
                    }
                }
            }
        };
        withoutLoop.start();
    }


    /**
     * check if person enters The Hall
     */
    public void enterHall() {
        lobbyLoop = new AnimationTimer() {


            @Override
            public void handle(long now) {


                for (int i = 0; i < hall.getAllPersonsInLobby().size(); i++) {
                    if (!hall.getAllPersonsInLobby().get(i).getVirtualLocation().equals("lobbyHalle")) {
                        hall.addPersonInHall(hall.getAllPersonsInLobby().get(i));
                        hall.getAllPersonsInLobby().remove(i);


                    }

                }
                if (hall.getAllPersonsInHall().size()>0)
                    for (int j = 0; j < hall.getAllPersonsAtOffice().size(); j++) {
                        if (hall.getAllPersonsAtOffice().get(j).getVirtualLocation().equals("Rhalle")) {
                            hall.enterHall(j);
                        }

                    }

                hall.getAllPersonsInLobby().forEach(Sprite::display);
                hall.getAllPersonsInLobby().forEach(Sprite::move);


            }};

        lobbyLoop.start();

    }


    public boolean checkLeave(){

    if(client.getPerson()==null)
        return true;

        if (!client.getPerson().getVirtualLocation().equals("Rhalle")) {
            client.getPerson().resetgroup();
            removeperson(client.getPerson());
            if (client.getPerson().getVirtualLocation().equals("left") )
            {

                hall.left();



            }
            else
            {
                //client.lefthall();
                hall.leftHall();




            }

            hall.getAllPersonsInHall().forEach(Sprite::stop);
            hall.getAllPersonsAtOffice().forEach(Sprite::stop);
            return true;

        } else {
            //hall.getAllPersonsInHall().get(i).seek(hall.getAllPersonsInHall().get(i).getLocation(), 0);
            return false;

        }

    }
}
