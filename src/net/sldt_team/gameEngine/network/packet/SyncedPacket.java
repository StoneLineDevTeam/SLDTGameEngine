package net.sldt_team.gameEngine.network.packet;

import net.sldt_team.gameEngine.GameApplication;

/**
 * Extend this class if you want to call render functions when a packet gets received
 */
public abstract class SyncedPacket implements IPacket {

    public abstract void readPacket(PacketInputStream stream);
    public abstract void writePacket(PacketOutputStream stream);
    public abstract void handleSyncedPacket(GameApplication app);

    public void handlePacket(final GameApplication app){
        app.syncedRunList.add(new Runnable() {
            public void run() {
                handleSyncedPacket(app);
            }
        });
    }
}
