package Items;

import Components.ItemGraphicsComponent;
import WIP.DebugLog;
import WIP.GameObject;
import WIP.Transform;
import WIP.Vector;

/**
 * Created with IntelliJ IDEA.
 * User: Jean-Luc
 * Date: 05.11.13
 * Time: 16:09
 */
public abstract class Item extends GameObject {
    protected String name;

    protected Item(String name, Vector position, ItemGraphicsComponent g) {
        super(new Transform(position), g);
        this.name = name;
    }

    public void update() {
        //Leave empty
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        DebugLog.write("Renamed item " + this.name + " + to:" + name);
        this.name = name;

    }

    public boolean equals(Item item) {
        return this.name.equals(item.getName());
    }

    public boolean isCollidable() {
        return false;
    }

    public abstract boolean isStackable();
}
