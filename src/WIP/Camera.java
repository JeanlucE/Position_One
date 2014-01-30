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

    public static Camera getInstance() {
        if (instance == null) {
            instance = new Camera();
        }
        return instance;
    }

    private Vector parent;

    private Camera() {
    }

    private int getScreenWidth() {
        return Renderer.getScreenWidth();
    }

    private int getScreenHeight() {
        return Renderer.getScreenHeight();
    }

    private int getClipX() {
        return getScreenWidth() / 2 + Renderer.TILESIZE;
    }

    private int getClipY() {
        return getScreenHeight() / 2 + Renderer.TILESIZE;
    }

    public Vector getParentPosition() {
        return new Vector(getScreenWidth() / 2, getScreenHeight() / 2);
    }

    public Map<Vector, WorldSpace> worldToRender() {
        World world = Game.getInstance().getCurrentWorld();
        this.parent = Game.getInstance().getPlayer().getTransform().getPosition();
        Map<Vector, WorldSpace> visibleSpaces;
        Map<Vector, WorldSpace> floors = new HashMap<>(200);
        Map<Vector, WorldSpace> walls = new HashMap<>(200);

        //Get the camera clipping lines relative to the player
        int northClip = parent.getY() + getClipY();
        int southClip = parent.getY() - getClipY();
        int westClip = parent.getX() - getClipX();
        int eastClip = parent.getX() + getClipX();
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
            if (xDist < getScreenWidth() / 2 + p.getWidth() / 2 && yDist < getScreenHeight() / 2 + p.getHeight() / 2) {
                return true;
            }
        } else {
            if (xDist < getScreenWidth() / 2 && yDist < getScreenHeight() / 2) {
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
