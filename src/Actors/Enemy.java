package Actors;

import Components.ActorGraphicsComponent;
import Components.PhysicsComponent;
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

    public Enemy(String name, int maxHealth, Transform t, ActorGraphicsComponent g, PhysicsComponent p) {
        super(name, t, g, p);
        setMaxHealth(maxHealth);
        setCurrentHealth(maxHealth);
        g.setParent(this);
    }

    public void update() {
        //randomVel();
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
        if (getTransform().getPosition().equals(new Vector(40, 40))
                || (getTransform().getPosition().getX() >= 700 && getTransform().getPosition().getY() >= 700))
            direction = direction * -1;
        setXVel((random.nextInt(xMaxSpeed) + 1 + xMinSpeed) * direction);
        setYVel((random.nextInt(yMaxSpeed) + 1 + yMinSpeed) * direction);
    }
}
