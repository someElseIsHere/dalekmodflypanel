package org.theplaceholder.dalekmodflypanel.util;

import com.swdteam.common.init.DMBlocks;
import com.swdteam.common.init.DMDimensions;
import com.swdteam.common.init.DMTardis;
import com.swdteam.common.tardis.TardisData;
import com.swdteam.common.tileentity.TardisTileEntity;
import net.minecraft.block.Blocks;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import org.theplaceholder.dalekmodflypanel.packet.SyncTardisPacket;
import org.theplaceholder.dalekmodflypanel.capability.TardisCapability;
import org.theplaceholder.dalekmodflypanel.interfaces.ITardisData;

import static org.theplaceholder.dalekmodflypanel.util.TardisUtils.getTardisCapability;

public class TardisFlightUtils {
    private static AttributeModifier flightSpeedModifier = new AttributeModifier("TardisFlightSpeed", -255, AttributeModifier.Operation.ADDITION);

    public static void stopPlayerFlight(ServerPlayerEntity player) {
        TardisCapability capa = getTardisCapability(player);
        if (!capa.isInFlight()) return;
        BlockPos playerPos = player.blockPosition();
        TardisData tardisData = DMTardis.getTardis(capa.getTardisId());
        ServerWorld playerWorld = (ServerWorld) player.level;

        ((ITardisData) tardisData).dalekmodflypanel$setInFlightMode(false);

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

        teleportPlayer(player, DMDimensions.TARDIS, capa.getInteriorPos(), 0);

        player.setInvulnerable(false);
        player.abilities.mayfly = false;
        player.abilities.flying = false;
        player.abilities.mayBuild = true;
        player.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(flightSpeedModifier);
        player.setInvisible(false);


        player.onUpdateAbilities();
    }

    public static void startPlayerFlight(ServerPlayerEntity player) {
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
        tardisData.setDoorOpen(false);

        capa.setInFlight(true);
        capa.setTardisId(tardisData.getGlobalID());
        capa.setInteriorPos(interiorPos);
        capa.setRotation(rotation);
        SyncTardisPacket.sync(player.getUUID(), capa);

        exteriorWorld.setBlock(exteriorPos, Blocks.AIR.defaultBlockState(), 3);

        teleportPlayer(player, tardisData.getCurrentLocation().dimensionWorldKey(), exteriorPos, rotation);

        player.setInvulnerable(true);
        player.abilities.mayfly = true;
        player.abilities.flying = true;
        player.abilities.mayBuild = false;
        player.getAttribute(Attributes.MOVEMENT_SPEED).addPermanentModifier(flightSpeedModifier);
        player.setInvisible(true);
        player.onUpdateAbilities();
    }

    public static void teleportPlayer(ServerPlayerEntity entity, RegistryKey<World> destinationType, BlockPos destinationPos, float yRot) {
        ServerWorld nextWorld = entity.getServer().getLevel(destinationType);
        entity.teleportTo(nextWorld, destinationPos.getX() + 0.5, destinationPos.getY() + 0.5, destinationPos.getZ() + 0.5, yRot, 0.0f);
    }
}