package WIP;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Jean-Luc
 * Date: 06.11.13
 * Time: 21:19
 */
public class DebugLog {

    private static BufferedWriter logWriter;
    private static DateFormat timeFormat;


    public static void instantiate() throws IOException {
        File log = new File("log.txt");
        if (!log.exists()) {
            log.createNewFile();
        }
        timeFormat = new SimpleDateFormat("[HH:mm:ss.SSS]");
        logWriter = new BufferedWriter(new FileWriter(log));
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy");
        logWriter.write("Log created at: " + dateFormat.format(new Date()) + "\n\n");
    }

    public static void write(Object obj) {
        write(obj.toString());
    }

    public static void write(String content) {
        try {
            logWriter.write(timeFormat.format(new Date()) + " ");
            logWriter.write(content + "\n");
            System.out.println(content);
            logWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
