package WIP;

import javax.swing.*;

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
        Game game = Game.getInstance();
        add(Renderer.getInstance());
        pack();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(100, 100, getWidth(), getHeight());
        setVisible(true);

        System.out.println(this.getWidth() + "|" + this.getHeight());
        System.out.println(game.getRenderer().getWidth() + "|" + game.getRenderer().getHeight());
    }
}
