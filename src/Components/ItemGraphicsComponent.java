package Components;

import java.awt.image.BufferedImage;

/**
 * Created with IntelliJ IDEA.
 * User: Jean-Luc
 * Date: 18.11.13
 * Time: 10:00
 */
public class ItemGraphicsComponent extends GraphicsComponent {

    private BufferedImage[] images;

    public ItemGraphicsComponent() {

    }

    //TODO later Take Dynamic Resource as parameter as soon as i have the sprites
    public ItemGraphicsComponent(Resource resource) {
        images = new BufferedImage[2];
        images[0] = resource.getImage();
    }

    //returns dropped image
    public BufferedImage getImage() {
        return images[0];
    }

    //returns image in inventory
    public BufferedImage getInventoryImage() {
        return images[1];
    }
}
