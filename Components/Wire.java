package Components;

import java.awt.geom.Path2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.AffineTransform;

public class Wire {
    
    public Path2D path;

    public Wire(int initialX, int initialY) {
        path = new GeneralPath();
        path.moveTo(initialX, initialY);
    }

    public void addPoint(int x, int y) {
        path.lineTo(x, y);
    }

    public void shiftLeft(int distance) {
        path.transform(AffineTransform.getTranslateInstance(-distance, 0));
    }
}