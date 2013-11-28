package WIP;

import Components.MouseInputComponent;
import Components.PhysicsComponent;
import Items.Item;
import Items.Projectile;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Jean-Luc
 * Date: 07.08.13
 * Time: 20:40
 * <p/>
 * This class handles anything that has to be drawn on the JPanel in the window.
 * The paintComponent method is overridden and then draws all enemies, dropped items, the player and the worldspaces.
 * It is also a Singleton which means there should only ever be one instance of this object.
 * Important Note:
 * JPanels in java use different coordinate systems than the world class.
 * Coordinate System:
 * *                /\ -y
 * *                |
 * *                |
 * *                |
 * *                |
 * *                |
 * * <--------------0--------------->
 * * -x             |              +x
 * *                |
 * *                |
 * *                |
 * *                |
 * *                \/ +y
 */
public class Renderer extends JPanel {
    private static Renderer instance = null;
    //This variable is very important as it determines the relative size of the world
    //TILESIZE determines how walls and floors are spaced, how far weapon ranges are and how big colliders are
    public final static int TILESIZE = 40;
    //This variable determines how wide and high the game screen is. This is important information for the camera to
    // know the clipping edges but is also important to calculate draw positions from world position.
    private final static int screenWidth = 500, screenHeight = 500;
    private Graphics2D g2d;

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

    public void paintComponent(Graphics g) {

        g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Map<Vector, WorldSpace> toRender = Camera.getInstance().worldToRender();

        drawWorld(toRender);
        drawItems(toRender);

        //DEBUGGING
        drawPlayer();

        drawActors();
        drawProjectiles();
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

    private void drawProjectiles() {
        Map<Projectile, Vector> projectiles = Camera.getInstance().projectilesToRender();
        for (Projectile p : projectiles.keySet())
            drawImage(p, projectiles.get(p));
    }

    /*
    Draws the sprite of the given gameobject at a given position.
    This position must be a draw coordinate.
     */
    private void drawImage(GameObject go, Vector position) {
        if (go instanceof GameObject) {
            BufferedImage bf = go.getGraphic().getImage();
            g2d.drawImage(bf, position.getX(), screenHeight - (bf.getHeight() + position.getY()), this);
        } else {
            g2d.setColor(Color.RED);
            g2d.fillRect(position.getX(), screenHeight - (TILESIZE + position.getY()), TILESIZE, TILESIZE);
        }
    }

    //DEBUGGING draws walls and floors as white and black rectangles and empty worldspaces as red blocks
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

    /*
    Gets all worldspaces that are visible to the camera and renders these
     */
    private void drawWorld(Map<Vector, WorldSpace> toRender) {
        for (Map.Entry e : toRender.entrySet()) {
            WorldSpace w = (WorldSpace) e.getValue();
            drawImage(w, ((Vector) e.getKey()));
        }
    }

    /*
    Draws all items that are lying on the floor.
     */
    private void drawItems(Map<Vector, WorldSpace> toRender) {
        for (Map.Entry e : toRender.entrySet()) {
            if (e.getValue() instanceof Floor && ((Floor) e.getValue()).hasDroppedItems()) {
                Item[] items = ((Floor) e.getValue()).getDroppedItems();
                drawImage(items[0], (Vector) e.getKey());
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

    /*
    TODO Draws all actors even if they are not visible to the player!
    Also draws all healthbars.
     */
    private void drawActors() {
        Map<Actor, Vector> actorPositionMap = Camera.getInstance().actorsToRender();
        for (Actor a : actorPositionMap.keySet()) {
            Vector drawPosition = actorPositionMap.get(a);
            PhysicsComponent phys = a.getCollider();
            g2d.drawImage(a.getGraphic().getImage(),
                    drawPosition.getX(), screenHeight - (phys.getHeight() + drawPosition.getY()),
                    phys.getWidth(), phys.getHeight(), this);

        }
        for (Actor a : actorPositionMap.keySet()) {
            Vector drawPosition = actorPositionMap.get(a);
            drawHealthBar(a, drawPosition);
        }
    }

    //Draws the health bar of a single actor
    private void drawHealthBar(Actor actor, Vector drawPosition) {
        int xOffset = -14;
        int yOffset = 7;
        g2d.setColor(Color.RED);
        g2d.fillRect(actor.getCollider().getWidth() / 2 + drawPosition.getX() + xOffset,
                screenHeight - (actor.getCollider().getHeight() + drawPosition.getY() + yOffset),
                30, 5);
        g2d.setColor(Color.GREEN);
        g2d.fillRect(actor.getCollider().getWidth() / 2 + drawPosition.getX() + xOffset,
                screenHeight - (actor.getCollider().getHeight() + drawPosition.getY() + yOffset),
                (int) (30 * actor.getHealthPercentage()), 5);
    }

    private void drawGUI() {
    }
}
