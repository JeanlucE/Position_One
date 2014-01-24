package Actors;

import Components.ActorGraphicsComponent;
import Components.PhysicsComponent;
import WIP.DebugLog;
import WIP.Game;
import WIP.Transform;
import WIP.Vector;

import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: Jean-Luc
 * Date: 14.11.13
 * Time: 14:32
 */
public class Enemy extends NPC {
    public static boolean DEBUG_ALL_ENEMIES_RANDOM_MOVE = false;
    public static boolean DEBUG_ALL_ENEMIES_MOVE_TOWARD_PLAYER = false;
    private int experience = 100;

    public Enemy(String name, int maxHealth, Transform t, ActorGraphicsComponent g, PhysicsComponent p) {
        super(name, t, g, p);
        setMaxHealth(maxHealth);
        setCurrentHealth(maxHealth);
        g.setParent(this);
    }

    protected void updateThis() {

        if (DEBUG_ALL_ENEMIES_RANDOM_MOVE && DEBUG_ALL_ENEMIES_MOVE_TOWARD_PLAYER) {
            DebugLog.write("Enemy debug variables conflict: DEBUG_ALL_ENEMIES_RANDOM_MOVE, " +
                    "DEBUG_ALL_ENEMIES_MOVE_TOWARD_PLAYER are both true!");
            if (getXVel() != 0 || getYVel() != 0) {
                move();
            }
            return;
        }

        if (DEBUG_ALL_ENEMIES_RANDOM_MOVE) {
            randomVel();
        } else if (DEBUG_ALL_ENEMIES_MOVE_TOWARD_PLAYER) {
            aggressiveBehaviour();
            damageOnTouch();
        }

        if (getXVel() != 0 || getYVel() != 0) {
            move();
        }
    }

    @Override
    public Faction getFaction() {
        return Faction.ENEMY;
    }

    //DEBUGGING
    private int xMinSpeed = 0;
    private int xMaxSpeed = 2;
    private int yMinSpeed = 0;
    private int yMaxSpeed = 2;
    private int direction = -1;
    private Random random = new Random();

    private void randomVel() {
        if (getTransform().getPosition().equals(new Vector(60, 60))
                || (getTransform().getPosition().getX() >= 700 && getTransform().getPosition().getY() >= 700))
            direction = direction * -1;
        setXVel((random.nextInt(xMaxSpeed) + 1 + xMinSpeed) * direction);
        setYVel((random.nextInt(yMaxSpeed) + 1 + yMinSpeed) * direction);
    }

    private void aggressiveBehaviour() {
        Actor player = Game.getInstance().getPlayer();
        if (player.withinRangeOf(this, 300)) {
            Vector directionToPlayer = directionTo(player);
            setXVel(directionToPlayer.getX());
            setYVel(directionToPlayer.getY());
        } else {
            setXVel(0);
            setYVel(0);
        }
    }

    private void damageOnTouch() {
        Actor player = Game.getInstance().getPlayer();
        if (player.withinRangeOf(this, 75)) {
            player.damage(5);
        }
    }

    @Override
    protected int getDamageTimeout() {
        return 250;
    }

    @Override
    protected int actualDamage(int damage) {
        return damage;
    }

    public Enemy clone() {
        return new Enemy(getName(), getMaxHealth(), getTransform().clone(),
                ((ActorGraphicsComponent) getGraphic()).clone(), getCollider().clone());
    }

    @Override
    protected void death() {
        Game.getInstance().getPlayer().addExperience(experience);
        super.death();
    }
}
