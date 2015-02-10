package net.sldt_team.gameEngine.network.packet;

import net.sldt_team.gameEngine.GameApplication;

import java.io.IOException;

public interface IPacket {

    /**
     * Writes data to the server
     */
    public void writePacket(PacketOutputStream stream) throws IOException;

    /**
     * Reads data from the server
     */
    public void readPacket(PacketInputStream stream) throws IOException;

    /**
     * Handles this packet
     * NOTE : All packets are handled in the networking thread, so calling any render function will simply crash your game.
     * NOTE_2 : For newbies at OpenGL, a Screen is a rendering component, so calling displayScreen wil result in game crash.
     */
    public void handlePacket(GameApplication app);

    /**
     * Returns whenever or not the client is able to send this packet
     */
    public boolean isClientPacket();

    /**
     * Returns whenever or not the server is able to send this packet
     */
    public boolean isServerPacket();
}
