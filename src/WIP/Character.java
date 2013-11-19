package WIP;

import Components.ActorGraphicsComponent;
import Components.InputComponent;
import Components.PhysicsComponent;
import Components.Resource;
import Items.*;

import java.lang.reflect.Field;

/**
 * Created with IntelliJ IDEA.
 * User: Jean-Luc
 * Date: 30.09.13
 * Time: 16:00
 */
public class Character extends Actor {

    //region Stats
    private int level;
    private int experience;
    private int skillPoints;
    private int maxHealth, currenthealth;
    private int maxStamina, currentStamina;
    private int maxMana, currentMana;
    //endregion

    //region Skills
    public Skill STRENGTH;
    public Skill ENDURANCE;
    public Skill INTELLIGENCE;
    public Skill DEXTERITY;
    public Skill CHARISMA;
    public Skill PERCEPTION;
    public Skill LUCK;
    public Skill STEALTH;
    //Array of all skills
    private Skill[] skills;
    //endregion

    //region Player Items
    private final Inventory inventory;
    private EquipmentSlot helmetSlot;
    private EquipmentSlot bodySlot;
    private EquipmentSlot legsSlot;
    private EquipmentSlot mainHandSlot;
    private EquipmentSlot offHandSlot;
    //endregion

    private int moveSpeed = 3;

    //Constructor for final build
    public Character(Game game, String name) {
        this(game, name, 0, 0, 0);
    }

    //Debug Purposes
    public Character(Game game, String name, int level, int experience, int skillPoints) {
        super(name,
                new Transform(),
                new ActorGraphicsComponent(new Resource[]{
                        Resource.player_UP,
                        Resource.player_DOWN,
                        Resource.player_LEFT,
                        Resource.player_RIGHT
                }),
                new PhysicsComponent(30, 30));
        ((ActorGraphicsComponent)this.getGraphic()).setParent(this);

        this.level = level;
        this.experience = experience;
        this.skillPoints = skillPoints;

        maxHealth = 100;
        currenthealth = maxHealth;

        maxMana = 20;
        currentMana = maxMana;

        maxStamina = 100;
        currentStamina = maxStamina;

        this.inventory = new Inventory();
        this.instantiateSkills();
        this.instantiateEquipmentSlots();
        DebugLog.write("New Character created: " + name);
    }

    public void update() {
        currentXVelocity = InputComponent.getInstance().getXAxis() * moveSpeed;
        currentYVelocity = InputComponent.getInstance().getYAxis() * moveSpeed;
        if (currentXVelocity != 0 || currentYVelocity != 0) {
            move();
        }
    }
    //endregion

    static int getXPOfLevel(int level) {
        return 20 * level * level;
    }

    /*
    Gets all public fields in Character.java and instatntiates each of the skills.
    Skill names are read the name of the public Field like so:
    "STRENGTH"'s name would be "Strength"
    It then adds all skills to an array for later purposes
     */
    private void instantiateSkills() {
        Field[] fields = this.getClass().getFields();
        skills = new Skill[fields.length];
        int i = 0;
        for (Field f : fields) {
            try {
                f.set(this, new Skill(getSkillName(f)));
                skills[i] = (Skill) f.get(this);
                i++;
            } catch (IllegalAccessException e) {
                System.out.println("Player skill creation failed!");
                e.printStackTrace();
            }
        }
    }

    private String getSkillName(Field f) {
        return f.getName().charAt(0) + f.getName().substring(1, f.getName().length()).toLowerCase();
    }

    /*
    Instantiates all equipment slots with what kind of equipment that particular slot can carry
    Main hand should always be a weapon
    Problems could arise here with Two-Handed and off-hand weapons
     */
    private void instantiateEquipmentSlots() {
        helmetSlot = new EquipmentSlot(Armour.class);
        bodySlot = new EquipmentSlot(Armour.class);
        legsSlot = new EquipmentSlot(Armour.class);
        mainHandSlot = new EquipmentSlot(Weapon.class);
        offHandSlot = new EquipmentSlot(Armour.class);
    }

    //region Health, Mana, Stamina
    public int getMaxHealth() {
        return maxHealth;
    }

    public int getMaxStamina() {
        return maxStamina;
    }

    public int getMaxMana() {
        return maxMana;
    }

    public int getCurrentMana() {
        return currentMana;
    }

    public void setCurrentMana(int currentMana) {
        this.currentMana = currentMana;
    }

    public int getCurrentStamina() {
        return currentStamina;
    }

    public void setCurrentStamina(int currentStamina) {
        this.currentStamina = currentStamina;
    }

    public int getCurrenthealth() {
        return currenthealth;
    }

    public void setCurrenthealth(int currenthealth) {
        this.currenthealth = currenthealth;
    }
    //endregion

