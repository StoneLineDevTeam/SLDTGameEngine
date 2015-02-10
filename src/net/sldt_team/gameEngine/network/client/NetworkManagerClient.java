package net.sldt_team.gameEngine.network.client;

import net.sldt_team.gameEngine.GameApplication;
import net.sldt_team.gameEngine.network.packet.IPacket;
import net.sldt_team.gameEngine.network.packet.PacketInputStream;
import net.sldt_team.gameEngine.network.packet.PacketList;
import net.sldt_team.gameEngine.network.packet.PacketOutputStream;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class NetworkManagerClient implements Runnable {

    //Server infos
    private ServerInfo currentConnectedServer;

    //Event handler
    private IClientEventHandler eventHandler;

    // In/Out streams
    private PacketInputStream inputStream;
    private PacketOutputStream outputStream;

    private GameApplication theGame;

    private Thread clientThread;

    /**
     * @exclude
     */
    public NetworkManagerClient(GameApplication app, IClientEventHandler handler) {
        eventHandler = handler;
        theGame = app;
    }

    /**
     * Is this client connected to a server
     */
    public boolean isMultiplayerGame() {
        return currentConnectedServer != null;
    }

    /**
     * Connect to the given server
     */
    public boolean connectToServer(ServerInfo server) {
        try {
            Socket socket = new Socket(server.ipAddress, server.gamePort);
            currentConnectedServer = server;
            inputStream = new PacketInputStream(socket.getInputStream());
            outputStream = new PacketOutputStream(socket.getOutputStream());
            clientThread = new Thread(this);
            clientThread.setName("SLDTGameEngine_InternalClientThread");
            clientThread.start();
            return true;
        } catch (UnknownHostException e) {
            eventHandler.onConnectionError(EnumConnectionError.UNKNOWN_HOST, e);
        } catch (ConnectException e) {
            eventHandler.onConnectionError(EnumConnectionError.CONNECTION_REFUSED, e);
        } catch (SocketTimeoutException e) {
            eventHandler.onConnectionError(EnumConnectionError.TIME_OUT, e);
        } catch (IOException e) {
            eventHandler.onConnectionError(EnumConnectionError.IO_ERROR, e);
        }
        return false;
    }

    /**
     * Forces to disconnect from the current server
     */
    public void forceDisconnect(){
        currentConnectedServer = null;
        clientThread.interrupt();
        clientThread.stop();
        clientThread = null;
        try {
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            GameApplication.engineLogger.info("Streams not closed");
        }
        eventHandler.onClientDisconnected();
    }

    /**
     * Returns the current connected server of this client
     */
    public ServerInfo getCurrentConnectedServer(){
        return currentConnectedServer;
    }

    /**
     * Sends the given packet to the server
     */
    public void sendPacketToServer(IPacket packet) throws IOException {
        if (!packet.isClientPacket() || packet.isServerPacket()){
            eventHandler.onInvalidSidedPacketSend(packet);
            GameApplication.engineLogger.severe("Not sending packet " + packet + " : tried to send from invalid side");
            return;
        }
        int id = PacketList.instance.getIDFromPacket(packet);
        outputStream.writeInt(id);
        packet.writePacket(outputStream);
    }

    /**
     * @exclude
     */
    public void run() {
        try {
            while (isMultiplayerGame()) {
                int id = inputStream.readInt();
                IPacket packet = PacketList.instance.getPacketFromID(id);
                if (packet != null){
                    if (!packet.isServerPacket() || packet.isClientPacket()){
                        eventHandler.onInvalidSidedPacketReceive(packet);
                        GameApplication.engineLogger.severe("Not reading packet " + packet + " : tried to receive from invalid side");
                        return;
                    }
                    packet.readPacket(inputStream);
                    packet.handlePacket(theGame);
                }
            }
        } catch (IOException e) {
            currentConnectedServer = null;
            eventHandler.onConnectionError(EnumConnectionError.CONNECTION_LOST, e);
        }
    }
}
