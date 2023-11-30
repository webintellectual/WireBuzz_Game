import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JFrame;
import javax.swing.Timer;
import java.awt.geom.Point2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.Graphics2D;
import java.util.Random;
import java.awt.event.KeyEvent;

class Ring {
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

class Wire {
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

public class WireBuzz implements ActionListener, MouseListener, KeyListener {

    public static WireBuzz wireBuzz;

    public static final int WIDTH = 800, HEIGHT = 800;

    public Renderer renderer;

    // variable for ring
    public Ring ring;
    public static final int ringHeight = HEIGHT / 7;
    public static final int ringThickness = 4;
    public static final int ringX = (WIDTH) / 2 - 60;
    public static final int ringY = (HEIGHT) / 2 - ringHeight / 2;

    public int ticks, yMotion, time;
    public boolean gameOver = false, started = false;

    // variable for wire
    public Wire wire;

    int numberOfturns = 4; // The number of turns the wire will make in the current screen
    // increase the number of turns to increase the difficulty

    public WireBuzz() {
        JFrame jframe = new JFrame();
        Timer timer = new Timer(20, this);

        renderer = new Renderer();

        jframe.add(renderer);
        jframe.setTitle("WireBuzz");
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.setSize(WIDTH, HEIGHT);
        jframe.addMouseListener(this);
        jframe.addKeyListener(this);
        jframe.setResizable(false);
        jframe.setVisible(true);

        ring = new Ring(ringX, ringY, ringHeight, ringThickness);

        wire = new Wire(0, HEIGHT / 2); // Start the wire at the leftmost middle of the screen

        addWirePoint(true); // Add the first five points to the wire

        timer.start();
    }

    public void addWirePoint(boolean start) {

        // Set the upper and lower limits for the y coordinate
        int lowerLimit = HEIGHT / 2 - 50; // 50 units above the middle
        int upperLimit = HEIGHT / 2 + 50; // 50 units below the middle

        // when start then add five points
        if (start) {
            for (int i = 0; i < numberOfturns + 1; i++) {
                wire.path.lineTo(i * WIDTH / numberOfturns, HEIGHT / 2);
            }
        } else { // when not start then add one point

            // Get the last point in the wire
            Point2D lastPoint = wire.path.getCurrentPoint();

            // Generate a new x coordinate that is ahead of the last point's x coordinate
            int newX = (int) lastPoint.getX() + WIDTH / numberOfturns;

            // Generate a new y coordinate that is within the upper and lower limits
            Random random = new Random();
            int newY = random.nextInt(upperLimit - lowerLimit) + lowerLimit;

            // Add the new point to the wire
            wire.path.lineTo(newX, newY);
        }
    }

    public void paintWire(Graphics g) {
        g.setColor(Color.green.darker());
        Graphics2D g2d = (Graphics2D) g;
        float thickness = 5.5f; // Set the thickness of the wire
        g2d.setStroke(new BasicStroke(thickness));
        g2d.draw(wire.path);
    }

    public void removeOutOfScreenPoints() {
        Path2D newPath = new Path2D.Double();
        PathIterator iterator = wire.path.getPathIterator(null);
        float[] coords = new float[6];
        while (!iterator.isDone()) {
            int type = iterator.currentSegment(coords);
            if (type != PathIterator.SEG_MOVETO && type != PathIterator.SEG_LINETO) {
                iterator.next();
                continue;
            }
            float x = coords[0];
            float y = coords[1];
            if (x >= -WIDTH / numberOfturns) { // Only add the point if it is within the screen
                if (newPath.getCurrentPoint() == null) {
                    newPath.moveTo(x, y);
                } else {
                    newPath.lineTo(x, y);
                }
            }
            iterator.next();
        }
        wire.path = newPath; // Replace the old path with the new path
    }

    public void updateWire() {
        // Add a new point to the wire
        addWirePoint(false);

        // Remove points that are out of the screen
        removeOutOfScreenPoints();
    }

