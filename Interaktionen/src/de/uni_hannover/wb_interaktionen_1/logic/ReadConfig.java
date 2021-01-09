package de.uni_hannover.wb_interaktionen_1.logic;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ReadConfig {
    public String url;
    public String user;
    public String password;
    public String DATABASE_NAME;
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
        } catch (Exception e) {
            System.out.println("Exception: "+e);
        } finally {
            inputS.close();
        }
    }

    public void TestClass(){
        System.out.println(url);
        System.out.println(user);
        System.out.println(password);
        System.out.println(DATABASE_NAME);
    }

    /*
    public void Write(){
        PropertiesConfiguration config = new PropertiesConfiguration("/Users/<username>/Documents/config.properties");
        config.setProperty("company1", "Crunchify");
        config.setProperty("company2", "Google");
        config.setProperty("Crunchify_Address", "NYC, US");
        config.setProperty("Google_Address", "Mountain View, CA, US");
        config.save();

        System.out.println("Config Property Successfully Updated..");
    }
    */
}
