package User;


import Halle.Grp;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Sprite extends Region {


    protected String name;
    protected int id;

    private String virtualLocation;
    private Map<Integer , Integer > friendsValue = new HashMap<>();



    private boolean clicked=false;

    Vector2D location;
    Vector2D velocity;
    public Vector2D acceleration;


    public double maxForce = Settings.SPRITE_MAX_FORCE;
    public  double maxSpeed = Settings.SPRITE_MAX_SPEED;

    Node view;

    // view dimensions
    double width;
    double height;
    double centerX;
    double centerY;
    double radius;

    double angle;
    Pane layer = null;

    public void setLayer(Pane layer) {

        // add this node to layer
        layer.getChildren().add(this);
    }


    public void setLocation(Vector2D location) {
        this.location = location;
    }

    public void setVelocity(Vector2D velocity) {
        this.velocity = velocity;
    }

    public Vector2D getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(Vector2D acceleration) {
        this.acceleration = acceleration;
    }

    public double getCenterX() {
        return centerX;
    }

    public void setCenterX(double centerX) {
        this.centerX = centerX;
    }

    public double getCenterY() {
        return centerY;
    }

    public void setCenterY(double centerY) {
        this.centerY = centerY;
    }

    public void setnewinfo(Vector2D plocation, Vector2D pacceleration,Vector2D velo, double x, double y){
        velocity=velo;
        location=plocation;
        acceleration=pacceleration;
        centerX=x;
        centerY=y;
    }

    public Sprite( Pane layer, Vector2D location, Vector2D velocity, Vector2D acceleration, double width, double height, String name, int id) {



        this.location = location;

        this.velocity = velocity;
        this.acceleration = acceleration;
        this.width = width;
        this.height = height;
        this.centerX = width / 2;
        this.centerY = height / 2;
        this.name = name;
        this.id = id;
        this.layer = layer;
        this.radius = Settings.PERSON_SIZE;

        this.view = createView();

        setPrefSize(width, height);

        // add view to this node
        getChildren().add(view);

        layer.getChildren().add(this);


    }

    public abstract Node createView();

    public void applyForce(Vector2D force) {
        acceleration.add(force);
    }

    private boolean inBounds(){
        // check x bounds
        if (location.x - radius < 0 || location.x + radius > layer.getWidth()) {
            //System.out.println("Too close to x-Bounds.");
            return false;
        }
        // check y bounds
        if (location.y - radius < 0 || location.y + radius > layer.getHeight()) {
            //System.out.println("Too close to y-Bounds.");
            return false;
        }
        return true;
    }

    public void move() {
        // x bounds
        if (location.x - radius < 0) {
            // left bound -> only move right
            if (acceleration.x < 0) {
                acceleration.x = 0;
            }
        } else if (location.x + radius > layer.getWidth()) {
            // right bound -> only move left
            if (acceleration.x > 0) {
                acceleration.x = 0;
            }
        }

        // y bounds
        if (location.y - radius < 0) {
            //System.out.println("The radius of all circles is" + radius);
            // upper bound -> only move down
            if (acceleration.y < 0) {
                acceleration.y = 0;
            }
        } else if (location.y + radius > layer.getHeight()) {
            // lower bound -> only move up
            if (acceleration.y > 0) {
                acceleration.y = 0;
            }
        }
        // set velocity depending on acceleration
        velocity.add(acceleration);

        // limit velocity to max speed
        velocity.limit(maxSpeed);

        // change location depending on velocity
        location.add(velocity);

        // angle: towards velocity (ie target)
        angle = velocity.heading2D();

        // clear acceleration
        acceleration.multiply(0);
    }

    public void stop() {
        // set velocity depending on acceleration
        velocity.add(new Vector2D(0, 0));

        // limit velocity to max speed
        velocity.limit(0);

        // change location depending on velocity
        location.add(velocity);

        // angle: towards velocity (ie target)
        angle = velocity.heading2D();

        // clear acceleration
        acceleration.multiply(0);

    }

    /**
     * Move sprite towards target
     */
    public void seek(Vector2D target,double  attractiveSpeed) {

        Vector2D desired = Vector2D.subtract(target, location);

        // The distance is the magnitude of the vector pointing from location to target.

        double d = desired.magnitude();
        desired.normalize();
        Vector2D steer;


        // If we are closer than 100 pixels...
        if (d < Settings.SPRITE_SLOW_DOWN_DISTANCE) {

            // ...set the magnitude according to how close we are.
            double m = Utils.map(d, 0, Settings.SPRITE_SLOW_DOWN_DISTANCE, 0, attractiveSpeed);




        }
        // Otherwise, proceed at maximum speed.
        else {
            desired.multiply(attractiveSpeed*10);

        }
         steer = Vector2D.subtract(desired, velocity);

        steer.limit(maxForce);


        applyForce(steer);
        // The usual steering = desired - velocity


    }

    /**
     * Move sprite away from all targets
     */
    public void flee(List<Person> allAttractors) {

        Vector2D desired = new Vector2D(0,0);
        allAttractors.forEach(attractor -> {


            Vector2D target = attractor.getLocation();
            if (Vector2D.subtract(target, location).magnitude() < Settings.SPRITE_TRACKING_RANGE) {
                Vector2D temp = Vector2D.subtract(location, target);
                double d = Settings.SPRITE_TRACKING_RANGE - temp.magnitude();
                temp.multiply(d/0.1);
                desired.add(temp);
            }
            if (layer.getWidth() - location.x < Settings.SPRITE_TRACKING_RANGE) {
                Vector2D temp = Vector2D.subtract(location, new Vector2D(layer.getWidth(),location.y));
                double d = Settings.SPRITE_TRACKING_RANGE - temp.magnitude();
                temp.multiply(d/0.1);
                desired.add(temp);
            }
            if (location.x < Settings.SPRITE_TRACKING_RANGE) {
                Vector2D temp = Vector2D.subtract(location, new Vector2D(0,location.y));
                double d = Settings.SPRITE_TRACKING_RANGE - temp.magnitude();
                temp.multiply(d/0.1);
                desired.add(temp);
            }
        });

        if (desired.magnitude() < Settings.FORCE_TOLERANCE) {
            desired.multiply(0);
        }

        // The usual steering = desired - velocity
        Vector2D steer = Vector2D.subtract(desired, velocity);
        steer.limit(maxForce);

        applyForce(steer);

    }

    public void fleep(List<Person> allAttractors, double value) {

        Vector2D desired = new Vector2D(0,0);
        allAttractors.forEach(attractor -> {

            Vector2D target = attractor.getLocation();
            if (Vector2D.subtract(target, location).magnitude() < value) {
                Vector2D temp = Vector2D.subtract(location, target);
                double d = value - temp.magnitude();
                temp.multiply(d/0.1);
                desired.add(temp);
            }
            if (layer.getWidth() - location.x < value) {
                Vector2D temp = Vector2D.subtract(location, new Vector2D(layer.getWidth(),location.y));
                double d = value - temp.magnitude();
                temp.multiply(d/0.1);
                desired.add(temp);
            }
            if (location.x < value) {
                Vector2D temp = Vector2D.subtract(location, new Vector2D(0,location.y));
                double d = value - temp.magnitude();
                temp.multiply(d/0.1);
                desired.add(temp);
            }
        });

        if (desired.magnitude() < Settings.FORCE_TOLERANCE) {
            desired.multiply(0);
        }

        // The usual steering = desired - velocity
        Vector2D steer = Vector2D.subtract(desired, velocity);
        steer.limit(maxForce);

        applyForce(steer);

    }
    public void flee(Person attractor) {

        Vector2D desired = new Vector2D(0,0);

            Vector2D target = attractor.getLocation();
            if (Vector2D.subtract(target, location).magnitude() < Settings.SPRITE_TRACKING_RANGE) {
                Vector2D temp = Vector2D.subtract(location, target);
                double d = Settings.SPRITE_TRACKING_RANGE - temp.magnitude();
                temp.multiply(d/0.1);
                desired.add(temp);
            }
            if (layer.getWidth() - location.x < Settings.SPRITE_TRACKING_RANGE) {
                Vector2D temp = Vector2D.subtract(location, new Vector2D(layer.getWidth(),location.y));
                double d = Settings.SPRITE_TRACKING_RANGE - temp.magnitude();
                temp.multiply(d/0.1);
                desired.add(temp);
            }
            if (location.x < Settings.SPRITE_TRACKING_RANGE) {
                Vector2D temp = Vector2D.subtract(location, new Vector2D(0,location.y));
                double d = Settings.SPRITE_TRACKING_RANGE - temp.magnitude();
                temp.multiply(d/0.1);
                desired.add(temp);
            }


        if (desired.magnitude() < Settings.FORCE_TOLERANCE) {
            desired.multiply(0);
        }

        // The usual steering = desired - velocity
        Vector2D steer = Vector2D.subtract(desired, velocity);
        steer.limit(maxForce);

        applyForce(steer);

    }


    /**
     * Update node position
     */
    public void display() {

        //relocate(location.x - centerX, location.y - centerY);
        relocate(location.x-centerX, location.y-centerY);


        //setRotate(Math.toDegrees(angle));

    }
    public String getName() {
        return name;
    }
    public Vector2D getVelocity() {
        return velocity;
    }

    public Vector2D getLocation() {
        return location;
    }

    public void setLocation(double x, double y) {
        location.x = x;
        location.y = y;
    }

    public void setLocationOffset(double x, double y) {
        location.x += x;
        location.y += y;
    }
    public void addFriend(int id,int value ) {
        friendsValue.put(id,value);
        //Collections.sort(powerList);
        //group.getChildren().add(connect(person,value));
    }
    public Integer getFriendValue(int id ) {
        return friendsValue.get(id);
        //Collections.sort(powerList);
        //group.getChildren().add(connect(person,value));
    }



    public int getMyId() {
        return id;
    }

    @Override
    public String toString() {
        return "Name: " + name + ", ID: " + id;
    }

    public String getVirtualLocation() {
        return virtualLocation;
    }

    public void setVirtualLocation(String virtualLocation) {
        this.virtualLocation = virtualLocation;
    }
    public void setRoom( String Position )
    {
        if (Position.equals("Halle"))
            this.location = new Vector2D( 100,100);
        else if (Position.equals("Office"))
            this.location=new Vector2D(-35,63);


    }
    public void clicked(boolean click)
    {
        this.clicked=click;
    }
    public boolean isClicked() {
        return clicked;
    }

}