    public float checkIntersection() {

        float midX = ring.lower.x + ring.lower.width / 2; // The x-coordinate of the middle of the lower and upper rings
        float lowerMidY = ring.lower.y - ring.lower.height / 2; // The y-coordinate of the middle of the lower ring
        float upperMidY = ring.upper.y + ring.upper.height / 2; // The y-coordinate of the middle of the upper ring
        float wireY = -1;

        PathIterator iterator = wire.path.getPathIterator(null);
        float[] coords = new float[6];
        float[] prevCoords = new float[6];
        iterator.currentSegment(prevCoords);
        iterator.next();
        while (!iterator.isDone()) {
            int type = iterator.currentSegment(coords);
            if (type == PathIterator.SEG_LINETO) {

                if (prevCoords[0] <= midX && midX <= coords[0]) {
                    // Interpolate the y-coordinate for the given x-coordinate
                    float t = (midX - prevCoords[0]) / (coords[0] - prevCoords[0]);
                    wireY = prevCoords[1] + t * (coords[1] - prevCoords[1]);
                    if (wireY >= lowerMidY || wireY <= upperMidY) {
                        gameOver = true;
                        break;
                    }
                }

                Line2D segment = new Line2D.Float(prevCoords[0], prevCoords[1], coords[0], coords[1]);
                if (segment.intersects(ring.upper.x, ring.upper.y, ring.upper.width, ring.upper.height)
                        || segment.intersects(ring.lower.x, ring.lower.y, ring.lower.width, ring.lower.height)) {
                    gameOver = true;
                    break;
                }
            }
            prevCoords = coords.clone();
            iterator.next();
        }
        return wireY;
    }

    public void jump() {
        // checkIntersection();
        if (gameOver) {
            ring = new Ring(ringX, ringY, ringHeight, ringThickness);
            wire = null;
            wire = new Wire(0, HEIGHT / 2); // Start the wire at the leftmost middle of the screen
            yMotion = -1;
            time = 0;
            addWirePoint(true); // Add the first five points to the wire
            gameOver = false;
            started = false;
        }
        if (!started) {
            started = true;
            time = 0;
        } else if (!gameOver) {
            if (yMotion > 0) {
                yMotion = 0;
            }
            float wireY = checkIntersection();
            float lowerMidY = ring.lower.y - ring.lower.height / 2; // The y-coordinate of the middle of the lower ring
            float gap = -1;
            int jumpSize = 4;
            gap = lowerMidY - wireY;
            if(gap < jumpSize){
                yMotion -= gap;
            }
            else{
                yMotion -= jumpSize;
            }
        }
    }

    public void actionPerformed(ActionEvent e) {
        int speed = 8;
        ticks++;
        if (started) {
            if (!gameOver) {
                // Move the wire to the left
                wire.shiftLeft(speed);

                if (ticks % 2 == 0 && yMotion < 15) {
                    yMotion += 1;
                }
                if (ticks % 50 == 0) {
                    time++;  // Increment the score every second
                }
                updateWire();
                ring.upper.y += yMotion;
                ring.lower.y += yMotion;
                ring.left.y += yMotion;
                ring.right.y += yMotion;
                checkIntersection();
            }
        }
        renderer.repaint();
    }

    public void repaint(Graphics g) {
        g.setColor(Color.cyan);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        g.setColor(Color.orange);
        g.fillRect(0, HEIGHT - 120, WIDTH, 120);

        g.setColor(Color.green);
        g.fillRect(0, HEIGHT - 120, WIDTH, 20);

        g.setColor(Color.red);

        g.fillRect(ring.left.x, ring.left.y, ring.left.width, ring.left.height);
        paintWire(g);
        g.setColor(Color.red);
        g.fillRect(ring.right.x, ring.right.y, ring.right.width, ring.right.height);
        g.fillRect(ring.upper.x, ring.upper.y, ring.upper.width, ring.upper.height);
        g.fillRect(ring.lower.x, ring.lower.y, ring.lower.width, ring.lower.height);

        g.setColor(Color.white);
        g.setFont(new Font("Arial", 1, 100));

        if (!started && !gameOver) {
            g.drawString("Click to start!", 75, HEIGHT / 2 - 50);
        }

        if (gameOver) {
            g.drawString("Game Over!", 75, HEIGHT / 2 - 50);
        }

        g.setFont(new Font("Arial", 1, 70));
        g.drawString("Score: " + time, WIDTH/2-150, 100);  // Display the score

    }

    public static void main(String[] args) {
        wireBuzz = new WireBuzz();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        jump();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            jump();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

}