package WIP;

/**
 * Created with IntelliJ IDEA.
 * User: Jean-Luc
 * Date: 02.11.13
 * Time: 18:29
 */
public class Test {


    public static void main(String[] args) {
        long t0 = System.nanoTime();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long t1 = System.nanoTime();
        long t2 = t1 - t0;
        int millis = (int) (t2 / 1000000);
        System.out.println(t2 + "->" + millis);
    }


    public Test() {
    }
}
