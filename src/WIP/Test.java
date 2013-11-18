package WIP;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * Created with IntelliJ IDEA.
 * User: Jean-Luc
 * Date: 02.11.13
 * Time: 18:29
 */
public class Test {
    public static Scanner input;


    public static void main(String[] args) {
        File file = new File("images/player.png");
        System.out.println(file.exists());
    }

    static Position testPosition = new Position(0, 0);

    public Test() {
        input = new Scanner(System.in);
        try {
            DebugLog.instantiate();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
