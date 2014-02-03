package WIP;

import Actors.Enemy;
import Environment.World;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created with IntelliJ IDEA.
 * User: Jean-Luc
 * Date: 02.02.14
 * Time: 21:52
 */
public class DebugWindow {

    private JRadioButton drawActorPositionsRadioButton;
    private JPanel panel1;
    private JRadioButton drawActorCollidersRadioButton;
    private JRadioButton enemiesAttackPlayerRadioButton;
    private JSlider slider1;
    private JLabel numberOfEnemiesLabel;
    private JSlider slider2;
    private JLabel enemySpawnTimeLabel;

    public DebugWindow() {
        drawActorCollidersRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Renderer.getInstance().DEBUG_DRAW_ACTOR_COLLIDERS = drawActorCollidersRadioButton.isSelected();
                DebugLog.write("Draw Actor Colliders set to " + drawActorCollidersRadioButton.isSelected(), true);
            }
        });

        drawActorPositionsRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Renderer.getInstance().DEBUG_DRAW_ACTOR_POSITIONS = drawActorPositionsRadioButton.isSelected();
                DebugLog.write("Draw Actor Positions set to " + drawActorPositionsRadioButton.isSelected(), true);
            }
        });
        enemiesAttackPlayerRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Enemy.DEBUG_ALL_ENEMIES_MOVE_TOWARD_PLAYER = enemiesAttackPlayerRadioButton.isSelected();
                DebugLog.write("Enemies move to player set to " + enemiesAttackPlayerRadioButton.isSelected(), true);
            }
        });
        slider1.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                World.getInstance().setMaxEnemies(slider1.getValue());
                numberOfEnemiesLabel.setText("Number of Enemies: " + slider1.getValue());
            }
        });
        slider2.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                World.getInstance().setEnemySpawnTime(slider2.getValue() * 1000);
                enemySpawnTimeLabel.setText("Enemy Spawn Time: " + slider2.getValue() + "s");
            }
        });
    }

    public static void show() {
        JFrame frame = new JFrame("DebugWindow");
        frame.setContentPane(new DebugWindow().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
