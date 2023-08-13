package org.theplaceholder.dalekmodflypanel.util;

import com.swdteam.common.init.DMBlocks;
import com.swdteam.common.init.DMDimensions;
import com.swdteam.common.init.DMTardis;
import com.swdteam.common.tardis.TardisData;
import com.swdteam.common.tardis.TardisSaveHandler;
import com.swdteam.common.tileentity.TardisTileEntity;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import org.theplaceholder.dalekmodflypanel.capability.SyncTardisPacket;
import org.theplaceholder.dalekmodflypanel.capability.TardisCapability;
import org.theplaceholder.dalekmodflypanel.interfaces.ITardisData;

import static org.theplaceholder.dalekmodflypanel.util.TardisUtils.getTardisCapability;

public class TardisFlightUtils {
    public static void stopPlayerFlight(PlayerEntity player) {
        TardisCapability capa = getTardisCapability(player);
        if (!capa.isInFlight()) return;
        BlockPos playerPos = player.blockPosition();
        TardisData tardisData = DMTardis.getTardis(capa.getTardisId());
        ServerWorld playerWorld = (ServerWorld) player.level;

        ((ITardisData) tardisData).dalekmodflypanel$setInFlightMode(false);

        player.setInvulnerable(false);
        player.abilities.mayfly = false;
        player.abilities.flying = false;
        player.eyeHeight = 1.6f;
        player.setInvisible(false);
        player.onUpdateAbilities();
        player.setForcedPose(null);

        playerWorld.setBlock(playerPos, DMBlocks.TARDIS.get().defaultBlockState(), 3);
        TardisTileEntity tardisTileEntity = ((TardisTileEntity) playerWorld.getBlockEntity(playerPos));
        tardisTileEntity.globalID = capa.getTardisId();
        tardisTileEntity.tardisData = tardisData;
        tardisTileEntity.setChanged();
        capa.setInFlight(false);
        capa.setTickOnGround(0);
        SyncTardisPacket.sync(player.getUUID(), capa);

        tardisTileEntity.setRotation(capa.getRotation());
        tardisData.setCurrentLocation(playerPos, playerWorld.dimension());

        teleportPlayer((ServerPlayerEntity) player, DMDimensions.TARDIS, capa.getInteriorPos(), 0);

        TardisSaveHandler.saveTardisData(tardisData);
    }

    public static void startPlayerFlight(PlayerEntity player) {
        TardisCapability capa = getTardisCapability(player);
        if (capa.isInFlight()) return;
        BlockPos interiorPos = player.blockPosition();
        BlockPos exteriorPos = DMTardis.getTardisFromInteriorPos(interiorPos).getCurrentLocation().getBlockPosition();
        TardisData tardisData = DMTardis.getTardisFromInteriorPos(interiorPos);
        World interiorWorld = player.level;
        ServerWorld exteriorWorld = interiorWorld.getServer().getLevel(tardisData.getCurrentLocation().dimensionWorldKey());
        TardisTileEntity tardisTileEntity = ((TardisTileEntity) exteriorWorld.getBlockEntity(exteriorPos));
        if (tardisTileEntity == null) return;
        float rotation = tardisTileEntity.getRotation();

        ((ITardisData) tardisData).dalekmodflypanel$setInFlightMode(true);

        player.setInvulnerable(true);
        player.abilities.mayfly = true;
        player.abilities.flying = true;
        player.setInvisible(true);
        player.setForcedPose(Pose.STANDING);

        capa.setInFlight(true);
        capa.setTardisId(tardisData.getGlobalID());
        capa.setInteriorPos(interiorPos);
        capa.setRotation(rotation);
        SyncTardisPacket.sync(player.getUUID(), capa);

        exteriorWorld.setBlock(exteriorPos, Blocks.AIR.defaultBlockState(), 3);

        teleportPlayer((ServerPlayerEntity) player, tardisData.getCurrentLocation().dimensionWorldKey(), exteriorPos, rotation);

        TardisSaveHandler.saveTardisData(tardisData);
    }

    public static void teleportPlayer(ServerPlayerEntity entity, RegistryKey<World> destinationType, BlockPos destinationPos, float yRot) {
        ServerWorld nextWorld = entity.getServer().getLevel(destinationType);
        entity.teleportTo(nextWorld, destinationPos.getX() + 0.5, destinationPos.getY() + 0.5, destinationPos.getZ() + 0.5, yRot, 0.0f);
    }
}