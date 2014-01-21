package Items;

import Components.ItemGraphicsComponent;

/**
 * Created with IntelliJ IDEA.
 * User: Jean-Luc
 * Date: 21.01.14
 * Time: 10:05
 */
public class Helmet extends Armour {
    public Helmet(String name, int level, int skillRequirement, EquipmentClass armourClass, int defence, ItemGraphicsComponent g) {
        super(name, level, skillRequirement, armourClass, defence, ArmourType.HELMET, g);
    }
}
