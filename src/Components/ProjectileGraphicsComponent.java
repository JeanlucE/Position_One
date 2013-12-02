package Components;

import WIP.Vector;

import java.awt.image.BufferedImage;

/**
 * Created with IntelliJ IDEA.
 * User: Jean-Luc
 * Date: 02.12.13
 * Time: 10:11
 */
public class ProjectileGraphicsComponent extends ItemGraphicsComponent {

    private DynamicResource projectileImages;
    private Vector direction;

    //TODO make all Bufferedimage[] NORTH, EAST, SOUTH, WEST
    public ProjectileGraphicsComponent(Vector direction, DynamicResource images) {
        this.direction = direction;
        //NORTH, EAST, SOUTH, WEST
        projectileImages = images;
    }

    //returns dropped image
    public BufferedImage getImage() {
        if (direction == null)
            return projectileImages.getNorth();

        if (direction.equals(Vector.NORTH)) {
            return projectileImages.getNorth();
        } else if (direction.equals(Vector.EAST)) {
            return projectileImages.getEast();
        } else if (direction.equals(Vector.SOUTH)) {
            return projectileImages.getSouth();
        } else {//direction.equals(Vector.WEST)
            return projectileImages.getWest();
        }
    }
}
