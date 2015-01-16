package net.sldt_team.gameEngine.network.packet;

import java.io.DataOutputStream;
import java.io.OutputStream;

public class PacketOutputStream extends DataOutputStream {

    public PacketOutputStream(OutputStream out) {
        super(out);
    }


}
