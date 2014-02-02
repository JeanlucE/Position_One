package Items;

/**
 * Created with IntelliJ IDEA.
 * User: Jean-Luc
 * Date: 02.02.14
 * Time: 17:41
 */
public interface Stackable {

    void addToStack(int add);

    void setStack(int stack);

    boolean removeFromStack(int remove);

    int getStack();
}
