package WIP;

import Components.GraphicsComponent;
import Components.PhysicsComponent;

/**
 * Created with IntelliJ IDEA.
 * User: Jean-Luc
 * Date: 13.11.13
 * Time: 14:26
 * <p/>
 * This class is used for any Gameobject that can move by its own
 */
public abstract class Actor extends GameObject {

    private PhysicsComponent physicsComponent;
    private String name;
    protected int maxHealth, currentHealth;
    private int currentXVelocity, currentYVelocity;

    protected Actor(String name, Transform transform, GraphicsComponent graphic, PhysicsComponent collider) {
        super(transform, graphic);
        physicsComponent = collider;
        this.name = name;
    }

    public abstract void update();

    public boolean isCollidable() {
        return true;
    }

    public PhysicsComponent getCollider() {
        return physicsComponent;
    }

    public void move() {
        Vector currentPosition = getTransform().getPosition();
        World world = Game.getInstance().getCurrentWorld();

        //If player can move to inputted position, move there
        if (world.resolveCollision(this, currentPosition.shiftedPosition(currentXVelocity, currentYVelocity))) {
            translate(currentXVelocity, currentYVelocity);
        } else {
            //If player cannot, but another inputted direction is possible, slide past the wall
            if (world.resolveCollision(this, currentPosition.shiftedPosition(currentXVelocity, 0))) {
                translate(currentXVelocity, 0);
            } else if (world.resolveCollision(this, currentPosition.shiftedPosition(0, currentYVelocity))) {
                translate(0, currentYVelocity);
            }
        }
    }

    private void translate(int x, int y) {
        getTransform().getPosition().shift(x, y);
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

    public void damage(int damage) {
        currentHealth -= damage;
        DebugLog.write("Damaged enemy " + name + " for " + damage + " damage.");
        if (currentHealth <= 0)
            death();

    }

    private void death() {
        Game.getInstance().removeActor(this);
        DebugLog.write("Enemy " + name + " is dead.");
    }

    /*
    This method checks if a certain actor is within the attack range of this actor.
    It returns true if the enemy can be attacked
    It returns false if the enemy is out of range or the player is looking in the wrong direction.
     */
    protected boolean enemyWithinRange(Actor other, float range) {
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

    protected boolean inLinearRange(int num1, int num2, float range) {
        return num1 - num2 < range * Renderer.TILESIZE;
    }

    //region Position Comparison methods
    protected boolean leftOf(Actor toCompare) {
        Vector[] thisCollider = getCollider().getCorners();
        Vector[] otherCollider = toCompare.getCollider().getCorners();
        return thisCollider[1].getX() < otherCollider[0].getX();
    }

    protected boolean rightOf(Actor toCompare) {
        Vector[] thisCollider = getCollider().getCorners();
        Vector[] otherCollider = toCompare.getCollider().getCorners();
        return thisCollider[0].getX() > otherCollider[1].getX();
    }

    protected boolean below(Actor toCompare) {
        Vector[] thisCollider = getCollider().getCorners();
        Vector[] otherCollider = toCompare.getCollider().getCorners();
        return thisCollider[0].getY() < otherCollider[2].getY();
    }

    protected boolean above(Actor toCompare) {
        Vector[] thisCollider = getCollider().getCorners();
        Vector[] otherCollider = toCompare.getCollider().getCorners();
        return thisCollider[2].getY() > otherCollider[0].getY();
    }

    protected boolean onX(Actor toCompare) {
        Vector[] thisCollider = getCollider().getCorners();
        Vector[] otherCollider = toCompare.getCollider().getCorners();
        return thisCollider[0].getX() > otherCollider[0].getX() - Renderer.TILESIZE * 0.5
                && thisCollider[1].getX() < otherCollider[1].getY() + Renderer.TILESIZE * 0.5;
    }

    protected boolean onY(Actor toCompare) {
        Vector[] thisCollider = getCollider().getCorners();
        Vector[] otherCollider = toCompare.getCollider().getCorners();
        return thisCollider[0].getY() < otherCollider[0].getY() + Renderer.TILESIZE * 0.5
                && thisCollider[2].getY() > otherCollider[2].getY() - Renderer.TILESIZE * 0.5;
    }
    //endregion
}
