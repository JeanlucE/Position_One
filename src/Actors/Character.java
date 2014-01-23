package Actors;

import Components.ActorGraphicsComponent;
import Components.DynamicResource;
import Components.InputComponent;
import Components.PhysicsComponent;
import Environment.Floor;
import Environment.WorldSpace;
import Items.*;
import WIP.DebugLog;
import WIP.Game;
import WIP.Transform;
import WIP.Vector;

import java.lang.reflect.Field;

/**
 * Created with IntelliJ IDEA.
 * User: Jean-Luc
 * Date: 30.09.13
 * Time: 16:00
 */
public class Character extends Actor {
    //region Skills
    public Skill STRENGTH;
    public Skill ENDURANCE;
    public Skill INTELLIGENCE;
    public Skill DEXTERITY;
    public Skill CHARISMA;
    public Skill PERCEPTION;
    public Skill LUCK;
    public Skill STEALTH;
    //endregion

    //region Stats
    private int level;
    private int experience;
    private int skillPoints;
    private int maxStamina, currentStamina;
    private int maxMana, currentMana;
    //endregion

    //Array of all skills
    private Skill[] skills;

    //Manages all the equipment of the player
    private EquipmentManager equipment;

    //Manages the inventory of the player
    private final Inventory inventory;

    //The speed at which the player walks per frame
    private int walkSpeed = 3;
    //The speed at which the player sprints per frame
    private int sprintSpeed = 6;
    //true == player is sprinting, false == player is not sprinting
    private boolean sprinting = false;

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

        maxStamina = 1000;
        currentStamina = maxStamina;

        this.inventory = new Inventory();
        equipment = new EquipmentManager(this);
        this.instantiateSkills();
        DebugLog.write("New Character created: " + name);
    }

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
    //endregion

    private Vector lastDirection = new Vector(0, 0), newDirection;

    protected void updateThis() {
        applyMovement();

        if (InputComponent.getInstance().isSpaceDown()) {
            attack();
        }

        if (InputComponent.getInstance().isQTyped()) {
            pickupNextItem();
        }

    }

    private void applyMovement() {
        int moveDistance;
        if (InputComponent.getInstance().isShiftDown()) {
            if (currentStamina >= 3) {
                moveDistance = sprintSpeed;
                sprinting = true;
                currentStamina -= 3;
            } else {
                moveDistance = walkSpeed;
                sprinting = false;
            }
        } else {
            moveDistance = walkSpeed;
            sprinting = false;
            if (currentStamina < maxStamina) {
                currentStamina += 1;
            }
        }

        int XAxis = InputComponent.getInstance().getXAxis();
        int YAxis = InputComponent.getInstance().getYAxis();

        calculateDirection(XAxis, YAxis);

        setXVel(XAxis * moveDistance);
        setYVel(YAxis * moveDistance);

        move();

        lastDirection = new Vector(XAxis, YAxis);
    }

    private void attack() {
        Weapon weapon = equipment.getMainHand();
        if (weapon != null) {
            weapon.use();
        }
    }

    /**
     * Given the input of x and y from the InputComponent this method determines where the player is facing
     *
     * @param x x Direction
     * @param y y Direction
     */
    private void calculateDirection(int x, int y) {
        newDirection = new Vector(x, y);
        if (!newDirection.equals(lastDirection)) {

            if (newDirection.getY() == 1)
                getTransform().setDirection(Vector.NORTH);
            else if (newDirection.getY() == -1)
                getTransform().setDirection(Vector.SOUTH);

            if (newDirection.getX() == 1)
                getTransform().setDirection(Vector.EAST);
            else if (newDirection.getX() == -1)
                getTransform().setDirection(Vector.WEST);
        }
    }

    /**
     * Picks up an item nearest to the player
     */
    private void pickupNextItem() {
        WorldSpace nextAbsPos = Game.getInstance().getCurrentWorld().get(getNextWorldPosition());
        if (nextAbsPos != null && nextAbsPos.isFloor()) {
            Floor floor = (Floor) nextAbsPos;
            inventory.add(floor.hasDroppedItems() ? (floor.getTopItem()) : (null));
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
        DebugLog.write("Player level set to: " + level, true);
    }

    public void addExperience(int experience) {
        this.experience += experience;
        while (getNextLevelXP() <= 0) {
            levelUp();
        }
        DebugLog.write("Player gained experience: " + experience);
    }

    //TODO: separate method for assigning skill points
    //TODO: every time skill points are newly assigned recalculate maxHealth, maxMana, maxStamina
    private void levelUp() {
        level++;
        skillPoints++;

        DebugLog.write("Player " + getName() + "has leveled up. \nPlayer " + getName() + "is now level" + level);
    }

    /**
     * Returns how much xp is needed for this character to level up
     *
     * @return Returns how much xp is needed for this character to level up
     */
    public int getNextLevelXP() {
        return Character.getXPOfLevel(level + 1) - this.experience;
    }

    /**
     * Returns how many skillpoint the character currently has
     *
     * @return Returns how many skillpoint the character currently has
     */
    public int getSkillPoints() {
        return skillPoints;
    }

    public void setSkillPoints(int skillPoints) {
        this.skillPoints = skillPoints;
        DebugLog.write("Player skill points set to: " + skillPoints, true);
    }

    //endregion

    //region Equipment Methods
    public Inventory getInventory() {
        return inventory;
    }

    public void equip(Equipment equipment) {
        this.equipment.equip(equipment);
    }

    //TODO public void unequip()

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

    public Ammunition getAmmunition() {
        return equipment.getAmmunition();
    }

    public String printEquipment() {
        return equipment.toString();
    }
    //endregion

    @Override
    public Faction getFaction() {
        return Faction.PLAYER;
    }

    //For Debugging purposes
    public String toString() {
        return "Character: " + getName() + ", " + this.level + ", " + this.experience;
    }

    @Override
    protected void death() {
        DebugLog.write("Player " + getName() + " is dead.");
        this.getTransform().getPosition().set(new Vector(60, 60));
        this.currentHealth = maxHealth;
    }

    @Override
    protected int getDamageTimeout() {
        return 750;
    }

    private int getTotalDefense() {
        int result = 0;
        result += (equipment.hasHelmetEquipped()) ? (getHelmet().getDefence()) : 0;
        result += (equipment.hasBodyEquipped()) ? (getBody().getDefence()) : 0;
        result += (equipment.hasLegsEquipped()) ? (getLegs().getDefence()) : 0;
        return result;
    }

    @Override
    protected int actualDamage(int damage) {
        int actualDamage = damage - getTotalDefense();
        return (actualDamage > 1) ? (actualDamage) : 1;
    }
}
