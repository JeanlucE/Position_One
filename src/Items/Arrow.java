package Items;

import Components.ItemGraphicsComponent;

/**
 * Created with IntelliJ IDEA.
 * User: Jean-Luc
 * Date: 27.11.13
 * Time: 23:26
 */
public class Arrow extends Equipment {

    protected int stack;

    public Arrow(String name, int level, ItemGraphicsComponent g) {
        super(name, level, 0, EquipmentClass.RANGED, g);
    }

    public int getStack() {
        return stack;
    }

    public void addToStack(int amount) {
        stack += amount;
    }

    public int removeFromStack() {
        if (stack > 0) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public boolean isStackable() {
        return true;
    }

    public boolean equals(Arrow p) {
        if (p == null) return false;
        if (this == p) return true;
        if (!p.getName().equals(this.getName())) return false;
        if (!(p.getLevel() == this.getLevel())) return false;
        if (!(p.getSkillRequirement() == this.getSkillRequirement())) return false;
        if (!(p.getType().equals(this.getType()))) return false;

        return true;
    }
}
