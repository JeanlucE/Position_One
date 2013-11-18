package WIP;

/**
 * Created with IntelliJ IDEA.
 * User: Jean-Luc
 * Date: 09.11.13
 * Time: 21:34
 */
public class Transform {
    //transform should point to the position in the middle of the renderable
    private Position position;
    private Direction direction;

    public Transform() {
        this(new Position());
    }

    public Transform(Position position) {
        this.position = position;
        direction = Direction.SOUTH;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }
}
