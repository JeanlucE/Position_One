package Items;

import Actors.Actor;
import Components.GraphicsComponent;
import Components.PhysicsComponent;
import Environment.World;
import WIP.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Jean-Luc
 * Date: 28.11.13
 * Time: 10:00
 */
public class Projectile extends GameObject implements Collidable {
    private static List<Projectile> projectiles = new ArrayList<>();
    private Vector flyVector;
    private Vector origin;
    private int range;
    private int damage;
    private PhysicsComponent collider;

    public Projectile(Transform origin, GraphicsComponent graphic, PhysicsComponent phys, int speed, int range,
                      int damage) {
        super(origin, graphic);
        setCollider(phys);
        phys.setParent(getTransform());

        //Spawn position
        this.origin = origin.getPosition();

        DebugLog.write("New Projectile at: " + origin.getPosition());

        //Set flying direction and speed
        this.flyVector = getTransform().getDirection().clone();
        this.flyVector.setX(flyVector.getX() * speed);
        this.flyVector.setY(flyVector.getY() * speed);

        //Shifts origin of projectile forward of player
        Vector toShift = Vector.multiply(flyVector.normalizedVector(), 15);
        this.origin.shift(toShift.getX(), toShift.getY());

        //Set how far the projectile flies
        this.range = range * Renderer.TILESIZE;

        //Set damage
        this.damage = damage;

        projectiles.add(this);
    }

    public static Projectile[] getProjectiles() {
        return projectiles.toArray(new Projectile[projectiles.size()]);
    }

    public static void removeDeadProjectiles() {
        for (int i = projectiles.size() - 1; i >= 0; i--)
            if (projectiles.get(i).isDestroyed()) {
                projectiles.remove(i);
            }
    }

    @Override
    public PhysicsComponent getCollider() {
        return collider;
    }

    @Override
    public void setCollider(PhysicsComponent physicsComponent) {
        collider = physicsComponent;
    }

    @Override
    public void update() {
        Vector p = getTransform().getPosition().shiftedPosition(flyVector.getX(), flyVector.getY());

        //If out of range destroy this
        if ((Math.abs(p.getX() - origin.getX()) > range || Math.abs(p.getY() - origin.getY()) > range)) {
            this.destroy();
            DebugLog.write("Projectile destroyed");
            return;
        }

        World.CollisionEvent collisionEvent = World.getInstance().resolveCollision(this, p);
        World.CollisionState collisionState = collisionEvent.getCollisionState();
        switch (collisionState) {
            case ENEMY_HIT:
                DebugLog.write("Projectile destroyed at " + getTransform().getPosition());
                ((Actor) collisionEvent.getCollisionObject()).damage(damage);
            case WALL_HIT:
                this.destroy();
                return;
            default:
                break;
        }

        getTransform().setPosition(p);
    }

    @Override
    public boolean isCollidable() {
        return true;
    }

    public int getDamage() {
        return damage;
    }
}
