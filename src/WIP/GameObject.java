package WIP;

import Actors.Actor;
import Components.GraphicsComponent;
import Items.Projectile;

import java.util.LinkedList;
import java.util.List;

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
    private static List<GameObject> gameObjects = new LinkedList<>();

    protected GameObject(Transform transform, GraphicsComponent graphic) {
        this.transform = transform;
        this.graphic = graphic;
        destroyed = false;
        gameObjects.add(this);
    }

    public static GameObject[] getGameObjects() {
        return gameObjects.toArray(new GameObject[gameObjects.size()]);
    }

    public static void removeDestroyedGameObjects() {
        GameObject[] g = getGameObjects();
        for (int i = g.length - 1; i >= 0; i--) {
            if (g[i].isDestroyed()) {
                gameObjects.remove(i);
            }
        }
        Actor.removeDeadActors();
        Projectile.removeDeadProjectiles();

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
