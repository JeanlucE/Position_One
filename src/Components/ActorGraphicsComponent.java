package Components;

import WIP.GameObject;

import java.awt.image.BufferedImage;

/**
 * Created with IntelliJ IDEA.
 * User: Jean-Luc
 * Date: 18.11.13
 * Time: 09:30
 */
public class ActorGraphicsComponent extends GraphicsComponent {

    private GameObject parent;

    public ActorGraphicsComponent(Resource[] resource) {
        //resource.length must be 4
        image = new BufferedImage[resource.length];
        for (int i = 0; i < resource.length; i++) {
            image[i] = resource[i].getImage();
        }
    }

    public BufferedImage getImage() {
        switch (parent.getTransform().getDirection()) {
            case NORTH:
                return image[0];
            case SOUTH:
                return image[1];
            case WEST:
                return image[2];
            case EAST:
                return image[3];
            default:
                return image[0];
        }
    }

    //This must be called right after the actors graphic component is created
    public void setParent(GameObject parent) {
        this.parent = parent;
    }
}
