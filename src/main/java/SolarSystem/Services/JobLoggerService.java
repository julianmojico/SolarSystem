package SolarSystem.Services;

import SolarSystem.Utilities.JobLogger;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;


public class JobLoggerService {

    public static void init() {

        Map<String,String> dbParamsMap = new HashMap<String,String>();

        dbParamsMap.put("userName", "root");
        dbParamsMap.put("password", "qwe123asd");
        dbParamsMap.put("dbms", "mysql");
        dbParamsMap.put("serverName", "localhost");
        dbParamsMap.put("portNumber", "3306");
        dbParamsMap.put("logToFile", "true");
        dbParamsMap.put("logToConsole", "true");
        dbParamsMap.put("logToDatabase", "true");
        dbParamsMap.put("logFile", "testLog.txt");
        dbParamsMap.put("logFileFolder", "c:\\users\\julian\\");

        try {

            JobLogger.configLogger(dbParamsMap);
            JobLogger instance = JobLogger.getInstance();

            instance.LogMessage("Probando info",Level.INFO);
            instance.LogMessage("Probando WARNING",Level.WARNING);
            instance.LogMessage("Probando SEVERE",Level.SEVERE);

        } catch (Exception e) {

            e.printStackTrace();
        }



    }

}

