package net.sldt_team.gameEngine.network.client;

public class ServerInfo {

    /**
     * The ip address of the server
     */
    public final String ipAddress;

    /**
     * The port of the server
     */
    public final int gamePort;

    /**
     * Creates a server info class
     * @param ip The ip you want to connect to
     * @param port The port the server is using
     */
    public ServerInfo(String ip, int port){
        ipAddress = ip;
        gamePort = port;
    }
}
