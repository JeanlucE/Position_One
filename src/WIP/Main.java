package WIP;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Jean-Luc
 * Date: 07.08.13
 * Time: 20:39
 */
public class Main {
    public static void main(String[] args) {
        try {
            DebugLog.instantiate();
        } catch (IOException e) {
            e.printStackTrace();
        }
        DebugWindow.show();
        GameWindow.getInstance();

    }
}
