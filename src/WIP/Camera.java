package WIP;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Jean-Luc
 * Date: 11.11.13
 * Time: 17:26
 * <p/>
 * This class determines which spaces of the world are on screen and occludes the rest.
 * This class also converts all world coordinates to draw coordinates
 */
public class Camera {
    private static Camera instance = null;
    private World world;
    private Vector parent;
    private int clipX, clipY;
    private int screenWidth, screenHeight;
    private Map<Vector, WorldSpace> floors;
    private Map<Vector, WorldSpace> walls;

    public static Camera getInstance() {
        if (instance == null) {
            instance = new Camera();
        }
        return instance;
    }

    private Camera() {
        screenWidth = Renderer.getScreenWidth();
        screenHeight = Renderer.getScreenHeight();
        clipX = screenWidth / 2 + Renderer.TILESIZE;
        clipY = screenHeight / 2 + Renderer.TILESIZE;
    }

    public Map<Vector, WorldSpace> worldToRender() {
        this.world = Game.getInstance().getCurrentWorld();
        this.parent = Game.getInstance().getPlayer().getTransform().getPosition();
        Map<Vector, WorldSpace> visibleSpaces = new HashMap<>(100);
        floors = new HashMap<>(100);
        walls = new HashMap<>(100);

        //Get the camera clipping lines relative to the player
        int northClip = parent.getY() + clipX;
        int southClip = parent.getY() - clipX;
        int westClip = parent.getX() - clipY;
        int eastClip = parent.getX() + clipY;

        //From left to right
        for (int x = westClip; x < eastClip; x += Renderer.TILESIZE) {
            //From top to bottom
            for (int y = southClip; y < northClip; y += Renderer.TILESIZE) {
                WorldSpace worldSpace = world.get(x, y);
                Vector worldPosition = new Vector(x - x % Renderer.TILESIZE, y - y % Renderer.TILESIZE);
                if (worldSpace instanceof Wall) {
                    walls.put(worldPosition, worldSpace);
                } else {
                    floors.put(worldPosition, worldSpace);
                }

                //Get worldspace position relative to player position
                worldPosition.setX(screenWidth / 2 + (worldPosition.getX() - parent.getX()));
                worldPosition.setY(screenHeight / 2 + (worldPosition.getY() - parent.getY()));
            }
        }
        visibleSpaces.putAll(walls);
        visibleSpaces.putAll(floors);

        return visibleSpaces;
    }

    public Vector getParentPosition() {
        return new Vector(screenWidth / 2, screenHeight / 2);
    }

    public Map<Actor, Vector> actorsToRender() {
        Map<Actor, Vector> actorPositionMap = new HashMap<>();
        for (Actor a : Game.getInstance().getActors()) {
            Vector drawPosition = a.getTransform().getPosition().clone();
            drawPosition.setX(screenWidth / 2 + (drawPosition.getX() - parent.getX()));
            drawPosition.setY(screenHeight / 2 + (drawPosition.getY() - parent.getY()));
            actorPositionMap.put(a, drawPosition);
        }
        return actorPositionMap;
    }
}
