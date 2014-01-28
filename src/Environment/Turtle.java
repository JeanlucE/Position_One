package Environment;

import WIP.AdvancedTransform;
import WIP.AdvancedVector;

import java.util.Random;
import java.util.Stack;

/**
 * Created with IntelliJ IDEA.
 * User: Jean-Luc
 * Date: 16.12.13
 * Time: 17:25
 */
class Turtle {
    private AdvancedTransform transform = new AdvancedTransform();
    //TODO save this in a tree, which can then include easy implementation of intersections
    private Stack<TurtleMove> positions = new Stack<>();
    private Random r;

    public Turtle() {
        randomMoves();
    }

    public Turtle(long seed) {
        r = new Random(seed);
        randomMoves();
    }

    public Turtle(int minLength, int maxLength, int numOfMoves) {
        randomMoves(minLength, maxLength, numOfMoves);
    }

    private AdvancedTransform getTransform() {
        return transform;
    }


    private void randomMoves(int minCorridorLength, int maxCorridorLength, int numOfMoves) {
        if (r == null) {
            r = new Random();
        }
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
        randomMoves(10, 15, 30);
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

    TurtleMove[] getPositions() {
        return positions.toArray(new TurtleMove[positions.size()]);
    }
}
