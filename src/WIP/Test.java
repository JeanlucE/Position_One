package WIP;

import Components.Resource;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: Jean-Luc
 * Date: 02.11.13
 * Time: 18:29
 */
public class Test {


    public static void main(String[] args) {
        JFrame app = new JFrame();
        app.setIgnoreRepaint(true);
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Canvas canvas = new Canvas();
        canvas.setIgnoreRepaint(true);
        canvas.setSize(640, 480);

        app.add(canvas);
        app.pack();
        app.setVisible(true);


        canvas.createBufferStrategy(2);

        Graphics graphics = null;
        int frames = 0;
        String framerate = "";
        long timeStamp = Time.getTimeStamp();
        while (true) {
            try {

                // clear back buffer...

                graphics = canvas.getBufferStrategy().getDrawGraphics();
                graphics.setColor(Color.BLACK);
                graphics.fillRect(0, 0, 639, 479);

                //Draw stuff
                graphics.setColor(Color.WHITE);
                graphics.drawString(framerate, 10, 10);
                graphics.setColor(Color.BLUE);
                graphics.drawImage(Resource.player_SOUTH.getImage(), 100, 100, 30, 40, canvas);

                // blit the back buffer to the screen
                if (!canvas.getBufferStrategy().contentsLost())
                    canvas.getBufferStrategy().show();

                // Let the OS have a little time...

                Thread.yield();

            } finally {

                if (graphics != null)
                    graphics.dispose();

            }

            frames++;

            if (Time.getTimeStamp() - timeStamp >= 1000) {
                framerate = String.valueOf(frames);
                timeStamp = Time.getTimeStamp();
                frames = 0;
            }
        }
    }

    public static int getXPToNextLevel(int level) {
        return xpThreshold(level + 1) - xpThreshold(level);
    }

    //TODO approximate level xp function with exponential xp growth, but it must not be too steep
    private static int xpThreshold(int level) {
        return level * level * level * 20;
    }


    public Test() {
    }
}
