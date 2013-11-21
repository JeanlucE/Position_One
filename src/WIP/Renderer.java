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

        Map<Vector, WorldSpace> toRender = Camera.getInstance().worldToRender();

        drawWorld(toRender);
        drawItems(toRender);

        //DEBUGGING
        drawPlayer();

        drawActors();
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

    private void drawImage(GameObject go, Vector position) {
        if (go instanceof Wall || go instanceof Floor) {
            BufferedImage bf = go.getGraphic().getImage();
            g2d.drawImage(bf, position.getX(), screenHeight - (bf.getHeight() + position.getY()), this);
        } else {
            g2d.setColor(Color.RED);
            g2d.fillRect(position.getX(), screenHeight - (TILESIZE + position.getY()), TILESIZE, TILESIZE);
        }
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

    private void drawWorld(Map<Vector, WorldSpace> toRender) {
        for (Map.Entry e : toRender.entrySet()) {
            WorldSpace w = (WorldSpace) e.getValue();
            drawImage(w, ((Vector) e.getKey()));
        }
    }

    private void drawItems(Map<Vector, WorldSpace> toRender) {
        for (Map.Entry e : toRender.entrySet()) {
            if (e.getValue() instanceof Floor && ((Floor) e.getValue()).hasDroppedItems()) {
                g2d.setColor(Color.BLACK);
                Item[] items = ((Floor) e.getValue()).getDroppedItems();
                g2d.drawString(items[0].getName(), ((Vector) e.getKey()).getX(),
                        screenHeight - ((Vector) e.getKey()).getY
                                ());
            }
        }

    }

    //DEBUGGING draws player position and collider
    private void drawPlayer() {
        Game game = Game.getInstance();
        //Collider
        g2d.setColor(Color.RED);
        Vector playerDrawPos = Camera.getInstance().getParentPosition();
        PhysicsComponent collider = game.getPlayer().getCollider();
        g2d.drawRect(playerDrawPos.getX(), playerDrawPos.getY() - collider.getHeight(),
                collider.getWidth(), collider.getHeight());

        //Position
        g2d.setColor(Color.RED);
        Vector playerPos = game.getPlayer().getTransform().getPosition();
        g2d.drawString(playerPos.toString(), playerDrawPos.getX() - 5, playerDrawPos.getY() + 10);
    }

    private void drawActors() {
        Map<Actor, Vector> actorPositionMap = Camera.getInstance().actorsToRender();
        for (Actor a : actorPositionMap.keySet()) {
            Vector drawPosition = actorPositionMap.get(a);
            PhysicsComponent phys = a.getCollider();
            g2d.drawImage(a.getGraphic().getImage(), drawPosition.getX(), 400 - (phys.getHeight() + drawPosition.getY()),
                    phys.getWidth(), phys.getHeight(), this);
        }
    }

    private void drawGUI() {
    }
}
