package Components;

import WIP.Renderer;
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

    //Rotates the collider by 90 degrees
    public void rotate90() {
        int save = width;
        width = height;
        height = save;
    }

    //region Position Comparison methods
    public boolean leftOf(PhysicsComponent toCompare) {
        Vector[] thisCollider = getCorners();
        Vector[] otherCollider = toCompare.getCorners();
        return thisCollider[1].getX() < otherCollider[0].getX();
    }

    public boolean rightOf(PhysicsComponent toCompare) {
        Vector[] thisCollider = getCorners();
        Vector[] otherCollider = toCompare.getCorners();
        return thisCollider[0].getX() > otherCollider[1].getX();
    }

    public boolean below(PhysicsComponent toCompare) {
        Vector[] thisCollider = getCorners();
        Vector[] otherCollider = toCompare.getCorners();
        return thisCollider[0].getY() < otherCollider[2].getY();
    }

    public boolean above(PhysicsComponent toCompare) {
        Vector[] thisCollider = getCorners();
        Vector[] otherCollider = toCompare.getCorners();
        return thisCollider[2].getY() > otherCollider[0].getY();
    }

    public boolean onX(PhysicsComponent toCompare, float tolerance) {
        Vector[] thisCollider = getCorners();
        Vector[] otherCollider = toCompare.getCorners();
        return thisCollider[0].getX() > otherCollider[0].getX() - Renderer.TILESIZE * tolerance
                && thisCollider[1].getX() < otherCollider[1].getY() + Renderer.TILESIZE * tolerance;
    }

    public boolean onY(PhysicsComponent toCompare, float tolerance) {
        Vector[] thisCollider = getCorners();
        Vector[] otherCollider = toCompare.getCorners();
        return thisCollider[0].getY() < otherCollider[0].getY() + Renderer.TILESIZE * tolerance
                && thisCollider[2].getY() > otherCollider[2].getY() - Renderer.TILESIZE * tolerance;
    }
}
