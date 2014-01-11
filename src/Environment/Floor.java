package Environment;

import Components.StaticGraphicsComponent;
import Items.Item;
import WIP.Transform;

import java.util.Stack;

/**
 * Created with IntelliJ IDEA.
 * User: Jean-Luc
 * Date: 07.11.13
 * Time: 15:20
 */
public class Floor extends WorldSpace {

    private Stack<Item> itemStack;

    public Floor(Transform transform, StaticGraphicsComponent g) {
        super(transform, g, null);
        itemStack = new Stack<>();
    }

    @Override
    public boolean isWall() {
        return false;
    }

    @Override
    public boolean isFloor() {
        return true;
    }

    @Override
    public boolean isInteractable() {
        return false;
    }

    public boolean hasDroppedItems() {
        return !itemStack.empty();
    }

    public Item[] getDroppedItems() {
        return itemStack.toArray(new Item[itemStack.size()]);
    }

    public Item getTopItem() {
        if (itemStack.size() > 0)
            return itemStack.pop();

        return null;
    }

    public void dropItem(Item item) {
        itemStack.push(item);
    }

    /*public void setDroppedItems(Item[] droppedItems) {
        this.droppedItems = droppedItems;
    }*/

    @Override
    public boolean isCollidable() {
        return false;
    }

    public String toString() {
        return "Floor:" + getTransform().getPosition().toString();
    }
}
