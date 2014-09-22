package net.sldt_team.gameEngine.exception;

import net.sldt_team.gameEngine.exception.code.ErrorCode009;

import java.util.ArrayList;
import java.util.List;

public class GameException extends RuntimeException {
    private GameError errorCode;
    private Exception exception;

    public GameException(GameError code){
        errorCode = code;
        exception = new Exception(errorCode.getCode() + " -> " + errorCode.getDescription());
    }

    public GameException(Exception ex){
        exception = ex;
        if (errorCode == null){
            errorCode = new ErrorCode009();
        }
    }

    public StackTraceElement[] getStackTrace() {
        return exception.getStackTrace();
    }

    public String getMessage(){
        return exception.getMessage();
    }

    public void printStackTrace(){
        System.err.println("\n");
        System.err.println("\n");
        System.err.println(errorCode.getCode() + " -> " + errorCode.getDescription() + "\n");
        System.err.println("---------------------------");
        System.err.println("|--> SLDT'S GameEngine <--|");
        System.err.println("---------------------------");
        System.err.println("A fatal error has been thrown... We are sorry...");
        System.err.println("This may be a game engine issue or a game issue. Please contact developper for more informations.");
        System.err.println("-------------------------------------------------");
        System.err.println("You will now need to contact the developper of this game.");
        System.err.println("The developper should know what to do...\n");
        System.err.println("---------------------------");
        System.err.println("|-->    Stack Trace    <--|");
        System.err.println("---------------------------");
        /**
        System.err.println(exception.getMessage());
        for (String s : getJVMException()){
            System.err.println(s);
        }
         */
        exception.printStackTrace();
    }

    public List<String> getJVMException(){
        List<String> list = new ArrayList<String>();
        for (StackTraceElement element : getStackTrace()){
            list.add("\tat " + element);
        }
        return list;
    }
}
