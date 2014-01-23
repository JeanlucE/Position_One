package Environment;

import WIP.AdvancedVector;
import WIP.DebugLog;
import WIP.Vector;

/**
 * Created with IntelliJ IDEA.
 * User: Jean-Luc
 * Date: 23.01.14
 * Time: 16:38
 */
class TurtleInterpreter {

    private Turtle turtle;
    private int corridorWidth;
    private int scale = 2;

    TurtleInterpreter(Turtle t, int corridorWidth) {
        this.turtle = t;
        this.corridorWidth = corridorWidth;

        interpret();
    }

    void interpret() {
        TurtleMove[] positions = turtle.getPositions();

        addCorridor(new AdvancedVector(0, 0), new AdvancedVector(0, 1));

        for (TurtleMove move : positions) {
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
        double m = (vEnd.getY() - vStart.getY()) / (double) (vEnd.getX() - vStart.getX());
        if (m <= 1 && m >= -1) {
            vStart.shift(0, -(corridorWidth / 2));
            vEnd.shift(0, -(corridorWidth / 2));
            for (int i = 0; i < corridorWidth; i++) {
                superCoverLineInterpolation(vStart, vEnd, BlockID.FLOOR);
                vStart.shift(0, 1);
                vEnd.shift(0, 1);
            }
        } else {
            vStart.shift(-(corridorWidth / 2), 0);
            vEnd.shift(-(corridorWidth / 2), 0);
            for (int i = 0; i < corridorWidth; i++) {
                superCoverLineInterpolation(vStart, vEnd, BlockID.FLOOR);
                vStart.shift(1, 0);
                vEnd.shift(1, 0);
            }
        }
    }

    //TODO FIX BUG: for high deltaY's the interpolation is not correct
    private void bresenhamLineInterpolate(Vector vStart, Vector vEnd, BlockID id) {
        int deltaX = vEnd.getX() - vStart.getX();
        int deltaY = vEnd.getY() - vStart.getY();
        if (deltaX != 0) {
            float error = 0;
            float m = Math.abs(deltaY / (float) deltaX); //Assuming deltaX != 0
            int y = vStart.getY();
            for (int x = vStart.getX(); x <= vEnd.getX(); x++) {
                add(x, y, id);
                error += m;
                while (error > 0.5f) {
                    y++;
                    if (y <= vEnd.getY()) {
                        add(x, y, id);
                    }
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

    private void superCoverLineInterpolation(Vector vStart, Vector vEnd, BlockID id) {
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
        if (dy < 0) {
            ystep = -1;
            dy = -dy;
        } else
            ystep = 1;
        if (dx < 0) {
            xstep = -1;
            dx = -dx;
        } else
            xstep = 1;
        ddy = 2 * dy;  // work with double values for full precision
        ddx = 2 * dx;
        if (ddx >= ddy) {  // first octant (0 <= slope <= 1)
            // compulsory initialization (even for errorprev, needed when dx==dy)
            errorprev = error = dx;  // start in the middle of the square
            for (i = 0; i < dx; i++) {  // do not use the first point (already done)
                x += xstep;
                error += ddy;
                if (error > ddx) {  // increment y if AFTER the middle ( > )
                    y += ystep;
                    error -= ddx;
                    // three cases (octant == right->right-top for directions below):
                    if (error + errorprev < ddx)  // bottom square also
                        add(x, y - ystep, id);
                    else if (error + errorprev > ddx)  // left square also
                        add(x - xstep, y, id);
                    else {  // corner: bottom and left squares also
                        add(x, y - ystep, id);
                        add(x - xstep, y, id);
                    }
                }
                add(x, y, id);
                errorprev = error;
            }
        } else {  // the same as above
            errorprev = error = dy;
            for (i = 0; i < dy; i++) {
                y += ystep;
                error += ddx;
                if (error > ddy) {
                    x += xstep;
                    error -= ddy;
                    if (error + errorprev < ddy)
                        add(x - xstep, y, id);
                    else if (error + errorprev > ddy)
                        add(x, y - ystep, id);
                    else {
                        add(x - xstep, y, id);
                        add(x, y - ystep, id);
                    }
                }
                add(x, y, id);
                errorprev = error;
            }
        }
    }

    protected void add(int x, int y, BlockID id) {
        turtle.add(new Vector(x, y), id);
    }

    protected Vector interpolatePosition(AdvancedVector v) {
        return new Vector(round(v.getX()) * scale, round(v.getY()) * scale);
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
