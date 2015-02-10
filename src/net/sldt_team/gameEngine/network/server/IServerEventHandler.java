package net.sldt_team.gameEngine.network.server;

import net.sldt_team.gameEngine.network.packet.IPacket;

public interface IServerEventHandler {

    /**
     * Called when a client connects on the server
     * NOTE : This isn't thread safe, the server thread is alive !
     */
    public void onClientConnect(Client client);

    /**
     * Called when client disconnects
     * NOTE : This isn't thread safe, the client updater thread is still alive.
     */
    public void onClientDisconnect(Client client);

    /**
     * Called when clients is removed from the memory
     */
    public void onClientMemoryDelete(Client client);

    /**
     * Called when a packet is received at invalid side
     */
    public void onInvalidSidedPacketReceive(Client client, IPacket packet);

    /**
     * Called when a packet is sent at invalid side
     * NOTE : Client can be null if it's a broadcasted packet.
     */
    public void onInvalidSidedPacketSend(Client client, IPacket packet);

    /**
     * Called when a client gets kicked for you to perform an action
     */
    public void doPlayerKick(Client cl, String msg);
}
