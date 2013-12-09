package WIP;

import Components.PhysicsComponent;
import Items.Projectile;

/**
 * Created with IntelliJ IDEA.
 * User: Jean-Luc
 * Date: 07.11.13
 * Time: 15:00
 * <p/>
 * Holds Worldmap and resolves all collisions.
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

    public static World getInstance() {
        if (instance == null)
            instance = new World();
        return instance;
    }

    private World() {
        currentMap = new WorldMap();
    }

    public WorldSpace get(int x, int y) {
        return currentMap.getReal(x / 40, y / 40);
    }

    //DEBUGGING
    public WorldSpace getReal(int x, int y) {
        return currentMap.getReal(x, y);
    }

    public WorldSpace get(Vector position) {
        return currentMap.getReal(position.getX() / 40, position.getY() / 40);
    }

    /*
    This method resolves boolean collision between the gameworld and another gameobject (e.g. a player).
    It does this by getting the position of all 4 corners of the gameobject's colider and then checks if any of those
    corners would clip into a wall when the game object move to the next Position.
    If none of the corners clips a collidable world space (e.g. a wall) the method returns true.
    If any of the corners clips a collidable world space (e.g. a wall) the method returns false.
     */
    public CollisionEvent resolveCollision(Actor actor, Vector nextPosition) {
        //TODO if player cant move directly to the given position, test if he can still move nearer to that position
        PhysicsComponent collider = actor.getCollider();
        for (Vector p : collider.getCorners(nextPosition)) {
            CollisionEvent collisionEvent = resolveWallCollision(p);
            if (collisionEvent.isWallHit())
                return collisionEvent;
        }
        return resolveActorCollision(actor, nextPosition);
    }

    public CollisionEvent resolveCollision(Projectile p, Vector nextPosition) {
        PhysicsComponent collider = p.getCollider();
        for (Vector v : collider.getCorners(nextPosition)) {
            CollisionEvent wallCollision = resolveWallCollision(v);
            if (wallCollision.isWallHit())
                //TODO set wall collision object
                return wallCollision;
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

    //Returns if the position that is passed has any collidable gameobject
    private CollisionEvent resolveWallCollision(int x, int y) {
        if (currentMap.getReal(x / 40, y / 40).isCollidable()) {
            return new CollisionEvent(CollisionState.WALL_HIT, currentMap.getReal(x / 40, y / 40));
        } else {
            return new CollisionEvent(CollisionState.NO_COLLISION, null);
        }

    }

    private CollisionEvent resolveWallCollision(Vector position) {
        return resolveWallCollision(position.getX(), position.getY());
    }

    /*
    This method resolves collisions between the player and any enemy (for now) and any further actors in the scene.
    TODO make this between all actors
     */
    private CollisionEvent resolveActorCollision(Actor actor, Vector nextPosition) {
        Actor[] actors = Actor.getActors();
        for (int i = 0; i < actors.length; i++) {
            Actor otherActor = actors[i];
            if (actor != otherActor && !actor.getFaction().equals(otherActor.getFaction())) {
                PhysicsComponent nextCollider = actor.getCollider().clone(nextPosition);
                PhysicsComponent otherCollider = otherActor.getCollider();
                if (nextCollider.collision(otherCollider))
                    return new CollisionEvent(CollisionState.ENEMY_HIT, otherActor);
            }
        }
        return new CollisionEvent(CollisionState.NO_COLLISION, null);
    }

    public class CollisionEvent {

        private GameObject collisionObject;
        private CollisionState collisionState;

        public CollisionEvent(CollisionState collisionState, GameObject collisionObject) {
            this.collisionState = collisionState;
            if (collisionState.equals(CollisionState.ENEMY_HIT))
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
    }

    public enum CollisionState {
        ENEMY_HIT, WALL_HIT, NO_COLLISION;
    }
}
