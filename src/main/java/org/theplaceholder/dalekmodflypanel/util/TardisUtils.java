package org.theplaceholder.dalekmodflypanel.util;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
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

    public static boolean onGround(PlayerEntity player) {
        final BlockPos pos = new BlockPos(player.blockPosition().getX(), Math.ceil(player.position().y) - 1.0, player.blockPosition().getZ());
        final BlockState blip = player.level.getBlockState(pos);
        return blip.getBlock() != Blocks.AIR;
    }
}
