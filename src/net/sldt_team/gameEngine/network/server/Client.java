package net.sldt_team.gameEngine.network.server;

import net.sldt_team.gameEngine.GameApplication;
import net.sldt_team.gameEngine.network.packet.IPacket;
import net.sldt_team.gameEngine.network.packet.PacketInputStream;
import net.sldt_team.gameEngine.network.packet.PacketList;
import net.sldt_team.gameEngine.network.packet.PacketOutputStream;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Client implements Runnable {

    /**
     * Is this client connected
     */
    protected boolean connected;

    /**
     * The output stream for this client
     */
    protected PacketOutputStream outputStream;

    private PacketInputStream inputStream;
    private IServerEventHandler eventHandler;
    private Thread clientThread;
    private GameApplication theGame;

    private Map<String, Object> clientStoredVars;

    /**
     * @exclude
     */
    protected Client(GameApplication app, PacketOutputStream out, PacketInputStream in, IServerEventHandler handler){
        connected = true;
        outputStream = out;
        inputStream = in;
        eventHandler = handler;
        theGame = app;
        clientStoredVars = new HashMap<String, Object>();

        clientThread = new Thread(this);
        clientThread.setName("[ClientNetHandler-" + toString() + "]");
        clientThread.start();
    }

    /**
     * @exclude
     */
    public void run() {
        while(connected){
            try {
                int i = inputStream.readInt();
                IPacket packet = PacketList.instance.getPacketFromID(i);
                if (packet != null){
                    if (!packet.isClientPacket() || packet.isServerPacket()){
                        eventHandler.onInvalidSidedPacketReceive(this, packet);
                        GameApplication.engineLogger.severe("Not reading packet " + packet + " : tried to receive from invalid side");
                        return;
                    }
                    packet.readPacket(inputStream);
                    packet.handlePacket(theGame);
                }
            } catch (IOException e) {
                GameApplication.engineLogger.info("Client has disconnected !");
                eventHandler.onClientDisconnect(this);
                autoClean();
                return;
            }
        }
    }

    /**
     * Kicks this client
     */
    public void kickClient(String msg){
        eventHandler.doPlayerKick(this, msg);
    }

    /**
     * Returns the value of a stored object
     */
    public Object getStoredObject(String name){
        return clientStoredVars.get(name);
    }

    /**
     * Sets the value of a stored object
     */
    public void setStoredObject(String name, Object value){
        clientStoredVars.put(name, value);
    }

    private void autoClean(){
        connected = false;
        clientThread.interrupt();
        clientThread.stop();
        clientThread = null;
        eventHandler = null;
        try {
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            GameApplication.engineLogger.info("Streams not closed");
        }
        inputStream = null;
        outputStream = null;
    }
}
