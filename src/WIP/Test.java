package WIP;

import Environment.Turtle;
import Environment.WorldSpace;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Jean-Luc
 * Date: 02.11.13
 * Time: 18:29
 */
public class Test {


    public static void main(String[] args) {
        /*Turtle t = new Turtle();
        Map<Vector, WorldSpace> map = t.getGeneratedMap();
        for (Vector v : map.keySet()) {
            System.out.println(v + ", " + map.get(v).toString());
        }  */
        //System.out.println(Arrays.toString(t.getPositions()));
        //System.out.println(Math.round(1.5f));
        int x= -39;
        int y = -39;
        int xReal = (x >= 0) ? (x - x % Renderer.TILESIZE) : x - x % Renderer.TILESIZE - Renderer.TILESIZE;
        int yReal = (y >= 0) ? (y - y % Renderer.TILESIZE) : y - y % Renderer.TILESIZE - Renderer.TILESIZE;
        System.out.println(xReal + ", " + yReal);
    }


    public Test() {
    }
}
