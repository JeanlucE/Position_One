package WIP;

import Components.GraphicsComponent;
import Components.PhysicsComponent;
import Components.Resource;
import Components.StaticGraphicsComponent;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Jean-Luc
 * Date: 03.12.13
 * Time: 09:28
 * <p/>
 * Saves a single Instance of a map. Can be saved and later loaded
 */
public class WorldMap {
    //TODO worldmap save function
    private Map<Vector, WorldSpace> worldSpaceMap = new HashMap<>();

    public WorldMap() {
        int tilesize = 40;
        int roomsize = 20;
        //Creates a square room of with walk space 19x19
        GraphicsComponent g = new StaticGraphicsComponent(Resource.wall01);
        GraphicsComponent floor = new StaticGraphicsComponent(Resource.floor01);
        PhysicsComponent boxCollider = new PhysicsComponent(tilesize, tilesize);
        for (int i = 0; i < roomsize * tilesize; i += tilesize) {
            set(i, 0, new Wall(new Transform(new Vector(i, 0)), g, boxCollider));
            set(i, (roomsize - 1) * tilesize, new Wall(new Transform(new Vector(i, (roomsize - 1) * tilesize)), g,
                    boxCollider));
        }
        for (int i = 40; i < (roomsize - 1) * tilesize; i += tilesize) {
            set(0, i, new Wall(new Transform(new Vector(0, i)), g, boxCollider));
            set((roomsize - 1) * tilesize, i, new Wall(new Transform(new Vector((roomsize - 1) * tilesize, i)), g,
                    boxCollider));
            for (int j = 40; j < (roomsize - 1) * tilesize; j += tilesize) {
                //Floor
                set(i, j, new Floor(new Transform(new Vector(i, j)), floor));
            }
        }

        set(120, 120, new Wall(new Transform(new Vector(120, 120)), g, boxCollider));
        set(120, 160, new Wall(new Transform(new Vector(120, 160)), g, boxCollider));
        set(120, 200, new Wall(new Transform(new Vector(120, 200)), g, boxCollider));
        set(160, 200, new Wall(new Transform(new Vector(160, 200)), g, boxCollider));
        set(240, 200, new Wall(new Transform(new Vector(240, 200)), g, boxCollider));
        DebugLog.write("New World created with size: " + worldSpaceMap.size());
    }

    public WorldSpace getReal(int x, int y) {
        return worldSpaceMap.get(new Vector(x, y));
    }

    /*public WorldSpace get(int x, int y) {
        int xReal = x / 40;
        int yReal = y / 40;
        return getReal(xReal, yReal);
    }

    public WorldSpace get(Vector position) {
        return get(position.getX(), position.getY());
    }*/

    private void set(int x, int y, WorldSpace worldSpace) {
        int xReal = x / 40;
        int yReal = y / 40;
        setReal(xReal, yReal, worldSpace);
    }

    public void setReal(int x, int y, WorldSpace worldSpace) {
        worldSpaceMap.put(new Vector(x, y), worldSpace);
    }
}
