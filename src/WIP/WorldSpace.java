package WIP;

import Components.GraphicsComponent;

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
}
