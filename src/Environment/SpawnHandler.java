package Environment;

import Actors.Enemy;
import Components.ActorGraphicsComponent;
import Components.DynamicResource;
import Components.PhysicsComponent;
import WIP.DebugLog;
import WIP.Transform;
import WIP.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: Jean-Luc
 * Date: 21.01.14
 * Time: 12:46
 */
public class SpawnHandler {
    private int maxEnemies;
    private final World world;

    public SpawnHandler(World world, int maxEnemies) {
        this.maxEnemies = maxEnemies;
        this.world = world;
    }

    void spawnEnemyAt(Vector position) {
        Enemy e = new Enemy("Chu Chu", 100, new Transform(),
                new ActorGraphicsComponent(DynamicResource.ENEMY_CHUCHU),
                new PhysicsComponent(39, 39));
        e.getTransform().setPosition(position);

        DebugLog.write("SpawnHandler: tried spawning enemy at " + position.toString());
        World.CollisionEvent collisionEvent = world.resolveCollision(e, e.getTransform().getPosition());
        if (collisionEvent.getCollisionState() != World.CollisionState.NO_COLLISION) {
            e.destroy();
            DebugLog.write("SpawnHandler: could not spawn enemy at: " + position.toString());
            DebugLog.write("SpawnHandler: " + collisionEvent.toString());
        }
    }

    void spawnEnemyAround(Vector position, int radius) {
        if (radius <= 0) return;

        Vector bottomLeft = position.shiftedPosition(-radius, -radius);
        Vector topRight = position.shiftedPosition(radius, radius);
        Map<Vector, WorldSpace> possibleSpaces = world.getSubSpace(bottomLeft, topRight);
        List<Vector> floors = new ArrayList<>(40);
        for (Map.Entry<Vector, WorldSpace> e : possibleSpaces.entrySet()) {
            WorldSpace w = e.getValue();
            if (w != null && w.isFloor())
                floors.add(e.getKey());
        }

        Random r = new Random();
        Vector spawnPoint = floors.get(r.nextInt(floors.size())).shift(20, 20);
        spawnEnemyAt(spawnPoint);
    }
}
