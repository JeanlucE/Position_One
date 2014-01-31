package Components;

import Environment.Interactable;
import WIP.DebugLog;

import java.awt.image.BufferedImage;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 12/12/13
 * Time: 10:45 PM
 */
public class InteractableGraphicsComponent extends GraphicsComponent {

    private Interactable parent;
    private DynamicResource images;

    public InteractableGraphicsComponent(Interactable parent, DynamicResource images) {
        if (images.getSize() != 4) {
            DebugLog.write("Dynamic Resource size for Interactable graphics must be of length 2");
            throw new IllegalArgumentException();
        }
        this.parent = parent;
        this.images = images;
    }

    @Override
    public BufferedImage getImage() {
        if (parent.isActivated())
            return images.getActivated();
        else
            return images.getNotActivated();
    }
}
