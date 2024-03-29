package Items;

import Actors.Actor;
import Components.ItemGraphicsComponent;
import WIP.DebugLog;
import WIP.Vector;

/**
 * Created with IntelliJ IDEA.
 * User: Jean-Luc
 * Date: 04.11.13
 * Time: 19:46
 */
public abstract class Equipment extends Item {
    protected int level;
    protected int skillRequirement;
    protected final EquipmentClass equipmentClass;
    //Actor that has the equipment equipped
    protected Actor equipped;

    protected Equipment(String name, int level, int skillRequirement, EquipmentClass type, ItemGraphicsComponent g) {
        super(name, new Vector(), g);
        this.level = level;
        this.skillRequirement = skillRequirement;
        this.equipmentClass = type;
    }

    //Gets the skill skillRequirement level for the weapon:
    //Melee: Strength
    //Magic: Intelligence
    //Ranged: Dexterity
    public EquipmentClass getType() {
        return equipmentClass;
    }

    public abstract boolean isWeapon();

    public abstract boolean isArmour();

    public abstract boolean isAmmunition();

    public void setEquipped(Actor actor) {
        equipped = actor;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
        DebugLog.write("Set Equipment " + this.getName() + " level requirement to: " + level);
    }

    public int getSkillRequirement() {
        return skillRequirement;
    }

    public void setSkillRequirement(int skillRequirement) {
        this.skillRequirement = skillRequirement;
        DebugLog.write("Set Equipment " + this.getName() + " skill requirement to: " + skillRequirement);
    }

    public enum EquipmentClass {
        MELEE("Melee"), MAGIC("Magic"), RANGED("Ranged");

        private final String name;

        private EquipmentClass(String name) {
            this.name = name;
        }

        public String toString() {
            return name;
        }
    }
}
