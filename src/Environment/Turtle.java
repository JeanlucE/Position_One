package Environment;

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
    private Stack<TurtleMove> positions;
    private Map<Vector, WorldSpace> generatedMap;

    public Turtle() {
        transform = new AdvancedTransform();
        positions = new Stack<>();
        positions.push(new TurtleMove(new AdvancedVector(0f, 0f), transform.getPosition().clone()));
        randomMoves();
        generatedMap = new HashMap<>(400);
    }

    //TODO DEBUG make private later
    public AdvancedTransform getTransform() {
        return transform;
    }

    private void randomMoves(Random r) {
        for (int i = 0; i < 20; i++) {
            int action = r.nextInt(2);
            switch (action) {
                case 0:
                    turnLeft(r.nextInt(180) + 1);
                    break;
                case 1:
                    turnRight(r.nextInt(180) + 1);
                    break;
            }
            forward(r.nextInt(6) + 5);
        }

    }

    private void randomMoves() {
        randomMoves(new Random());
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
        TurtleInterpreter interpreter = new TurtleInterpreter(this, 10);

        return generatedMap;
    }

    void add(Vector vector, WorldSpace worldSpace) {
        if (!generatedMap.containsKey(vector)) {
            generatedMap.put(vector, worldSpace);
        }
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

    private class TurtleInterpreter {

        private Turtle turtle;
        private int corridorWidth;
        public static final int TILESIZE = 40;

        TurtleInterpreter(Turtle t, int corridorWidth) {
            this.turtle = t;
            this.corridorWidth = corridorWidth;
            TurtleMove[] positions = turtle.getPositions();

            //addCorridor(positions[1].getStartPos(), positions[1].getEndPos());
            addCorridor(new AdvancedVector(0, 0), new AdvancedVector(0, -10));

        }

        void addCorridor(AdvancedVector start, AdvancedVector end) {

            if (start.equals(end))
                return;

            //Absolute Position of the corridor start
            Vector vStart = interpolatePosition(start);
            //Absolute Position of the corridor end
            Vector vEnd = interpolatePosition(end);


            //Always check squares between the start and end from left to right
            if (vStart.getX() > vEnd.getX()) {
                int xTemp = vEnd.getX();
                int yTemp = vEnd.getY();

                vEnd.setX(vStart.getX());
                vEnd.setY(vStart.getY());

                vStart.setX(xTemp);
                vStart.setY(yTemp);
            }
            StaticGraphicsComponent floor = new StaticGraphicsComponent(Resource.floor01);
            int deltaX = vEnd.getX() - vStart.getX();
            int deltaY = vEnd.getY() - vStart.getY();
            if (deltaX != 0) {
                float error = 0;
                float deltaError = Math.abs(deltaY / (float) deltaX); //Assuming deltaX != 0
                int y = vStart.getY();
                for (int x = vStart.getX(); x <= vEnd.getX(); x++) {
                    add(new Vector(x, y), new Floor(new Transform(new Vector(x, y)), floor));
                    error += deltaError;
                    if (error >= 0.5f) {
                        y++;
                        error = error - 1.0f;
                    }
                }
            } else {

                if (deltaY == 0)
                    return;

                int x = vStart.getX();
                int yStart = Math.min(vStart.getY(), vEnd.getY());
                int yEnd = (yStart == vStart.getY()) ? (vEnd.getY()) : (vStart.getY());

                for (int y = yStart; y <= yEnd; y++) {
                    add(new Vector(x, y), new Floor(new Transform(new Vector(x, y)), floor));
                }

            }
        }

        private Vector interpolatePosition(AdvancedVector v) {
            //TODO include scale in interpolation
            return new Vector(round(v.getX()), round(v.getY()));
        }

        private int round(float f) {
            if (Float.isInfinite(f) || Float.isNaN(f)) {
                DebugLog.write("TurtleInterpreter tried to round a infinite float or NaN");
                return 0;
            }
            if (f > 0) {
                return Math.round(f);
            } else if (f < 0) {
                return -Math.round(-f);
            } else {
                return 0;
            }
        }
    }
}
