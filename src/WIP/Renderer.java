package WIP;

import Actors.Actor;
import Components.InputComponent;
import Components.MouseInputComponent;
import Components.PhysicsComponent;
import Environment.Floor;
import Environment.WorldSpace;
import Items.Inventory;
import Items.Item;
import Items.Projectile;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
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
    private static int screenWidth = 800, screenHeight = 600;
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
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                screenWidth = getWidth();
                screenHeight = getHeight();
                System.out.println(getWidth() + "|" + getScreenHeight());
                setPreferredSize(new Dimension(screenWidth, screenHeight));
            }
        });

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
        infoScreen.setPreferredSize(new Dimension(200, screenHeight));

        this.add(infoScreen, BorderLayout.EAST);
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
    }

    //Draws all items that are lying on the floor.
    private void drawItems(Map<Vector, WorldSpace> toRender) {
        for (Map.Entry<Vector, WorldSpace> e : toRender.entrySet()) {
            if (e.getValue() == null) continue;

            if (e.getValue().isFloor() && ((Floor) e.getValue()).hasDroppedItems()) {
                Item item = ((Floor) e.getValue()).peekTopItem();
                drawImage(item, e.getKey());
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
        for (Map.Entry<Actor, Vector> e : actors.entrySet()) {
            //Position
            Vector position = e.getValue();
            PhysicsComponent p = e.getKey().getCollider();
            g2d.drawString(e.getKey().getTransform().getPosition().toString(),
                    position.getX() - p.getWidth() / 2 - 5, screenHeight - (position.getY() - 32));
        }
    }

    private void drawActorColliders(Map<Actor, Vector> actors) {
        //Collider
        g2d.setColor(Color.RED);
        for (Map.Entry<Actor, Vector> e : actors.entrySet()) {
            Vector position = e.getValue();
            PhysicsComponent p = e.getKey().getCollider();
            g2d.drawRect(position.getX() - p.getWidth() / 2, screenHeight - (position.getY() + p.getHeight() / 2),
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

        private int size = Inventory.INVENTORYSIZE;
        private JLabel[] itemSlots = new ItemSlot[size];
        private JLabel weaponSlot;
        private JLabel offhandSlot;
        private JLabel helmetSlot;
        private JLabel bodySlot;
        private JLabel legsSlot;
        private JLabel ammunitionSlot;

        private InventoryPanel() {
            super("Inventory");
            setLayout(new BorderLayout());
            JPanel inventorySlots = new JPanel(new GridLayout(6, 4, 0, 0));

            for (int i = 0; i < size; i++) {
                itemSlots[i] = new ItemSlot(i, new Dimension(40, 40));
                inventorySlots.add(itemSlots[i]);
            }
            inventorySlots.setOpaque(false);
            add(inventorySlots, BorderLayout.NORTH);

            JPanel equipmentSlots = new JPanel();
            equipmentSlots.setBackground(Color.DARK_GRAY);
            equipmentSlots.setPreferredSize(new Dimension(100, 220));
            equipmentSlots.setLayout(new FlowLayout(FlowLayout.CENTER));
            weaponSlot = new ItemSlot(-1, new Dimension(60, 60));
            offhandSlot = new ItemSlot(-2, new Dimension(60, 60));
            helmetSlot = new ItemSlot(-3, new Dimension(60, 60));
            bodySlot = new ItemSlot(-4, new Dimension(60, 60));
            legsSlot = new ItemSlot(-5, new Dimension(60, 60));
            ammunitionSlot = new ItemSlot(-6, new Dimension(60, 60));
            equipmentSlots.add(weaponSlot);
            equipmentSlots.add(offhandSlot);
            equipmentSlots.add(helmetSlot);
            equipmentSlots.add(bodySlot);
            equipmentSlots.add(legsSlot);
            equipmentSlots.add(ammunitionSlot);
            add(equipmentSlots, BorderLayout.SOUTH);
        }

        @Override
        void update() {
            Actors.Character player = Game.getInstance().getPlayer();
            Inventory inventory = Game.getInstance().getPlayer().getInventory();
            for (int i = 0; i < 24; i++) {
                Item item = inventory.getItemAt(i);
                if (item != null) {
                    itemSlots[i].setText(item.getName());
                } else {
                    itemSlots[i].setText("");
                }
            }
            String weaponName = player.getMainHand() != null ? player.getMainHand().getName() : "";
            weaponSlot.setText(weaponName);
            String offhandName = player.getOffHand() != null ? player.getOffHand().getName() : "";
            offhandSlot.setText(offhandName);
            String helmetName = player.getHelmet() != null ? player.getHelmet().getName() : "";
            helmetSlot.setText(helmetName);
            String bodyName = player.getBody() != null ? player.getBody().getName() : "";
            bodySlot.setText(bodyName);
            String legsName = player.getLegs() != null ? player.getLegs().getName() : "";
            legsSlot.setText(legsName);
            String ammunitionName = player.getAmmunition() != null ? player.getAmmunition().getName() : "";
            ammunitionSlot.setText(ammunitionName);
        }

        private class ItemSlot extends JLabel {
            private ItemSlot(int num, Dimension d) {
                setPreferredSize(d);
                Border border = BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK);
                Border titleBorder = new TitledBorder(border, String.valueOf(num), TitledBorder.CENTER,
                        TitledBorder.BELOW_TOP, getFont(), Color.LIGHT_GRAY);
                setBorder(titleBorder);
                setForeground(Color.LIGHT_GRAY);
            }
        }
    }

    private class StatsPanel extends InfoPanel {
        private JLabel level;
        private JLabel skillPoints;
        private JLabel experience;
        private JLabel attack;
        private JLabel defense;
        private JLabel health;
        private JLabel mana;
        private JLabel stamina;
        private JLabel[] skills = new JLabel[8];


        private StatsPanel() {
            super("Stats");

            setLayout(new GridLayout(16, 1, 0, 3));

            Font font = CustomFont.SHERWOOD_Regular.deriveFont(14.0f);

            level = new JLabel();
            level.setForeground(Color.LIGHT_GRAY);
            level.setFont(font);
            add(level);

            skillPoints = new JLabel();
            skillPoints.setForeground(Color.LIGHT_GRAY);
            skillPoints.setFont(font);
            add(skillPoints);

            experience = new JLabel();
            experience.setForeground(Color.LIGHT_GRAY);
            experience.setFont(font);
            add(experience);

            health = new JLabel();
            health.setForeground(Color.LIGHT_GRAY);
            health.setFont(font);
            add(health);

            mana = new JLabel();
            mana.setForeground(Color.LIGHT_GRAY);
            mana.setFont(font);
            add(mana);

            stamina = new JLabel();
            stamina.setForeground(Color.LIGHT_GRAY);
            stamina.setFont(font);
            add(stamina);

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
            experience.setText("Experience: " + player.getCurrentXPProgress() + "/" + player.getXPOfNextLevel());
            health.setText("Health: " + player.getCurrentHealth() + "/" + player.getMaxHealth());
            mana.setText("Mana: " + player.getCurrentMana() + "/" + player.getMaxMana());
            stamina.setText("Stamina: " + player.getCurrentStamina() + "/" + player.getMaxStamina());
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
