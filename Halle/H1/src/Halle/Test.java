package Halle;

import User.Person;
import javafx.scene.shape.Circle;

public class Test {

    private Hall hall;
    private Circle circle;
    private double close_friends = 1.0;
    private double average_friends = 0.5;

    public Test(Hall hall){
        this.hall=hall;
    }
/*
    public void loadTest01(){
        clear();
        hall.addPerson("1",1000,"Office",0,0);
        hall.addListeners();
    }

    public void loadTest02() {
        clear();
        int  j=1000;
        //(A..E)
        add_group_in_cricle(350,200,5);
        for(int l =1000;l<=1004;l++)
            for(int i =1000;i<=1004;i++)
            {
                if(i==l)
                    continue;
                hall.setFriendValue(i, l, 1.0);

            }

        //(F..M)
        j=1005;
        add_group_in_cricle(370,290,8);
        for(int l =1005;l<=1012;l++)
            for(int i =1005;i<=1012;i++)
            {
                if(i==l)
                    continue;
                hall.setFriendValue(i, l, 1.0);

            }
        //

        //(N..Z)
        j=1013;
        add_group_in_cricle(250,250, 13);

        for(int l =1013;l<=1025;l++)
            for(int i =1013;i<=1025;i++)
            {
                if(i==l)
                    continue;
                hall.setFriendValue(i, l, 1.0);

            }

        hall.addPerson("Nutzer", 1030, "Office",0,0);

        //A..E
        for (int i=1000;i<=1005;i++) {
            hall.setFriendValue(1030, i, 1.0);


        }
        //F
        hall.setFriendValue(1030,1005,0.1);
        //M
        hall.setFriendValue(1030,1012,0.1);

        hall.setFriendValue(1030,1006,0.0);
        hall.setFriendValue(1030,1007,0.0);
        hall.setFriendValue(1030,1008,0.0);
        hall.setFriendValue(1030,1009,0.0);
        hall.setFriendValue(1030,1010,0.0);
        hall.setFriendValue(1030,1011,0.0);


        //(N..Q)
        for (int i=1013;i<=1016;i++) {
            hall.setFriendValue(1030, i, 0.6);


        }

        //R..W
        for (int i=1017;i<=1022;i++) {
            hall.setFriendValue(1030, i, 4.0);


        }
        //X,Y,Z
        for (int i=1023;i<=1025;i++) {
            hall.setFriendValue(1030, i, 0.1);


        }


        hall.addListeners();



    }

    public void loadTest03(){
        clear();
        int  j=1000;
        //(A..E)
        add_group_in_cricle(350,200,5);
        for(int l =1000;l<=1004;l++)
            for(int i =1000;i<=1004;i++)
            {
                if(i==l)
                    continue;
                hall.setFriendValue(i, l, 0.1);

            }

        //(F..M)
        j=1005;
        add_group_in_cricle(370,290,8);
        for(int l =1005;l<=1012;l++)
            for(int i =1005;i<=1012;i++)
            {
                if(i==l)
                    continue;
                hall.setFriendValue(i, l, 0.0);

            }
        //

        //(N..Z)
        j=1013;
        add_group_in_cricle(250,250, 13);
        for(int l =1013;l<=1025;l++)
            for(int i =1013;i<=1025;i++)
            {
                if(i==l)
                    continue;
                hall.setFriendValue(i, l, 0.0);

            }

        hall.addPerson("Nutzer", 1030, "Office",0,0);

        //A..E
        for (int i=1000;i<=1005;i++) {
            hall.setFriendValue(1030, i, 1.0);


        }
        //F
        hall.setFriendValue(1030,1005,0.1);
        //M
        hall.setFriendValue(1030,1012,0.1);

        hall.setFriendValue(1030,1006,0.0);
        hall.setFriendValue(1030,1007,0.0);
        hall.setFriendValue(1030,1008,0.0);
        hall.setFriendValue(1030,1009,0.0);
        hall.setFriendValue(1030,1010,0.0);
        hall.setFriendValue(1030,1011,0.0);


        //(N..Q)
        for (int i=1013;i<=1016;i++) {
            hall.setFriendValue(1030, i, 0.6);


        }

        //R..W
        for (int i=1017;i<=1022;i++) {
            hall.setFriendValue(1030, i, 1.0);


        }
        //X,Y,Z
        for (int i=1023;i<=1025;i++) {
            hall.setFriendValue(1030, i, 0.1);


        }

        j=1000;
        //(A..E)<->(F..M)
        for(int l =1000;l<=1004;l++)
            for(int i =1005;i<=1012;i++)
            {
                if(i==l)
                    continue;
                hall.setFriendValue(i, l, 1.0);

            }
        //(A..E)<->(N..Z)
        for(int l =1000;l<=1004;l++)
            for(int i =1013;i<=1025;i++)
            {
                if(i==l)
                    continue;
                hall.setFriendValue(i, l, 1.0);

            }
        //(F..M)<->(N..Z)
        for(int l =1005;l<=1012;l++)
            for(int i =1013;i<=1025;i++)
            {
                if(i==l)
                    continue;
                hall.setFriendValue(i, l, 1.0);

            }


        hall.addListeners();

    }

    public void loadTest04(){
        clear();
        //(A..E)
        add_group_in_cricle(350,300,5);
        for(int l =1000;l<=1004;l++)
            for(int i =1000;i<=1004;i++)
            {
                if(i==l)
                    continue;
                hall.setFriendValueRandom(l, i, 1.0);

            }

        //(F..M)
        add_group_in_cricle(450,100,8);
        for(int l =1005;l<=1012;l++)
            for(int i =1005;i<=1012;i++)
            {
                if(i==l)
                    continue;
                hall.setFriendValueRandom(l, i, 1.0);

            }
        //

        //(N..Z)
        add_group_in_cricle(250,200, 13);

        for(int l =1013;l<=1025;l++)
            for(int i =1013;i<=1025;i++)
            {
                if(i==l)
                    continue;
                hall.setFriendValueRandom(l, i, 1.0);

            }

        //(A..E)<->(F..M)
        for(int l =1000;l<=1004;l++)
            for(int i =1005;i<=1012;i++)
            {
                if(i==l)
                    continue;
                hall.setFriendValueRandom(l, i, 0.1);

            }
        //(A..E)<->(N..Z)
        for(int l =1000;l<=1004;l++)
            for(int i =1013;i<=1025;i++)
            {
                if(i==l)
                    continue;
                hall.setFriendValueRandom(l, i, 0.0);

            }
        //(F..M)<->(N..Z)
        for(int l =1005;l<=1012;l++)
            for(int i =1013;i<=1025;i++)
            {
                if(i==l)
                    continue;
                hall.setFriendValueRandom(l, i, 0.0);

            }

        hall.addPerson("Nutzer", 1030, "Office",0,0);

        hall.addListeners();

    }

    public void loadTest05() {
        clear();
        int a=150;
        int b=250;
        int  j=1000;

        for(int i =0;i<=15;i++)
        {
            double t = 2 * Math.PI * i /16;
            int x = (int) Math.round(a + 70 * Math.cos(t));
            int y = (int) Math.round(b + 70 * Math.sin(t));
            hall.addPerson("user "+i, j, "Halle",0,0).setLocation(x,y);
            j++;

        }
        for(int i =16;i<=31;i++)
        {

            double t = 2 * Math.PI * i /16;
            int x = (int) Math.round(a+240 + 70 * Math.cos(t));
            int y = (int) Math.round(b + 70 * Math.sin(t));
            hall.addPerson("user "+i, j, "Halle",0,0).setLocation(x,y);
            j++;
        }
        hall.addPerson("user 32", 1033, "Office",0,0);
        for(int l =1000;l<=1015;l++)
            for(int i =1000;i<=1015;i++)
            {
                if(i==l)
                    continue;
                hall.setFriendValue(i, l, 1.0);

            }
        for(int l =1016;l<=1031;l++)
            for(int i =1016;i<=1031;i++)
            {
                if(i==l)
                    continue;
                hall.setFriendValue(i, l, 1.0);

            }

        for(int i =1000;i<1032;i++)
        {

            hall.setFriendValue(1033, i, 1.0);

        }





        hall.addListeners();
    }


    public void loadTest06(){
        clear();

        add_group_in_cricle(100, 100, 5);
        add_group_in_cricle(600, 200, 10);

      /* hall.addPerson("A",1000,"Halle");
        hall.getAllPersonsInHall().get(hall.getAllPersonsInHall().size()-1).setLocation(100,100);
        hall.addPerson("B",1001,"Halle");
        hall.getAllPersonsInHall().get(hall.getAllPersonsInHall().size()-1).setLocation(125,100);
        hall.addPerson("C",1002,"Halle");
        hall.getAllPersonsInHall().get(hall.getAllPersonsInHall().size()-1).setLocation(100,125);
        hall.addPerson("D",1003,"Halle");
        hall.getAllPersonsInHall().get(hall.getAllPersonsInHall().size()-1).setLocation(125,125);
        hall.addPerson("E",1004,"Halle");
        hall.getAllPersonsInHall().get(hall.getAllPersonsInHall().size()-1).setLocation(112,150);
        hall.addPerson("F",1005,"Halle");
        hall.getAllPersonsInHall().get(hall.getAllPersonsInHall().size()-1).setLocation(635,270);
        hall.addPerson("G",1006,"Halle");
        hall.getAllPersonsInHall().get(hall.getAllPersonsInHall().size()-1).setLocation(600,200);
        hall.addPerson("H",1007,"Halle");
        hall.getAllPersonsInHall().get(hall.getAllPersonsInHall().size()-1).setLocation(625,200);
        hall.addPerson("I",1008,"Halle");
        hall.getAllPersonsInHall().get(hall.getAllPersonsInHall().size()-1).setLocation(650,200);
        hall.addPerson("J",1009,"Halle");
        hall.getAllPersonsInHall().get(hall.getAllPersonsInHall().size()-1).setLocation(600,225);
        hall.addPerson("K",1010,"Halle");
        hall.getAllPersonsInHall().get(hall.getAllPersonsInHall().size()-1).setLocation(600,250);
        hall.addPerson("L",1011,"Halle");
        hall.getAllPersonsInHall().get(hall.getAllPersonsInHall().size()-1).setLocation(625,225);
        hall.addPerson("M",1012,"Halle");
        hall.getAllPersonsInHall().get(hall.getAllPersonsInHall().size()-1).setLocation(625,250);
        hall.addPerson("N",1013,"Halle");
        hall.getAllPersonsInHall().get(hall.getAllPersonsInHall().size()-1).setLocation(650,225);
        hall.addPerson("O",1014,"Halle");
        hall.getAllPersonsInHall().get(hall.getAllPersonsInHall().size()-1).setLocation(650,245);
        hall.addPerson("User",1016,"Office",0,0);

        hall.setFriendValue(1000,1001,1.0);
        hall.setFriendValue(1000,1002,1.0);
        hall.setFriendValue(1000,1003,1.0);
        hall.setFriendValue(1000,1004,1.0);


        hall.setFriendValue(1001,1002,1.0);
        hall.setFriendValue(1001,1003,1.0);
        hall.setFriendValue(1001,1004,1.0);



        hall.setFriendValue(1002,1003,1.0);
        hall.setFriendValue(1002,1004,1.0);


        hall.setFriendValue(1003,1004,1.0);



        hall.setFriendValue(1005,1006,1.0);
        hall.setFriendValue(1005,1007,1.0);
        hall.setFriendValue(1005,1008,1.0);
        hall.setFriendValue(1005,1009,1.0);
        hall.setFriendValue(1005,1010,1.0);
        hall.setFriendValue(1005,1011,1.0);
        hall.setFriendValue(1005,1012,1.0);
        hall.setFriendValue(1005,1013,1.0);
        hall.setFriendValue(1005,1014,1.0);


        hall.setFriendValue(1006,1007,1.0);
        hall.setFriendValue(1006,1008,1.0);
        hall.setFriendValue(1006,1009,1.0);
        hall.setFriendValue(1006,1010,1.0);
        hall.setFriendValue(1006,1011,1.0);
        hall.setFriendValue(1006,1012,1.0);
        hall.setFriendValue(1006,1013,1.0);
        hall.setFriendValue(1006,1014,1.0);


        hall.setFriendValue(1007,1008,1.0);
        hall.setFriendValue(1007,1009,1.0);
        hall.setFriendValue(1007,1010,1.0);
        hall.setFriendValue(1007,1011,1.0);
        hall.setFriendValue(1007,1012,1.0);
        hall.setFriendValue(1007,1013,1.0);
        hall.setFriendValue(1007,1014,1.0);


        hall.setFriendValue(1008,1009,1.0);
        hall.setFriendValue(1008,1010,1.0);
        hall.setFriendValue(1008,1011,1.0);
        hall.setFriendValue(1008,1012,1.0);
        hall.setFriendValue(1008,1013,1.0);
        hall.setFriendValue(1008,1014,1.0);


        hall.setFriendValue(1009,1010,1.0);
        hall.setFriendValue(1009,1011,1.0);
        hall.setFriendValue(1009,1012,1.0);
        hall.setFriendValue(1009,1013,1.0);
        hall.setFriendValue(1009,1014,1.0);


        hall.setFriendValue(1010,1011,1.0);
        hall.setFriendValue(1010,1012,1.0);
        hall.setFriendValue(1010,1013,1.0);
        hall.setFriendValue(1010,1014,1.0);


        hall.setFriendValue(1011,1012,1.0);
        hall.setFriendValue(1011,1013,1.0);
        hall.setFriendValue(1011,1014,1.0);


        hall.setFriendValue(1012,1013,1.0);
        hall.setFriendValue(1012,1014,1.0);


        hall.setFriendValue(1013,1014,1.0);



        hall.addListeners();
    }


    public void loadTest07() {
        clear();
        add_group_in_cricle(100, 100, 6);
        add_group_in_cricle(600, 200, 10);

        hall.setFriendValue(1001, 1000, 1.0);
        hall.setFriendValue(1001, 1002, 1.0);
        hall.setFriendValue(1001, 1003, 1.0);
        hall.setFriendValue(1001, 1004, 1.0);
        hall.setFriendValue(1001, 1005, 1.0);
        hall.setFriendValue(1001, 1006, 0.5);

        hall.setFriendValue(1006, 1007, 1.0);
        hall.setFriendValue(1006, 1008, 1.0);
        hall.setFriendValue(1006, 1009, 1.0);
        hall.setFriendValue(1006, 1010, 1.0);
        hall.setFriendValue(1006, 1011, 1.0);
        hall.setFriendValue(1006, 1012, 1.0);
        hall.setFriendValue(1006, 1013, 1.0);
        hall.setFriendValue(1006, 1014, 1.0);
        hall.setFriendValue(1006, 1015, 1.0);

        hall.setFriendValue(1007, 1008, 1.0);
        hall.setFriendValue(1007, 1009, 1.0);
        hall.setFriendValue(1007, 1010, 1.0);
        hall.setFriendValue(1007, 1011, 1.0);
        hall.setFriendValue(1007, 1012, 1.0);
        hall.setFriendValue(1007, 1013, 1.0);
        hall.setFriendValue(1007, 1014, 1.0);
        hall.setFriendValue(1007, 1015, 1.0);

        hall.setFriendValue(1008, 1009, 1.0);
        hall.setFriendValue(1008, 1010, 1.0);
        hall.setFriendValue(1008, 1011, 1.0);
        hall.setFriendValue(1008, 1012, 1.0);
        hall.setFriendValue(1008, 1013, 1.0);
        hall.setFriendValue(1008, 1014, 1.0);
        hall.setFriendValue(1008, 1015, 1.0);

        hall.setFriendValue(1009, 1010, 1.0);
        hall.setFriendValue(1009, 1011, 1.0);
        hall.setFriendValue(1009, 1012, 1.0);
        hall.setFriendValue(1009, 1013, 1.0);
        hall.setFriendValue(1009, 1014, 1.0);
        hall.setFriendValue(1009, 1015, 1.0);

        hall.setFriendValue(1010, 1011, 1.0);
        hall.setFriendValue(1010, 1012, 1.0);
        hall.setFriendValue(1010, 1013, 1.0);
        hall.setFriendValue(1010, 1014, 1.0);
        hall.setFriendValue(1010, 1015, 1.0);

        hall.setFriendValue(1011, 1012, 1.0);
        hall.setFriendValue(1011, 1013, 1.0);
        hall.setFriendValue(1011, 1014, 1.0);
        hall.setFriendValue(1011, 1015, 1.0);

        hall.setFriendValue(1012, 1013, 1.0);
        hall.setFriendValue(1012, 1014, 1.0);
        hall.setFriendValue(1012, 1015, 1.0);

        hall.setFriendValue(1013, 1014, 1.0);
        hall.setFriendValue(1013, 1015, 1.0);

        hall.setFriendValue(1014, 1015, 1.0);

        hall.addListeners();

    }

    public void loadTest08(){
        clear();
        add_group_in_cricle(100, 100, 3);

        hall.setFriendValue(1000,1001,1.0);
        hall.setFriendValue(1001,1002,1.0);

        hall.addListeners();
    }

    public void loadTest09(){
        clear();
        add_group_in_cricle(100, 100, 2);
        add_group_in_cricle(400, 200, 5);


        hall.setFriendValue(1000,1001,1.0);

        hall.setFriendValue(1001,1005,1.0);

        hall.setFriendValue(1002,1003,1.0);
        hall.setFriendValue(1002,1006,1.0);

        hall.setFriendValue(1004,1005,1.0);
        hall.setFriendValue(1004,1006,1.0);

        hall.addListeners();
    }

    public void loadTest10(){
        clear();
        add_group_in_cricle(100, 100, 2);

        hall.setFriendValue(1000,1001,1.0);

        hall.addListeners();
    }

    public void loadTest11(){
        clear();
        hall.addPerson("A",1000,"Halle",0,0);
        hall.getAllPersonsInHall().get(hall.getAllPersonsInHall().size()-1).setLocation(100,100);

        hall.addPerson("B",1001,"Halle",0,0);
        hall.getAllPersonsInHall().get(hall.getAllPersonsInHall().size()-1).setLocation(150,150);
        hall.setFriendValue(1000,1001,0.5);
        hall.addListeners();
    }

    public void loadTest12() {
        clear();
/*
        hall.addPerson("A",1000,"Halle");
        hall.getAllPersonsInHall().get(hall.getAllPersonsInHall().size()-1).setLocation(300,300);

        hall.addPerson("B",1001,"Halle");
        hall.getAllPersonsInHall().get(hall.getAllPersonsInHall().size()-1).setLocation(350,350);

        hall.addPerson("C",1002,"Halle");
        hall.getAllPersonsInHall().get(hall.getAllPersonsInHall().size()-1).setLocation(250,350);


        hall.addPerson("D",1003,"Halle");
        hall.getAllPersonsInHall().get(hall.getAllPersonsInHall().size()-1).setLocation(100,50);

        hall.addPerson("E",1004,"Halle");
        hall.getAllPersonsInHall().get(hall.getAllPersonsInHall().size()-1).setLocation(130,75);

        hall.addPerson("F",1005,"Halle");
        hall.getAllPersonsInHall().get(hall.getAllPersonsInHall().size()-1).setLocation(115,100);

        hall.addPerson("G",1006,"Halle");
        hall.getAllPersonsInHall().get(hall.getAllPersonsInHall().size()-1).setLocation(85,100);

        hall.addPerson("H",1007,"Halle");
        hall.getAllPersonsInHall().get(hall.getAllPersonsInHall().size()-1).setLocation(70,75);


        hall.addPerson("I",1008,"Halle");
        hall.getAllPersonsInHall().get(hall.getAllPersonsInHall().size()-1).setLocation(600,200);

        hall.addPerson("J",1009,"Halle");
        hall.getAllPersonsInHall().get(hall.getAllPersonsInHall().size()-1).setLocation(625,200);

        hall.addPerson("K",1010,"Halle");
        hall.getAllPersonsInHall().get(hall.getAllPersonsInHall().size()-1).setLocation(650,225);

        hall.addPerson("L",1011,"Halle");
        hall.getAllPersonsInHall().get(hall.getAllPersonsInHall().size()-1).setLocation(650,250);

        hall.addPerson("M",1012,"Halle");
        hall.getAllPersonsInHall().get(hall.getAllPersonsInHall().size()-1).setLocation(625,275);

        hall.addPerson("N",1013,"Halle");
        hall.getAllPersonsInHall().get(hall.getAllPersonsInHall().size()-1).setLocation(600,275);

        hall.addPerson("O",1014,"Halle");
        hall.getAllPersonsInHall().get(hall.getAllPersonsInHall().size()-1).setLocation(575,250);

        hall.addPerson("P",1015,"Halle");
        hall.getAllPersonsInHall().get(hall.getAllPersonsInHall().size()-1).setLocation(575,225);



        // A..C
        add_group_in_cricle(300, 300, 3);

        // D..H
        add_group_in_cricle(100,50, 5);

        // I..P
        add_group_in_cricle(600,200, 8);

        hall.addPerson("Admin", id_index, "Office",0,0);

        // friend values

        // A, B, C
        hall.setFriendValue(1000,1001,close_friends);
        hall.setFriendValue(1001,1002,close_friends);
        hall.setFriendValue(1000,1002,close_friends);

        // close friends of D
        int[] close_friends_of_d = {1005, 1006, 1012};
        for (int i : close_friends_of_d) {
            hall.setFriendValue(1003,i,close_friends);

        }

        // average friends of D
        int[] average_friends_of_d = {1004,1007,1008,1009,1010,1011,1013,1014,1015};
        for (int i : average_friends_of_d) {
            hall.setFriendValue(1003,i,average_friends);

        }

        // friend of H
        hall.setFriendValue(1006,1007,close_friends);

        hall.addListeners();

        // last group
        for (int i=1008; i<1016; i++) {
            for (int j=i; j<1016; j++) {
                hall.setFriendValue(i,j,close_friends);
            }
            hall.setFriendValue(1003,i,average_friends);
        }
    }

    public void loadTest13() {
        clear();
        loadTest12();
    }

    public void loadTest14() {
        clear();
        loadTest12();
    }

    public void loadTest15() {
        clear();
    }

    public void loadTestStayAlone() {
        clear();
        hall.addPerson("A",1000,"Halle",0,0);
        hall.getAllPersonsInHall().get(hall.getAllPersonsInHall().size()-1).setLocation(300,300);

        hall.addPerson("B",1001,"Halle",0,0);
        hall.getAllPersonsInHall().get(hall.getAllPersonsInHall().size()-1).setLocation(350,350);

        hall.addPerson("C",1002,"Halle",0,0);
        hall.getAllPersonsInHall().get(hall.getAllPersonsInHall().size()-1).setLocation(250,350);

        Person p1 = hall.addPerson("D",1003,"Halle",0,0);
        hall.getAllPersonsInHall().get(hall.getAllPersonsInHall().size()-1).setLocation(100,50);

        Person p =hall.addPerson("E",1004,"Halle",0,0);
        hall.getAllPersonsInHall().get(hall.getAllPersonsInHall().size()-1).setLocation(130,75);


        hall.setFriendValue(1000,1001,close_friends);
        hall.setFriendValue(1001,1002,close_friends);
        hall.setFriendValue(1000,1002,close_friends);
        hall.setFriendValue(1003,1002,average_friends);
        hall.setFriendValue(1004,1002,close_friends);
        p.stayAlone();
        p1.stayAlone();


    }
    public void clear()
    {
        hall.getPlayfield().getChildren().removeAll(hall.getAllPersonsInHall());
        hall.getPlayfield().getChildren().removeAll(hall.getAllPersonsAtOffice());
        hall.getAllPersonsInHall().clear();
        hall.getPersonsInHall().clear();
        name_index = 0;
        id_index = 1000;
    }

    // index of next possible person in alphabetical order (0=A)
    private int name_index = 0;
    // next possible id
    private int id_index = 1000;
    /**
     * Adds a group of persons to the hall.
     * Persons will be arranged in a circle.
     *
     * @param a x coordinate of group
     * @param b y coordinate of group
     * @param group_size number of persons to add
     */
    /*
    private void add_group_in_cricle(int a, int b, int group_size) {
        char[] alphabet = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};

        for(int i=0; i<group_size; i++) {
            double t = 2 * Math.PI * i / group_size;
            int x = (int) Math.round(a + group_size * 6 * Math.cos(t));
            int y = (int) Math.round(b + group_size * 6 * Math.sin(t));
            hall.addPerson(Character.toString(alphabet[name_index]), id_index, "Halle",0,0).setLocation(x,y);
            name_index++;
            id_index++;
        }
    }
*/
}
