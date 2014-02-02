package WIP;

/**
 * Created with IntelliJ IDEA.
 * User: Jean-Luc
 * Date: 02.11.13
 * Time: 18:29
 */
public class Test {


    public static void main(String[] args) {
        g(-6, -6);
    }

    public static int getXPToNextLevel(int level) {
        return xpThreshold(level + 1) - xpThreshold(level);
    }

    //TODO approximate level xp function with exponential xp growth, but it must not be too steep
    private static int xpThreshold(int level) {
        return level * level * level * 20;
    }

    private static void f(int x, int y) {
        int xMove = x;
        int yMove = y;
        while (xMove != 0 || yMove != 0) {
            if (Math.abs(xMove) >= Math.abs(yMove)) {
                if (yMove > 0) {
                    yMove--;
                } else if (yMove < 0) {
                    yMove++;
                }
            } else {
                if (xMove > 0) {
                    xMove--;
                } else if (xMove < 0) {
                    xMove++;
                }
            }
            System.out.println(xMove + "|" + yMove);
        }

    }

    private static void g(int xMove, int yMove) {
        while (!(xMove == 0 && yMove == 0)) {

            if (yMove == 0 || Math.abs(xMove) > Math.abs(yMove)) {
                if (xMove > 0) {
                    xMove--;
                } else {
                    xMove++;
                }
            } else {
                if (yMove > 0) {
                    yMove--;
                } else {
                    yMove++;
                }
            }
        }

    }

    public Test() {
    }
}
