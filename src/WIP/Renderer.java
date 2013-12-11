package WIP;

import Actors.Actor;
import Components.InputComponent;
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

    //DEBUGGING
    private boolean drawActorPositions = true;
    private boolean drawActorColliders = true;

    private GUIState guiState;

    //Singleton Design Pattern
    public static Renderer getInstance() {
        if (instance == null) {
            instance = new Renderer();
        }
        return instance;
    }

    private Renderer() {
        DebugLog.write("Renderer started");
        setPreferredSize(new Dimension(screenWidth, screenHeight));
        addMouseListener(MouseInputComponent.getInstance());
        addMouseMotionListener(MouseInputComponent.getInstance());
        setFocusable(true);
        addKeyListener(InputComponent.getInstance());
    }

    public static int getScreenWidth() {
        return screenWidth;
    }

    public static int getScreenHeight() {
        return screenHeight;
    }

    public void paintComponent(Graphics g) {

        g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Map<Vector, WorldSpace> toRender = Camera.getInstance().worldToRender();

        drawWorld(toRender);
        drawItems(toRender);

        drawProjectiles();

        //DEBUGGING
        Map<Actor, Vector> actorPositionMap = Camera.getInstance().actorsToRender();


        drawActors(actorPositionMap);

        if (drawActorPositions) {
            drawActorPositions(actorPositionMap);
        }

        if (drawActorColliders) {
            drawActorColliders(actorPositionMap);
        }

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

    /*
    Draws the sprite of the given gameobject at a given position.
    This position must be a draw coordinate.
     */
    private void drawImage(GameObject go, Vector position) {
        if (go != null) {
            BufferedImage bf = go.getGraphic().getImage();
            g2d.drawImage(bf, position.getX(), screenHeight - (bf.getHeight() + position.getY()),
                    this);
        } else {
            g2d.setColor(Color.RED);
            g2d.fillRect(position.getX(), screenHeight - (TILESIZE + position.getY()), TILESIZE, TILESIZE);
        }
    }

    private void drawImage(GameObject go, Vector position, int width, int height) {
        if (go != null) {
            BufferedImage bf = go.getGraphic().getImage();
            PhysicsComponent p = ((Collidable) go).getCollider();
            g2d.drawImage(bf,
                    position.getX() - p.getHeight() / 2,
                    screenHeight - (p.getHeight() / 2 + position.getY()),
                    width, height, this);
        } else {
            g2d.setColor(Color.RED);
            g2d.fillRect(position.getX(), screenHeight - (TILESIZE + position.getY()), TILESIZE, TILESIZE);
        }
    }

    //Gets all worldspaces that are visible to the camera and renders these
    private void drawWorld(Map<Vector, WorldSpace> toRender) {
        for (Map.Entry e : toRender.entrySet()) {
            WorldSpace w = (WorldSpace) e.getValue();
            drawImage(w, ((Vector) e.getKey()));
        }
    }


    private void drawActors(Map<Actor, Vector> actorPositionMap) {
        for (Actor a : actorPositionMap.keySet()) {
            Vector drawPosition = actorPositionMap.get(a);
            PhysicsComponent phys = a.getCollider();
            drawImage(a, drawPosition, phys.getWidth(), phys.getHeight());

        }

        //Draws all health bars
        for (Actor a : actorPositionMap.keySet()) {
            Vector drawPosition = actorPositionMap.get(a);
            drawHealthBar(a, drawPosition);
        }
    }

    //Draws the health bar of a single actor
    private void drawHealthBar(Actor actor, Vector drawPosition) {
        int xOffset = -14;
        int yOffset = 7;
        PhysicsComponent phys = actor.getCollider();
        g2d.setColor(Color.RED);
        g2d.fillRect(drawPosition.getX() + xOffset,
                screenHeight - (phys.getHeight() / 2 + drawPosition.getY() + yOffset),
                30, 5);
        g2d.setColor(Color.GREEN);
        g2d.fillRect(drawPosition.getX() + xOffset,
                screenHeight - (phys.getHeight() / 2 + drawPosition.getY() + yOffset),
                (int) (30 * actor.getHealthPercentage()), 5);
    }

    private void drawProjectiles() {
        Map<Projectile, Vector> projectiles = Camera.getInstance().projectilesToRender();
        for (Projectile p : projectiles.keySet()) {
            Vector drawPosition = projectiles.get(p);
            PhysicsComponent phys = p.getCollider();
            drawImage(p, drawPosition, phys.getWidth(), phys.getHeight());
        }
        //drawImage(p, projectiles.get(p));
    }

    //Draws all items that are lying on the floor.
    private void drawItems(Map<Vector, WorldSpace> toRender) {
        for (Map.Entry e : toRender.entrySet()) {
            if (e.getValue() == null) continue;

            if (((WorldSpace) e.getValue()).isFloor() && ((Floor) e.getValue()).hasDroppedItems()) {
                Item[] items = ((Floor) e.getValue()).getDroppedItems();
                drawImage(items[0], (Vector) e.getKey());
            }
        }

    }

    private void drawGUI() {
    }

    private enum GUIState {
        GAME, MAIN_MENU, INVENTORY, MAP, PAUSE_MENU
    }

    //DEBUGGING draws player position and collider
    private void drawActorPositions(Map<Actor, Vector> actors) {
        g2d.setColor(Color.RED);
        for (Actor a : actors.keySet()) {
            //Position
            Vector position = actors.get(a);
            PhysicsComponent p = a.getCollider();
            g2d.drawString(a.getTransform().getPosition().toString(),
                    position.getX() - p.getWidth() / 2 - 5, screenWidth - (position.getY() - 32));
        }
    }

    private void drawActorColliders(Map<Actor, Vector> actors) {
        //Collider
        g2d.setColor(Color.RED);
        for (Actor a : actors.keySet()) {
            Vector position = actors.get(a);
            PhysicsComponent p = a.getCollider();
            g2d.drawRect(position.getX() - p.getWidth() / 2, screenWidth - (position.getY() + p.getHeight() / 2),
                    p.getWidth(), p.getHeight());
        }
    }
}
