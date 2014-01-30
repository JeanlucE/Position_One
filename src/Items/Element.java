package Items;

/**
 * Created with IntelliJ IDEA.
 * User: Jean-Luc
 * Date: 30.01.14
 * Time: 15:11
 */
public enum Element {
    FIRE("Fire"), WATER("Water"), LIGHTNING("Lightning");

    private final String name;

    private Element(String name) {
        this.name = name;
    }

    public String toString() {
        return name;
    }
}
