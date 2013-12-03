package WIP;

import Components.*;
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
    private EquipmentManager equipment;
    //endregion

    private int moveSpeed = 3;

    //Constructor for final build
    public Character(String name) {
        this(name, 0, 0, 0);
    }

    //Debug Purposes
    public Character(String name, int level, int experience, int skillPoints) {
        super(name,
                new Transform(),
                new ActorGraphicsComponent(DynamicResource.PLAYER),
                new PhysicsComponent(30, 30));
        ((ActorGraphicsComponent) this.getGraphic()).setParent(this);

        this.level = level;
        this.experience = experience;
        this.skillPoints = skillPoints;

        maxHealth = 100;
        currentHealth = maxHealth;

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
        //TODO update direction player is facing from here
        setXVel(InputComponent.getInstance().getXAxis() * moveSpeed);
        setYVel(InputComponent.getInstance().getYAxis() * moveSpeed);
        if (getXVel() != 0 || getYVel() != 0) {
            move();
        }

        if (InputComponent.getInstance().isSpacePressed()) {
            attack();
            InputComponent.getInstance().resetSpacePressed();
        }

        if (InputComponent.getInstance().isCtrlPressed()) {
            WorldSpace nextAbsPos = Game.getInstance().getCurrentWorld().get(getNextWorldPosition());
            if (nextAbsPos instanceof Floor) {
                Floor floor = (Floor) nextAbsPos;
                inventory.add(floor.hasDroppedItems() ? (floor.getTopItem()) : (null));
            }
            InputComponent.getInstance().resetCtrlPressed();
        }
    }
    //endregion

    public static int getXPOfLevel(int level) {
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
        equipment = new EquipmentManager(this);
    }

    private void attack() {
        Weapon weapon = equipment.getMainHand();
        float range = 1f;
        int damage = weapon.getBaseDamage();
        if (weapon instanceof Weapon_Ranged) {

            Weapon_Ranged bow = (Weapon_Ranged) weapon;

            Arrow arrow = new Arrow("Arrow", 0,
                    new ProjectileGraphicsComponent(null,
                            DynamicResource.WOODENARROW),
                    new PhysicsComponent(10, 25));


            bow.use(this, arrow);
            return;
        }

        if (weapon instanceof Weapon_Melee) {
            Actor[] actors = Game.getInstance().getActors();
            for (Actor a : actors) {
                if (!a.equals(this)) {
                    if (enemyWithinRange(a, range))
                        a.damage(damage);
                }
            }
            return;
        }

    }

    //region Player Stats
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
        this.equipment.equip(equipment);
    }

    public Armour getHelmet() {
        return equipment.getHelmet();
    }

    public Armour getBody() {
        return equipment.getBody();
    }

    public Armour getLegs() {
        return equipment.getLegs();
    }

    public Weapon getMainHand() {
        return equipment.getMainHand();
    }

    public Armour getOffHand() {
        return equipment.getOffHand();
    }

    public String printEquipment() {
        return equipment.toString();
    }
    //endregion

    @Override
    protected Faction getFaction() {
        return Faction.PLAYER;
    }

    //For Debugging purposes
    public String toString() {
        return "Character: " + getName() + ", " + this.level + ", " + this.experience;
    }
}