    //region Level and Skill Methods
    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
        DebugLog.write("{DEBUG} Player level set to: " + level);
    }

    //TODO: separate method for assigning skill points
    //TODO: every time skill points are newly assigned recalculate maxHealth, maxMana, maxStamina
    private void levelUp() {

        System.out.println("Level up!");
        level++;
        skillPoints++;

        DebugLog.write("Player " + getName() + "has leveled up. \nPlayer " + getName() + "is now level" + level);

        //Takes text input for which skill should be leveled up
        String skillToLevelUp = Test.input.nextLine();
        for (Skill s : skills) {
            if (s.getName().toLowerCase().startsWith(skillToLevelUp)) {
                s.setLevel(s.getLevel() + 1);
                skillPoints--;
            }
        }
    }

    public void addExperience(int experience) {
        this.experience += experience;
        while (getNextLevelXP() <= 0) {
            levelUp();
        }
        DebugLog.write("Player gained experience: " + experience);
    }

    int getNextLevelXP() {
        return Character.getXPOfLevel(level + 1) - this.experience;
    }

    public int getSkillPoints() {
        return skillPoints;
    }

    public void setSkillPoints(int skillPoints) {
        this.skillPoints = skillPoints;
        DebugLog.write("{DEBUG} Player skill points set to: " + skillPoints);
    }

    //endregion

    //region Equipment Methods
    public Inventory getInventory() {
        return inventory;
    }

    /*
    Should only be called when equipment slot is not occupied
    This method equips the player with a piece of equipment.
    It first checks if the level and skill requirements of the equipment are met by the player.
    If the piece of equipment is a weapon it then assigns the weapon to the main hand slot.
    If the piece of equipment is an armour piece, it then check what armour type it is (Helmet, Body,
    Legs, Shield) and then assigns it to the appropriate equipment slot.
     */
    public void equip(Equipment equipment) {
        //TODO if isOccupied throw the current previous equipment into the inventory
        if (canEquip(equipment)) {
            if (Weapon.class.isAssignableFrom(equipment.getClass())) {
                if (canEquip(equipment))
                    mainHandSlot.setEquipment(equipment);
            } else {
                if (((Armour) equipment).getArmourType().equals(Armour.ArmourType.SHIELD)) {
                    offHandSlot.setEquipment(equipment);
                } else if (((Armour) equipment).getArmourType().equals(Armour.ArmourType.HELMET)) {
                    helmetSlot.setEquipment(equipment);
                } else if (((Armour) equipment).getArmourType().equals(Armour.ArmourType.BODY)) {
                    bodySlot.setEquipment(equipment);
                } else if (((Armour) equipment).getArmourType().equals(Armour.ArmourType.LEGS)) {
                    legsSlot.setEquipment(equipment);
                }
            }
            DebugLog.write("Player " + getName() + " has equipped: " + equipment.getName());
        } else {
            DebugLog.write("Player " + getName() + " cannot equip: " + equipment.getName());
        }

    }

    boolean canEquip(Equipment equipment) {
        //Checks if Player is high enough level
        boolean metLevelReq = level >= equipment.getLevel();

        //Checks if Player has a high enough skill in either strength, intelligence or dexterity
        Equipment.EquipmentClass equipmentClass = equipment.getType();
        boolean metSkillReq;
        if (equipmentClass.equals(Equipment.EquipmentClass.MELEE)) {
            metSkillReq = STRENGTH.getLevel() >= equipment.getSkillRequirement();
        } else if (equipmentClass.equals(Equipment.EquipmentClass.MAGIC)) {
            metSkillReq = INTELLIGENCE.getLevel() >= equipment.getSkillRequirement();
        } else {
            metSkillReq = DEXTERITY.getLevel() >= equipment.getSkillRequirement();
        }


        return metLevelReq && metSkillReq;
    }

    //takes an equipmentslot that should be unequipped and when in the inventory the item should be put
    public void unequip(EquipmentSlot equipmentSlot, Inventory.InventorySlot inventorySlot) {
        if (!inventorySlot.isOccupied() && equipmentSlot.isOccupied()) {
            inventorySlot.setItem(equipmentSlot.getEquipment());
            equipmentSlot.setEquipment(null);
        } else {
            System.out.println("Debug: Method Character.unequip called for an occupied inventory slot or an " +
                    "unoccupied equipment slot.");
        }
    }

    public EquipmentSlot getHelmetSlot() {
        return helmetSlot;
    }

    public EquipmentSlot getOffHandSlot() {
        return offHandSlot;
    }

    public EquipmentSlot getMainHandSlot() {
        return mainHandSlot;
    }

    public EquipmentSlot getLegsSlot() {
        return legsSlot;
    }

    public EquipmentSlot getBodySlot() {
        return bodySlot;
    }

    public Equipment getHelmet() {
        return helmetSlot.getEquipment();
    }

    public Equipment getBody() {
        return bodySlot.getEquipment();
    }

    public Equipment getLegs() {
        return legsSlot.getEquipment();
    }

    public Equipment getMainHand() {
        return mainHandSlot.getEquipment();
    }

    public Equipment getOffHand() {
        return offHandSlot.getEquipment();
    }

    public String printEquipment() {
        String equipment = "";
        equipment += "Helmet: " + helmetSlot.toString() + "\n";
        equipment += "Body: " + bodySlot.toString() + "\n";
        equipment += "Legs: " + legsSlot.toString() + "\n";
        equipment += "Main Hand: " + mainHandSlot.toString() + "\n";
        equipment += "Offhand: " + offHandSlot.toString();
        return equipment;
    }
    //endregion

    //For Debugging purposes
    public String toString() {
        return "Character: " + getName() + ", " + this.level + ", " + this.experience;
    }
}
