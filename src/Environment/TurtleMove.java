package Environment;

import WIP.AdvancedVector;

/**
 * Created with IntelliJ IDEA.
 * User: Jean-Luc
 * Date: 23.01.14
 * Time: 16:39
 */
class TurtleMove {
    private AdvancedVector startPos;
    private AdvancedVector endPos;

    TurtleMove(AdvancedVector startPos, AdvancedVector endPos) {
        this.startPos = startPos;
        this.endPos = endPos;
    }

    public AdvancedVector getStartPos() {
        return startPos;
    }

    public AdvancedVector getEndPos() {
        return endPos;
    }

    public String toString() {
        return "{" + startPos + " - " + endPos + "}";
    }
}
