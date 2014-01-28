package Environment;

import Actors.Enemy;
import Components.ActorGraphicsComponent;
import Components.DynamicResource;
import Components.PhysicsComponent;
import WIP.*;

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
    private int currentEnemies = 3;
    private List<Enemy> enemies;
    private final World world;
    private int spawnTime = 10000;

    public SpawnHandler(World world, int maxEnemies) {
        this.maxEnemies = maxEnemies;
        this.world = world;
        this.enemies = new ArrayList<>(maxEnemies);
    }

    private int currentSpawnTime = 0;

    void update() {
        if (currentSpawnTime < spawnTime) {
            currentSpawnTime += Time.deltaTime();
        } else {
            currentSpawnTime = 0;
            if (currentEnemies < maxEnemies) {
                spawnEnemyAround(Game.getInstance().getPlayer().getTransform().getPosition(), 200);
            }
        }
        for (int i = enemies.size() - 1; i >= 0; i--) {
            if (enemies.get(i).isDestroyed()) {
                enemies.remove(i);
                currentEnemies--;
            }
        }
    }

    boolean spawnEnemyAt(Vector position) {
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
            return false;
        } else {
            enemies.add(e);
            return true;
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
        while (!spawnEnemyAt(spawnPoint)) {
            spawnPoint = floors.get(r.nextInt(floors.size())).shift(20, 20);
        }
        currentEnemies++;
    }
}
