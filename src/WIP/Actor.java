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
    private int maxHealth;
    private int currentHealth;
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

    public void setCurrentHealth(int currentHealth) {
        this.currentHealth = currentHealth;
    }

    public void damage(int damage) {
        currentHealth -= damage;
        DebugLog.write("Damaged enemy "  + name + " for " + damage + " damage.");
        if (currentHealth <= 0)
            death();

    }

    private void death() {
        DebugLog.write("Enemy " + name + " is dead.");
    }
}
