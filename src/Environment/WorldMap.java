package Environment;

import Components.PhysicsComponent;
import Components.Resource;
import Components.StaticGraphicsComponent;
import WIP.DebugLog;
import WIP.Renderer;
import WIP.Transform;
import WIP.Vector;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
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
    public static String mapsPath = "./maps/";
    private Map<Vector, WorldSpace> worldSpaceMap = new HashMap<>();

    public static WorldMap getRandomMap() {
        Turtle t = new Turtle();
        TurtleInterpreter turtleInterpreter = new TurtleInterpreter(3);
        turtleInterpreter.addTurtle(t);
        return new WorldMap(turtleInterpreter.getGeneratedMap());
    }

    public static WorldMap getRandomMap(long seed) {
        Turtle t = new Turtle(seed);
        TurtleInterpreter turtleInterpreter = new TurtleInterpreter(3);
        turtleInterpreter.addTurtle(t);
        return new WorldMap(turtleInterpreter.getGeneratedMap());
    }

    public static WorldMap getRandomMap(int minCorridorLength, int maxCorridorLength, int numOfCorridors,
                                        int corridorWidth) {
        Turtle t = new Turtle(minCorridorLength, maxCorridorLength, numOfCorridors);
        TurtleInterpreter turtleInterpreter = new TurtleInterpreter(corridorWidth);
        turtleInterpreter.addTurtle(t);
        return new WorldMap(turtleInterpreter.getGeneratedMap());
    }

    //DEBUGGING
    public WorldMap() {
        int tilesize = Renderer.TILESIZE;
        int roomsize = 20;
        //Creates a square room of with walk space 19x19
        StaticGraphicsComponent g = new StaticGraphicsComponent(Resource.wall01);
        StaticGraphicsComponent floor = new StaticGraphicsComponent(Resource.floor01);
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

    private WorldMap(Map<Vector, WorldSpace> map) {
        worldSpaceMap = map;
        DebugLog.write("New World created with size: " + worldSpaceMap.size());
    }

    public WorldSpace getReal(int x, int y) {
        return worldSpaceMap.get(new Vector(x, y));
    }

    //DEBUGGING delete later
    private void set(int x, int y, WorldSpace worldSpace) {
        setReal(x, y, worldSpace);
    }

    public void setReal(int x, int y, WorldSpace worldSpace) {
        worldSpaceMap.put(new Vector(x, y), worldSpace);
    }

    public static WorldMap loadFromFile(String filename) throws IOException {
        String[] worldSpaces;
        try {
            worldSpaces = readFile(mapsPath + filename + ".txt");
        } catch (FileNotFoundException e) {
            DebugLog.write("[ERROR]: The file " + filename + ".txt was not found!");
            return new WorldMap();
        }
        if (worldSpaces.length == 0) {
            DebugLog.write("[ERROR]: The file " + filename + ".txt is empty!");
            return new WorldMap();
        }
        Map<Vector, WorldSpace> result = new HashMap<>();
        int tilesize = Renderer.TILESIZE;
        //Creates a square room of with walk space 19x19
        StaticGraphicsComponent wall = new StaticGraphicsComponent(Resource.wall01);
        StaticGraphicsComponent floor = new StaticGraphicsComponent(Resource.floor01);
        PhysicsComponent boxCollider = new PhysicsComponent(tilesize, tilesize);
        for (String worldSpace : worldSpaces) {
            if (worldSpace.length() > 0) {
                int cutIndex1 = 0, cutindex2 = 0;
                for (int j = 0; j < worldSpace.length(); j++) {
                    if (worldSpace.charAt(j) == ',') {
                        if (cutIndex1 == 0)
                            cutIndex1 = j;
                        else
                            cutindex2 = j;
                    }
                }
                String type = worldSpace.substring(0, cutIndex1);
                int xPos = Integer.parseInt(worldSpace.substring(cutIndex1 + 1, cutindex2));
                int yPos = Integer.parseInt(worldSpace.substring(cutindex2 + 1));
                if (type.startsWith("w")) { //if type is wall
                    result.put(new Vector(xPos, yPos),
                            new Wall(new Transform(new Vector(xPos * tilesize, yPos * tilesize)), wall,
                                    boxCollider));
                } else if (type.startsWith("f")) {
                    result.put(new Vector(xPos, yPos),
                            new Floor(new Transform(new Vector(xPos * tilesize, yPos * tilesize)), floor));
                }
            }
        }
        return new WorldMap(result);
    }

    public static WorldMap instantiateFromFile(String filename) {
        Map<Vector, WorldSpace> result = new HashMap<>();
        String[] worldSpaces;
        try {
            worldSpaces = readFile(mapsPath + filename + ".txt");
        } catch (FileNotFoundException e) {
            DebugLog.write("[ERROR]: The file " + filename + ".txt was not found!");
            return new WorldMap();
        }
        if (worldSpaces.length == 0) {
            DebugLog.write("[ERROR]: The file " + filename + ".txt is empty!");
            return new WorldMap();
        }
        int tilesize = Renderer.TILESIZE;
        StaticGraphicsComponent wall = new StaticGraphicsComponent(Resource.wall01);
        StaticGraphicsComponent floor = new StaticGraphicsComponent(Resource.floor01);
        PhysicsComponent boxCollider = new PhysicsComponent(tilesize, tilesize);

        for (int i = 0; i < worldSpaces.length; i++) {
            char[] worldSpace = worldSpaces[worldSpaces.length - i - 1].toCharArray();
            for (int j = 0; j < worldSpace.length; j++) {
                char c = worldSpace[j];
                if (c == 'w')
                    result.put(new Vector(j, i),
                            new Wall(new Transform(new Vector(j * tilesize, i * tilesize)), wall, boxCollider));
                else if (c == 'f')
                    result.put(new Vector(j, i),
                            new Floor(new Transform(new Vector(j * tilesize, i * tilesize)), floor));
            }
        }
        return new WorldMap(result);
    }

    public void saveToFile(String filename) throws IOException {
        BufferedWriter bf = new BufferedWriter(new FileWriter(new File(mapsPath + filename + ".txt")));
        for (Vector v : worldSpaceMap.keySet()) {
            WorldSpace w = worldSpaceMap.get(v);
            if (w.isWall())
                bf.append("wall," + v.getX() + "," + v.getY());
            else if (w.isFloor()) {
                bf.append("floor," + v.getX() + "," + v.getY());
            }
            bf.newLine();
        }
        bf.flush();
        bf.close();
    }

    private static String[] readFile(String fileName) throws FileNotFoundException {
        BufferedReader bufferedReader;
        bufferedReader = new BufferedReader(new FileReader(fileName));

        List<String> lines = new LinkedList<>();

        try {
            String line = bufferedReader.readLine();

            while (line != null) {
                lines.add(line);
                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            return null;
        }

        return lines.toArray(new String[lines.size()]);

    }
}
