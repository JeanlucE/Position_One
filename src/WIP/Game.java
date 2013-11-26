package WIP;

import Components.*;
import Items.Weapon;
import Items.Weapon_Melee;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

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
    private final Character player;
    private final Enemy enemy;
    private World currentWorld;
    private final Renderer renderer;
    private final GameLoop gameLoop;
    private List<Actor> actors = new ArrayList<>();

    //Singleton Design Pattern
    public static Game getInstance() {
        if (instance == null) {
            instance = new Game();
        }
        return instance;
    }

    private Game() {
        Weapon weapon = new Weapon_Melee("Sword", 0, 0, 1, 1.0f, new ItemGraphicsComponent());
        player = new Character(this, "Ned Stark");
        player.getCollider().setParent(player.getTransform());
        player.getTransform().getPosition().setX(200);
        player.getTransform().getPosition().setY(200);
        player.equip(weapon);

        enemy = new Enemy("Chu Chu", 100, new Transform(new Vector(400, 400)),
                new StaticGraphicsComponent(Resource.enemy_DOWN),
                new PhysicsComponent(40, 40));
        enemy.getCollider().setParent(enemy.getTransform());

        currentWorld = new World();
        ((Floor) currentWorld.getReal(6, 7)).dropItem(new Weapon_Melee("Swordish", 0, 0, 1, 1.0f,
                new ItemGraphicsComponent(Resource.weapon_melee_01_FLOOR)));
        addActor(player);
        addActor(enemy);
        DebugLog.write("New Game started");

        renderer = Renderer.getInstance();
        DebugLog.write("Renderer started");
        renderer.setInputMap(JComponent.WHEN_FOCUSED, InputComponent.getInstance());
        renderer.setActionMap(InputComponent.getInstance().getActionMap());

        gameLoop = new GameLoop();
        Timer refresh = new Timer(15, gameLoop);
        refresh.start();
    }

    public List<Actor> getActors() {
        return actors;
    }

    public void addActor(Actor actor) {
        actors.add(actor);
    }

    public int getFrameRate() {
        return gameLoop.getFrameRate();
    }

    public Character getPlayer() {
        return player;
    }

    public World getCurrentWorld() {
        return currentWorld;
    }

    //TODO make private later
    public Renderer getRenderer() {
        return renderer;
    }

    //DEBUGGING PURPOSES
    public Enemy getEnemy() {
        return enemy;
    }

    private class GameLoop implements ActionListener {

        int frames = 0;
        int frameRate = 60;
        long timeBetween;
        long milliseconds = System.currentTimeMillis();

        public void actionPerformed(ActionEvent e) {
            for (Actor a : actors) {
                a.update();
            }
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
