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

    //TODO Set Icon Image
    private GameWindow() {

        Game game = Game.getInstance();
        add(Renderer.getInstance());
        pack();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        setBounds(300, 50, getWidth(), getHeight());
        setVisible(true);
        setMinimumSize(new Dimension(560, 560));
        setIconImage(Resource.weapon_melee_01_FLOOR.getImage());
    }
}
