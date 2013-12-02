package Items;

import Components.GraphicsComponent;
import Components.PhysicsComponent;
import WIP.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Jean-Luc
 * Date: 28.11.13
 * Time: 10:00
 */
public class Projectile extends GameObject {
    private static List<Projectile> projectiles = new ArrayList<>();
    private Vector flyVector;
    private Vector origin;
    private int range;
    private int damage;
    private PhysicsComponent phys;

    public Projectile(Transform origin, GraphicsComponent graphic, PhysicsComponent phys, int speed, int range,
                      int damage) {
        super(origin.clone(), graphic);

        //Spawn position
        this.origin = origin.getPosition();

        //Set Collider
        this.phys = phys;

        //Set flying direction and speed
        flyVector = getTransform().getDirection();
        flyVector.setX(flyVector.getX() * speed);
        flyVector.setY(flyVector.getY() * speed);

        //Set how far the projectile flies
        this.range = range * Renderer.TILESIZE;

        //TODO shift origin position to the middle of the player

        //Set damage
        this.damage = damage;

        projectiles.add(this);
    }

    public static Projectile[] getProjectiles() {
        return projectiles.toArray(new Projectile[projectiles.size()]);
    }

    public static void removeDeadProjectiles() {
        for (int i = 0; i < projectiles.size(); i++)
            if (projectiles.get(i).isDestroyed()) {
                projectiles.remove(i);
            }
    }

    @Override
    public void update() {
        getTransform().getPosition().shift(flyVector.getX(), flyVector.getY());
        if (Math.abs(getTransform().getPosition().getX() - origin.getX()) > range
                || Math.abs(getTransform().getPosition().getY() - origin.getY()) > range) {
            this.destroy();
            DebugLog.write("Projectile destroyed");
        }
    }

    @Override
    public boolean isCollidable() {
        return true;
    }

    public PhysicsComponent getCollider() {
        return phys;
    }

    public int getDamage() {
        return damage;
    }
}
