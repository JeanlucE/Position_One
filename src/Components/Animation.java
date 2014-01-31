package Components;

import WIP.Time;

import java.awt.image.BufferedImage;

/**
 * Created with IntelliJ IDEA.
 * User: Jean-Luc
 * Date: 31.01.14
 * Time: 21:22
 */
public class Animation {

    private Resource[] componentImages;
    private int animationIncrement;

    /**
     * Creates an animation with the given images. The images will then get cycled through when they are called. Each
     * cycle takes componentImages.length * timePerAnimationFrame in milliseconds.
     *
     * @param componentImages       Each animation frame
     * @param timePerAnimationFrame Time to show each animation frame in milliseconds.
     */
    Animation(Resource[] componentImages, int timePerAnimationFrame) {
        this.componentImages = componentImages;
        animationIncrement = timePerAnimationFrame;
    }

    private int iterator = 0;
    private long lastAnimationFrame = 0;

    BufferedImage getCurrentAnimationFrame() {
        if (lastAnimationFrame == 0)
            lastAnimationFrame = Time.getTimeStamp();

        BufferedImage b = componentImages[iterator].getImage();
        if (Time.getTimeStamp() - lastAnimationFrame >= animationIncrement) {
            iterator = (iterator + 1) % componentImages.length;
            lastAnimationFrame = Time.getTimeStamp();
        }
        return b;
    }

    void resetAnimation() {
        iterator = 0;
    }
}
