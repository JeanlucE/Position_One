package WIP;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created with IntelliJ IDEA.
 * User: Jean-Luc
 * Date: 28.01.14
 * Time: 21:56
 */
public class CustomButton extends JButton {

    CustomButton(final String str, final Action a) {
        super();
        for (MouseListener l : getMouseListeners()) {
            removeMouseListener(l);
        }

        setBackground(Color.DARK_GRAY);
        setForeground(Color.LIGHT_GRAY);
        setText(str);
        setFont(CustomFont.SHERWOOD_Regular);

        final Border buttonBorder = BorderFactory.createBevelBorder(BevelBorder.RAISED);
        final Border clickedBorder = BorderFactory.createBevelBorder(BevelBorder.LOWERED);
        this.setBorder(buttonBorder);

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                a.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
            }

            @Override
            public void mousePressed(MouseEvent e) {
                setBorder(clickedBorder);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                setBorder(buttonBorder);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                //setText("Hi");
            }

            @Override
            public void mouseExited(MouseEvent e) {
                //setText(str);
            }
        });
    }
}
