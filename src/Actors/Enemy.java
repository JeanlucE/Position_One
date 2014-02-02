package Actors;

import Components.ActorGraphicsComponent;
import Components.PhysicsComponent;
import WIP.Game;
import WIP.Transform;
import WIP.Vector;

/**
 * Created with IntelliJ IDEA.
 * User: Jean-Luc
 * Date: 14.11.13
 * Time: 14:32
 */
public class Enemy extends NPC {
    public static boolean DEBUG_ALL_ENEMIES_MOVE_TOWARD_PLAYER = false;
    private int experience = 25;

    public Enemy(String name, int maxHealth, Transform t, ActorGraphicsComponent g, PhysicsComponent p) {
        super(name, t, g, p);
        setMaxHealth(maxHealth);
        setCurrentHealth(maxHealth);
        g.setParent(this);
    }

    protected void updateThis() {

        if (DEBUG_ALL_ENEMIES_MOVE_TOWARD_PLAYER) {
            aggressiveBehaviour();
            damageOnTouch();
        }

        if (getXVel() != 0 || getYVel() != 0) {
            move();
        }
    }

    @Override
    protected void lateUpdate() {

    }

    @Override
    public Faction getFaction() {
        return Faction.ENEMY;
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
