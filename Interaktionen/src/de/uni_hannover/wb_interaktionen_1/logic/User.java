package de.uni_hannover.wb_interaktionen_1.logic;

import de.uni_hannover.wb_interaktionen_1.bbb_api.BBBMeeting;
import de.uni_hannover.wb_interaktionen_1.rooms.Hall;
import de.uni_hannover.wb_interaktionen_1.rooms.HallGroup;
import de.uni_hannover.wb_interaktionen_1.rooms.Meeting;
import de.uni_hannover.wb_interaktionen_1.rooms.Room;
import de.uni_hannover.wb_interaktionen_1.test_db.TestDB;
import de.uni_hannover.wb_interaktionen_1.website.WebsiteOpener;
import de.uni_hannover.wb_interaktionen_1.xml.XMLParser;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.sql.SQLException;

/** Class User in which the necessary information to connect a user with the appropriate ID and name is stored.
 *
 * @author David Sebode
 * @version 1.0
 */
public class User {
    private String id;
    private String name;
    private Meeting current_meeting;
    private Room current_room;
    private HallGroup group;
    private WebDriver web;
    private boolean creator;

    /** Constructor of User
     * @param id Personal ID of the user to identify him
     * @param name Name of the user that will be shown in meetings
     */
    public User(String id, String name, Room starting_room, HallGroup group){
        this.id = id;
        this.name = name;
        this.current_meeting = null;
        this.group = group;
        this.current_room = starting_room;
        this.creator = false;
    }

    /** Getter for the ID
     * @return ID of the user
     */
    public String getId() {
        return id;
    }

    /** Getter for the name
     * @return Name of the user
     */
    public String getName() {
        return name;
    }

    /** Getter for the current meeting
     * @return Meeting the user is currently part of
     */
    public Meeting getCurrent_meeting() { return current_meeting; }

    /** Setter for the current meeting
     * @param new_meeting : Meeting the user joined
     */
    public void setCurrent_meeting(Meeting new_meeting) {
        this.current_meeting = new_meeting;
        update_toDB();
    }

    /** Getter for the current room
     * @return room the user is currently part of
     */
    public Room getCurrent_room() { return this.current_room; }

    /** Setter for the current room
     * @param new_room : Room the user joined
     */
    public void setCurrent_room(Room new_room, TestDB db) {
        this.current_room = new_room;
        try{
            db.addUserToRoom(getId(), new_room.getId());
        } catch (SQLException e){
            e.printStackTrace();
        }
        //update_toDB();
    }

    public HallGroup getGroup(){
        return group;
    }

    public void setGroup(HallGroup g){
        this.group = g;
    }

    /** Setter for the parameter creator. It is used to mark the creator of a meeting.
     * @param b value that has to be set to the creator variable
     */
    public void setCreator(boolean b){
        this.creator = b;
    }

    /** This function opens a webpage in Firefox. It opens the BBB-Meeting.
     * @param url the URL from the BBB-Meeting
     * @author David Sasse, David Sebode
     */
    public void openWebpage(String url){
        try{
            if(WebsiteOpener.isMac()){
                System.setProperty("webdriver.gecko.driver", "geckodriver");
                System.setProperty("webdriver.chrome.driver", "chromedriver");
            } else {
                System.setProperty("webdriver.gecko.driver", "geckodriver.exe");
                System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
            }
            web = new ChromeDriver();
            web.get(url);
        } catch (WebDriverException ex1){ // Fallback if the driver or Chrome fail TODO Exception verfeinern
            try{
                web = new FirefoxDriver();
                web.get(url);
            } catch (WebDriverException ex2){ // Fallback if the driver or Firefox fail TODO Exception verfeinern
                WebsiteOpener WO = new WebsiteOpener(url); // Create a new website opener
                WO.open();
            }
        }
    }

    /** It closes the webpage, which was opened by openWebpage.
     * It is used to leave a BBB-Meeting.
     */
    public void closeWebpage(){
        try{
            if(web != null) {
                web.close();
            }
        } catch (org.openqa.selenium.WebDriverException ex){
            return;
        }
    }

    /** Activate the webcam of the user
     */
    public void activateWebcam(){
        try{
            web.findElement(By.id("tippy-29")).click();
        } catch (org.openqa.selenium.NoSuchElementException ex){
            try{
                web.findElement(By.id("tippy-25")).click();
            } catch (org.openqa.selenium.NoSuchElementException sex){
                System.out.println("Whoops, there was a problem with the webcam request");
            }
        }

    }

    public void update_toDB(){
        //push meeting and room to db
        //current_room.main.getDB().update_meeting(id, current_meeting);
        //current_room.main.getDB().update_room(id, current_room);
    }

    public String toString() {
        return this.name;
    }
}
