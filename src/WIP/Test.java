package WIP;

/**
 * Created with IntelliJ IDEA.
 * User: Jean-Luc
 * Date: 02.11.13
 * Time: 18:29
 */
public class Test {


    public static void main(String[] args) {
        System.out.println(nearestPosition(-100));
    }

    public static int getXPToNextLevel(int level) {
        return xpThreshold(level + 1) - xpThreshold(level);
    }

    //TODO approximate level xp function with exponential xp growth, but it must not be too steep
    private static int xpThreshold(int level) {
        return level * level * level * 20;
    }

    private static int nearestPosition(int x) {
        return (x >= 0) ? (x - x % Renderer.TILESIZE)
                : (x - x % Renderer.TILESIZE - Renderer.TILESIZE);
    }

    public Test() {
    }
}
