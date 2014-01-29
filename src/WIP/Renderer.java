package WIP;

import Actors.Actor;
import Components.InputComponent;
import Components.MouseInputComponent;
import Components.PhysicsComponent;
import Environment.Floor;
import Environment.WorldSpace;
import Items.Item;
import Items.Projectile;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Jean-Luc
 * Date: 07.08.13
 * Time: 20:40
 * <p/>
 * This class handles anything that has to be drawn on the JPanel in the window.
 * The paintComponent method is overridden and then draws all enemies, dropped items, the player and the worldspaces.
 * It is also a Singleton which means there should only ever be one instance of this object.
 * Important Note:
 * JPanels in java use different coordinate systems than the world class.
 * Coordinate System:
 * *                /\ -y
 * *                |
 * *                |
 * *                |
 * *                |
 * *                |
 * * <--------------0--------------->
 * * -x             |              +x
 * *                |
 * *                |
 * *                |
 * *                |
 * *               \/ +y
 */
public class Renderer extends JPanel {

    private static Renderer instance = null;
    //This variable is very important as it determines the relative size of the world
    //TILESIZE determines how walls and floors are spaced, how far weapon ranges are and how big colliders are
    public final static int TILESIZE = 40;
    //This variable determines how wide and high the game screen is. This is important information for the camera to
    // know the clipping edges but is also important to calculate draw positions from world position.
    //FIXME rendering for non square game window
    private final static int screenWidth = 600, screenHeight = 600;
    private Graphics2D g2d;

    //DEBUGGING
    public boolean DEBUG_DRAW_ACTOR_POSITIONS = true;
    public boolean DEBUG_DRAW_ACTOR_COLLIDERS = true;

    //Singleton Design Pattern
    public static Renderer getInstance() {
        if (instance == null) {
            instance = new Renderer();
        }
        return instance;
    }

    private JPanel infoScreen;
    private CardLayout c;

    private InfoPanel inventoryPanel = new InventoryPanel();
    private InfoPanel statsPanel = new StatsPanel();
    private InfoPanel mapPanel = new MapPanel();

