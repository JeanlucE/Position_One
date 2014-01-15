package WIP;

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
        int x = -40;
        int y = -40;
        int xReal = (x >= 0) ? (x / 40) : ((x >= -39 && x <= -1) ? -1 : (x / 40 - 1));
        int yReal = (y >= 0) ? (y / 40) : (y / 40 - 1);
        System.out.println(xReal + ", " + yReal);
    }


    public Test() {
    }
}
