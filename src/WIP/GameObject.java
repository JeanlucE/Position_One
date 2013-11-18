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


    protected GameObject(Transform transform, GraphicsComponent graphic) {
        this.transform = transform;
        this.graphic = graphic;
    }

    public abstract void update();

    public abstract boolean isCollidable();

    public Transform getTransform() {
        return transform;
    }

    public GraphicsComponent getGraphic() {
        return graphic;
    }
}
