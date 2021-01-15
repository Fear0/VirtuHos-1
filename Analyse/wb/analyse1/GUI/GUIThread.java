package wb.analyse1.GUI;

import wb.analyse1.analyse.User;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.LinkedHashSet;

import javax.swing.*;

/**
 * the View Class that will be compiled
 *
 * @author Frikha
 */
public  class GUIThread implements Runnable {

    private Model model;
    public View view;
    private Controller controller;
    private boolean exit = false;

    public GUIThread() {

    }

    public GUIThread(int[][] networkMatrix, LinkedHashSet<User> users, LinkedHashSet<User> onlineUsers, Client client) {

        if (networkMatrix == null || users == null || onlineUsers == null) {
            int[][] empty = {{}};
            LinkedHashSet<User> usersSet = new LinkedHashSet<>();
            LinkedHashSet<User> onlineUsersSet = new LinkedHashSet<>();
            this.model = new Model(empty, usersSet, onlineUsersSet);
        } else {
            this.model = new Model(networkMatrix, users, onlineUsers);
        }
        this.view = new View();
        this.controller = new Controller(model, view);
        model.addObserver(this.controller);
        view.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        view.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);

                if(client != null) {
                    client.end();
                    System.out.println("windowlistener if");
                }
                view.dispose();
            }
        });
        view.setTitle("WB ANALYSE 1");
        view.setBounds(500, 200, 1280, 720);
        //View.setExtendedState(JFrame.MAXIMIZED_BOTH);
        view.setResizable(false);
        view.revalidate();
        view.repaint();
        view.setVisible(true);

    }

    public void refresh() {
        this.view.revalidate();
        this.view.repaint();
    }

    public void setModel(int[][] networkMatrix, LinkedHashSet<User> users, LinkedHashSet<User> onlineUsers) {
        this.model.setCalc(new Calculation(networkMatrix, users, onlineUsers));
    }


    @Override
    public void run() {
        //SwingUtilities.invokeLater(this);
        System.out.println("run GUIThread");
    }
}