    private Renderer() {
        DebugLog.write("Renderer started");
        setPreferredSize(new Dimension(screenWidth, screenHeight));
        addMouseListener(MouseInputComponent.getInstance());
        addMouseMotionListener(MouseInputComponent.getInstance());
        setFocusable(true);
        //TODO test if there are any major changes
        setDoubleBuffered(true);
        addKeyListener(InputComponent.getInstance());

        setLayout(new BorderLayout());

        //Buttons for inventory, stats and map
        JPanel buttons = new JPanel();
        buttons.setLayout(new GridLayout(1, 3, 15, 0));
        Border empty = BorderFactory.createEmptyBorder(5, 60, 5, 60);
        buttons.setBorder(empty);
        buttons.setOpaque(false);
        CustomButton inventory = new CustomButton("Inventory [I]", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DebugLog.write("Inventory opened");
                Game.getInstance().toggleState(Game.GUIState.INVENTORY);
                requestFocus();
            }
        });
        CustomButton character = new CustomButton("Stats [C]", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Game.getInstance().toggleState(Game.GUIState.STATS);
                requestFocus();
            }
        });
        CustomButton map = new CustomButton("Map [M]", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Game.getInstance().toggleState(Game.GUIState.MAP);
                requestFocus();
            }
        });
        buttons.add(inventory, 0);
        buttons.add(character, 1);
        buttons.add(map, 2);

        add(buttons, BorderLayout.SOUTH);

        //Panels for inventory, stats and map
        infoScreen = new JPanel();
        infoScreen.setBackground(Color.WHITE);

        c = new CardLayout();
        infoScreen.setLayout(c);

        infoScreen.add(inventoryPanel, "inventory");
        infoScreen.add(statsPanel, "stats");
        infoScreen.add(mapPanel, "map");
        infoScreen.setVisible(false);
        infoScreen.setPreferredSize(new Dimension(150, screenHeight));

        this.add(infoScreen, BorderLayout.EAST);

        //infoScreen.setPreferredSize(new Dimension(75, 400));
    }

    public static int getScreenWidth() {
        return screenWidth;
    }

    public static int getScreenHeight() {
        return screenHeight;
    }

    public void paintComponent(Graphics g) {

        g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        drawGame();

        MouseInputComponent mouseListener = MouseInputComponent.getInstance();
        if (mouseListener.isMouseInScreen()) {
            g2d.setColor(Color.CYAN);
            g2d.fillRect(10, 10, 10, 10);
        }

        if (mouseListener.isMousePressed()) {
            g2d.setColor(Color.GREEN);
            g2d.fillRect(30, 10, 10, 10);
        }

        g2d.setColor(Color.WHITE);
        g2d.drawString(Game.getInstance().getGuiState().name(), screenWidth - 55, screenHeight - 10);

        GameWindow.getInstance().setTitle("Frames:" + String.valueOf(Game.getInstance().getFrameRate()));
    }

    private void drawGame() {
        Map<Vector, WorldSpace> toRender = Camera.getInstance().worldToRender();

        drawWorld(toRender);
        drawItems(toRender);

        drawProjectiles();

        Map<Actor, Vector> actorPositionMap = Camera.getInstance().actorsToRender();
        drawActors(actorPositionMap);

        drawPlayerManaAndStamina();

        if (DEBUG_DRAW_ACTOR_POSITIONS) {
            drawActorPositions(actorPositionMap);
        }

        if (DEBUG_DRAW_ACTOR_COLLIDERS) {
            drawActorColliders(actorPositionMap);
        }

        drawGUI();
    }

    private void drawPlayerManaAndStamina() {
        int xOffset = -14;

        Actors.Character player = Game.getInstance().getPlayer();
        PhysicsComponent phys = player.getCollider();

        g2d.setColor(Color.RED);
        g2d.fillRect(screenWidth / 2 + xOffset,
                screenHeight - (phys.getHeight() / 2 + screenHeight / 2 + 14),
                30, 5);
        g2d.setColor(Color.BLUE);
        g2d.fillRect(screenWidth / 2 + xOffset,
                screenHeight - (phys.getHeight() / 2 + screenHeight / 2 + 14),
                (int) (30 * player.getManaPercentage()), 5);

        g2d.setColor(Color.RED);
        g2d.fillRect(screenWidth / 2 + xOffset,
                screenHeight - (phys.getHeight() / 2 + screenHeight / 2 + 21),
                30, 5);
        g2d.setColor(new Color(0xFFE41C));
        g2d.fillRect(screenWidth / 2 + xOffset,
                screenHeight - (phys.getHeight() / 2 + screenHeight / 2 + 21),
                (int) (30 * player.getStaminaPercentage()), 5);
    }

    /*
    Draws the sprite of the given gameobject at a given position.
    This position must be a draw coordinate.
     */
    private void drawImage(GameObject go, Vector position) {
        if (go != null) {
            BufferedImage bf = go.getGraphic().getImage();
            g2d.drawImage(bf, position.getX(), screenHeight - (bf.getHeight() + position.getY()),
                    this);
        } else {
            g2d.setColor(Color.RED);
            g2d.fillRect(position.getX(), screenHeight - (TILESIZE + position.getY()), TILESIZE, TILESIZE);
        }
    }

    private void drawImage(GameObject go, Vector position, int width, int height) {
        if (go != null) {
            BufferedImage bf = go.getGraphic().getImage();
            PhysicsComponent p = ((Collidable) go).getCollider();
            g2d.drawImage(bf,
                    position.getX() - p.getWidth() / 2,
                    screenHeight - (position.getY() + p.getHeight() / 2),
                    width, height, this);
        } else {
            g2d.setColor(Color.RED);
            g2d.fillRect(position.getX(), screenHeight - (TILESIZE + position.getY()), TILESIZE, TILESIZE);
        }
    }

    //Gets all worldspaces that are visible to the camera and renders these
    private void drawWorld(Map<Vector, WorldSpace> toRender) {
        for (Map.Entry<Vector, WorldSpace> e : toRender.entrySet()) {
            WorldSpace w = e.getValue();
            Vector v = e.getKey();
            drawImage(w, v);
        }
    }

    private void drawActors(Map<Actor, Vector> actorPositionMap) {
        for (Map.Entry<Actor, Vector> e : actorPositionMap.entrySet()) {
            Vector drawPosition = e.getValue();
            PhysicsComponent phys = e.getKey().getCollider();
            drawImage(e.getKey(), drawPosition, phys.getWidth(), phys.getHeight());

        }

        //Draws all health bars
        for (Map.Entry<Actor, Vector> e : actorPositionMap.entrySet()) {
            Vector drawPosition = e.getValue();
            drawHealthBar(e.getKey(), drawPosition);
        }
    }

    //Draws the health bar of a single actor
    private void drawHealthBar(Actor actor, Vector drawPosition) {
        int xOffset = -14;
        int yOffset = 7;
        PhysicsComponent phys = actor.getCollider();
        g2d.setColor(Color.RED);
        g2d.fillRect(drawPosition.getX() + xOffset,
                screenHeight - (phys.getHeight() / 2 + drawPosition.getY() + yOffset),
                30, 5);
        g2d.setColor(Color.GREEN);
        g2d.fillRect(drawPosition.getX() + xOffset,
                screenHeight - (phys.getHeight() / 2 + drawPosition.getY() + yOffset),
                (int) (30 * actor.getHealthPercentage()), 5);
    }

    private void drawProjectiles() {
        Map<Projectile, Vector> projectiles = Camera.getInstance().projectilesToRender();
        for (Map.Entry<Projectile, Vector> e : projectiles.entrySet()) {
            Vector drawPosition = e.getValue();
            PhysicsComponent phys = e.getKey().getCollider();
            drawImage(e.getKey(), drawPosition, phys.getWidth(), phys.getHeight());
        }
        //drawImage(p, projectiles.get(p));
    }

    //Draws all items that are lying on the floor.
    private void drawItems(Map<Vector, WorldSpace> toRender) {
        for (Map.Entry e : toRender.entrySet()) {
            if (e.getValue() == null) continue;

            if (((WorldSpace) e.getValue()).isFloor() && ((Floor) e.getValue()).hasDroppedItems()) {
                Item[] items = ((Floor) e.getValue()).getDroppedItems();
                drawImage(items[0], (Vector) e.getKey());
            }
        }

    }

    private void drawGUI() {
        Game.GUIState g = Game.getInstance().getGuiState();

        if (Game.getInstance().isPaused()) {
            infoScreen.setVisible(false);
            return;
        }

        if (g != Game.GUIState.GAME) {
            infoScreen.setVisible(true);
            if (g == Game.GUIState.INVENTORY) {
                inventoryPanel.update();
                c.show(infoScreen, "inventory");
            } else if (g == Game.GUIState.STATS) {
                statsPanel.update();
                c.show(infoScreen, "stats");
            } else if (g == Game.GUIState.MAP) {
                mapPanel.update();
                c.show(infoScreen, "map");
            }
        } else {
            infoScreen.setVisible(false);
        }
    }

    //DEBUGGING draws player position and collider
    private void drawActorPositions(Map<Actor, Vector> actors) {
        g2d.setColor(Color.RED);
        for (Actor a : actors.keySet()) {
            //Position
            Vector position = actors.get(a);
            PhysicsComponent p = a.getCollider();
            g2d.drawString(a.getTransform().getPosition().toString(),
                    position.getX() - p.getWidth() / 2 - 5, screenWidth - (position.getY() - 32));
        }
    }

    private void drawActorColliders(Map<Actor, Vector> actors) {
        //Collider
        g2d.setColor(Color.RED);
        for (Actor a : actors.keySet()) {
            Vector position = actors.get(a);
            PhysicsComponent p = a.getCollider();
            g2d.drawRect(position.getX() - p.getWidth() / 2, screenWidth - (position.getY() + p.getHeight() / 2),
                    p.getWidth(), p.getHeight());
        }
    }

    private abstract class InfoPanel extends JPanel {
        private InfoPanel(String title) {
            setBackground(Color.DARK_GRAY);
            setForeground(Color.LIGHT_GRAY);

            setLayout(new BorderLayout());

            Border lineBorder = BorderFactory.createLineBorder(Color.LIGHT_GRAY, 3, true);
            Border titledBorder = BorderFactory.createTitledBorder(lineBorder, title,
                    TitledBorder.CENTER, TitledBorder.CENTER,
                    CustomFont.SHERWOOD_Regular.deriveFont(20.0f), Color.LIGHT_GRAY);
            Border emptyBorder = BorderFactory.createEmptyBorder(3, 3, 3, 3);
            Border compoundBorder = BorderFactory.createCompoundBorder(emptyBorder, titledBorder);
            this.setBorder(compoundBorder);
        }

        abstract void update();
    }

    private class InventoryPanel extends InfoPanel {

        private InventoryPanel() {
            super("Inventory");
        }

        @Override
        void update() {

        }
    }

    private class StatsPanel extends InfoPanel {
        private JLabel level;
        private JLabel skillPoints;
        private JLabel[] skills = new JLabel[8];
        private JLabel attack;
        private JLabel defense;

        private StatsPanel() {
            super("Stats");

            setLayout(new GridLayout(12, 1, 0, 3));

            Font font = CustomFont.SHERWOOD_Regular.deriveFont(14.0f);

            level = new JLabel();
            level.setForeground(Color.LIGHT_GRAY);
            level.setFont(font);
            add(level);

            skillPoints = new JLabel();
            skillPoints.setForeground(Color.LIGHT_GRAY);
            skillPoints.setFont(font);
            add(skillPoints);

            attack = new JLabel();
            attack.setForeground(Color.LIGHT_GRAY);
            attack.setFont(font);
            add(attack);

            defense = new JLabel();
            defense.setForeground(Color.LIGHT_GRAY);
            defense.setFont(font);
            add(defense);

            for (int i = 0; i < skills.length; i++) {
                skills[i] = new JLabel();
                skills[i].setForeground(Color.LIGHT_GRAY);
                skills[i].setFont(font);
                add(skills[i]);
            }
        }

        void update() {
            Actors.Character player = Game.getInstance().getPlayer();
            level.setText("Player level: " + player.getLevel());
            skillPoints.setText("Skill points: " + player.getSkillPoints());
            attack.setText("Attack: " + player.getAttack());
            defense.setText("Defense: " + player.getDefense());
            String[] strings = player.getSkills();
            for (int i = 0; i < strings.length; i++) {
                skills[i].setText(strings[i]);
            }
        }
    }

    private class MapPanel extends InfoPanel {
        private MapPanel() {
            super("Map");
        }

        @Override
        void update() {

        }
    }
}
