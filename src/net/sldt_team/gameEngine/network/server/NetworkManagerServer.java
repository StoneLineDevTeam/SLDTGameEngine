package net.sldt_team.gameEngine.network.server;

import net.sldt_team.gameEngine.GameApplication;
import net.sldt_team.gameEngine.network.packet.IPacket;
import net.sldt_team.gameEngine.network.packet.PacketInputStream;
import net.sldt_team.gameEngine.network.packet.PacketList;
import net.sldt_team.gameEngine.network.packet.PacketOutputStream;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class NetworkManagerServer implements Runnable {

    private boolean serverRunning;
    private int networkPort = -1;
    private IServerEventHandler eventHandler;
    private Client[] connectedClients;

    private GameApplication theGame;

    private Thread serverAcceptThread;

    /**
     * @exclude
     */
    public NetworkManagerServer(GameApplication app, IServerEventHandler handler){
        eventHandler = handler;
        theGame = app;
    }

    /**
     * Starts this server manager's network
     */
    public void startServerNetwork(int maxPlayers, int gamePort){
        serverRunning = true;
        networkPort = gamePort;
        connectedClients = new Client[maxPlayers];

        serverAcceptThread = new Thread(this);
        serverAcceptThread.setName("SLDTGameEngine_InternalServerThread");
        serverAcceptThread.start();

        GameApplication.engineLogger.info("Internal server started");
    }


    /**
     * Stops this server manager's network
     */
    public void stopServerNetwork(){
        networkPort = -1;
        serverRunning = false;

        serverAcceptThread.interrupt();
        serverAcceptThread.stop();
        serverAcceptThread = null;

        GameApplication.engineLogger.info("Internal server stopped");
    }

    /**
     * Broadcasts the given packet to all connected clients
     */
    public void broadcastPacket(IPacket packet) throws IOException {
        if (!packet.isServerPacket() || packet.isClientPacket()){
            eventHandler.onInvalidSidedPacketSend(null, packet);
            GameApplication.engineLogger.severe("Not broadcasting packet " + packet + " : tried to send from invalid side");
            return;
        }
        int id = PacketList.instance.getIDFromPacket(packet);
        for (Client cl : connectedClients){
            if (cl.connected) {
                cl.outputStream.writeInt(id);
                packet.writePacket(cl.outputStream);
            }
        }
    }

    /**
     * Sends a given packet to a given client
     */
    public void sendPacketToClient(IPacket packet, Client sendTo) throws IOException {
        if (!packet.isServerPacket() || packet.isClientPacket()){
            eventHandler.onInvalidSidedPacketSend(sendTo, packet);
            GameApplication.engineLogger.severe("Not sending packet " + packet + " : tried to send from invalid side");
            return;
        }
        int id = PacketList.instance.getIDFromPacket(packet);
        if (sendTo.connected) {
            sendTo.outputStream.writeInt(id);
            packet.writePacket(sendTo.outputStream);
        }
    }

    /**
     * Returns all clients including disconnected ones
     */
    public Client[] getAllClients(){
        return connectedClients;
    }

    private void checkAndDeleteClients(){
        for (int i = 0 ; i < connectedClients.length ; i++){
            final Client cl = connectedClients[i];
            if (!cl.connected){
                connectedClients[i] = null;
                theGame.syncedRunList.add(new Runnable() {
                    public void run() {
                        eventHandler.onClientMemoryDelete(cl);
                    }
                });
                return;
            }
        }
    }
    private void addClient(Client cl){
        for (int i = 0 ; i < connectedClients.length ; i++){
            Client c = connectedClients[i];
            if (c == null){
                connectedClients[i] = cl;
            }
        }
    }

    /**
     * @exclude
     */
    public void run() {
        try {
            ServerSocket socket = new ServerSocket(networkPort);
            while (serverRunning){
                //Check for disconnected clients
                checkAndDeleteClients();

                //Check for new connections
                Socket client = socket.accept();
                Client cl = new Client(theGame, new PacketOutputStream(client.getOutputStream()), new PacketInputStream(client.getInputStream()), eventHandler);
                eventHandler.onClientConnect(cl);
                addClient(cl);
            }
        } catch (IOException e) {
            GameApplication.engineLogger.warning("Error while creating the server socket");
        }
    }
}
