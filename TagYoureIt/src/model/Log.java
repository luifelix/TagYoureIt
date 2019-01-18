package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * A Log records all file renaming done by the application.
 *
 * @author David C., Felix L., Helen J., Alex T.
 * @version 1.8
 */

class Log {
    /**
     * The log.
     */
    private Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /**
     * The name of the file that stores the log.
     */
    private String fileName;

    /**
     * Constructs a log with the file name provided.
     *
     * @param fileName the name of the file where the log is to be stored
     * @throws IOException if the system file object associated with the log could not be opened.
     */
    Log(String fileName) throws IOException{
        this.fileName = fileName;

        this.logger.setUseParentHandlers(false);
        File file = new File(fileName);
        if (!file.exists()) {
            Files.createFile(file.toPath());
        }
        FileHandler fh = new FileHandler(fileName, true);
        logger.addHandler(fh);
        Formatter formatter = new LogFormatter();
        fh.setFormatter(formatter);
    }

    /**
     * Write message to logger
     *
     * @param message the message to be written to logger
     */
    void writeLog(String message) {
        this.logger.info(message);
    }

    /**
     * Returns the contents of the log.
     *
     * @return the contents of the log
     */
    String readLog() {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            Scanner sc = new Scanner(new File(this.fileName));
            while (sc.hasNextLine()) {
                stringBuilder.append(sc.nextLine());
                stringBuilder.append(System.getProperty("line.separator"));
            }
            sc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
}

/**
 * A LogFormatter object formats a Log object.
 */
//Adapted from https://kodejava.org/how-do-i-create-a-custom-logger-formatter/
class LogFormatter extends Formatter {
    /**
     * The date format.
     */
    private static final DateFormat df =
            new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

    /**
     * Returns the record provided formatted as a string to be added to a Log.
     *
     * @param record the recorded to be added
     * @return a formatted record
     */
    @Override
    public String format(LogRecord record) {
        return "[" + df.format(new Date()) + "] - " + formatMessage(record) +
                "\n\n";
    }
}
