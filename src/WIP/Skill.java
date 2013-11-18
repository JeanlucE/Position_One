package WIP;

/**
 * Created with IntelliJ IDEA.
 * User: Jean-Luc
 * Date: 03.11.13
 * Time: 18:12
 */
public class Skill {

    private final String name;
    private int level;

    public Skill(String name) {
        this.name = name;
        this.level = 0;
    }

    public String getName() {
        return this.name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String toString() {
        return "PlayerSkill: " + this.name + ", " + this.level;
    }
}

