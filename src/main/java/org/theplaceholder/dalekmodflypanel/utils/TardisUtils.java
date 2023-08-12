package org.theplaceholder.dalekmodflypanel.utils;

import net.minecraft.entity.player.PlayerEntity;
import org.theplaceholder.dalekmodflypanel.capability.TardisCapability;
import org.theplaceholder.dalekmodflypanel.capability.TardisProvider;

public class TardisUtils {
    public static TardisCapability getTardisCapability(PlayerEntity player) {
        return (TardisCapability) player.getCapability(TardisProvider.TARDIS_CAPABILITY).orElse(null);
    }

    public static boolean isInFlightMode(PlayerEntity player) {
        if (player == null)
            return false;
        TardisCapability capa = getTardisCapability(player);
        return capa.getInFlight();
    }
}
