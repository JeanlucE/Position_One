package WIP;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Jean-Luc
 * Date: 11.11.13
 * Time: 17:26
 * <p/>
 * This class determines which spaces of the world are on screen and occludes the rest
 */
public class Camera {
    private static Camera instance = null;
    private World world;
    private Position parent;
    private int clipX, clipY;
    private int screenWidth, screenHeight;
    private Map<Position, WorldSpace> floors;
    private Map<Position, WorldSpace> walls;

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

    public Map<Position, WorldSpace> toRender() {
        this.world = Game.getInstance().getCurrentWorld();
        this.parent = Game.getInstance().getPlayer().getTransform().getPosition();
        Map<Position, WorldSpace> visibleSpaces = new HashMap<>(100);
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
                Position worldPosition = new Position(x - x % Renderer.TILESIZE, y - y % Renderer.TILESIZE);
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

    public Position getParentPosition() {
        return new Position(screenWidth / 2, screenHeight / 2);
    }
}
