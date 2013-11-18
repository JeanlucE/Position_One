package WIP;

import Components.GraphicsComponent;
import Components.PhysicsComponent;

import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: Jean-Luc
 * Date: 14.11.13
 * Time: 14:32
 */
public class Enemy extends NPC {

    public Enemy(String name, int maxHealth, Transform t, GraphicsComponent g, PhysicsComponent p) {
        super(name, t, g, p);
        setMaxHealth(maxHealth);
        setCurrentHealth(maxHealth);
    }

    public void update(){
        randomVel();
        if (currentXVelocity != 0 || currentYVelocity != 0) {
            move();
        }
    }

    private void randomVel(){
        Random random = new Random();
        int xMinSpeed = 0;
        int xMaxSpeed = 2;
        int yMinSpeed = 0;
        int yMaxSpeed = 2;

        currentXVelocity = random.nextInt(xMaxSpeed) + xMinSpeed + 1;
        currentYVelocity = random.nextInt(yMaxSpeed) + yMinSpeed + 1;
    }
}
