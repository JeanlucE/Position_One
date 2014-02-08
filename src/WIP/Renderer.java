package WIP;

import Actors.Actor;
import Components.InputComponent;
import Components.MouseInputComponent;
import Components.PhysicsComponent;
import Components.Resource;
import Environment.Floor;
import Environment.WorldSpace;
import Items.Inventory;
import Items.Item;
import Items.Projectile;
import Items.Stackable;

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
//TODO make a unified drawImage method which handles all gameobject specific checks,
// - possibly put some flags into Graphicscomponent
// - possibly subclass Graphics2D
public class Renderer extends JPanel {

    private static Renderer instance = null;
    //This variable is very important as it determines the relative size of the world
    //TILESIZE determines how walls and floors are spaced, how far weapon ranges are and how big colliders are
    public final static int TILESIZE = 40;
    //This variable determines how wide and high the game screen is. This is important information for the camera to
    // know the clipping edges but is also important to calculate draw positions from world position.
    private static int screenWidth = 800;
    private static int screenHeight = 600;
    private Graphics2D g2d;

    //DEBUGGING
    public boolean DEBUG_DRAW_ACTOR_POSITIONS = false;
    public boolean DEBUG_DRAW_ACTOR_COLLIDERS = false;
    private Color nullColor = Color.DARK_GRAY;

    //Singleton Design Pattern
    public static Renderer getInstance() {
        if (instance == null) {
            instance = new Renderer();
        }
        return instance;
    }

    private JPanel infoScreen;
    private CardLayout c;

    private RoundedInfoPanel inventoryPanel = new InventoryPanel();
    private RoundedInfoPanel statsPanel = new StatsPanel();
    private RoundedInfoPanel mapPanel = new MapPanel();
    private RoundedInfoPanel pausePanel = new PausePanel();

    private Renderer() {
        setPreferredSize(new Dimension(screenWidth, screenHeight));
        addMouseListener(MouseInputComponent.getInstance());
        addMouseMotionListener(MouseInputComponent.getInstance());
        setFocusable(true);
        //TODO test if there are any major changes
        //setDoubleBuffered(true);
        addKeyListener(InputComponent.getInstance());
        setLayout(new BorderLayout());
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                screenWidth = getWidth();
                screenHeight = getHeight();
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
        infoScreen.setOpaque(false);

        c = new CardLayout();
        infoScreen.setLayout(c);

        infoScreen.add(inventoryPanel, "inventory");
        infoScreen.add(statsPanel, "stats");
        infoScreen.add(mapPanel, "map");
        infoScreen.add(pausePanel, "pause");
        infoScreen.setVisible(false);

        JPanel paddingPanel = new JPanel(null);
        paddingPanel.setOpaque(false);
        paddingPanel.add(infoScreen);

        this.add(paddingPanel, BorderLayout.CENTER);
        DebugLog.write("Renderer started");
    }

    public static int getScreenWidth() {
        return screenWidth;
    }

    public static int getScreenHeight() {
        return screenHeight;
    }

    private long lastTime = Time.getTimeStamp();

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
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

        long currentTime = Time.getTimeStamp();
        if (currentTime - lastTime >= 1000) {
            ((JFrame) SwingUtilities.getWindowAncestor(this)).setTitle("Frames:" + String.valueOf(Game.getInstance()
                    .getFrameRate()) + " " + "Memory:" + String.valueOf(Game.getInstance().getCurrentMemory()) + "MB");
            lastTime = currentTime;
        }

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

