package WIP;

/**
 * Created with IntelliJ IDEA.
 * User: Jean-Luc
 * Date: 30.09.13
 * Time: 15:55
 * <p/>
 * Coordinate System:
 * *                /\ +y
 * *                |
 * *                |
 * *                |
 * *                |
 * *                |
 * * <--------------0--------------->
 * * -x             |              +x
 * *                |
 * *                |
 * *                |
 * *                |
 * *                \/ -y
 */
public class Vector {

    public final static
    Vector NORTH, SOUTH, EAST, WEST;

    static {
        NORTH = new Vector(0, 1);
        SOUTH = new Vector(0, -1);
        EAST = new Vector(1, 0);
        WEST = new Vector(-1, 0);
    }

    private int x, y;

    public Vector() {
        x = 0;
        y = 0;
    }

    public Vector(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Vector clone() {
        return new Vector(x, y);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void shift(int x, int y) {
        this.x += x;
        this.y += y;
    }

    public Vector shiftedPosition(int x, int y) {
        return new Vector(this.x + x, this.getY() + y);
    }

    public void set(Vector newVector) {
        this.x = newVector.getX();
        this.y = newVector.getY();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vector)) return false;

        Vector position = (Vector) o;

        return x == position.x && y == position.y;

    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }

    public String toString() {
        return x + "|" + y;
    }
}
