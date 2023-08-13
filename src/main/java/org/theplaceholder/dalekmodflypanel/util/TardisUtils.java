package org.theplaceholder.dalekmodflypanel.util;

import net.minecraft.entity.player.PlayerEntity;
import org.theplaceholder.dalekmodflypanel.capability.TardisCapability;
import org.theplaceholder.dalekmodflypanel.capability.TardisCapabilityManager;

public class TardisUtils {
    public static TardisCapability getTardisCapability(PlayerEntity player) {
        return player.getCapability(TardisCapabilityManager.CAPABILITY).orElse(null);
    }

    public static boolean isInFlightMode(PlayerEntity player) {
        if (player == null)
            return false;
        TardisCapability capa = getTardisCapability(player);
        return capa.isInFlight();
    }

    public static boolean isOnGround(PlayerEntity player) {
        if (player == null)
            return false;
        return !player.level.getBlockState(player.blockPosition().below()).getBlockState().isAir();
    }
}
