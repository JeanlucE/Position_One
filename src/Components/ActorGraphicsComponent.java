package Components;

import Actors.Actor;
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
    private boolean hasAnimations;

    public ActorGraphicsComponent(ActorGraphicsResource images, boolean hasAnimations) {
        this.images = images;
        this.hasAnimations = hasAnimations;
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
        return new ActorGraphicsComponent(images, hasAnimations);
    }

    private BufferedImage getNorth() {
        if (parent.getAnimationState().isIdle() || !hasAnimations) {
            images.resetAnimations();
            return images.getNorthIdle();
        } else if (parent.getAnimationState().isWalking()) {
            images.getNORTH_WALK().setAnimationIncrement(55);
            return images.getNorthWalk();
        } else {
            images.getNORTH_WALK().setAnimationIncrement(35);
            return images.getNorthWalk();
        }
    }

    private BufferedImage getEast() {
        if (parent.getAnimationState().isIdle() || !hasAnimations) {
            images.resetAnimations();
            return images.getEastIdle();
        } else if (parent.getAnimationState().isWalking()) {
            images.getEAST_WALK().setAnimationIncrement(60);
            return images.getEastWalk();
        } else {
            images.getEAST_WALK().setAnimationIncrement(40);
            return images.getEastWalk();
        }
    }


    private BufferedImage getSouth() {
        if (parent.getAnimationState().isIdle() || !hasAnimations) {
            images.resetAnimations();
            return images.getSouthIdle();
        } else if (parent.getAnimationState().isWalking()) {
            images.getSOUTH_WALK().setAnimationIncrement(55);
            return images.getSouthWalk();
        } else {
            images.getSOUTH_WALK().setAnimationIncrement(35);
            return images.getSouthWalk();
        }
    }

    private BufferedImage getWest() {
        if (parent.getAnimationState().isIdle() || !hasAnimations) {
            images.resetAnimations();
            return images.getWestIdle();
        } else if (parent.getAnimationState().isWalking()) {
            images.getWEST_WALK().setAnimationIncrement(60);
            return images.getWestWalk();
        } else {
            images.getWEST_WALK().setAnimationIncrement(40);
            return images.getWestWalk();
        }
    }
}
