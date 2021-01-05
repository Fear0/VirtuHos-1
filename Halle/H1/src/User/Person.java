package User;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;


public class Person extends Sprite {
    private Circle circle;
    public boolean effect= false;
    private StackPane pane;
    //false means here that the person doesn't want to stay alone
    boolean alone = false;

    private int groupid;
    public Person( Pane layer,Vector2D location, Vector2D velocity, Vector2D acceleration, double width, double height, String name, int id) {
        super(layer, location, velocity, acceleration, width, height, name, id);
        groupid=-1;
    }
    public void resetgroup(){ groupid=-1;}
    @Override
    public Node createView() {
         pane = Utils.createCircleImageView( (int) width,id,name,pane );
         circle= (Circle) pane.getChildren().get(0);
         return pane;
    }

    public int getGroupid() {
        return groupid;
    }

    public void setGroupid(int groupid) {
        this.groupid = groupid;
    }

    public Circle getCircle(){
        return (Circle) pane.getChildren().get(0);
    }
public StackPane getstackPane(){return pane;}

    /**
     *  adds a flag to person that he doesn't want to join a group
     */
    public void stayAlone(){
        getCircle().setFill(Color.RED);
        alone = true;
    }
    public void notAlone(){

        alone = false;
    }

    public boolean isAlone() {
        return alone;
    }


}