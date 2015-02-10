package net.sldt_team.gameEngine.util;

public class MemoryUtilities {

    /**
     * Returns used memory in MB
     */
    public static long getUsedMemory(){
        Runtime runtime = Runtime.getRuntime();
        int mb = 1024 * 1024;
        return (runtime.totalMemory() - runtime.freeMemory()) / mb;
    }

    /**
     * Returns allocated memory in MB
     */
    public static long getAllocatedMemory(){
        Runtime runtime = Runtime.getRuntime();
        int mb = 1024 * 1024;
        return (runtime.freeMemory() / mb);
    }

    /**
     * Returns maximum memory in MB
     */
    public static long getMaxMemory(){
        Runtime runtime = Runtime.getRuntime();
        int mb = 1024 * 1024;
        return (runtime.maxMemory() / mb);
    }
}
