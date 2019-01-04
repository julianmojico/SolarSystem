package SolarSystem.Utilities;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.Properties;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JobLogger {

    private static JobLogger instance = null;

    //Parametrizacion del Map para evitar errores de tipo

    private static boolean configured;
    private static Map<String,String> dbParams;
    private static Logger logger;
    private static Connection connection = null;
    private static FileHandler fh;
    private static ConsoleHandler ch;

    //Implementacion singleton; evitar multiples instancias del logger
    //Solo un tipo de configuracion por handler

    private JobLogger() {
    }

    public static void configLogger(Map<String,String> inputParams) throws InstantiationException {

        if (instance==null) {

            logger = Logger.getLogger("MyLog");
            dbParams = inputParams;

            //parametrizacion de todos los settings en un solo Map.
            //De esta manera seria mas facil leerlo desde un archivo de config de la app.

            try {

                if(inputParams.get("logToDatabase") == "true" ) {
                    createDBConnection();
                };

                if(inputParams.get("logToFile") == "true") {

                    logger.addHandler(createFileHandler());
                };

                if(inputParams.get("logToConsole") == "true") {

                    logger.addHandler(createConsoleHandler());
                };

                //Guardar settings si fue satisfactorio
                configured = true;


            } catch (Exception e) {

                System.out.println("Logger configuration failed; please review input settings");
                e.printStackTrace();
                return;
            }


        } else {
            InstantiationException e = new InstantiationException("Cannot configure logger since it's already instantiated");
            throw  e;
        }
    }



    public static Map<String,String> getDbParams() {
        return dbParams;
    }

    public static void setDbParams(Map<String,String> dbParams) {
        JobLogger.dbParams = dbParams;
    }

    public static JobLogger getInstance() throws InstantiationException {

        //inicializacion

        if (configured == false) {
            InstantiationException e = new InstantiationException("Cannot get Logger instance since it has not been configured");
            throw  e;
        } else {

            if (instance==null) {

                instance = new JobLogger();
            }
            return instance;
        }


    }

    //db connection settings pueden ser parametrizados y asi cambiar de DB facilmente
    private static void createDBConnection() {

        Properties connectionProps = new Properties();
        connectionProps.put("user", dbParams.get("userName"));
        connectionProps.put("password", dbParams.get("password"));

        try {
            connection = DriverManager.getConnection("jdbc:" + dbParams.get("dbms") + "://" + dbParams.get("serverName")
                    + ":" + dbParams.get("portNumber") + "/", connectionProps);
        } catch (SQLException e) {

            e.printStackTrace();
        }

    }

    private static FileHandler createFileHandler() {


        //Se agrega logFile como parametro del Map para poder definir el nombre de archivo de log
        String filepath = dbParams.get("logFileFolder") + dbParams.get("logFile").toString();
        File logFile = new File(filepath);


        try {

            if (!logFile.exists()) {
                logFile.createNewFile();
            }

            fh = new FileHandler(filepath);

        }

        catch (IOException e) {
            e.printStackTrace();
        }

        return fh;
    }

    private static ConsoleHandler createConsoleHandler() {
        ch = new ConsoleHandler();
        return ch;
    }

    //crear varios metodos para handlers distintos

    public void LogMessage(String messageText,Level level) {

        messageText.trim();

        if (messageText == null || messageText.length() == 0) {
            return;
        }
        //parametrizacion del log level, eliminacion de ifs
        logger.log(level, messageText);

        //limpio buffers
        fh.flush();
        ch.flush();

        if(dbParams.get("logToDatabase").toString() == "true") {

            //obtener el loglevel para guardarlo en la base
            int t = level.intValue();
            Statement stmt;
            try {
                stmt = connection.createStatement();
                stmt.setQueryTimeout(3);
                String query = "insert into travellersdonations.Log_Values VALUES ('" + messageText + "',"  + String.valueOf(t) + ");";
                stmt.executeUpdate(query);

                //TODO: verificar que no fallo el insert

                stmt.close();

            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


        }
    }

    //TODO: faltaria un metodo para cerrar la connection db
}

