package Items;

import Components.ItemGraphicsComponent;

/**
 * Created with IntelliJ IDEA.
 * User: Jean-Luc
 * Date: 21.01.14
 * Time: 10:08
 */
public class Legs extends Armour {
    public Legs(String name, int level, int skillRequirement, EquipmentClass armourClass, int defence, ItemGraphicsComponent g) {
        super(name, level, skillRequirement, armourClass, defence, ArmourType.LEGS, g);
    }
}