    /**
     * Resizes a Dimension to a boundary, keeping aspect ratio
     *
     * @param boundary Boundary to resize image to
     * @return Returns original image resized to fit in boundary
     */
    public Image getScaledImage(Image original, Dimension boundary) {

        int original_width = original.getWidth(this);
        int original_height = original.getHeight(this);
        int bound_width = boundary.width;
        int bound_height = boundary.height;
        int new_width = original_width;
        int new_height = original_height;

        // first check if we need to scale width
        if (original_width < bound_width) {
            //scale width to fit
            new_width = bound_width;
            //scale height to maintain aspect ratio
            new_height = (new_width * original_height) / original_width;
        }

        // then check if we need to scale even with the new height
        if (new_height < bound_height) {
            //scale height to fit instead
            new_height = bound_height;
            //scale width to maintain aspect ratio
            new_width = (new_height * original_width) / original_height;
        }
        return original.getScaledInstance(new_width, new_height,
                Image.SCALE_DEFAULT);
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
            g2d.setColor(nullColor);
            g2d.fillRect(position.getX(), screenHeight - (TILESIZE + position.getY()), TILESIZE, TILESIZE);
        }
    }

    private void drawImage_ASPECT(GameObject go, Vector position, int width, int height) {
        BufferedImage bf = go.getGraphic().getImage();
        Dimension boundary = new Dimension(width, height);
        Image scaledImage = getScaledImage(bf, boundary);
        g2d.drawImage(scaledImage,
                position.getX() - scaledImage.getWidth(this) / 2,
                screenHeight - (position.getY() + scaledImage.getHeight(this) / 2), this);
    }

    private void drawImage_COLLIDER(GameObject go, Vector position, int width, int height) {
        BufferedImage bf = go.getGraphic().getImage();
        PhysicsComponent p = ((Collidable) go).getCollider();
        g2d.drawImage(bf,
                position.getX() - p.getWidth() / 2,
                screenHeight - (position.getY() + p.getHeight() / 2),
                width, height, this);

    }

    private void drawImage(GameObject go, Vector centerOfImage, int width, int height, boolean keepAspect) {
        if (go != null) {
            if (keepAspect)
                drawImage_ASPECT(go, centerOfImage, width, height);
            else
                drawImage_COLLIDER(go, centerOfImage, width, height);
        } else {
            g2d.setColor(nullColor);
            g2d.fillRect(centerOfImage.getX(), screenHeight - (TILESIZE + centerOfImage.getY()), TILESIZE, TILESIZE);
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
            if (e.getKey().equals(Game.getInstance().getPlayer()))
                drawImage(e.getKey(), drawPosition, phys.getWidth(), phys.getHeight(), true);
            else
                drawImage(e.getKey(), drawPosition, phys.getWidth(), phys.getHeight(), false);

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

    private void drawProjectiles() {
        Map<Projectile, Vector> projectiles = Camera.getInstance().projectilesToRender();
        for (Map.Entry<Projectile, Vector> e : projectiles.entrySet()) {
            Vector drawPosition = e.getValue();
            PhysicsComponent phys = e.getKey().getCollider();
            drawImage_ASPECT(e.getKey(), drawPosition, phys.getWidth(), phys.getHeight());
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

        if (g != Game.GUIState.GAME) {
            int width = 100;
            int height = 100;
            boolean centered = false;
            if (g == Game.GUIState.PAUSE_MENU) {
                c.show(infoScreen, "pause");
                width = 200;
                height = 250;
                centered = true;
            } else if (g == Game.GUIState.INVENTORY) {
                inventoryPanel.update();
                c.show(infoScreen, "inventory");
                width = 250;
                height = 500;
                centered = false;
            } else if (g == Game.GUIState.STATS) {
                statsPanel.update();
                c.show(infoScreen, "stats");
                width = 200;
                height = 400;
                centered = false;
            } else if (g == Game.GUIState.MAP) {
                mapPanel.update();
                c.show(infoScreen, "map");
                width = 400;
                height = 400;
                centered = true;
            }
            if (centered) {
                infoScreen.setBounds(screenWidth / 2 - width / 2, screenHeight / 2 - height / 2, width, height);
            } else {
                infoScreen.setBounds(screenWidth - width - 10, screenHeight / 2 - height / 2, width, height);
            }
            infoScreen.setVisible(true);
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
            g2d.drawRect(position.getX() - p.getWidth() / 2,
                    screenHeight - (position.getY() + p.getHeight() / 2) - 1,
                    //-1 because rectangles are drawn directly at given coordinates
                    p.getWidth(), p.getHeight());
        }
    }

    /**
     * Info panel that is used as a superclass for the inventory, character screen and map screen
     * It also has a rounded border which uses source code from here:
     * http://www.codeproject.com/Articles/114959/Rounded-Border-JPanel-JPanel-graphics-improvements
     */
    private abstract class RoundedInfoPanel extends JPanel {

        /**
         * Stroke size. it is recommended to set it to 1 for better view
         */
        private int strokeSize = 3;
        /**
         * Double values for Horizontal and Vertical radius of corner arcs
         */
        private Dimension arcs = new Dimension(30, 30);

        private RoundedInfoPanel(String title) {
            setBackground(Color.DARK_GRAY);
            setForeground(Color.LIGHT_GRAY);

            setLayout(new BorderLayout());
            Border emptyBorder = BorderFactory.createEmptyBorder(30, 9, 9, 9);
            Border titledBorder = BorderFactory.createTitledBorder(emptyBorder, title,
                    TitledBorder.CENTER, TitledBorder.CENTER,
                    CustomFont.SHERWOOD_Regular.deriveFont(20.0f), Color.LIGHT_GRAY);
            Border anotherEmptyBorder = BorderFactory.createEmptyBorder(5, 0, 0, 0);
            Border compoundBorder = BorderFactory.createCompoundBorder(titledBorder, anotherEmptyBorder);
            this.setBorder(compoundBorder);

            setOpaque(false);
        }

        abstract void update();

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            int width = getWidth();
            int height = getHeight();
            Graphics2D graphics = (Graphics2D) g;
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            //Draws the rounded opaque panel with borders.
            graphics.setColor(getBackground());
            graphics.fillRoundRect(0, 0, width, height, arcs.width, arcs.height);
            graphics.setColor(getForeground());
            graphics.setStroke(new BasicStroke(strokeSize));
            graphics.drawRoundRect(0, 0, width - 1, height - 1, arcs.width, arcs.height);

            //Sets strokes to default, is better.
            graphics.setStroke(new BasicStroke());
        }
    }

    private class InventoryPanel extends RoundedInfoPanel {

        private int size = Inventory.INVENTORYSIZE;
        private ItemDisplaySlot[] itemDisplaySlots = new ItemDisplaySlot[size];
        private ItemDisplaySlot weaponSlot;
        private ItemDisplaySlot offhandSlot;
        private ItemDisplaySlot helmetSlot;
        private ItemDisplaySlot bodySlot;
        private ItemDisplaySlot legsSlot;
        private ItemDisplaySlot ammunitionSlot;

        private InventoryPanel() {
            super("Inventory");
            setLayout(new BorderLayout());
            JPanel inventorySlots = new JPanel(new GridLayout(6, 4, 0, 0));

            for (int i = 0; i < size; i++) {
                itemDisplaySlots[i] = new ItemDisplaySlot(i, new Dimension(50, 50));
                itemDisplaySlots[i].setHorizontalAlignment(JLabel.CENTER);
                itemDisplaySlots[i].setVerticalAlignment(JLabel.CENTER);
                inventorySlots.add(itemDisplaySlots[i]);
            }
            inventorySlots.setOpaque(false);

            JPanel paddingPanel = new JPanel();
            paddingPanel.setOpaque(false);
            paddingPanel.add(inventorySlots);
            add(paddingPanel, BorderLayout.NORTH);

            JPanel equipmentSlots = new JPanel();
            equipmentSlots.setBackground(Color.DARK_GRAY);
            equipmentSlots.setPreferredSize(new Dimension(100, 120));
            equipmentSlots.setLayout(new FlowLayout(FlowLayout.CENTER));
            weaponSlot = new ItemDisplaySlot(-1, new Dimension(60, 60));
            offhandSlot = new ItemDisplaySlot(-2, new Dimension(60, 60));
            helmetSlot = new ItemDisplaySlot(-3, new Dimension(60, 60));
            bodySlot = new ItemDisplaySlot(-4, new Dimension(60, 60));
            legsSlot = new ItemDisplaySlot(-5, new Dimension(60, 60));
            ammunitionSlot = new ItemDisplaySlot(-6, new Dimension(60, 60));
            ammunitionSlot.setHorizontalAlignment(JLabel.RIGHT);
            ammunitionSlot.setHorizontalTextPosition(JLabel.LEADING);
            equipmentSlots.add(weaponSlot);
            equipmentSlots.add(offhandSlot);
            equipmentSlots.add(helmetSlot);
            equipmentSlots.add(bodySlot);
            equipmentSlots.add(legsSlot);
            equipmentSlots.add(ammunitionSlot);
            add(equipmentSlots, BorderLayout.CENTER);
        }

        @Override
        void update() {
            Actors.Character player = Game.getInstance().getPlayer();
            Inventory inventory = Game.getInstance().getPlayer().getInventory();
            for (int i = 0; i < 24; i++) {
                Item item = inventory.getItemAt(i);
                itemDisplaySlots[i].setItem(item);
            }
            weaponSlot.setItem(player.getMainHand());
            offhandSlot.setItem(player.getOffHand());
            helmetSlot.setItem(player.getHelmet());
            bodySlot.setItem(player.getBody());
            legsSlot.setItem(player.getLegs());
            ammunitionSlot.setItem(player.getAmmunition());
        }

        private class ItemDisplaySlot extends JLabel {
            private ItemDisplaySlot(int num, Dimension d) {
                setPreferredSize(d);
                Border border = BorderFactory.createLineBorder(Color.WHITE);
                Border titleBorder = new TitledBorder(border, String.valueOf(num), TitledBorder.CENTER,
                        TitledBorder.BELOW_TOP, getFont(), Color.LIGHT_GRAY);
                setBorder(titleBorder);
                setForeground(Color.LIGHT_GRAY);
            }

            private Item currentItem;

            private void setItem(Item item) {
                if (item == null) {
                    setText("");
                    this.currentItem = null;
                    return;
                }
                if (this.currentItem == null || !this.currentItem.equals(item)) {
                    this.currentItem = item;
                    BufferedImage b = item.getGraphic().getImage();
                    if (b != null) {
                        setIcon(new ImageIcon(b));
                        setText("");
                    } else {
                        setIcon(null);
                        setText(item.getName());
                    }
                } else if (currentItem.isStackable()) {
                    setText(String.valueOf(((Stackable) item).getStack()));
                }
            }
        }
    }

    private class StatsPanel extends RoundedInfoPanel {
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

    private class MapPanel extends RoundedInfoPanel {
        private MapPanel() {
            super("Map");
        }

        @Override
        void update() {
            repaint();
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D graphics2D = (Graphics2D) g;
            Map<Vector, WorldSpace> mapSpaces = Camera.getInstance().mapToRender();
            int horizCenter = this.getWidth() / 2;
            int vertCenter = this.getHeight() / 2;

            for (Map.Entry<Vector, WorldSpace> e : mapSpaces.entrySet()) {
                Vector v = Vector.multiply(e.getKey(), 4);
                WorldSpace w = e.getValue();
                if (w == null || w.isWall()) {
                    graphics2D.fillRect(horizCenter + v.getX(), vertCenter - v.getY(), 4, 4);
                }
            }
            //player position
            Actor player = Game.getInstance().getPlayer();
            BufferedImage bf;
            if (player.isFacing(Vector.NORTH)) {
                bf = Resource.player_MAP_NORTH.getImage();
            } else if (player.isFacing(Vector.EAST)) {
                bf = Resource.player_MAP_EAST.getImage();
            } else if (player.isFacing(Vector.SOUTH)) {
                bf = Resource.player_MAP_SOUTH.getImage();
            } else if (player.isFacing(Vector.WEST)) {
                bf = Resource.player_MAP_WEST.getImage();
            } else {
                bf = Resource.player_MAP_SOUTH.getImage();
                DebugLog.write("Player is not facing any cardinal direction!", true);
            }
            graphics2D.drawImage(bf, this.getWidth() / 2 - bf.getWidth() / 2, this.getHeight() / 2 - bf.getHeight() / 2, this);
        }


    }

    private class PausePanel extends RoundedInfoPanel {
        private PausePanel() {
            super("Pause Menu");
            CustomButton resume = new CustomButton("Resume Game", null);
            CustomButton save = new CustomButton("Save Game", null);
            CustomButton options = new CustomButton("Options", null);
            CustomButton quit = new CustomButton("Quit Game", null);
            JPanel buttons = new JPanel(new GridLayout(4, 1, 0, 7));
            buttons.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            buttons.add(resume);
            buttons.add(save);
            buttons.add(options);
            buttons.add(quit);
            buttons.setBackground(getBackground());
            buttons.setForeground(getForeground());

            add(buttons);
        }

        @Override
        void update() {

        }
    }
}
