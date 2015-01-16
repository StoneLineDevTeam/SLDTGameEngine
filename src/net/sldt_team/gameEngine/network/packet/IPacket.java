package net.sldt_team.gameEngine.network.packet;

import net.sldt_team.gameEngine.GameApplication;

public interface IPacket {

    /**
     * Writes data to the server
     */
    public void writePacket(PacketOutputStream stream);

    /**
     * Read
     */
    public void readPacket(PacketInputStream stream);
    public void handlePacket(GameApplication app);
}
