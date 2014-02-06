package WIP;

import Actors.Actor;
import Actors.Character;
import Components.*;
import Environment.World;
import Items.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created with IntelliJ IDEA.
 * User: Jean-Luc
 * Date: 07.11.13
 * Time: 22:23
 * <p/>
 * Handles all Input, player action, interactions with the world.
 * Also instantiates all game objects and the renderer.
 */
public class Game {
    private static Game instance = null;
    private final Actors.Character player;
    private final GameLoop gameLoop;
    private GUIState guiState = GUIState.GAME;

    //Singleton
    public static Game getInstance() {
        if (instance == null) {
            instance = new Game();
        }
        return instance;
    }

    private Game() {
        new FileStructureHandler();

        DebugLog.write("New Game started");
        Weapon sword = new Weapon_Melee("Sword", 0, 0, 15, 0.5f, new ItemGraphicsComponent(Resource.weapon_melee_01_FLOOR));
        Weapon bow = new Weapon_Ranged("The OP Bow", 0, 0, 10, 12, 0.3f, new ItemGraphicsComponent());
        Weapon staff = new Weapon_Magic("Fire Staff", 0, 0, 20, 8, 0.75f, Element.FIRE,
                new ItemGraphicsComponent());
        player = new Character("Ned Stark");
        player.getTransform().getPosition().set(60, 60);
        Ammunition arrow = new Ammunition("Wooden Arrow", 0,
                new ProjectileGraphicsComponent(null, DynamicResource.WOODEN_ARROW),
                new PhysicsComponent(10, 25));
        Armour helmet = new Helmet("Helmet", 0, 0, Equipment.EquipmentClass.MELEE, 1, new ItemGraphicsComponent());
        Armour body = new Body("Body", 0, 0, Equipment.EquipmentClass.MELEE, 1, new ItemGraphicsComponent());
        Armour legs = new Legs("Legs", 0, 0, Equipment.EquipmentClass.MELEE, 1, new ItemGraphicsComponent());
        Armour shield = new Shield("Shield", 0, 0, 1, new ItemGraphicsComponent());
        player.equip(helmet);
        player.equip(body);
        player.equip(legs);
        player.equip(shield);
        player.equip(staff);
        player.equip(arrow);
        Ammunition dropArrow = new Ammunition("Wooden Arrow", 0,
                new ProjectileGraphicsComponent(null, DynamicResource.WOODEN_ARROW),
                new PhysicsComponent(10, 25));
        getCurrentWorld().dropItem(dropArrow, 80, 80);
        //getCurrentWorld().dropItem(sword, 120, 80);

        World.getInstance();

        gameLoop = new GameLoop();
        Timer updateLoop = new Timer(16, gameLoop);
        updateLoop.start();
    }

    public Actor[] getActors() {
        return Actor.getActors();
    }

    private void removeDestroyedGameObjects() {
        GameObject.removeDestroyedGameObjects();
    }

    public Character getPlayer() {
        return player;
    }

    public World getCurrentWorld() {
        return World.getInstance();
    }

    public boolean isPaused() {
        return guiState != GUIState.GAME;
    }

    public int getFrameRate() {
        return gameLoop.frameRate;
    }

    public long getCurrentMemory() {
        return gameLoop.currentMemory;
    }

    private class GameLoop implements ActionListener {

        int frames = 0;
        int frameRate = 60;
        long timeBetween;
        long milliseconds = Time.getTimeStamp();
        long currentMemory = 0;


        public void actionPerformed(ActionEvent e) {
            try {
                Time.update();

                if (InputComponent.getInstance().isEscapeTyped()) {
                    toggleState(GUIState.PAUSE_MENU);
                }

                if (!isPaused()) {

                    for (GameObject g : GameObject.getGameObjects()) {
                        g.update();
                    }

                    getCurrentWorld().update();
                    removeDestroyedGameObjects();
                }
            } catch (Exception e1) {
                DebugLog.write(e1);
            }

            if (InputComponent.getInstance().isI_TYPED()) {
                toggleState(GUIState.INVENTORY);
            }

            if (InputComponent.getInstance().isC_TYPED()) {
                toggleState(GUIState.STATS);
            }

            if (InputComponent.getInstance().isM_TYPED()) {
                toggleState(GUIState.MAP);
            }

            //CanvasRenderer.getInstance().paint();
            Renderer.getInstance().repaint();
            InputComponent.getInstance().resetTypedKeys();

            calcFrameRate();
            calcMemory();
        }

        /**
         * Counts frame rate about every second
         */
        private void calcFrameRate() {
            frames++;
            timeBetween = Time.getTimeStamp();
            //if one second has passed
            if (timeBetween - milliseconds >= 1000) {
                frameRate = frames;
                frames = 0;
                milliseconds = timeBetween;
            }
        }

        private long lastMeasurement = Time.getTimeStamp();

        private void calcMemory() {
            long currentTime = Time.getTimeStamp();
            if (currentTime - lastMeasurement >= 1000) {
                currentMemory = (Runtime.getRuntime().totalMemory() - Runtime
                        .getRuntime().freeMemory()) / (1024L * 1024L);
                lastMeasurement = currentTime;
            }
        }
    }


    GUIState getGuiState() {
        return guiState;
    }

    void toggleState(GUIState guiState) {
        if (guiState == GUIState.PAUSE_MENU) {
            if (this.guiState != GUIState.PAUSE_MENU) {
                this.guiState = GUIState.PAUSE_MENU;
            } else {
                this.guiState = GUIState.GAME;
            }
            return;
        } else {
            if (this.guiState != guiState) {
                this.guiState = guiState;
            } else {
                this.guiState = GUIState.GAME;
            }
        }
    }

    //FOR DEBUGGING
    private void setGuiState(GUIState guiState) {
        this.guiState = guiState;
    }

    enum GUIState {
        GAME, MAIN_MENU, INVENTORY, MAP, STATS, PAUSE_MENU
    }
}
