package Components;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created with IntelliJ IDEA.
 * User: Jean-Luc
 * Date: 11.12.13
 * Time: 22:33
 */
public class InputComponent implements KeyListener {

    //If any focus problems occur try this on renderer: jPanel.requestFocusInWindow();

    private static InputComponent instance;

    public static InputComponent getInstance() {
        if (instance == null) {
            instance = new InputComponent();
        }
        return instance;
    }

    private int XAxis = 0;
    private int YAxis = 0;
    private boolean spaceTyped = false;
    private boolean spaceReleased = true;
    private boolean shiftDown = false;
    private boolean qTyped = false;
    private boolean qReleased = true;


    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {

        if (e.isShiftDown()) {
            shiftDown = true;
        }

        switch (e.getKeyCode()) {

            case KeyEvent.VK_W:
            case KeyEvent.VK_UP:
                if (YAxis != 1) {
                    YAxis = 1;
                }
                break;

            case KeyEvent.VK_S:
            case KeyEvent.VK_DOWN:
                if (YAxis != -1) {
                    YAxis = -1;
                }
                break;

            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
                if (XAxis != -1) {
                    XAxis = -1;
                }
                break;

            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
                if (XAxis != 1) {
                    XAxis = 1;
                }
                break;

            case KeyEvent.VK_SPACE:
                if (spaceReleased) {
                    spaceTyped = true;
                    spaceReleased = false;

                }
                break;

            case KeyEvent.VK_Q:
                if (qReleased) {
                    qTyped = true;
                    qReleased = false;
                }
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (!e.isShiftDown() && shiftDown) {
            shiftDown = false;
        }

        switch (e.getKeyCode()) {

            case KeyEvent.VK_W:
            case KeyEvent.VK_UP:
                YAxis = 0;
                break;

            case KeyEvent.VK_S:
            case KeyEvent.VK_DOWN:
                YAxis = 0;
                break;

            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
                XAxis = 0;
                break;

            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
                XAxis = 0;
                break;

            case KeyEvent.VK_SPACE:
                spaceReleased = true;
                break;

            case KeyEvent.VK_Q:
                qReleased = true;
                break;
        }
    }

    public boolean isShiftDown() {
        return shiftDown;
    }

    public int getXAxis() {
        return XAxis;
    }

    public int getYAxis() {
        return YAxis;
    }

    public boolean isSpaceTyped() {
        return spaceTyped;
    }

    public boolean isSpaceDown() {
        return !spaceReleased;
    }

    public boolean isQTyped() {
        return qTyped;
    }

    //Called every frame to reset typed Keys
    public void resetTypedKeys() {
        spaceTyped = false;
        qTyped = false;
    }
}
