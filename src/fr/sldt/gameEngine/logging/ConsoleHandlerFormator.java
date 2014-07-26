package fr.sldt.gameEngine.logging;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class ConsoleHandlerFormator extends Formatter{

    private SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy kk:mm:ss");

    public String format(LogRecord record) {
        Date today = new Date();
        String date = format.format(today);
        String logName = "[" + date + " - " + record.getLoggerName() + "]";
        String level = "[" + record.getLevel() + "]  :  ";
        return logName + " " + level + record.getMessage() + "\n";
    }

}
