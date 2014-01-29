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

    private static final int INVENTORYSIZE = 24;

    private InventorySlot[] inventorySlots;

    public Inventory() {
        inventorySlots = new InventorySlot[INVENTORYSIZE];
        for (int i = 0; i < inventorySlots.length; i++) {
            inventorySlots[i] = new InventorySlot();

        }
    }

    public void add(Item item) {
        if (item != null && !isFull()) {
            getFirstEmpty().addItem(item);
            DebugLog.write(this.toString());
        }
    }

    public InventorySlot getSlot(int x) {
        try {
            return inventorySlots[x];
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("There is no inventory slot at position: " + x);
        }
        return null;
    }

    //Called when an item is moved from one inventory slot to another
    //Player can only move an item from an inventory slot, if it actually contains an item
    public void swap(int from, int to) {
        if (getSlot(to).isOccupied()) {

            Item item = getSlot(to).getItem();
            getSlot(to).addItem(getSlot(from).getItem());
            getSlot(from).addItem(item);

        } else {
            getSlot(to).addItem(getSlot(from).getItem());
            getSlot(from).empty();
        }
    }

    public boolean isFull() {
        for (int i = 0; i < inventorySlots.length; i++) {
            if (!getSlot(i).isOccupied())
                return false;

        }
        return true;
    }

    private InventorySlot getFirstEmpty() {
        for (int i = 0; i < INVENTORYSIZE; i++) {
            if (!getSlot(i).isOccupied())
                return getSlot(i);

        }
        DebugLog.write("No empty inventory slot found!");
        return null;
    }

    public String toString() {
        String str = "";
        for (int i = 0; i < INVENTORYSIZE; i++) {
            if (getSlot(i).isOccupied()) {
                str += ("Item at " + i + ": " + getSlot(i).toString() + "; ");
            }

        }
        return str;
    }

    private class InventorySlot {
        private Item item = null;

        //Should only be called when isOccupied returns true
        private Item getItem() {
            if (isOccupied()) {
                return item;
            }
            return null;
        }

        private void addItem(Item item) {
            if (!isOccupied()) {
                this.item = item;
            } else {
                if (item instanceof Ammunition && this.item instanceof Ammunition && ((Ammunition) item).equals((Ammunition) this.item))
                    ((Ammunition) this.item).addToStack(((Ammunition) item).getStack());
            }
        }

        private boolean isOccupied() {
            return item != null;
        }

        private void empty() {
            addItem(null);
        }

        public String toString() {
            return getItem().getName();
        }
    }
}
