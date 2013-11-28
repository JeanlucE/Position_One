package Items;

import Components.GraphicsComponent;
import WIP.DebugLog;
import WIP.GameObject;
import WIP.Transform;
import WIP.Vector;

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

    public Projectile(Transform origin, GraphicsComponent graphic, int speed, int range) {
        super(origin.clone(), graphic);
        this.origin = origin.clone().getPosition();
        flyVector = getTransform().getDirection();
        flyVector.setX(flyVector.getX() * speed);
        flyVector.setY(flyVector.getY() * speed);
        this.range = range;
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
}
