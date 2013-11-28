package Items;

import Components.GraphicsComponent;
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

    //TODO debug direction of arrow movement
    public Projectile(Transform origin, GraphicsComponent graphic, int speed) {
        super(origin.clone(), graphic);
        flyVector = getTransform().getDirection();
        flyVector.setX(flyVector.getX() * speed);
        flyVector.setY(flyVector.getY() * speed);
        projectiles.add(this);
    }

    public static Projectile[] getProjectiles() {
        return projectiles.toArray(new Projectile[projectiles.size()]);
    }

    @Override
    public void update() {
        getTransform().getPosition().shift(flyVector.getX(), flyVector.getY());
    }

    @Override
    public boolean isCollidable() {
        return true;
    }
}
