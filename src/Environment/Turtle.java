package Environment;

import Components.PhysicsComponent;
import Components.Resource;
import Components.StaticGraphicsComponent;
import WIP.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Stack;
//TODO later: make class package-local

/**
 * Created with IntelliJ IDEA.
 * User: Jean-Luc
 * Date: 16.12.13
 * Time: 17:25
 */
public class Turtle {
    private AdvancedTransform transform;
    //TODO save this in a tree, which can then include easy implementation of intersections
    private Stack<TurtleMove> positions;
    //TODO outsource this to turtleinterpreter
    private Map<Vector, WorldSpace> generatedMap;


    public Turtle() {
        transform = new AdvancedTransform();
        positions = new Stack<>();
        positions.push(new TurtleMove(new AdvancedVector(0f, 0f), transform.getPosition().clone()));
        randomMoves();
        //positions.push(new TurtleMove(new AdvancedVector(0f, 0f), new AdvancedVector(10, 10)));
        //randomMoves();
        generatedMap = new HashMap<>(400);
    }

    private AdvancedTransform getTransform() {
        return transform;
    }


    private void randomMoves(Random r, int minCorridorLength, int maxCorridorLength, int numOfMoves) {
        int max = maxCorridorLength - minCorridorLength + 1;
        for (int i = 0; i < numOfMoves; i++) {
            int action = r.nextInt(2);
            switch (action) {
                case 0:
                    turnLeft(r.nextInt(180) + 1);
                    break;
                case 1:
                    turnRight(r.nextInt(180) + 1);
                    break;
            }
            forward(r.nextInt(max) + minCorridorLength);
        }

    }

    private void randomMoves() {
        randomMoves(new Random(), 10, 15, 30);
    }

    private AdvancedVector endPos;

    private void forward(float distance) {
        AdvancedVector startPos;
        if (endPos == null)
            endPos = transform.getPosition().clone();

        startPos = endPos;
        transform.getPosition().shift(
                transform.getDirection().getX() * distance,
                transform.getDirection().getY() * distance);
        endPos = transform.getPosition().clone();
        positions.push(new TurtleMove(startPos, endPos));
    }

    private void turnLeft(double angle) {
        angle = angle % 360f;
        angle = Math.toRadians(angle);
        AdvancedVector dir = transform.getDirection();
        double x = dir.getX();
        double y = dir.getY();
        dir.setX((float) (x * Math.cos(angle) - y * Math.sin(angle)));
        dir.setY((float) (x * Math.sin(angle) + y * Math.cos(angle)));

        if (dir.getX() < 0.01 && dir.getX() > -0.01)
            dir.setX(0f);
        if (dir.getY() < 0.01 && dir.getY() > -0.01)
            dir.setY(0f);
    }

    private void turnRight(float angle) {
        turnLeft(-angle);
    }

    //TODO DEBUG make private later
    TurtleMove[] getPositions() {
        return positions.toArray(new TurtleMove[positions.size()]);
    }

    //TODO DEBUG make package-local later
    public Map<Vector, WorldSpace> getGeneratedMap() {
        TurtleInterpreter interpreter = new TurtleInterpreter(this, 3);

        return generatedMap;
    }

    private static StaticGraphicsComponent floor = new StaticGraphicsComponent(Resource.floor01);
    private static StaticGraphicsComponent wall = new StaticGraphicsComponent(Resource.wall01);

    void add(Vector vector, BlockID id) {

        WorldSpace worldSpace;
        if (id == BlockID.FLOOR) {
            worldSpace = new Floor(new Transform(transformToWorldSpace(vector.clone())), floor);
            surroundWithWalls(vector);
        } else {
            PhysicsComponent p = new PhysicsComponent(Renderer.TILESIZE, Renderer.TILESIZE);
            worldSpace = new Wall(new Transform(transformToWorldSpace(vector.clone())), wall, p);
        }

        WorldSpace w = generatedMap.get(vector);
        if (w == null || w.isWall()) {
            generatedMap.put(vector, worldSpace);
        }
    }

    private Vector transformToWorldSpace(Vector v) {
        v.setX(v.getX() * Renderer.TILESIZE);
        v.setY(v.getY() * Renderer.TILESIZE);
        return v;
    }

    /**
     * Surrounds a given floor at a vector with walls. This ensures the palyer does not collide with null Worldspaces
     *
     * @param floorVector Vector of the floor space that should be surrounded
     */
    private void surroundWithWalls(Vector floorVector) {
        add(floorVector.shiftedPosition(0, 1), BlockID.WALL);
        add(floorVector.shiftedPosition(1, 1), BlockID.WALL);
        add(floorVector.shiftedPosition(1, 0), BlockID.WALL);
        add(floorVector.shiftedPosition(1, -1), BlockID.WALL);
        add(floorVector.shiftedPosition(0, -1), BlockID.WALL);
        add(floorVector.shiftedPosition(-1, -1), BlockID.WALL);
        add(floorVector.shiftedPosition(-1, 0), BlockID.WALL);
        add(floorVector.shiftedPosition(-1, 1), BlockID.WALL);
    }
}
