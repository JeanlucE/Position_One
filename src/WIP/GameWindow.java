package WIP;

import Components.Resource;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: Jean-Luc
 * Date: 07.08.13
 * Time: 20:39
 */
class GameWindow extends JFrame {
    private static GameWindow instance = null;

    //Singleton Design Pattern
    static GameWindow getInstance() {
        if (instance == null) {
            instance = new GameWindow();
        }
        return instance;

    }

    private GameWindow() {
        Game.getInstance();
        add(Renderer.getInstance());
        setSize(Renderer.getScreenWidth() + 16, Renderer.getScreenHeight() + 38);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        setBounds(300, 50, getWidth(), getHeight());
        setVisible(true);
        setMinimumSize(new Dimension(560, 560));
        setIconImage(Resource.player_SOUTH.getImage());
    }
}
