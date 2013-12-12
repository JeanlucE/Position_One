package Environment;

import Components.GraphicsComponent;
import Components.PhysicsComponent;
import WIP.Collidable;
import WIP.Transform;

/**
 * Created with IntelliJ IDEA.
 * User: Jean-Luc
 * Date: 07.11.13
 * Time: 15:05
 */
public abstract class WorldSpace extends Collidable {
    protected WorldSpace(Transform transform, GraphicsComponent g, PhysicsComponent physicsComponent) {
        super(transform, g, physicsComponent);
    }

    public void update() {
    }

    public abstract boolean isWall();

    public abstract boolean isFloor();
}
