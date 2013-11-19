package WIP;

import Components.MouseInputComponent;
import Components.PhysicsComponent;
import Items.Item;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Jean-Luc
 * Date: 07.08.13
 * Time: 20:40
 */
public class Renderer extends JPanel {
    private static Renderer instance = null;
    public final static int TILESIZE = 40;
    private final static int screenWidth = 400, screenHeight = 400;

    //Singleton Design Pattern
    public static Renderer getInstance() {
        if (instance == null) {
            instance = new Renderer();
        }
        return instance;
    }

    public static int getScreenWidth() {
        return screenWidth;
    }

    public static int getScreenHeight() {
        return screenHeight;
    }

    private Renderer() {
        setPreferredSize(new Dimension(screenWidth, screenHeight));
        addMouseListener(MouseInputComponent.getInstance());
        addMouseMotionListener(MouseInputComponent.getInstance());
    }

    private Graphics2D g2d;

    public void paintComponent(Graphics g) {

        g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Map<Position, WorldSpace> toRender = Camera.getInstance().toRender();

        drawWorld(toRender);
        drawItems(toRender);
        drawPlayer();
        drawEnemy();
        drawGUI();

        MouseInputComponent mouseListener = MouseInputComponent.getInstance();
        if (mouseListener.isMouseInScreen()) {
            g2d.setColor(Color.CYAN);
            g2d.fillRect(10, 10, 10, 10);
        }

        if (mouseListener.isMousePressed()) {
            g2d.setColor(Color.GREEN);
            g2d.fillRect(30, 10, 10, 10);
        }


        GameWindow.getInstance().setTitle("Frames:" + String.valueOf(Game.getInstance().getFrameRate()));
    }

    private void drawImage(BufferedImage image, Position position) {
        g2d.drawImage(image, position.getX(), screenHeight - (30 + position.getY()), 30, 30, this);
    }

    private void drawImage(BufferedImage image, int x, int y) {
        g2d.drawImage(image, x, screenHeight - y, this);
    }

    //DEBUGGING draws walls and floors as white and black rectangles until i have some sprites
    private void drawSpace(WorldSpace worldSpace, int x, int y) {
        if (worldSpace instanceof Wall) {
            g2d.setColor(Color.BLACK);
        } else if (worldSpace instanceof Floor) {
            g2d.setColor(Color.WHITE);
        } else {
            g2d.setColor(Color.RED);
        }
        g2d.fillRect(x, screenHeight - (TILESIZE + y), TILESIZE, TILESIZE);
    }

    private void drawWorld(Map<Position, WorldSpace> toRender) {
        for (Map.Entry e : toRender.entrySet()) {
            WorldSpace w = (WorldSpace) e.getValue();
            drawSpace(w, ((Position) e.getKey()).getX(), ((Position) e.getKey()).getY());
        }
    }

    private void drawItems(Map<Position, WorldSpace> toRender) {
        for (Map.Entry e : toRender.entrySet()) {
            if (e.getValue() instanceof Floor && ((Floor) e.getValue()).hasDroppedItems()) {
                g2d.setColor(Color.BLACK);
                Item[] items = ((Floor) e.getValue()).getDroppedItems();
                g2d.drawString(items[0].getName(), ((Position) e.getKey()).getX(),
                        screenHeight - ((Position) e.getKey()).getY
                                ());
            }
        }
    }

    private void drawPlayer() {
        Game game = Game.getInstance();
        //DEBUGGING Draws players collison box
        g2d.setColor(Color.RED);
        Position playerDrawPos = Camera.getInstance().getParentPosition();
        PhysicsComponent collider = game.getPlayer().getCollider();
        g2d.drawRect(playerDrawPos.getX(), playerDrawPos.getY() - collider.getHeight(),
                collider.getWidth(), collider.getHeight());

        //Draws Player image
        drawImage(game.getPlayer().getGraphic().getImage(), playerDrawPos);

        g2d.setColor(Color.RED);
        Position playerPos = game.getPlayer().getTransform().getPosition();
        g2d.drawString(playerPos.toString(), playerDrawPos.getX() - 5, playerDrawPos.getY() + 10);
    }

    private void drawEnemy() {
        //TODO Take all of this and put it in Camera.class
        Enemy enemy = Game.getInstance().getEnemy();
        Position playerPos = Game.getInstance().getPlayer().getTransform().getPosition();
        Position p = enemy.getTransform().getPosition().clone();
        p.setX(screenWidth / 2 + (p.getX() - playerPos.getX()));
        p.setY(screenHeight / 2 + (p.getY() - playerPos.getY()));
        g2d.drawImage(enemy.getGraphic().getImage(), p.getX(), 400 - (40 + p.getY()), 40, 40, this);
    }

    private void drawGUI() {
    }
}
