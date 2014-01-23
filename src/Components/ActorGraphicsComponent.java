package Components;

import Actors.Actor;
import WIP.DebugLog;
import WIP.Vector;

import java.awt.image.BufferedImage;

/**
 * Created with IntelliJ IDEA.
 * User: Jean-Luc
 * Date: 18.11.13
 * Time: 09:30
 */
public class ActorGraphicsComponent extends GraphicsComponent {

    private Actor parent;
    private DynamicResource images;

    public ActorGraphicsComponent(DynamicResource dynamicResource) {
        if (dynamicResource.getSize() != 4) {
            DebugLog.write("Dynamic Resource size for Actor graphics must be of length 4");
            throw new IllegalArgumentException();
        }
        images = dynamicResource;
    }

    public BufferedImage getImage() {
        if (parent.getTransform().getDirection().equals(Vector.NORTH))
            return images.getNorth();
        else if (parent.getTransform().getDirection().equals(Vector.SOUTH))
            return images.getSouth();
        else if (parent.getTransform().getDirection().equals(Vector.WEST))
            return images.getWest();
        else if (parent.getTransform().getDirection().equals(Vector.EAST))
            return images.getEast();
        else
            return images.getNorth();
    }

    //This must be called right after the actors graphic component is created
    public void setParent(Actor parent) {
        this.parent = parent;
    }

    public ActorGraphicsComponent clone() {
        return new ActorGraphicsComponent(images);
    }
}
