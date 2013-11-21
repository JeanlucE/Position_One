package Components;

import java.awt.image.BufferedImage;

/**
 * Created with IntelliJ IDEA.
 * User: Jean-Luc
 * Date: 18.11.13
 * Time: 10:00
 */
public class ItemGraphicsComponent extends GraphicsComponent {

    public ItemGraphicsComponent() {

    }

    public ItemGraphicsComponent(Resource resource) {
        image = new BufferedImage[2];
        System.out.println();
        image[0] = resource.getImage();
    }

    //returns dropped image
    public BufferedImage getImage() {
        return image[0];
    }

    //returns image in inventory
    public BufferedImage getInventoryImage() {
        return null;
    }
}
