import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Log4jLogger {
    // Create a logger instance
    private static final Logger logger = LogManager.getLogger(Log4jLogger.class);
    
    public static void computeLog(String configFilePath,String level,String message) {
        System.setProperty("log4j.configurationFile", configFilePath);
        LogManager.getLogger(Log4jLogger.class).info("in java from compute");
        
        switch (level.toUpperCase()) {
        case "INFO":
        	LogManager.getLogger(Log4jLogger.class).info(message);
            break;
        case "DEBUG":
        	LogManager.getLogger(Log4jLogger.class).debug(message);
            break;
        case "ERROR":
        	LogManager.getLogger(Log4jLogger.class).error(message);
            break;
        case "WARN":
        	LogManager.getLogger(Log4jLogger.class).warn(message);
            break;
        default:
        	LogManager.getLogger(Log4jLogger.class).info("DEFAULT: " + message);
            break;
    }
    }

    // Method to log messages
    public static void logMessage(String level, String message) {
        switch (level.toUpperCase()) {
            case "INFO":
            	LogManager.getLogger(Log4jLogger.class).info(message);
                break;
            case "DEBUG":
            	LogManager.getLogger(Log4jLogger.class).debug(message);
                break;
            case "ERROR":
            	LogManager.getLogger(Log4jLogger.class).error(message);
                break;
            case "WARN":
            	LogManager.getLogger(Log4jLogger.class).warn(message);
                break;
            default:
            	LogManager.getLogger(Log4jLogger.class).info("DEFAULT: " + message);
                break;
        }
    }
}
