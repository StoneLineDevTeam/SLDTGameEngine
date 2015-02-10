package net.sldt_team.gameEngine.network.client;

import net.sldt_team.gameEngine.network.packet.IPacket;

public interface IClientEventHandler {

    /**
     * Called when the protocol has thrown a connection error
     */
    public void onConnectionError(EnumConnectionError error, Exception ex);

    /**
     * Called when a packet is received at invalid side
     */
    public void onInvalidSidedPacketReceive(IPacket packet);

    /**
     * Called when a packet is sent at invalid side
     * NOTE : Client can be null if it's a broadcasted packet.
     */
    public void onInvalidSidedPacketSend(IPacket packet);

    /**
     * Called when the client network gets stopped
     */
    public void onClientDisconnected();
}
