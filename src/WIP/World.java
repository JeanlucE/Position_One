package WIP;

import Components.GraphicsComponent;
import Components.PhysicsComponent;
import Components.Resource;
import Components.StaticGraphicsComponent;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Jean-Luc
 * Date: 07.11.13
 * Time: 15:00
 * <p/>
 * Saves the position of all walls, floors and later: other gameobjects(chests, doors)
 * Important note:
 * Coordinate System:
 * *                /\ +y
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
 * *                \/ -y
 */
public class World {
    private Map<Vector, WorldSpace> worldSpaceMap = new HashMap<>();

    public World() {
        int tilesize = 40;
        int roomsize = 20;
        //Creates a square room of with walk space 19x19
        GraphicsComponent g = new StaticGraphicsComponent(Resource.wall01);
        GraphicsComponent floor = new StaticGraphicsComponent(Resource.floor01);
        PhysicsComponent boxCollider = new PhysicsComponent(tilesize, tilesize);
        for (int i = 0; i < roomsize * tilesize; i += tilesize) {
            set(i, 0, new Wall(new Transform(new Vector(i, 0)), g, boxCollider));
            set(i, (roomsize - 1) * tilesize, new Wall(new Transform(new Vector(i, (roomsize - 1) * tilesize)), g,
                    boxCollider));
        }
        for (int i = 40; i < (roomsize - 1) * tilesize; i += tilesize) {
            set(0, i, new Wall(new Transform(new Vector(0, i)), g, boxCollider));
            set((roomsize - 1) * tilesize, i, new Wall(new Transform(new Vector((roomsize - 1) * tilesize, i)), g,
                    boxCollider));
            for (int j = 40; j < (roomsize - 1) * tilesize; j += tilesize) {
                //Floor
                set(i, j, new Floor(new Transform(new Vector(i, j)), floor));
            }
        }

        set(120, 120, new Wall(new Transform(new Vector(120, 120)), g, boxCollider));
        set(120, 160, new Wall(new Transform(new Vector(120, 160)), g, boxCollider));
        set(120, 200, new Wall(new Transform(new Vector(120, 200)), g, boxCollider));
        set(160, 200, new Wall(new Transform(new Vector(160, 200)), g, boxCollider));
        set(240, 200, new Wall(new Transform(new Vector(240, 200)), g, boxCollider));
        DebugLog.write("New World created with size: " + worldSpaceMap.size());
    }

    //DEBUGGING
    public WorldSpace getReal(int x, int y) {
        return worldSpaceMap.get(new Vector(x, y));
    }

    public WorldSpace get(int x, int y) {
        int xReal = x / 40;
        int yReal = y / 40;
        return getReal(xReal, yReal);
    }

    public WorldSpace get(Vector position) {
        return get(position.getX(), position.getY());
    }

    void set(int x, int y, WorldSpace worldSpace) {
        int xReal = x / 40;
        int yReal = y / 40;
        setReal(xReal, yReal, worldSpace);
    }

    void setReal(int x, int y, WorldSpace worldSpace) {
        worldSpaceMap.put(new Vector(x, y), worldSpace);
    }

    /*
    This method resolves boolean collision between the gameworld and another gameobject (e.g. a player).
    It does this by getting the position of all 4 corners of the gameobject's colider and then checks if any of those
    corners would clip into a wall when the game object move to the next Position.
    If none of the corners clips a collidable world space (e.g. a wall) the method returns true.
    If any of the corners clips a collidable world space (e.g. a wall) the method returns false.
     */
    public boolean resolveCollision(Actor actor, Vector nextPosition) {
        //TODO if player cant move directly to the given position, test if he can still move nearer to that position
        PhysicsComponent collider = actor.getCollider();
        boolean canMove = false;
        for (Vector p : collider.getCorners(nextPosition)) {
            canMove = canMoveTo(p);
            if (!canMove)
                break;
        }
        return canMove && canMoveTo(actor, nextPosition);
    }

    //Returns if the position that is passed has any collidable gameobject
    boolean canMoveTo(int x, int y) {
        int xReal = x / 40;
        int yReal = y / 40;
        return !worldSpaceMap.get(new Vector(xReal, yReal)).isCollidable();
    }

    boolean canMoveTo(Vector position) {
        return canMoveTo(position.getX(), position.getY());
    }

    /*
    This method resolves collisions between the player and any enemy (for now) and any further actors in the scene.
    TODO make this between all actors
     */
    boolean canMoveTo(Actor actor, Vector nextPosition) {
        boolean canMove = true;
        Actor[] actors = Actor.getActors();
        for (int i = 0; i < actors.length && canMove; i++) {
            Actor otherActor = actors[i];
            if (actor.getFaction().equals(otherActor.getFaction()))
                canMove = true;
            else if (actor != otherActor) {
                Vector[] thisCorners = actor.getCollider().getCorners(nextPosition);
                Vector[] otherCorners = otherActor.getCollider().getCorners();

                boolean actorAbove = thisCorners[2].getY() > otherCorners[0].getY();
                boolean actorBelow = thisCorners[0].getY() < otherCorners[2].getY();
                boolean actorLeftOf = thisCorners[1].getX() < otherCorners[0].getX();
                boolean actorRightOf = thisCorners[0].getX() > otherCorners[1].getX();

                canMove = actorAbove || actorBelow || actorLeftOf
                        || actorRightOf;
            }
        }
        return canMove;
    }
}
