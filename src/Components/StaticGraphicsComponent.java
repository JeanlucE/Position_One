package Components;

import java.awt.image.BufferedImage;

/**
 * Created with IntelliJ IDEA.
 * User: Jean-Luc
 * Date: 18.11.13
 * Time: 09:29
 */
public class StaticGraphicsComponent extends GraphicsComponent{

    public StaticGraphicsComponent(){

    }

    public StaticGraphicsComponent(Resource resource){
        image = new BufferedImage[1];
        image[0] = resource.getImage();
    }

    @Override
    public BufferedImage getImage() {
        return image[0];
    }
}
