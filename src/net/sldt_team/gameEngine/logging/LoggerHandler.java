package net.sldt_team.gameEngine.logging;

import net.sldt_team.gameEngine.GameApplication;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class LoggerHandler extends Handler {

    private GameApplication theGame;
    private SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy kk:mm:ss");

    public LoggerHandler(GameApplication game){
        theGame = game;
    }

    public void publish(LogRecord record) {
        Date today = new Date();
        String date = format.format(today);
        String logName = "[" + date + " - " + record.getLoggerName() + "]";
        String level = "[" + record.getLevel() + "]  :  ";

        String finalRecord =  logName + " " + level + record.getMessage() + "\n";
        theGame.logs.add(finalRecord);
    }

    public void flush() {

    }

    public void close() throws SecurityException {

    }
}
