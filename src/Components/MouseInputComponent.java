package Components;

import WIP.Renderer;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;

/**
 * Created with IntelliJ IDEA.
 * User: Jean-Luc
 * Date: 15.11.13
 * Time: 13:41
 */
public class MouseInputComponent extends MouseMotionAdapter implements MouseListener {

    private static MouseInputComponent instance = null;

    //Singleton design pattern
    public static MouseInputComponent getInstance() {
        if (instance == null) {
            instance = new MouseInputComponent();
        }
        return instance;
    }

    public void mouseMoved(MouseEvent e) {
        Point mousePos = Renderer.getInstance().getMousePosition();
    }

    //Fields providing state of mouse
    private boolean mouseInScreen = false;
    private boolean mousePressed = false;
    /*
    Mousehover is an integer that gives information on where the mouse is hovering.
    This can be used for hover areas in menus, where some action could happen when the mouse hovers over that area
    (e.g. it gets highlighted)
     */
    private int mouseHover = -1;

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        mousePressed = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mousePressed = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        mouseInScreen = true;
    }

    @Override
    public void mouseExited(MouseEvent e) {
        mouseInScreen = false;
    }

    //public void add(HoverArea hoverArea)

    public boolean isMouseInScreen() {
        return mouseInScreen;
    }

    public boolean isMousePressed() {
        return mousePressed;
    }

    public int getMouseHover() {
        return mouseHover;
    }
}
