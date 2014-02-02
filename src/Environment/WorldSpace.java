package Environment;

import Components.GraphicsComponent;
import WIP.GameObject;
import WIP.Transform;

/**
 * Created with IntelliJ IDEA.
 * User: Jean-Luc
 * Date: 07.11.13
 * Time: 15:05
 */
public abstract class WorldSpace extends GameObject {

    protected WorldSpace(Transform transform, GraphicsComponent g) {
        super(transform, g);
    }

    public void update() {
    }

    public abstract boolean isWall();

    public abstract boolean isFloor();

    public abstract boolean isInteractable();
}
