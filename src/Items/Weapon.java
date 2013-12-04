package Items;

import Components.ItemGraphicsComponent;
import WIP.Actor;
import WIP.DebugLog;

/**
 * Created with IntelliJ IDEA.
 * User: Jean-Luc
 * Date: 03.11.13
 * Time: 12:05
 * <p/>
 * Needs an abstract class because different weapons have different behaviour in use()
 */
public abstract class Weapon extends Equipment {

    //region Stats
    //Damage per hit
    private int baseDamage;
    //Hit Range
    private int baseRange;
    //Hits per second
    private float useTime;
    //Actor that has the weapon equipped
    protected Actor equipped;
    //endregion

    /*
    Creates a weapon with the level requirement and skill requirement to wear it.
    Base damage determines the damage of the weapon without skill bonuses.
    Base range determines the range of the weapon in percentage of the tilesize.
    Use time determines how long the delay between 2 attacks is.
    The weaponClass defines what skill is required to equip its weapon.
    GraphicsComponent
     */
    protected Weapon(String name, int level, int skillRequirement, int baseDamage, int baseRange, float useTime,
                     EquipmentClass weaponClass, ItemGraphicsComponent g) {
        super(name, level, skillRequirement, weaponClass, g);
        this.baseDamage = baseDamage;
        this.baseRange = baseRange;
        this.useTime = useTime;
    }

    //Called when a weapon is used
    public abstract void use();


    public void setEquipped(Actor actor) {
        equipped = actor;
    }

    public int getBaseRange() {
        return baseRange;
    }

    public void setBaseRange(int baseRange) {
        this.baseRange = baseRange;
        DebugLog.write("Set Weapon " + this.getName() + " base range to: " + baseRange);
    }

    public int getBaseDamage() {
        return baseDamage;
    }

    public void setBaseDamage(int baseDamage) {
        this.baseDamage = baseDamage;
        DebugLog.write("Set Weapon " + this.getName() + " base damage to: " + baseDamage);
    }

    public float getDPS() {
        return baseDamage / useTime;
    }

    public float getUseTime() {
        return useTime;
    }

    public void setUseTime(float useTime) {
        this.useTime = useTime;
        DebugLog.write("Set Weapon " + this.getName() + " useTime to: " + useTime);
    }

    @Override
    public boolean isStackable() {
        return false;
    }

    public String toString() {
        return this.getName() + ":\n"
                + "Type: " + this.getClass().toString() + "\n"
                + "Level: " + this.getLevel() + "\n"
                + "Skill Requirement: " + this.getSkillRequirement() + "\n"
                + "Weapon Type: " + this.getType().toString() + "\n"
                + "Base Damage: " + this.baseDamage + "\n"
                + "Base Range: " + this.baseRange + "\n"
                + "Hits per Second: " + this.useTime + "\n";
    }
}
