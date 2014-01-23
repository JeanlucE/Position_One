package WIP;

import Actors.Actor;
import Actors.Character;
import Actors.Enemy;
import Components.*;
import Environment.World;
import Items.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ConcurrentModificationException;

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
    private final Renderer renderer;
    private final GameLoop gameLoop;

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
        Weapon sword = new Weapon_Melee("Sword", 0, 0, 10, 0.65f, new ItemGraphicsComponent());
        Weapon bow = new Weapon_Ranged("The OP Bow", 0, 0, 10, 10, 0.5f, new ItemGraphicsComponent());
        player = new Character("Ned Stark");
        player.getTransform().getPosition().setX(60);
        player.getTransform().getPosition().setY(60);
        Ammunition arrow = new Ammunition("Ammunition", 0,
                new ProjectileGraphicsComponent(null, DynamicResource.WOODENARROW),
                new PhysicsComponent(10, 25));
        Armour helmet = new Helmet("Helmet", 0, 0, Equipment.EquipmentClass.MELEE, 1, new ItemGraphicsComponent());
        Armour body = new Body("Body", 0, 0, Equipment.EquipmentClass.MELEE, 1, new ItemGraphicsComponent());
        Armour legs = new Legs("Legs", 0, 0, Equipment.EquipmentClass.MELEE, 1, new ItemGraphicsComponent());
        player.equip(helmet);
        player.equip(body);
        player.equip(legs);
        player.equip(bow);
        player.equip(arrow);
        Enemy.DEBUG_ALL_ENEMIES_MOVE_TOWARD_PLAYER = true;

        World currentWorld = World.getInstance();

        renderer = Renderer.getInstance();
        gameLoop = new GameLoop();
        Timer refresh = new Timer(15, gameLoop);
        refresh.start();
    }

    public Actor[] getActors() {
        return Actor.getActors();
    }

    private void removeDestroyedGameObjects() {
        Projectile.removeDeadProjectiles();
        try {
            Actor.removeDeadActors();
        } catch (ConcurrentModificationException e) {
            DebugLog.write(e);
        }
    }

    public int getFrameRate() {
        return gameLoop.getFrameRate();
    }

    public Character getPlayer() {
        return player;
    }

    public World getCurrentWorld() {
        return World.getInstance();
    }

    //TODO make private later
    public Renderer getRenderer() {
        return renderer;
    }

    private class GameLoop implements ActionListener {

        int frames = 0;
        int frameRate = 60;
        long timeBetween;
        long milliseconds = System.currentTimeMillis();


        public void actionPerformed(ActionEvent e) {
            Time.update();

            for (Actor a : Actor.getActors()) {
                a.update();
            }

            for (Projectile p : Projectile.getProjectiles()) {
                p.update();
            }

            removeDestroyedGameObjects();
            InputComponent.getInstance().resetTypedKeys();
            getRenderer().repaint();

            //Functionality to count frames and show frame rate circa every second
            frames++;
            timeBetween = System.currentTimeMillis();
            //if one second has passed
            if (timeBetween - milliseconds >= 1000) {
                frameRate = frames;
                frames = 0;
                milliseconds = System.currentTimeMillis();
            }
        }

        public int getFrameRate() {
            return frameRate;
        }
    }
}
