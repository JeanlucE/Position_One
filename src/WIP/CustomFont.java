package WIP;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Jean-Luc
 * Date: 29.01.14
 * Time: 11:31
 */
public class CustomFont {
    public static Font SHERWOOD_Regular;

    static {
        try {
            SHERWOOD_Regular = Font.createFont(Font.PLAIN, new File("./fonts/SherwoodRegular.ttf"));
            SHERWOOD_Regular = SHERWOOD_Regular.deriveFont(14.0f);
        } catch (FontFormatException e) {
            DebugLog.write("FontLoader: SherwoodRegular does not have the correct file format!");
        } catch (FileNotFoundException e) {
            DebugLog.write("FontLoader: SherwoodRegular.ttf not found!");
        } catch (IOException e) {
            DebugLog.write(e);
        }

    }
}
