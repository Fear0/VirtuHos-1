package wb.analyse1.GUI;

import java.awt.*;

public class Vertex {
        private Point point;

        public Vertex(Point point) {
            this.point = point;
        }

        public Point getPoint() {
            return point;
        }

        @Override
        public String toString() {
            return "Vertex @ " ;
        }


}
