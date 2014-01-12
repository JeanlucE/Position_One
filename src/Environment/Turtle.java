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
    private Stack<TurtleMove> positions;
    private Map<Vector, WorldSpace> generatedMap;
    //TODO save this in a tree, which can then include easy implementation of intersections

    public Turtle() {
        transform = new AdvancedTransform();
        positions = new Stack<>();
        positions.push(new TurtleMove(new AdvancedVector(0f, 0f), transform.getPosition().clone()));
        randomMoves();
        transform.setPosition(new AdvancedVector(0, 0));
        randomMoves();
        generatedMap = new HashMap<>(400);
    }

    //TODO DEBUG make private later
    public AdvancedTransform getTransform() {
        return transform;
    }

    private void randomMoves(Random r) {
        for (int i = 0; i < 15; i++) {
            int action = r.nextInt(2);
            switch (action) {
                case 0:
                    turnLeft(r.nextInt(180) + 1);
                    break;
                case 1:
                    turnRight(r.nextInt(180) + 1);
                    break;
            }
            forward(r.nextInt(11) + 5);
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
        TurtleInterpreter interpreter = new TurtleInterpreter(this, 3);

        return generatedMap;
    }

    private static StaticGraphicsComponent floor = new StaticGraphicsComponent(Resource.floor01);
    private static StaticGraphicsComponent wall = new StaticGraphicsComponent(Resource.wall01);

    void add(Vector vector, BlockID id) {

        WorldSpace worldSpace;
        if(id == BlockID.FLOOR){
            worldSpace = new Floor(new Transform(vector.clone()), floor);
        } else {
            PhysicsComponent p = new PhysicsComponent(40, 40);
            worldSpace = new Wall(new Transform(vector.clone()), wall, p);
        }

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


            for (TurtleMove move: positions) {
                addCorridor(move.getStartPos(), move.getEndPos());
            }
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
            //bresenhamLineInterpolate(vStart, vEnd);
            int thisCorridor = corridorWidth;
            int side = -1;
            double m = (vEnd.getY() - vStart.getY())/(double)(vEnd.getX() - vStart.getX());
            if(m <= 1 && m >= -1){
                for (int i = 0; i < corridorWidth; i++) {
                    superCoverLineInterpolation(vStart, vEnd, BlockID.FLOOR);
                    vStart.shift(0, 1);
                    vEnd.shift(0, 1);
                }
            } else {
                for (int i = 0; i < corridorWidth; i++) {
                    superCoverLineInterpolation(vStart, vEnd, BlockID.FLOOR);
                    vStart.shift(1, 0);
                    vEnd.shift(1, 0);
                }
            }
        }

        private void bresenhamLineInterpolate(Vector vStart, Vector vEnd, BlockID id){
            int deltaX = vEnd.getX() - vStart.getX();
            int deltaY = vEnd.getY() - vStart.getY();
            if (deltaX != 0) {
                float error = 0;
                float deltaError = Math.abs(deltaY / (float) deltaX); //Assuming deltaX != 0
                int y = vStart.getY();
                for (int x = vStart.getX(); x <= vEnd.getX(); x++) {
                    add(x, y, id);
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
                    add(x, y, id);
                }

            }
        }

        private void superCoverLineInterpolation(Vector vStart, Vector vEnd, BlockID id){
            int i;               // loop counter
            int ystep, xstep;    // the step on y and x axis
            double error;           // the error accumulated during the increment
            double errorprev;       // *vision the previous value of the error variable
            int y = vStart.getY();
            int x = vStart.getX();  // the line points
            double ddy, ddx;        // compulsory variables: the double values of dy and dx
            int dx = vEnd.getX() - vStart.getX();
            int dy = vEnd.getY() - vStart.getY();
            add(vStart.getX(), vStart.getY(), id);  // first point
            // NB the last point can't be here, because of its previous point (which has to be verified)
            if (dy < 0){
                ystep = -1;
                dy = -dy;
            }else
                ystep = 1;
            if (dx < 0){
                xstep = -1;
                dx = -dx;
            }else
                xstep = 1;
            ddy = 2 * dy;  // work with double values for full precision
            ddx = 2 * dx;
            if (ddx >= ddy){  // first octant (0 <= slope <= 1)
                // compulsory initialization (even for errorprev, needed when dx==dy)
                errorprev = error = dx;  // start in the middle of the square
                for (i=0 ; i < dx ; i++){  // do not use the first point (already done)
                    x += xstep;
                    error += ddy;
                    if (error > ddx){  // increment y if AFTER the middle ( > )
                        y += ystep;
                        error -= ddx;
                        // three cases (octant == right->right-top for directions below):
                        if (error + errorprev < ddx)  // bottom square also
                            add(x, y - ystep, id);
                        else if (error + errorprev > ddx)  // left square also
                            add(x - xstep, y, id);
                        else{  // corner: bottom and left squares also
                            add(x, y - ystep, id);
                            add(x - xstep, y, id);
                        }
                    }
                    add(x, y, id);
                    errorprev = error;
                }
            }else{  // the same as above
                errorprev = error = dy;
                for (i=0 ; i < dy ; i++){
                    y += ystep;
                    error += ddx;
                    if (error > ddy){
                        x += xstep;
                        error -= ddy;
                        if (error + errorprev < ddy)
                            add(x - xstep, y, id);
                        else if (error + errorprev > ddy)
                            add(x, y - ystep, id);
                        else{
                            add(x - xstep, y, id);
                            add(x, y - ystep, id);
                        }
                    }
                    add(x, y, id);
                    errorprev = error;
                }
            }
        }

        private void add(int x, int y, BlockID id){
            turtle.add(new Vector(x, y), id);
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
