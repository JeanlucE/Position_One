package Components;

import Environment.Interactable;

import java.awt.image.BufferedImage;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 12/12/13
 * Time: 10:45 PM
 */
public class InteractableGraphicsComponent extends GraphicsComponent{

    private Interactable parent;
    private DynamicResource images;

    public InteractableGraphicsComponent(Interactable parent, DynamicResource images) {
        this.parent = parent;
        this.images = images;
    }

    @Override
    public BufferedImage getImage() {
        if(parent.isActivated())
            return images.getActivated();
        else
            return images.getNotActivated();
    }
}
