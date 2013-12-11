package WIP;

/**
 * Created with IntelliJ IDEA.
 * User: Jean-Luc
 * Date: 09.11.13
 * Time: 21:34
 */
public class Transform implements Cloneable {
    private Vector position;
    private Vector direction;

    public Transform() {
        this(new Vector());
    }

    public Transform(Vector position) {
        this.position = position;
        direction = Vector.SOUTH;
    }

    public Transform(Vector position, Vector direction) {
        this.position = position;
        this.direction = direction;
    }

    public Vector getPosition() {
        return position;
    }

    public void setPosition(Vector position) {
        this.position = position;
    }

    public Vector getDirection() {
        return direction;
    }

    public void setDirection(Vector direction) {
        this.direction = direction;
    }

    public Transform clone() {
        return new Transform(new Vector(position.getX(), position.getY()), new Vector(direction.getX(),
                direction.getY()));
    }
}
