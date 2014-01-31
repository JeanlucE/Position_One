package Components;

/**
 * Created with IntelliJ IDEA.
 * User: Jean-Luc
 * Date: 31.01.14
 * Time: 20:43
 */

import java.awt.image.BufferedImage;

/**
 * Object that holds all the images for the actorgraphicscomponent for easier construction and better overview.
 * 0 - 9 North Animation images
 * 10 - 19 East Animation images
 * 20 - 29 South Animation Images
 * 30- 39 West Aninmation Images
 * 40 North Idle image
 * 41 East Idle image
 * 42 South idle image
 * 43 West idle image
 */
public class ActorGraphicsResource extends DynamicResource {

    private final Resource NORTH_IDLE;
    private final Resource EAST_IDLE;
    private final Resource SOUTH_IDLE;
    private final Resource WEST_IDLE;
    private final Animation NORTH_WALK;
    private final Animation EAST_WALK;
    private final Animation SOUTH_WALK;
    private final Animation WEST_WALK;

    public ActorGraphicsResource(Resource NORTH_IDLE, Resource EAST_IDLE, Resource SOUTH_IDLE, Resource WEST_IDLE,
                                 Animation NORTH_WALK, Animation EAST_WALK, Animation SOUTH_WALK, Animation WEST_WALK) {
        this.NORTH_IDLE = NORTH_IDLE;
        this.EAST_IDLE = EAST_IDLE;
        this.SOUTH_IDLE = SOUTH_IDLE;
        this.WEST_IDLE = WEST_IDLE;
        this.NORTH_WALK = NORTH_WALK;
        this.EAST_WALK = EAST_WALK;
        this.SOUTH_WALK = SOUTH_WALK;
        this.WEST_WALK = WEST_WALK;
    }

    BufferedImage getNorthIdle() {
        return NORTH_IDLE.getImage();
    }

    BufferedImage getEastIdle() {
        return EAST_IDLE.getImage();
    }

    BufferedImage getSouthIdle() {
        return SOUTH_IDLE.getImage();
    }

    BufferedImage getWestIdle() {
        return WEST_IDLE.getImage();
    }

    BufferedImage getNorthWalk() {
        if (NORTH_WALK == null) {
            return SOUTH_IDLE.getImage();
        }
        return NORTH_WALK.getCurrentAnimationFrame();
    }

    BufferedImage getEastWalk() {
        if (EAST_WALK == null) {
            return SOUTH_IDLE.getImage();
        }
        return EAST_WALK.getCurrentAnimationFrame();
    }

    BufferedImage getSouthWalk() {
        if (SOUTH_WALK == null) {
            return SOUTH_IDLE.getImage();
        }
        return SOUTH_WALK.getCurrentAnimationFrame();
    }

    BufferedImage getWestWalk() {
        if (WEST_WALK == null) {
            return SOUTH_IDLE.getImage();
        }
        return WEST_WALK.getCurrentAnimationFrame();
    }

    void resetAnimations() {
        if (NORTH_WALK != null) {
            NORTH_WALK.resetAnimation();
        }
        if (EAST_WALK != null) {
            EAST_WALK.resetAnimation();
        }
        if (SOUTH_WALK != null) {
            SOUTH_WALK.resetAnimation();
        }
        if (WEST_WALK != null) {
            WEST_WALK.resetAnimation();
        }
    }
}
