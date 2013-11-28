package Items;

import Components.ItemGraphicsComponent;
import WIP.DebugLog;

/**
 * Created with IntelliJ IDEA.
 * User: Jean-Luc
 * Date: 04.11.13
 * Time: 17:17
 * <p/>
 * Not required to be abstract because all armours have the same behaviour
 */
public class Armour extends Equipment {
    private int defence;
    private final ArmourType armourType;

    public Armour(String name, int level, int skillRequirement, EquipmentClass armourClass, int defence,
                  ArmourType armourType, ItemGraphicsComponent g) {
        super(name, level, skillRequirement, armourClass, g);
        this.defence = defence;
        this.armourType = armourType;
        DebugLog.write("New Armour created: " + name);
    }

    public int getDefence() {
        return defence;
    }

    public void setDefence(int defence) {
        this.defence = defence;
        DebugLog.write("Set Armour " + getName() + " defense to: " + defence);
    }

    public ArmourType getArmourType() {
        return armourType;
    }

    @Override
    public boolean isCollidable() {
        return false;
    }

    @Override
    public boolean isStackable() {
        return false;
    }

    public enum ArmourType {
        HELMET("Helmet"), BODY("Body"), LEGS("Legs"), SHIELD("Shield");

        private final String name;

        private ArmourType(String name) {
            this.name = name;
        }

        public String toString() {
            return name;
        }
    }
}
