package Environment;

import WIP.AdvancedTransform;
import WIP.AdvancedVector;

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
    private Stack<TurtleMove> positions;

    public Turtle() {
        transform = new AdvancedTransform();
        positions = new Stack<>();
        positions.push(new TurtleMove(new AdvancedVector(0f, 0f), transform.getPosition().clone()));
        randomMoves();
    }

    public AdvancedTransform getTransform() {
        return transform;
    }

    private void randomMoves(Random r) {
        forward(5);
    }

    private void randomMoves() {
        randomMoves(new Random());
    }

    private AdvancedVector endPos;
    private boolean init = false;

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

    public TurtleMove[] getPositions() {
        return positions.toArray(new TurtleMove[positions.size()]);
    }

    public class TurtleMove {
        private AdvancedVector startPos;
        private AdvancedVector endPos;

        public TurtleMove(AdvancedVector startPos, AdvancedVector endPos) {
            this.startPos = startPos;
            this.endPos = endPos;
        }

        public AdvancedVector getStartPos() {
            return startPos;
        }

        public AdvancedVector getEndPos() {
            return endPos;
        }

        public String toString() {
            return "{" + startPos + " - " + endPos + "}";
        }
    }
}
