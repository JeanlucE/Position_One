package Components;

import java.awt.image.BufferedImage;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Jean-Luc
 * Date: 09.11.13
 * Time: 16:15
 */
public abstract class GraphicsComponent implements Serializable {

    private final static long serialVersionUID = 93487283489292423l;

    //TODO clean Graphicscomponents up
    protected BufferedImage[] image;

    //DEBUGGING
    public GraphicsComponent() {
        image = null;
    }

    public abstract BufferedImage getImage();


}
