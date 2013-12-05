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

    public CollisionEvent resolveCollision(Projectile p, Vector nextPosition) {
        PhysicsComponent collider = p.getCollider();
        for (Vector v : collider.getCorners(nextPosition)) {
            if (!canMoveTo(v))
                //TODO set wall collision object
                return new CollisionEvent(CollisionState.WALL_HIT, null);
        }

        for (Actor a : Actor.getActors()) {
            if (a != Game.getInstance().getPlayer()) {
                if (p.getCollider().onX(a.getCollider(), 0.3f) && p.getCollider().onY(a.getCollider(), 0.3f)) {
                    System.out.println(a.toString() + " hit!");
                    //CollisionState.ENEMY_HIT.setCollisionObject(a);
                    return new CollisionEvent(CollisionState.ENEMY_HIT, a);
                }
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

    //Returns if the position that is passed has any collidable gameobject
    boolean canMoveTo(int x, int y) {
        return !currentMap.getReal(x / 40, y / 40).isCollidable();
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
