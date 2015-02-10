package net.sldt_team.gameEngine.network.packet;

import net.sldt_team.gameEngine.GameApplication;

import java.util.HashMap;
import java.util.Map;

public class PacketList {

    public static final PacketList instance = new PacketList();

    private Map<Integer, Class<? extends IPacket>> idToPacketMap;
    private Map<Class<? extends IPacket>, Integer> packetToIDMap;

    /**
     * Registers a packet
     */
    public void registerPacket(int id, Class<? extends IPacket> packet){
        if (idToPacketMap == null){
            idToPacketMap = new HashMap<Integer, Class<? extends IPacket>>();
        }
        if (packetToIDMap == null){
            packetToIDMap = new HashMap<Class<? extends IPacket>, Integer>();
        }
        idToPacketMap.put(id, packet);
        packetToIDMap.put(packet, id);
    }

    /**
     * Returns a packet corresponding to the given index
     */
    public IPacket getPacketFromID(int id){
        try {
            return idToPacketMap.get(id).newInstance();
        } catch (InstantiationException e) {
            GameApplication.engineLogger.severe("Can't open packet : InstantiationException !");
        } catch (IllegalAccessException e) {
            GameApplication.engineLogger.severe("Can't open packet : IllegalAccessException !");
        }
        return null;
    }

    /**
     * Returns an index corresponding to the given packet
     */
    public int getIDFromPacket(IPacket packet){
        return packetToIDMap.get(packet.getClass());
    }
}
