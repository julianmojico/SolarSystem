package SolarSystem.Utilities;

import java.io.IOException;
import java.util.logging.*;

public class SimpleLogger {
    static Logger logger;
    public Handler fileHandler;
    Formatter plainText;

    public SimpleLogger()  throws IOException {

        //instance the logger
        logger = Logger.getLogger(SimpleLogger.class.getName());
        //instance the filehandler
        fileHandler = new FileHandler("myLog.txt",true);
        //instance formatter, set formatting, and handler
        plainText = new SimpleFormatter();
        fileHandler.setFormatter(plainText);
        logger.addHandler(fileHandler);

    }
    private static Logger getLogger(){
        if(logger == null){
            try {
                new SimpleLogger();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return logger;
    }
    public static void log(Level level, String msg){
        getLogger().log(level, msg);
        System.out.println(msg);
    }
}
