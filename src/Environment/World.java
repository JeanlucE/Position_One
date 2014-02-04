package Environment;

import Actors.Actor;
import Components.PhysicsComponent;
import Items.Item;
import Items.Projectile;
import WIP.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * <p/>
 * Date: 07.11.13
 * Time: 15:00
 * <p/>
 * World handles all interactions between the Worldmap and other objects. Collision detection,
 * saving and loading maps from .txt and giving any GameObject information about certain spaces in the worldmap grid.
 * This also includes the Renderer.
 * <p/>
 * This class is written in a Singleton design pattern as there can only ever be one WorldMap with which the player
 * and his surroundings can interact with.
 * at once.
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
    //TODO put melee hit boolean collision here
    //TODO put arrow hit boolean collision here

    private static World instance;
    private WorldMap currentMap;
    private SpawnHandler spawnHandler;

    public static World getInstance() {
        if (instance == null)
            instance = new World();
        return instance;
    }

    private World() {
        //currentMap = WorldMap.getRandomMap(10, 15, 20, 4);

        //initiateMap("world");
        //saveMap("randomMap");
        loadMap("coctestinghall");
        spawnHandler = new SpawnHandler(this, 0);
    }

    public void update() {
        spawnHandler.update();
    }

    /**
     * Gets all WorldSpaces in the area of the rectangle defined by the bottomLeft Vector and the topRight Vector.
     * Then a Map of Real Vectors to WorldSpaces is returned.
     *
     * @param bottomLeft Bottom left corner of the Rectangle to be searched for WorldSpaces
     * @param topRight   Top right corner of the Rectangle to be searched for WorldSpaces
     * @return Returns a Map of Real Vectors to WorldSpaces.
     */
    //TODO should return WorldPosition in the vectors
    public Map<Vector, WorldSpace> getSubSpace(Vector bottomLeft, Vector topRight) {
        int tileSize = Renderer.TILESIZE;
        int x0 = (bottomLeft.getX() >= 0) ? (bottomLeft.getX() / tileSize) : (bottomLeft.getX() / tileSize - 1);
        int y0 = (bottomLeft.getY() >= 0) ? (bottomLeft.getY() / tileSize) : (bottomLeft.getY() / tileSize - 1);
        int x1 = (topRight.getX() >= 0) ? (topRight.getX() / tileSize) : (topRight.getX() / tileSize - 1);
        int y1 = (topRight.getY() >= 0) ? (topRight.getY() / tileSize) : (topRight.getY() / tileSize - 1);

        Map<Vector, WorldSpace> result = new HashMap<>(Math.abs(x1 - x0) * Math.abs(y1 - y0)); //Approximate Size
        for (int x = x0; x < x1; x++) {
            for (int y = y0; y < y1; y++) {
                Vector v = new Vector(x, y);
                WorldSpace w = getReal(x, y);
                result.put(transformToWorldSpace(v), w);
            }
        }
        return result;
    }

    /**
     * A method which returns the WorldSpace stored at a coordinate
     *
     * @param x X Coordinate of the WorldSpace
     * @param y Y Coordinate of the WorldSpace
     * @return Returns the WorldSpace at coordinate x, y. If it doesn't exist it returns null.
     */
    public WorldSpace get(int x, int y) {
        int tilesize = Renderer.TILESIZE;
        int xReal = (x >= 0) ? (x / tilesize) : (x / tilesize - 1);
        int yReal = (y >= 0) ? (y / tilesize) : (y / tilesize - 1);
        return currentMap.getReal(xReal, yReal);
    }

    //DEBUGGING
    private WorldSpace getReal(int x, int y) {
        return currentMap.getReal(x, y);
    }

    /**
     * Overloaded Method for World.get(int, int)
     *
     * @param position x and y Coordinates of the WorldSpace
     * @return Returns the WorldSpace at coordinate x, y. If it doesn't exist it returns null.
     * @see Environment.World#get(int, int)
     */
    public WorldSpace get(Vector position) {
        return get(position.getX(), position.getY());
    }

    private Vector transformToWorldSpace(Vector v) {
        return Vector.multiply(v, Renderer.TILESIZE);
    }

    /**
     * This method resolves boolean collision between the gameworld and another actor.
     * It does this by getting the position of all 4 corners of the actor's collider and then checks if any of those
     * corners would clip into a wall when the game object moves to the next position.
     * If none of the corners clips a collidable world space (e.g. a wall) the method returns a CollisionEvent with a
     * CollisionState of NO_COLLISION attached to it.
     * If any of the corners clips a collidable world space (e.g. a wall) the method returns a CollisionEvent with a
     * CollisionState and CollisionObject of type GameObject.
     *
     * @param actor        The actor which has to be checked for collisions
     * @param nextPosition The Vector of the position the actor is moving to.
     * @return Returns a CollisionEvent which holds the collided GameObject if applicable
     * @see Components.PhysicsComponent
     */
    public CollisionEvent resolveCollision(Actor actor, Vector nextPosition) {
        PhysicsComponent collider = actor.getCollider();
        for (Vector p : collider.getCorners(nextPosition)) {
            CollisionEvent collisionEvent = resolveWallCollision(p);
            if (collisionEvent.isWallHit())
                return collisionEvent;
        }
        return resolveActorCollision(actor, nextPosition);
    }

    /**
     * @param p            Projectile to check for collisions.
     * @param nextPosition The Vector of the position the projectile is moving to.
     * @return Returns a CollisionEvent which holds the CollisionState and the collided GameObject if applicable
     * @see Environment.World#resolveActorCollision(Actors.Actor, WIP.Vector)
     */
    public CollisionEvent resolveCollision(Projectile p, Vector nextPosition) {
        PhysicsComponent collider = p.getCollider();
        for (Vector v : collider.getCorners(nextPosition)) {
            CollisionEvent wallCollision = resolveWallCollision(v);
            if (wallCollision.isWallHit()) {
                return wallCollision;
            }
        }

        for (Actor a : Actor.getActors()) {
            if (a != Game.getInstance().getPlayer()) {
                if (p.getCollider().onX(a.getCollider(), 0.3f) && p.getCollider().onY(a.getCollider(), 0.3f)) {
                    DebugLog.write(a + " hit!");
                    return new CollisionEvent(CollisionState.ENEMY_HIT, a);
                }
            }
        }

        return new CollisionEvent(CollisionState.NO_COLLISION, null);
    }

    /**
     * Returns if for a given coordinate there is a collidable WorldSpace
     *
     * @param x X Coordinate of the WorldSpace
     * @param y Y Coordinate of the WorldSpace
     * @return Returns a CollisionEvent which holds the CollisionState and the collided GameObject if applicable
     */
    private CollisionEvent resolveWallCollision(int x, int y) {
        WorldSpace spaceToCheck = get(x, y);
        if (spaceToCheck == null) {
            return new CollisionEvent(CollisionState.WALL_HIT, null);
        } else if (spaceToCheck.isCollidable()) {
            return new CollisionEvent(CollisionState.WALL_HIT, spaceToCheck);
        } else {
            return new CollisionEvent(CollisionState.NO_COLLISION, null);
        }

    }

    /**
     * Overloaded Method for resolveWallCollision(int, int)
     *
     * @param position Vector which holds the position of the WorldSpace to be checked
     * @return Returns a CollisionEvent which holds the CollisionState and the collided GameObject if applicable
     * @see Environment.World#resolveWallCollision(int, int)
     */
    private CollisionEvent resolveWallCollision(Vector position) {
        return resolveWallCollision(position.getX(), position.getY());
    }

    /**
     * Resolves collisions between actors only. For a CollisionEvent to have the CollisionState ENEMY_HIT attached to
     * it the other actor must be from a different faction that his own.
     *
     * @param actor        The actor which has to be checked for collisions.
     * @param nextPosition The Vector of the position the actor is moving to.
     * @return Returns a CollisionEvent which holds the CollisionState and the collided Actor if applicable
     */
    private CollisionEvent resolveActorCollision(Actor actor, Vector nextPosition) {
        Actor[] actors = Actor.getActors();
        for (Actor otherActor : actors) {
            //Collisions between same faction
            if (actor != otherActor /*&& !actor.getFaction().equals(otherActor.getFaction())*/) {
                PhysicsComponent nextCollider = actor.getCollider().clone(nextPosition);
                PhysicsComponent otherCollider = otherActor.getCollider();
                if (nextCollider.collision(otherCollider))
                    return new CollisionEvent(CollisionState.ENEMY_HIT, otherActor);
            }
        }
        return new CollisionEvent(CollisionState.NO_COLLISION, null);
    }

    public void dropItem(Item item, int x, int y) {
        WorldSpace w = get(x, y);
        if (w != null && w.isFloor()) {
            Floor f = (Floor) w;
            f.dropItem(item);
            System.out.println("World: " + item.getName() + " dropped at " + w.getTransform().getPosition());
        }
    }

    public class CollisionEvent {

        private GameObject collisionObject;
        private CollisionState collisionState;

        public CollisionEvent(CollisionState collisionState, GameObject collisionObject) {
            this.collisionState = collisionState;
            if (!collisionState.equals(CollisionState.NO_COLLISION))
                this.collisionObject = collisionObject;
        }

        public GameObject getCollisionObject() {
            return collisionObject;
        }

        public CollisionState getCollisionState() {
            return collisionState;
        }

        public boolean isEnemyHit() {
            return collisionState.equals(CollisionState.ENEMY_HIT);
        }

        public boolean isWallHit() {
            return collisionState.equals(CollisionState.WALL_HIT);
        }

        public boolean isNoCollision() {
            return collisionState.equals(CollisionState.NO_COLLISION);
        }

        @Override
        public String toString() {
            return collisionState.name();
        }
    }

    public enum CollisionState {
        ENEMY_HIT, WALL_HIT, NO_COLLISION
    }

    public void initiateMap(String worldName) {
        currentMap = WorldMap.instantiateFromFile(worldName);
    }

    public void loadMap(String worldName) {
        currentMap = WorldMap.loadFromFile(worldName);
    }

    public void saveMap(String worldName) {

        try {
            currentMap.saveToFile(worldName);
            DebugLog.write("Saved Map: " + worldName);
        } catch (IOException e) {
            e.printStackTrace();
            DebugLog.write("Map Save failed: " + e.getMessage());
        }
    }

    public void spawnEnemy(Vector position) {
        spawnHandler.spawnEnemyAt(position);
    }

    public void spawnEnemyAround(Vector position, int radius) {
        spawnHandler.spawnEnemyAround(position, radius);
    }

    public void setMaxEnemies(int maxEnemies) {
        spawnHandler.setMaxEnemies(maxEnemies);
    }

    public void setEnemySpawnTime(int spawnTimeInMillis) {
        spawnHandler.setSpawnTime(spawnTimeInMillis);
    }

    public float getSpawnTime() {
        return spawnHandler.getSpawnTime();
    }

}