package Components;

import WIP.GameObject;
import WIP.Vector;

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
        if (parent.getTransform().getDirection().equals(Vector.NORTH))
            return image[0];
        else if (parent.getTransform().getDirection().equals(Vector.SOUTH))
            return image[1];
        else if (parent.getTransform().getDirection().equals(Vector.WEST))
            return image[2];
        else if (parent.getTransform().getDirection().equals(Vector.EAST))
            return image[3];
        else
            return image[0];
    }

    //This must be called right after the actors graphic component is created
    public void setParent(GameObject parent) {
        this.parent = parent;
    }
}
