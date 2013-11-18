package WIP;

/**
 * Created with IntelliJ IDEA.
 * User: Jean-Luc
 * Date: 09.11.13
 * Time: 18:48
 */
public enum Direction {
    NORTH(0, 1), SOUTH(0, -1), WEST(-1, 0), EAST(1, 0);

    private final int xDir, yDir;

    private Direction(int xDir, int yDir) {
        this.xDir = xDir;
        this.yDir = yDir;
    }

    public int getxDir() {
        return xDir;
    }

    public int getyDir() {
        return yDir;
    }
}
