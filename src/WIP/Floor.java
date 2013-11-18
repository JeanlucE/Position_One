package WIP;

import Components.GraphicsComponent;
import Items.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Jean-Luc
 * Date: 07.11.13
 * Time: 15:20
 */
public class Floor extends WorldSpace {

    private List<Item> droppedItems;

    public Floor(Transform transform, GraphicsComponent g) {
        super(transform, g);
    }

    public boolean hasDroppedItems() {
        return droppedItems != null && droppedItems.size() != 0;
    }

    public Item[] getDroppedItems() {
        return droppedItems.toArray(new Item[droppedItems.size()]);
    }

    public void dropItem(Item item) {
        droppedItems = (droppedItems == null) ? droppedItems = new ArrayList<>() : droppedItems;
        droppedItems.add(item);
    }

    /*public void setDroppedItems(Item[] droppedItems) {
        this.droppedItems = droppedItems;
    }*/

    @Override
    public boolean isCollidable() {
        return false;
    }
}
