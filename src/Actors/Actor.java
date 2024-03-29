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
 * This class is used for any Gameobject that can canMove by its own
 */
public abstract class Actor extends GameObject implements Collidable {

    //Static list of all actors to iterate over when updating or rendering
    private static List<Actor> actors = new ArrayList<>(10);

    //Name of the Actor
    private String name;
    //Maximum and current health of the Actor
    protected int maxHealth, currentHealth;
    //The velocity of the Actor in the current frame, component-wise x and y
    private int currentXVelocity, currentYVelocity;
    //time how long the actor is invincible in ms
    private int currentDamageTimeout;
    //flag if player is moving
    private boolean isMoving = false;
    //Actors collider
    private PhysicsComponent collider;

    /**
     * Returns an array of all instantiated Actors on the level.
     *
     * @return an array of all instantiated Actors
     */
    public static Actor[] getActors() {
        return actors.toArray(new Actor[actors.size()]);
    }

    /**
     * Removes all actors that died in this frame. This method should be called after the update loop of each actor
     * is called.
     */
    public static void removeDeadActors() {
        for (int i = actors.size() - 1; i >= 0; i--) {
            Actor a = actors.get(i);
            if (a.isDestroyed())
                actors.remove(i);
        }

    }

    /**
     * Creates an Actor.
     *
     * @param name             Name of the actor
     * @param transform        Position and direction the actor should be instantiated with
     * @param graphic          GraphicCompomonent for the Renderer
     * @param physicsComponent the collider of the actor
     */
    protected Actor(String name, Transform transform, ActorGraphicsComponent graphic,
                    PhysicsComponent physicsComponent) {
        super(transform, graphic);
        setCollider(physicsComponent);
        physicsComponent.setParent(getTransform());

        this.name = name;
        actors.add(this);
        currentDamageTimeout = getDamageTimeout();
    }

    @Override
    public PhysicsComponent getCollider() {
        return collider;
    }

    public void setCollider(PhysicsComponent collider) {
        this.collider = collider;
    }

    /**
     * Update loop of the actor
     */
    public final void update() {
        if (currentDamageTimeout > 0) {
            currentDamageTimeout -= Time.deltaTime();
        }
        updateThis();
        //System.out.println(isMoving);
        move();
        animate();
        lateUpdate();
    }


    protected abstract void updateThis();

    protected abstract void lateUpdate();

    protected void animate() {
        if (isMoving()) {
            setAnimationState(AnimationState.WALKING);
        } else {
            setAnimationState(AnimationState.IDLE);
        }
    }

    public boolean isCollidable() {
        return true;
    }

    /**
     * Moves the actor according to the values set at currentXVelocity and currentYVelocity. These fields should be
     * changed before move is called. If the movement is hindered by a collision the method tries to reduce the
     * velocity of the actor in this frame only to canMove as close as possible to the collision object. If that still
     * results in a collision the method tried to slide past if any of the orthagonal axes relative to the collision
     * are not 0.
     */
    protected final void move() {
        if (currentXVelocity == 0 && currentYVelocity == 0) {
            isMoving = false;
            return;
        }

        int xMove = currentXVelocity;
        int yMove = currentYVelocity;

        while (!(xMove == 0 && yMove == 0)) {

            boolean hasMoved = canMove(xMove, yMove);
            if (hasMoved)
                break;

            if (yMove == 0 || Math.abs(xMove) > Math.abs(yMove)) {
                if (xMove > 0) {
                    xMove--;
                } else {
                    xMove++;
                }
            } else {
                if (yMove > 0) {
                    yMove--;
                } else {
                    yMove++;
                }
            }
        }
        int possibleXSlide = xSlide(currentXVelocity);
        int possibleYSlide = ySlide(currentYVelocity);

        if (Math.abs(possibleXSlide) > Math.abs(xMove)) {
            xMove = possibleXSlide;
            yMove = 0;
        } else if (Math.abs(possibleYSlide) > Math.abs(yMove)) {
            xMove = 0;
            yMove = possibleYSlide;
        }

        translate(xMove, yMove);

        if (xMove != 0 || yMove != 0) {
            isMoving = true;
            if (yMove == 0) {
                getTransform().setDirection(new Vector(xMove, 0).normalize());
            } else if (xMove == 0) {
                getTransform().setDirection(new Vector(0, yMove).normalize());
            } else { //Prioritze east west axis
                getTransform().setDirection(new Vector(xMove, 0).normalize());
            }
        } else {
            if (currentYVelocity == 0) {
                getTransform().setDirection(new Vector(currentXVelocity, 0).normalize());
            } else if (currentXVelocity == 0) {
                getTransform().setDirection(new Vector(0, currentYVelocity).normalize());
            } else { //Prioritze east west axis
                getTransform().setDirection(new Vector(currentXVelocity, 0).normalize());
            }
            isMoving = false;
        }
    }

    private boolean canMove(int x, int y) {
        Vector currentPosition = getTransform().getPosition();
        World world = Game.getInstance().getCurrentWorld();

        World.CollisionEvent w = world.resolveCollision(this, currentPosition.shiftedPosition(x, y));
        return w.isNoCollision();
    }

