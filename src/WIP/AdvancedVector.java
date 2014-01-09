package WIP;

/**
 * Created with IntelliJ IDEA.
 * User: Jean-Luc
 * Date: 16.12.13
 * Time: 17:30
 */
public class AdvancedVector implements Cloneable {
    private float x;
    private float y;

    public AdvancedVector() {
        x = 0f;
        y = 0f;
    }

    public AdvancedVector(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public AdvancedVector clone() {
        return new AdvancedVector(x, y);
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void shift(float x, float y) {
        this.x += x;
        this.y += y;
    }

    public AdvancedVector shiftedPosition(float x, float y) {
        return new AdvancedVector(this.getX() + x, this.getY() + y);
    }

    public void set(AdvancedVector newVector) {
        this.x = newVector.getX();
        this.y = newVector.getY();
    }

    public float length() {
        return (float) Math.sqrt(x * x + y * y);
    }

    public void normalize() {
        x = x / length();
        y = y / length();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AdvancedVector)) return false;

        AdvancedVector that = (AdvancedVector) o;

        if (Float.compare(that.x, x) != 0) return false;
        if (Float.compare(that.y, y) != 0) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (x != +0.0f ? Float.floatToIntBits(x) : 0);
        result = 31 * result + (y != +0.0f ? Float.floatToIntBits(y) : 0);
        return result;
    }

    @Override
    public String toString() {
        return x + "|" + y;
    }
}
