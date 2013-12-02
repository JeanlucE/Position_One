package Components;

import WIP.Transform;
import WIP.Vector;

/**
 * Created with IntelliJ IDEA.
 * User: Jean-Luc
 * Date: 09.11.13
 * Time: 16:15
 */
public class PhysicsComponent {
    private int width;
    private int height;
    private Transform parent;

    public PhysicsComponent(int width, int height) {
        this.width = width;
        this.height = height;
    }

    //Returns the corner positions of the players collision box
    // the +/- 1 are so that there is some tolerance for the player
    public Vector[] getCorners() {
        Vector playerPos = parent.getPosition();
        Vector toReturn[] = new Vector[4];
        //Top left corner
        toReturn[0] = new Vector(playerPos.getX(), playerPos.getY() + height);
        //Top right corner
        toReturn[1] = new Vector(playerPos.getX() + width, playerPos.getY() + height);
        //Bottom left corner
        toReturn[2] = new Vector(playerPos.getX(), playerPos.getY());
        //Bottom right corner
        toReturn[3] = new Vector(playerPos.getX() + width, playerPos.getY());
        return toReturn;
    }

    public Vector[] getCorners(Vector playerPos) {
        Vector toReturn[] = new Vector[4];
        //Top left corner
        toReturn[0] = new Vector(playerPos.getX(), playerPos.getY() + height);
        //Top right corner
        toReturn[1] = new Vector(playerPos.getX() + width, playerPos.getY() + height);
        //Bottom left corner
        toReturn[2] = new Vector(playerPos.getX(), playerPos.getY());
        //Bottom right corner
        toReturn[3] = new Vector(playerPos.getX() + width, playerPos.getY());
        return toReturn;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setParent(Transform t) {
        this.parent = t;
    }

    public void rotate90() {
        int save = width;
        width = height;
        height = save;
    }
}
