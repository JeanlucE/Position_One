package WIP;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 1/12/14
 * Time: 10:38 PM
 */
public class FileStructureHandler {
    FileStructureHandler(){
        createDirectories();
    }

    void createDirectories(){
        File mapsFolder = new File("./maps/");
        boolean b = mapsFolder.mkdir();
        if(b){
            DebugLog.write("Maps Folder created");
        }
    }
}
