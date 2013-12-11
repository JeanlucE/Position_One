package Components;

import WIP.Game;
import WIP.Transform;
import WIP.Vector;

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
    private boolean shiftDown = false;
    private boolean qTyped = false;

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {

        if (e.isShiftDown() && !shiftDown) {
            shiftDown = true;
            System.out.println("Shift pressed");
        }

        Transform playerTransform = Game.getInstance().getPlayer().getTransform();
        switch (e.getKeyCode()) {

            case KeyEvent.VK_W:
            case KeyEvent.VK_UP:
                if (YAxis != 1) {
                    YAxis = 1;
                    playerTransform.setDirection(Vector.NORTH);
                    System.out.println("YAxis: " + YAxis);
                }
                break;

            case KeyEvent.VK_S:
            case KeyEvent.VK_DOWN:
                if (YAxis != -1) {
                    YAxis = -1;
                    playerTransform.setDirection(Vector.SOUTH);
                    System.out.println("YAxis: " + YAxis);
                }
                break;

            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
                if (XAxis != -1) {
                    XAxis = -1;
                    playerTransform.setDirection(Vector.WEST);
                    System.out.println("XAxis: " + XAxis);
                }
                break;

            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
                if (XAxis != 1) {
                    XAxis = 1;
                    playerTransform.setDirection(Vector.EAST);
                    System.out.println("XAxis: " + XAxis);
                }
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (!e.isShiftDown() && shiftDown) {
            shiftDown = false;
            System.out.println("Shift released");
        }

        switch (e.getKeyCode()) {

            case KeyEvent.VK_W:
            case KeyEvent.VK_UP:
                YAxis = 0;
                System.out.println("YAxis: " + YAxis);
                break;

            case KeyEvent.VK_S:
            case KeyEvent.VK_DOWN:
                YAxis = 0;
                System.out.println("YAxis: " + YAxis);
                break;

            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
                XAxis = 0;
                System.out.println("XAxis: " + XAxis);
                break;

            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
                XAxis = 0;
                System.out.println("XAxis: " + XAxis);
                break;

            case KeyEvent.VK_SPACE:
                spaceTyped = true;
                System.out.println("Space typed");
                break;
            case KeyEvent.VK_Q:
                qTyped = true;
                System.out.println("Ctrl typed");
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

    public boolean isqTyped() {
        return qTyped;
    }

    //Called every frame to reset typed Keys
    public void resetTypedKeys() {
        spaceTyped = false;
        qTyped = false;
    }

    /*private class APanel extends JPanel{
        public void paintComponent(Graphics g){
            resetTypedKeys();
            repaint();
        }
    }*/
}
