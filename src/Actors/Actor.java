package Actors;

import Components.ActorGraphicsComponent;
import Components.PhysicsComponent;
import Environment.World;
import WIP.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Jean-Luc
 * Date: 13.11.13
 * Time: 14:26
 * <p/>
 * This class is used for any Gameobject that can move by its own
 */
public abstract class Actor extends Collidable {

    private static List<Actor> actors = new ArrayList<>(10);
    private String name;
    protected int maxHealth, currentHealth;
    private int currentXVelocity, currentYVelocity;


    public static Actor[] getActors() {
        return actors.toArray(new Actor[actors.size()]);
    }

    public static void removeDeadActors() {
        for (int i = 0; i < actors.size(); i++) {
            if (actors.get(i).isDestroyed())
                actors.remove(i);
        }
    }

    protected Actor(String name, Transform transform, ActorGraphicsComponent graphic,
                    PhysicsComponent physicsComponent) {
        super(transform, graphic, physicsComponent);
        this.name = name;
        actors.add(this);
    }

    public abstract void update();

    public boolean isCollidable() {
        return true;
    }

    public void move() {
        if (currentXVelocity == 0 && currentYVelocity == 0) return;
        Vector currentPosition = getTransform().getPosition();
        World world = Game.getInstance().getCurrentWorld();

        //TODO slow down character movement for diagonal movement

        //If player can move to inputted position, move there
        World.CollisionEvent collisionEvent = world.resolveCollision(this, currentPosition.shiftedPosition
                (currentXVelocity, currentYVelocity));
        if (collisionEvent.isNoCollision()) {
            translate(currentXVelocity, currentYVelocity);
        } else {
            //If player cannot, but another inputted direction is possible, slide past the wall
            if (world.resolveCollision(this, currentPosition.shiftedPosition(currentXVelocity, 0)).isNoCollision()) {
                translate(currentXVelocity, 0);
            } else if (world.resolveCollision(this, currentPosition.shiftedPosition(0, currentYVelocity)).isNoCollision()) {
                translate(0, currentYVelocity);
            }
        }
        //System.out.println(currentXVelocity + "|" + currentYVelocity);
    }

    private void translate(int x, int y) {
        getTransform().getPosition().shift(x, y);
    }

    protected Vector getNextWorldPosition() {
        Vector currentPos = getTransform().getPosition();
        return new Vector(currentPos.getX() - currentPos.getX() % 40, currentPos.getY() - currentPos.getY() % 40);
    }

    public void setXVel(int xVel) {
        this.currentXVelocity = xVel;
    }

    public void setYVel(int yVel) {
        this.currentYVelocity = yVel;
    }

    public int getXVel() {
        return currentXVelocity;
    }

    public int getYVel() {
        return currentYVelocity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        DebugLog.write("{DEBUG}" + name + "'s name set to: " + name);
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public void setCurrentHealth(int health) {
        currentHealth = health;
    }

    public float getHealthPercentage() {
        return currentHealth / (float) maxHealth;
    }

    public void damage(int damage) {
        currentHealth -= damage;
        DebugLog.write("Damaged enemy " + name + " for " + damage + " damage.");
        if (currentHealth <= 0)
            death();

    }

    private void death() {
        this.destroy();
        DebugLog.write("Actor " + name + " is dead.");
    }

    /*
    This method checks if a certain actor is within the attack range of this actor.
    It returns true if the enemy can be attacked
    It returns false if the enemy is out of range or the player is looking in the wrong direction.
     */
    public boolean withinLinearRangeOf(Actor other, float range) {
        Vector direction = getTransform().getDirection();
        Vector[] otherCollider = other.getCollider().getCorners();
        Vector[] thisCollider = getCollider().getCorners();
        if (direction.equals(Vector.EAST)) {
            if (onY(other) && leftOf(other))
                return inLinearRange(otherCollider[0].getX(), thisCollider[1].getX(), range);
        } else if (direction.equals(Vector.WEST)) {
            if (onY(other) && rightOf(other))
                return inLinearRange(thisCollider[0].getX(), otherCollider[1].getX(), range);
        } else if (direction.equals(Vector.NORTH)) {
            if (onX(other) && below(other))
                return inLinearRange(otherCollider[2].getY(), thisCollider[0].getY(), range);
        } else {
            if (onX(other) && above(other))
                return inLinearRange(thisCollider[2].getY(), otherCollider[0].getY(), range);
        }
        return false;
    }

    private boolean inLinearRange(int num1, int num2, float range) {
        return num1 - num2 < range * Renderer.TILESIZE;
    }

    public boolean withinAgressiveRadiusOf(Actor other, double range) {
        double distance = Vector.subtract(other.getTransform().getPosition(), getTransform().getPosition()).length();
        return Double.compare(distance, range) == -1;
    }

    protected Vector directionTo(Actor other) {
        Vector vectorDistance = Vector.subtract(other.getTransform().getPosition(), getTransform().getPosition());
        vectorDistance.signum();
        return vectorDistance;
    }

    //region Position Comparison methods
    protected boolean leftOf(Actor toCompare) {
        return getCollider().leftOf(toCompare.getCollider());
    }

    protected boolean rightOf(Actor toCompare) {
        return getCollider().rightOf(toCompare.getCollider());
    }

    protected boolean below(Actor toCompare) {
        return getCollider().below(toCompare.getCollider());
    }

    protected boolean above(Actor toCompare) {
        return getCollider().above(toCompare.getCollider());
    }

    protected boolean onX(Actor toCompare) {
        return getCollider().onX(toCompare.getCollider(), 0.5f);
    }

    protected boolean onY(Actor toCompare) {
        return getCollider().onY(toCompare.getCollider(), 0.5f);
    }
    //endregion

    public abstract Faction getFaction();

    public String toString() {
        return getName() + "(" + currentHealth + "/" + maxHealth + ")";
    }

    public enum Faction {
        PLAYER, FRIENDLY, ENEMY
    }
}
