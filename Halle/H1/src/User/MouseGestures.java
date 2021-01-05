package User;

import Server.Client;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;

import javafx.scene.shape.Rectangle;


public class MouseGestures {
    Client client;

    final DragContext dragContext = new DragContext();
    private  Rectangle areas[];
    public MouseGestures(Rectangle areas[]){

        this.areas=areas;
    }

    public void setClient(Client client) {
        this.client = client;
    }



    Group group;
    public void makeDraggable(final Sprite sprite) {
        sprite.setOnMousePressed(onMousePressedEventHandler);
        sprite.setOnMouseDragged(onMouseDraggedEventHandler);
        sprite.setOnMouseReleased(onMouseReleasedEventHandler);

    }


    public void checkBounds(Sprite block) {
        boolean collisionDetected = false;
        for (Rectangle static_bloc : areas) {


            Bounds a=static_bloc.localToScene(static_bloc.getBoundsInLocal());
            Bounds b=block.localToScene(block.getBoundsInLocal());
            if (a.intersects(b)){


                    collisionDetected = true;
                    block.setVirtualLocation(static_bloc.getId());



                }
            else {




            }
        }

        if (collisionDetected) {

        } else {

            block.setVirtualLocation("left");


        }
    }


    EventHandler<MouseEvent> onMousePressedEventHandler = new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent event) {

            dragContext.x = event.getSceneX();
            dragContext.y = event.getSceneY();

            if (((Sprite) event.getSource()).isClicked())
                ((Sprite) event.getSource()).clicked(false);
            else
            ((Sprite) event.getSource()).clicked(true);






        }


    };

    EventHandler<MouseEvent> onMouseDraggedEventHandler = new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent event) {

            Sprite sprite = (Sprite) event.getSource();
            //  if(event.getSceneX()<640 &&event.getSceneY()<490&&event.getSceneX()>250&&event.getSceneY()>170){
            double offsetX = event.getSceneX() - dragContext.x;
            double offsetY = event.getSceneY() - dragContext.y;

            sprite.setLocationOffset(offsetX, offsetY);

            dragContext.x = event.getSceneX();
            dragContext.y = event.getSceneY();
            client.moveperson(offsetX ,offsetY, dragContext.x, dragContext.y,sprite.getMyId());

            checkBounds(sprite);
        }
    };

    EventHandler<MouseEvent> onMouseReleasedEventHandler = new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent event) {

        }
    };

    class DragContext {

        double x;
        double y;

    }

}