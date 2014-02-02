package Components;

import WIP.DebugLog;
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
     */
    Animation(Resource[] componentImages) {
        this.componentImages = componentImages;
        animationIncrement = 10;
    }

    private int iterator = 0;
    private long lastAnimationFrame = 0;

    BufferedImage getCurrentAnimationFrame() {
        if (animationIncrement == 10)
            DebugLog.write("Animation: No animation increment set!", true);
        if (lastAnimationFrame == 0)
            lastAnimationFrame = Time.getTimeStamp();

        BufferedImage b = componentImages[iterator].getImage();
        if (Time.getTimeStamp() - lastAnimationFrame >= getAnimationIncrement()) {
            iterator = (iterator + 1) % componentImages.length;
            lastAnimationFrame = Time.getTimeStamp();
        }
        return b;
    }

    void resetAnimation() {
        iterator = 0;
    }

    public int getAnimationIncrement() {
        return animationIncrement;
    }

    public void setAnimationIncrement(int animationIncrement) {
        this.animationIncrement = animationIncrement;
    }
}
