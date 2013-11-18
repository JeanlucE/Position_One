package Components;

import WIP.Position;

/**
 * Created with IntelliJ IDEA.
 * User: Jean-Luc
 * Date: 09.11.13
 * Time: 16:15
 */
public class PhysicsComponent {
    private final int width;
    private final int height;

    public PhysicsComponent(int width, int height) {
        this.width = width;
        this.height = height;
    }

    //Returns the corner positions of the players collision box
    // the +/- 1 are so that there is some tolerance for the player
    public Position[] getCorners(Position playerPos) {
        Position toReturn[] = new Position[4];
        //Top left corner
        toReturn[0] = new Position(playerPos.getX(), playerPos.getY() + height);
        //Top right corner
        toReturn[1] = new Position(playerPos.getX() + width, playerPos.getY() + height);
        //Bottom left corner
        toReturn[2] = new Position(playerPos.getX(), playerPos.getY());
        //Bottom right corner
        toReturn[3] = new Position(playerPos.getX() + width, playerPos.getY());
        return toReturn;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
