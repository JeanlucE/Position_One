package Components;

import Actors.Actor;
import Actors.AnimationState;
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
    private ActorGraphicsResource images;

    public ActorGraphicsComponent(ActorGraphicsResource images) {
        this.images = images;
    }

    public BufferedImage getImage() {
        if (parent.isFacing(Vector.NORTH))
            return getNorth();
        else if (parent.isFacing(Vector.EAST))
            return getEast();
        else if (parent.isFacing(Vector.SOUTH))
            return getSouth();
        else if (parent.isFacing(Vector.WEST))
            return getWest();
        else
            return getSouth();
    }

    //This must be called right after the actors graphic component is created
    public void setParent(Actor parent) {
        this.parent = parent;
    }

    public ActorGraphicsComponent clone() {
        return new ActorGraphicsComponent(images);
    }

    private BufferedImage getNorth() {
        if (parent.getAnimationState() == AnimationState.IDLE) {
            images.resetAnimations();
            return images.getNorthIdle();
        } else {
            return images.getNorthWalk();
        }
    }

    private BufferedImage getEast() {
        if (parent.getAnimationState() == AnimationState.IDLE) {
            images.resetAnimations();
            return images.getEastIdle();
        } else {
            return images.getEastWalk();
        }
    }


    private BufferedImage getSouth() {
        if (parent.getAnimationState() == AnimationState.IDLE) {
            images.resetAnimations();
            return images.getSouthIdle();
        } else {
            return images.getSouthWalk();

        }
    }

    private BufferedImage getWest() {
        if (parent.getAnimationState() == AnimationState.IDLE) {
            images.resetAnimations();
            return images.getWestIdle();
        } else {
            return images.getWestWalk();
        }
    }
}
