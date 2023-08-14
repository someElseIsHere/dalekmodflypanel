package org.theplaceholder.dalekmodflypanel.util;

import com.swdteam.common.init.DMBlocks;
import com.swdteam.common.init.DMDimensions;
import com.swdteam.common.init.DMTardis;
import com.swdteam.common.tardis.TardisData;
import com.swdteam.common.tardis.TardisSaveHandler;
import com.swdteam.common.tileentity.TardisTileEntity;
import net.minecraft.block.Blocks;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import org.theplaceholder.dalekmodflypanel.capability.SyncTardisPacket;
import org.theplaceholder.dalekmodflypanel.capability.TardisCapability;
import org.theplaceholder.dalekmodflypanel.interfaces.ITardisData;

import java.util.UUID;

import static org.theplaceholder.dalekmodflypanel.util.TardisUtils.getTardisCapability;

public class TardisFlightUtils {
    public static final String FLIGHT_SPEED_MODIFIER_NAME = "e3c5b4a0-9c4d-11ea-bb37-0242ac130002";
    public static final UUID FLIGHT_SPEED_MODIFIER = UUID.fromString(FLIGHT_SPEED_MODIFIER_NAME);

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
        TardisSaveHandler.saveTardisData(tardisData);

        player.setInvulnerable(false);
        player.abilities.mayfly = false;
        player.abilities.flying = false;
        player.abilities.mayBuild = true;
        player.onUpdateAbilities();
        player.setInvisible(false);
        player.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(FLIGHT_SPEED_MODIFIER);
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
        TardisSaveHandler.saveTardisData(tardisData);

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
        player.setInvisible(true);
        player.onUpdateAbilities();

        player.getAttribute(Attributes.MOVEMENT_SPEED).addPermanentModifier(new AttributeModifier(FLIGHT_SPEED_MODIFIER, FLIGHT_SPEED_MODIFIER_NAME, -256, AttributeModifier.Operation.ADDITION));
    }

    public static void teleportPlayer(ServerPlayerEntity entity, RegistryKey<World> destinationType, BlockPos destinationPos, float yRot) {
        ServerWorld nextWorld = entity.getServer().getLevel(destinationType);
        entity.teleportTo(nextWorld, destinationPos.getX() + 0.5, destinationPos.getY() + 0.5, destinationPos.getZ() + 0.5, yRot, 0.0f);
    }
}