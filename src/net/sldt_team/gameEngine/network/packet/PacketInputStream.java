package net.sldt_team.gameEngine.network.packet;

import java.io.DataInputStream;
import java.io.InputStream;

public class PacketInputStream extends DataInputStream {

    public PacketInputStream(InputStream in) {
        super(in);
    }


}
