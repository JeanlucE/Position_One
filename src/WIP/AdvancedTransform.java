package WIP;

/**
 * Created with IntelliJ IDEA.
 * User: Jean-Luc
 * Date: 16.12.13
 * Time: 17:29
 */
public class AdvancedTransform implements Cloneable {
    private AdvancedVector position;
    private AdvancedVector direction;

    public AdvancedTransform() {
        position = new AdvancedVector(0f, 0f);
        direction = new AdvancedVector(0f, 1f);
    }

    public AdvancedTransform(AdvancedVector position) {
        this.position = position;
    }

    public AdvancedTransform(AdvancedVector position, AdvancedVector direction) {
        this.position = position;
        this.direction = direction;
    }

    public AdvancedVector getPosition() {
        return position;
    }

    public void setPosition(AdvancedVector position) {
        this.position = position;
    }

    public AdvancedVector getDirection() {
        return direction;
    }

    public void setDirection(AdvancedVector direction) {
        this.direction = direction;
    }

    @Override
    public AdvancedTransform clone() {
        AdvancedVector newPosition = position.clone();
        AdvancedVector newDirection = direction.clone();
        return new AdvancedTransform(newPosition, newDirection);
    }
}
