package WIP;

/**
 * Created with IntelliJ IDEA.
 * User: Jean-Luc
 * Date: 09.11.13
 * Time: 21:34
 */
public class Transform {
    //TODO transform should point to the position in the middle of the Gameobject
    //vectors?
    private Vector position;
    private Vector direction;

    public Transform() {
        this(new Vector());
    }

    public Transform(Vector position) {
        this.position = position;
        direction = Vector.SOUTH;
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
}
