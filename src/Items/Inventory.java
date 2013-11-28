package Items;

import WIP.DebugLog;

/**
 * Created with IntelliJ IDEA.
 * User: Jean-Luc
 * Date: 05.11.13
 * Time: 22:03
 * <p/>
 * This class organises the character inventory with an array of Inventory slots.
 * <p/>
 * INVENTORYSIZE determines the amount of inventory slots: INVENOTORYSIZE^2
 */
public class Inventory {

    private static final int INVENTORYSIZE = 5;

    private InventorySlot[][] inventorySlots;

    public Inventory() {
        inventorySlots = new InventorySlot[INVENTORYSIZE][INVENTORYSIZE];
        for (int i = 0; i < inventorySlots.length; i++) {
            for (int j = 0; j < inventorySlots[0].length; j++) {
                inventorySlots[i][j] = new InventorySlot();
            }
        }
    }

    public void add(Item item) {
        if (item != null && !isFull()) {
            getFirstEmpty().addItem(item);
            DebugLog.write(this);
        }
    }

    public InventorySlot getSlot(int x, int y) {
        try {
            return inventorySlots[x][y];
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("There is no inventory slot with position: " + x + "|" + y);
        }
        return null;
    }

    //Called when an item is moved from one inventory slot to another
    //Player can only move an item from an inventory slot, if it actually contains an item
    public void swap(int fromX, int fromY, int toX, int toY) {
        if (getSlot(toX, toY).isOccupied()) {

            Item item = getSlot(toX, toY).getItem();
            getSlot(toX, toY).addItem(getSlot(fromX, fromY).getItem());
            getSlot(fromX, fromY).addItem(item);

        } else {
            getSlot(toX, toY).addItem(getSlot(fromX, fromY).getItem());
            getSlot(fromX, fromY).empty();
        }
    }

    public boolean isFull() {
        boolean bool = true;
        for (int i = 0; i < inventorySlots.length && bool; i++) {
            for (int j = 0; j < inventorySlots[0].length && bool; j++) {
                bool = getSlot(i, j).isOccupied();
            }
        }
        return bool;
    }

    private InventorySlot getFirstEmpty() {
        for (int i = 0; i < INVENTORYSIZE; i++) {
            for (int j = 0; j < INVENTORYSIZE; j++) {
                if (!getSlot(i, j).isOccupied())
                    return getSlot(i, j);
            }
        }
        DebugLog.write("No empty inventory slot found!");
        return null;
    }

    public String toString() {
        String str = "";
        for (int i = 0; i < INVENTORYSIZE; i++) {
            for (int j = 0; j < INVENTORYSIZE; j++) {
                if (getSlot(i, j).isOccupied()) {
                    String itemName = getSlot(i, j).getItem().getName();
                    str += ("Item at " + i + "|" + j + ": " + itemName + "\n");
                }
            }
        }
        return str;
    }

    public class InventorySlot {
        private Item item = null;

        //Should only be called when isOccupied returns true
        public Item getItem() {
            if (isOccupied()) {
                return item;
            }
            return null;
        }

        public void addItem(Item item) {
            if (!isOccupied()) {
                this.item = item;
            } else {
                if (item instanceof Arrow && this.item instanceof Arrow && ((Arrow) item).equals((Arrow) this.item))
                    ((Arrow) this.item).addToStack(((Arrow) item).getStack());
            }
        }

        public boolean isOccupied() {
            return item != null;
        }

        public void empty() {
            addItem(null);
        }
    }
}
