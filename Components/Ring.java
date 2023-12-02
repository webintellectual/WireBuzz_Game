package Components;

import java.awt.Rectangle;

public class Ring {
    public Rectangle upper;
    public Rectangle lower;
    public Rectangle left;
    public Rectangle right;

    public Ring(int x, int y, int height, int thickness) {
        int outerWidth = height * 2 / 5; // Make the width 75% of the height
        // int innerWidth = outerWidth - 2 * thickness;
        this.upper = new Rectangle(x, y, outerWidth, thickness);
        this.lower = new Rectangle(x, y + height - thickness, outerWidth, thickness);
        this.left = new Rectangle(x, y + thickness, thickness, height - 2 * thickness);
        this.right = new Rectangle(x + outerWidth - thickness, y + thickness, thickness, height - 2 * thickness);
    }
}