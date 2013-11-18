package Components;

import WIP.Direction;
import WIP.Game;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * Created with IntelliJ IDEA.
 * User: Jean-Luc
 * Date: 09.11.13
 * Time: 22:44
 */
public class InputComponent extends InputMap {

    private static InputComponent instance;
    private final ActionMap actionMap;
    private int XAxis, YAxis;

    public static InputComponent getInstance() {
        if (instance == null) {
            instance = new InputComponent();
        }
        return instance;
    }

    private InputComponent() {

        //W and UP ARROW control upward movement
        put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, false), "w_pressed");
        put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, true), "w_released");
        put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, false), "w_pressed");
        put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, true), "w_released");

        //S and DOWN ARROW control downward movement
        put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, false), "s_pressed");
        put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, true), "s_released");
        put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, false), "s_pressed");
        put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, true), "s_released");

        //A and LEFT ARROW control movement to the left
        put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, false), "a_pressed");
        put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, true), "a_released");
        put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, false), "a_pressed");
        put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, true), "a_released");

        //D and RIGHT ARROW control movement to the right
        put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, false), "d_pressed");
        put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, true), "d_released");
        put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, false), "d_pressed");
        put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, true), "d_released");

        //Space uses the equipped weapon
        put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, false), "space_pressed");
        put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, true), "space_released");

        actionMap = new ActionMap();

        translateUp = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                YAxis = +1;
                //getGame().getPlayer().setYVel(speed);
                getGame().getPlayer().getTransform().setDirection(Direction.NORTH);
            }
        };

        translateDown = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                YAxis = -1;
                //getGame().getPlayer().setYVel(-speed);
                getGame().getPlayer().getTransform().setDirection(Direction.SOUTH);
            }
        };

        translateLeft = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                XAxis = -1;
                //getGame().getPlayer().setXVel(-speed);
                getGame().getPlayer().getTransform().setDirection(Direction.WEST);
            }
        };

        translateRight = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                XAxis = +1;
                //getGame().getPlayer().setXVel(speed);
                getGame().getPlayer().getTransform().setDirection(Direction.EAST);
            }
        };

        stopX = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                XAxis = 0;
                //getGame().getPlayer().setXVel(0);
            }
        };
        stopY = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                YAxis = 0;
                //getGame().getPlayer().setYVel(0);
            }
        };

        //Map all keys defined above to the approriate actions
        actionMap.put("w_pressed", translateUp);
        actionMap.put("w_released", stopY);
        actionMap.put("s_pressed", translateDown);
        actionMap.put("s_released", stopY);
        actionMap.put("a_released", stopX);
        actionMap.put("a_pressed", translateLeft);
        actionMap.put("d_pressed", translateRight);
        actionMap.put("d_released", stopX);
    }

    public ActionMap getActionMap() {
        return actionMap;
    }

    public Game getGame() {
        return Game.getInstance();
    }

    public int getXAxis(){
        return XAxis;
    }

    public int getYAxis(){
        return YAxis;
    }

    private int speed = 3;

    private final Action translateUp, translateDown, translateLeft, translateRight;

    private final Action stopX, stopY;
}
