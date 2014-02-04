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
        return getCorners(parent.getPosition());
    }

    public Vector[] getCorners(Vector playerPos) {
        Vector toReturn[] = new Vector[4];
        //Top left corner
        toReturn[0] = new Vector(playerPos.getX() - width / 2, playerPos.getY() + height / 2);
        //Top right corner
        toReturn[1] = new Vector(playerPos.getX() + width / 2, playerPos.getY() + height / 2);
        //Bottom left corner
        toReturn[2] = new Vector(playerPos.getX() - width / 2, playerPos.getY() - height / 2);
        //Bottom right corner
        toReturn[3] = new Vector(playerPos.getX() + width / 2, playerPos.getY() - height / 2);
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
        //works only because there are only rectangle colliders
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
                && thisCollider[1].getX() < otherCollider[1].getX() + Renderer.TILESIZE * tolerance;
    }

    public boolean onY(PhysicsComponent toCompare, float tolerance) {
        Vector[] thisCollider = getCorners();
        Vector[] otherCollider = toCompare.getCorners();
        return thisCollider[0].getY() < otherCollider[0].getY() + Renderer.TILESIZE * tolerance
                && thisCollider[2].getY() > otherCollider[2].getY() - Renderer.TILESIZE * tolerance;
    }

    public boolean collision(PhysicsComponent otherCollider) {
        boolean actorAbove = above(otherCollider);
        boolean actorBelow = below(otherCollider);
        boolean actorLeftOf = leftOf(otherCollider);
        boolean actorRightOf = rightOf(otherCollider);

        return !actorAbove && !actorBelow && !actorLeftOf && !actorRightOf;
    }

    public PhysicsComponent clone() {
        return new PhysicsComponent(width, height);
    }

    public PhysicsComponent clone(Transform t) {
        PhysicsComponent p = new PhysicsComponent(width, height);
        p.setParent(t);
        return p;
    }

    public PhysicsComponent clone(Vector t) {
        PhysicsComponent p = new PhysicsComponent(width, height);
        p.setParent(new Transform(t));
        return p;
    }
}
