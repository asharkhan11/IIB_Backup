import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Log4jLogger {
    // Create a logger instance
    private static final Logger logger = LogManager.getLogger(Log4jLogger.class);

    // Method to log messages
    public static void logMessage(String level, String message) {
        switch (level.toUpperCase()) {
            case "INFO":
                logger.info(message);
                break;
            case "DEBUG":
                logger.debug(message);
                break;
            case "ERROR":
                logger.error(message);
                break;
            case "WARN":
                logger.warn(message);
                break;
            default:
                logger.info("DEFAULT: " + message);
                break;
        }
    }
}
