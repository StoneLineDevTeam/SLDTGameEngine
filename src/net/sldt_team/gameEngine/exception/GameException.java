package net.sldt_team.gameEngine.exception;

import net.sldt_team.gameEngine.exception.code.ErrorCode009;

import java.util.ArrayList;
import java.util.List;

public class GameException extends RuntimeException {
    private IGameError errorCode;
    private Exception exception;

    /**
     * Generates a Game Exception
     *
     * @param code Error code
     */
    public GameException(IGameError code) {
        errorCode = code;
        exception = new Exception(errorCode.getCode() + " -> " + errorCode.getDescription());
    }

    /**
     * Generates a Game Exception
     *
     * @param ex The JVM thrown exception
     */
    public GameException(Exception ex) {
        exception = ex;
        if (errorCode == null) {
            errorCode = new ErrorCode009();
        }
    }

    /**
     * Returns stack trace of this exception's JVM base exception class
     */
    public StackTraceElement[] getStackTrace() {
        return exception.getStackTrace();
    }

    /**
     * Get the message of this exception
     */
    public String getMessage() {
        return exception.getMessage();
    }

    /**
     * Prints the whole stack trace
     */
    public void printStackTrace() {
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
        exception.printStackTrace();
    }

    /**
     * Returns the error code of this exception
     */
    public IGameError getErrorCode(){
        return errorCode;
    }

    /**
     * Returns exception content as String
     */
    public List<String> getJVMException() {
        List<String> list = new ArrayList<String>();
        list.add(exception.getClass().getName());
        for (StackTraceElement element : getStackTrace()) {
            list.add("\tat " + element);
        }
        return list;
    }
}
