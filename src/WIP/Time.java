package WIP;

/**
 * Created with IntelliJ IDEA.
 * User: Jean-Luc
 * Date: 20.01.14
 * Time: 13:40
 */
public class Time {
    private static long timeLastFrame = System.nanoTime() - 15000;
    private static long timeBetweenFrames;

    public static void update() {
        long timeThisFrame = System.nanoTime();
        timeBetweenFrames = timeThisFrame - timeLastFrame;
        timeLastFrame = timeThisFrame;
    }

    private static int roundToInt(long timeInNanoSeconds) {
        if (timeInNanoSeconds >= Integer.MAX_VALUE || timeInNanoSeconds <= Integer.MIN_VALUE) {
            DebugLog.write("Time.roundToInt called with timeInNanoSeconds = " + timeInNanoSeconds);
        }
        return (int) (timeInNanoSeconds / 1000000);
    }

    public static int deltaTime() {
        return roundToInt(timeBetweenFrames);
    }

    public static long getTimeStamp() {
        return System.currentTimeMillis();
    }
}
