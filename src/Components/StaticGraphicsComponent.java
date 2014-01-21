package Components;

import java.awt.image.BufferedImage;

/**
 * Created with IntelliJ IDEA.
 * User: Jean-Luc
 * Date: 18.11.13
 * Time: 09:29
 */
public class StaticGraphicsComponent extends GraphicsComponent {

    private BufferedImage image;

    public StaticGraphicsComponent(Resource resource) {
        image = resource.getImage();
    }

    @Override
    public BufferedImage getImage() {
        return image;
    }
}
