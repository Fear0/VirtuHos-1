package wb.analyse1.GUI;

import wb.analyse1.analyse.User;

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

    public GUIThread() {

    }

    public GUIThread(int[][] networkMatrix, LinkedHashSet<User> users, LinkedHashSet<User> onlineUsers) {

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
        view.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
        SwingUtilities.invokeLater(this);
    }
}


