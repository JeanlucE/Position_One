package WIP;

import Components.GraphicsComponent;

/**
 * Created with IntelliJ IDEA.
 * User: Jean-Luc
 * Date: 09.11.13
 * Time: 21:33
 */
public abstract class GameObject {
    private final Transform transform;
    private final GraphicsComponent graphic;
    private boolean destroyed;

    protected GameObject(Transform transform, GraphicsComponent graphic) {
        this.transform = transform;
        this.graphic = graphic;
        destroyed = false;
    }

    public abstract void update();

    public abstract boolean isCollidable();

    public Transform getTransform() {
        return transform;
    }

    public GraphicsComponent getGraphic() {
        return graphic;
    }

    public void destroy() {
        destroyed = true;
    }

    public boolean isDestroyed() {
        return destroyed;
    }
}
