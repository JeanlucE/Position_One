package WIP;

/**
 * Created with IntelliJ IDEA.
 * User: Jean-Luc
 * Date: 30.09.13
 * Time: 15:55
 * <p/>
 * Coordinate System:
 * /\
 * | y
 * |
 * |
 * |
 * |
 * <---------------0--------------->
 * -x             |              +x
 * |
 * |
 * |
 * | -y
 * \/
 */
public class Position{

    private int x, y;

    public Position() {
        x = 0;
        y = 0;
    }

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Position clone(){
        return new Position(x, y);
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

    public Position shiftedPosition(int x, int y) {
        return new Position(this.x + x, this.getY() + y);
    }

    public void set(Position newPosition) {
        this.x = newPosition.getX();
        this.y = newPosition.getY();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Position)) return false;

        Position position = (Position) o;

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
