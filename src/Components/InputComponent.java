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
    //TODO put key typed actions in keyTyped method
    private static InputComponent instance;

    public static InputComponent getInstance() {
        if (instance == null) {
            instance = new InputComponent();
        }
        return instance;
    }

    private int XAxis = 0;
    private int YAxis = 0;
    private boolean SPACE_TYPED = false;
    private boolean SPACE_RELEASED = true;
    private boolean SHIFT_DOWN = false;
    private boolean Q_TYPED = false;
    private boolean Q_RELEASED = true;
    private boolean ESC_TYPED = false;
    private boolean I_TYPED = false;
    private boolean C_TYPED = false;
    private boolean M_TYPED = false;


    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {

        if (e.isShiftDown()) {
            SHIFT_DOWN = true;
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
                if (SPACE_RELEASED) {
                    SPACE_TYPED = true;
                    SPACE_RELEASED = false;

                }
                break;

            case KeyEvent.VK_Q:
                if (Q_RELEASED) {
                    Q_TYPED = true;
                    Q_RELEASED = false;
                }
                break;

            case KeyEvent.VK_ESCAPE:
                ESC_TYPED = true;
                break;

            case KeyEvent.VK_I:
                I_TYPED = true;
                break;

            case KeyEvent.VK_C:
                C_TYPED = true;
                break;

            case KeyEvent.VK_M:
                M_TYPED = true;
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (!e.isShiftDown() && SHIFT_DOWN) {
            SHIFT_DOWN = false;
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
                SPACE_RELEASED = true;
                break;

            case KeyEvent.VK_Q:
                Q_RELEASED = true;
                break;
        }
    }

    public boolean isShiftDown() {
        return SHIFT_DOWN;
    }

    public int getXAxis() {
        return XAxis;
    }

    public int getYAxis() {
        return YAxis;
    }

    public boolean isSpaceTyped() {
        return SPACE_TYPED;
    }

    public boolean isSpaceDown() {
        return !SPACE_RELEASED;
    }

    public boolean isQTyped() {
        return Q_TYPED;
    }

    public boolean isEscapeTyped() {
        return ESC_TYPED;
    }

    public boolean isI_TYPED() {
        return I_TYPED;
    }

    public boolean isC_TYPED() {
        return C_TYPED;
    }

    public boolean isM_TYPED() {
        return M_TYPED;
    }

    //Called every frame to reset typed Keys
    public void resetTypedKeys() {
        SPACE_TYPED = false;
        Q_TYPED = false;
        ESC_TYPED = false;
        I_TYPED = false;
        C_TYPED = false;
        M_TYPED = false;
    }
}