    private int xSlide(int x) {
        while (x != 0) {

            boolean hasMoved = canMove(x, 0);
            if (hasMoved)
                break;

            if (x > 0) {
                x--;
            } else {
                x++;
            }
        }
        return x;
    }

    private int ySlide(int y) {
        while (y != 0) {

            boolean hasMoved = canMove(0, y);
            if (hasMoved)
                break;

            if (y > 0) {
                y--;
            } else {
                y++;
            }
        }
        return y;
    }

    /**
     * Translates the actors transform
     *
     * @param x x translation distance
     * @param y y translation distance
     */
    private void translate(int x, int y) {
        getTransform().getPosition().shift(x, y);
    }

    protected final void setXVel(int xVel) {
        this.currentXVelocity = xVel;
    }

    protected final void setYVel(int yVel) {
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

    /**
     * Sets the name of the actor. ONLY FOR DEBUGGING PURPOSES
     *
     * @param name name to set
     */
    public void setName(String name) {
        this.name = name;
        DebugLog.write(name + "'s name set to: " + name, true);
    }

    /**
     * Returns the maximum health of the actor
     *
     * @return Returns the maximum health of the actor
     */
    public int getMaxHealth() {
        return maxHealth;
    }

    /**
     * Sets the maximum health of the actor
     *
     * @param maxHealth maximum health to set
     */
    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    /**
     * Returns the currentHealth of the Actor
     *
     * @return Returns the currentHealth of the Actor
     */
    public int getCurrentHealth() {
        return currentHealth;
    }

    /**
     * Sets the current health of the actor
     *
     * @param health current health to set
     */
    public void setCurrentHealth(int health) {
        currentHealth = health;
    }

    /**
     * Returns the health percentage in a float with 0.0 being 0% and 1.0 being 100%
     *
     * @return Returns the health percentageof the actor
     */
    public float getHealthPercentage() {
        return currentHealth / (float) maxHealth;
    }

    /**
     * Damages the actor, if the actor can be damaged.
     *
     * @param damage Raw damage taken without any modifications
     * @see Actor#actualDamage(int)
     * @see Actors.Actor#canBeDamaged()
     */
    public final void damage(int damage) {
        if (canBeDamaged()) {
            int actualDamage = actualDamage(damage);
            currentHealth -= actualDamage;
            DebugLog.write("Damaged actor " + name + " for " + actualDamage + " damage.");
            if (currentHealth <= 0)
                death();
            else
                currentDamageTimeout = getDamageTimeout();
        }
    }

    /**
     * Is called when Actor is damaged. Then actor can negate or multiply the damage depending on what armor is
     * equipped or if any buffs are active.
     *
     * @param damage Raw damage taken without any modifications
     * @return Returns damage taken with modifications made by the individual Actor.
     */
    protected abstract int actualDamage(int damage);

    /**
     * Sets the actor to a dead state and will be removed from the actors after this frame
     */
    protected void death() {
        this.destroy();
        DebugLog.write("Actor " + name + " is dead.");
    }

    /**
     * This method checks if a certain actor is within the attack range of this actor.
     * It returns true if the enemy is in range and is in the direction this actor is facing
     * It returns false if the enemy is out of range or this actor is not facing the other actor
     *
     * @param other Actor to compare range to
     * @param range range where the other actor should be
     * @return Returns if the other actor is within range of this actor, in direction of where this actor is facing
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

    /**
     * Returns if the other actor is generally in a certain range of this actor, regardless of where this actor or the
     * other actor is facing.
     *
     * @param other Actor to compare range to
     * @param range range where the other actor should be
     * @return Returns if the other actor is generally in a certain range of this actor.
     */
    public boolean withinRangeOf(Actor other, double range) {
        double distance = Vector.subtract(other.getTransform().getPosition(), getTransform().getPosition()).length();
        return Double.compare(distance, range) == -1;
    }

    protected Vector directionTo(Actor other) {
        Vector vectorDistance = Vector.subtract(other.getTransform().getPosition(), getTransform().getPosition());
        vectorDistance.normalize();
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

    protected boolean canBeDamaged() {
        return currentDamageTimeout <= 0;
    }

    /**
     * Returns the time of invincibility after which this actor can be damaged again
     *
     * @return Returns the time of invincibility after which this actor can be damaged again
     * @see Actors.Actor#canBeDamaged()
     */
    protected abstract int getDamageTimeout();

    boolean isMoving() {
        return isMoving;
    }

    private AnimationState animationState = AnimationState.IDLE;

    public AnimationState getAnimationState() {
        return animationState;
    }

    protected void setAnimationState(AnimationState animationState) {
        this.animationState = animationState;
    }

    public boolean isFacing(Vector v) {
        if ((Math.abs(v.getX()) == 1 && v.getY() == 0) || (Math.abs(v.getY()) == 1 && v.getX() == 0)) {
            return getTransform().getDirection().equals(v);
        } else {
            DebugLog.write("Actor cannot face " + v);
            return false;
        }
    }
}
