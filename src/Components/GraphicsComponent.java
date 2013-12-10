package Components;

import java.awt.image.BufferedImage;

/**
 * Created with IntelliJ IDEA.
 * User: Jean-Luc
 * Date: 09.11.13
 * Time: 16:15
 */
public abstract class GraphicsComponent {

    protected BufferedImage[] image;

    //DEBUGGING
    public GraphicsComponent() {
        image = null;
    }

    public abstract BufferedImage getImage();


}
