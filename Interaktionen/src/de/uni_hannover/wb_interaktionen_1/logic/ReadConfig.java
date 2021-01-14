package de.uni_hannover.wb_interaktionen_1.logic;

import java.io.*;
import java.util.Properties;

public class ReadConfig {
    public String url;
    public String user;
    public String password;
    public String DATABASE_NAME;
    public String API_URL;
    public String Building;
    InputStream inputS;

    public ReadConfig() throws IOException {

        try {
            Properties prop = new Properties();
            String propFileName = "config.properties";

            inputS = getClass().getClassLoader().getResourceAsStream(propFileName);

            if (inputS != null){
                prop.load(inputS);
            } else {
                throw new FileNotFoundException("property file '"+propFileName+"' not found");
            }

            url = prop.getProperty("url");
            user = prop.getProperty("user");
            password = prop.getProperty("password");
            DATABASE_NAME = prop.getProperty("DATABASE_NAME");
            API_URL = prop.getProperty("API_URL");
            Building = prop.getProperty("Building");
        } catch (Exception e) {
            System.out.println("Exception: "+e);
        } finally {
            inputS.close();
        }
    }

    /**
     * A simple class to Test the current class variables could be removed!
     */
    public void PrintClass(){
        System.out.println(url);
        System.out.println(user);
        System.out.println(password);
        System.out.println(DATABASE_NAME);
        System.out.println(API_URL);
        System.out.println(Building);
    }

    /**
     * Updates the class variables to the newest that can be found in the config file
     *
     * @throws IOException
     */
    public void Update() throws IOException{
        try {
            Properties prop = new Properties();
            String propFileName = "config.properties";

            inputS = getClass().getClassLoader().getResourceAsStream(propFileName);

            if (inputS != null){
                prop.load(inputS);
            } else {
                throw new FileNotFoundException("property file '"+propFileName+"' not found");
            }

            url = prop.getProperty("url");
            user = prop.getProperty("user");
            password = prop.getProperty("password");
            DATABASE_NAME = prop.getProperty("DATABASE_NAME");
            API_URL = prop.getProperty("API_URL");
            Building = prop.getProperty("Building");
        } catch (Exception e) {
            System.out.println("Exception: "+e);
        } finally {
            inputS.close();
        }
    }

    /**
     * Resets the config file to the default values
     */
    public void Reset(){
        String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        String ConfigPath = rootPath + "config.properties";
        //System.out.println(ConfigPath)
        Properties config = new Properties();
        try {
            config.load(new FileInputStream(ConfigPath));
        } catch (Exception e) {
            System.out.println("Exception: '"+e);
        }
        config.setProperty("url", "jdbc:mysql://goethe.se.uni-hannover.de:3306/?user=Interaktion_1");
        config.setProperty("user", "Interaktion_1");
        config.setProperty("password", "JVx;2brXZzFq");
        config.setProperty("DATABASE_NAME", "VirtuHoS_1.");
        config.setProperty("API_URL", "https://bbb2.se.uni-hannover.de/bigbluebutton/api/");
        config.setProperty("Building", "0");
        try {
            FileWriter writer = new FileWriter(ConfigPath);
            config.store(writer,"test");
            this.Update();
        } catch (Exception e) {
            System.out.println("Exception: '"+ e);
        }
    }

    /**
     * A function to update the url parameter in the config file
     * @param in the new url
     */
    public void write_url(String in){
        String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        String ConfigPath = rootPath + "config.properties";
        //System.out.println(ConfigPath)
        Properties config = new Properties();
        try {
            config.load(new FileInputStream(ConfigPath));
        } catch (Exception e) {
            System.out.println("Exception: '"+e);
        }
        config.setProperty("url", in);
        try {
            FileWriter writer = new FileWriter(ConfigPath);
            config.store(writer,"test");
            this.Update();
        } catch (Exception e) {
            System.out.println("Exception: '"+ e);
        }
    }

    /**
     * A function to update the user parameter in the config file
     * @param in the new user
     */
    public void write_user(String in){
        String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        String ConfigPath = rootPath + "config.properties";
        //System.out.println(ConfigPath)
        Properties config = new Properties();
        try {
            config.load(new FileInputStream(ConfigPath));
        } catch (Exception e) {
            System.out.println("Exception: '"+e);
        }
        config.setProperty("user", in);
        try {
            FileWriter writer = new FileWriter(ConfigPath);
            config.store(writer,"test");
            this.Update();
        } catch (Exception e) {
            System.out.println("Exception: '"+ e);
        }
    }

    /**
     * A function to update the password parameter in the config file
     * @param in the new password
     */
    public void write_password(String in){
        String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        String ConfigPath = rootPath + "config.properties";
        //System.out.println(ConfigPath)
        Properties config = new Properties();
        try {
            config.load(new FileInputStream(ConfigPath));
        } catch (Exception e) {
            System.out.println("Exception: '"+e);
        }
        config.setProperty("password", in);
        try {
            FileWriter writer = new FileWriter(ConfigPath);
            config.store(writer,"test");
            this.Update();
        } catch (Exception e) {
            System.out.println("Exception: '"+ e);
        }
    }

    /**
     * A function to update the DATABASE_NAME parameter in the config file
     * @param in the new DATABASE_NAME
     */
    public void write_DATABASE_NAME(String in){
        String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        String ConfigPath = rootPath + "config.properties";
        //System.out.println(ConfigPath)
        Properties config = new Properties();
        try {
            config.load(new FileInputStream(ConfigPath));
        } catch (Exception e) {
            System.out.println("Exception: '"+e);
        }
        config.setProperty("DATABASE_NAME", in);
        try {
            FileWriter writer = new FileWriter(ConfigPath);
            config.store(writer,"test");
            this.Update();
        } catch (Exception e) {
            System.out.println("Exception: '"+ e);
        }
    }

    /**
     * A function to update the API_URL parameter in the config file
     * @param in the new API_URL
     */
    public void write_API_URL(String in) {
        String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        String ConfigPath = rootPath + "config.properties";
        //System.out.println(ConfigPath)
        Properties config = new Properties();
        try {
            config.load(new FileInputStream(ConfigPath));
        } catch (Exception e) {
            System.out.println("Exception: '" + e);
        }
        config.setProperty("API_URL", in);
        try {
            FileWriter writer = new FileWriter(ConfigPath);
            config.store(writer, "test");
            this.Update();
        } catch (Exception e) {
            System.out.println("Exception: '" + e);
        }
    }

    /**
     * A function to update the Building parameter in the config file
     * @param in the new Building
     */
    public void write_Building(String in) {
        String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        String ConfigPath = rootPath + "config.properties";
        //System.out.println(ConfigPath)
        Properties config = new Properties();
        try {
            config.load(new FileInputStream(ConfigPath));
        } catch (Exception e) {
            System.out.println("Exception: '" + e);
        }
        config.setProperty("Building", in);
        try {
            FileWriter writer = new FileWriter(ConfigPath);
            config.store(writer, "test");
            this.Update();
        } catch (Exception e) {
            System.out.println("Exception: '" + e);
        }
    }
}
