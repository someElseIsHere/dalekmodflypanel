package org.theplaceholder.dalekmodflypanel.client;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TardisFlightDataManager {
    public static Map<UUID, TardisFlightData> playerTardisFlightDataMap = new HashMap<>();

    public static void setPlayerTardisFlightData(UUID uuid, TardisFlightData data) {
        playerTardisFlightDataMap.put(uuid, data);
    }

    public static TardisFlightData getPlayerTardisFlightData(UUID uuid) {
        return playerTardisFlightDataMap.get(uuid);
    }

    public static void removePlayerTardisFlightData(UUID uuid) {
        playerTardisFlightDataMap.remove(uuid);
    }

    public static class TardisFlightData {
        public boolean inFlightMode;
        public float rotation;
        public int tickOnGround;
        public int tickOffGround;
        public int tardisId;

        public TardisFlightData(int tardisId, boolean inFlightMode, float rotation, int tickOnGround, int tickOffGround) {
            this.inFlightMode = inFlightMode;
            this.rotation = rotation;
            this.tickOnGround = tickOnGround;
            this.tickOffGround = tickOffGround;
            this.tardisId = tardisId;
        }
    }
}
