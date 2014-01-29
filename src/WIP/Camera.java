package WIP;

import Actors.Actor;
import Components.PhysicsComponent;
import Environment.World;
import Environment.WorldSpace;
import Items.Projectile;

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
    private Vector parent;
    private int clipX, clipY;
    private int screenWidth, screenHeight;

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

    public Vector getParentPosition() {
        return new Vector(screenWidth / 2, screenHeight / 2);
    }

    public Map<Vector, WorldSpace> worldToRender() {
        World world = Game.getInstance().getCurrentWorld();
        this.parent = Game.getInstance().getPlayer().getTransform().getPosition();
        Map<Vector, WorldSpace> visibleSpaces;
        Map<Vector, WorldSpace> floors = new HashMap<>(200);
        Map<Vector, WorldSpace> walls = new HashMap<>(200);

        //Get the camera clipping lines relative to the player
        int northClip = parent.getY() + clipX;
        int southClip = parent.getY() - clipX;
        int westClip = parent.getX() - clipY;
        int eastClip = parent.getX() + clipY;
        visibleSpaces = world.getSubSpace(new Vector(westClip, southClip), new Vector(eastClip, northClip));
        for (Map.Entry<Vector, WorldSpace> e : visibleSpaces.entrySet()) {
            WorldSpace w = e.getValue();
            Vector v = e.getKey();
            v = transformToViewSpace(v);
            if (w != null) {
                if (w.isFloor()) {
                    floors.put(v, w);
                } else if (w.isWall()) {
                    walls.put(v, w);
                }
            }
        }
        return visibleSpaces;
    }

    public Map<Actor, Vector> actorsToRender() {
        Map<Actor, Vector> actorPositionMap = new HashMap<>();
        for (Actor a : Game.getInstance().getActors()) {
            if (withinScreenBounds(a)) {
                Vector drawPosition = a.getTransform().getPosition().clone();
                drawPosition = transformToViewSpace(drawPosition);
                actorPositionMap.put(a, drawPosition);
            }
        }
        return actorPositionMap;
    }

    public Map<Projectile, Vector> projectilesToRender() {
        Map<Projectile, Vector> projectileVectorMap = new HashMap<>();
        for (Projectile p : Projectile.getProjectiles()) {
            if (withinScreenBounds(p)) {
                Vector pos = p.getTransform().getPosition().clone();
                pos = transformToViewSpace(pos);
                projectileVectorMap.put(p, pos);
            }
        }
        return projectileVectorMap;
    }

    private boolean withinScreenBounds(GameObject go) {
        Vector v = go.getTransform().getPosition();
        int xDist = Math.abs(v.getX() - parent.getX());
        int yDist = Math.abs(v.getY() - parent.getY());
        if (go.isCollidable()) {
            PhysicsComponent p = ((Collidable) go).getCollider();
            if (xDist < screenWidth / 2 + p.getWidth() / 2 && yDist < screenHeight / 2 + p.getHeight() / 2) {
                return true;
            }
        } else {
            if (xDist < screenWidth / 2 && yDist < screenHeight / 2) {
                return true;
            }
        }
        return false;
    }

    private Vector transformToViewSpace(Vector v) {
        Vector a = Vector.add(getParentPosition(), Vector.subtract(v, parent));
        v.setX(a.getX());
        v.setY(a.getY());
        return v;
    }
}
