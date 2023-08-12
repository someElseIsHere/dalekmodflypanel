package org.theplaceholder.dalekmodflypanel.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import org.theplaceholder.dalekmodflypanel.capability.ITardisCapability;
import org.theplaceholder.dalekmodflypanel.capability.TardisProvider;

import java.util.function.Supplier;

public class SyncFlightModeCapPacket {
    public PlayerEntity player;
    public CompoundNBT compound;

    public SyncFlightModeCapPacket(PlayerEntity player, CompoundNBT compound) {
        this.player = player;
        this.compound = compound;
    }

    public static void encode(SyncFlightModeCapPacket msg, PacketBuffer buf) {
        buf.writeUUID(msg.player.getUUID());
        buf.writeNbt(msg.compound);
    }

    public static SyncFlightModeCapPacket decode(PacketBuffer buf) {
        PlayerEntity player = Minecraft.getInstance().player.level.getPlayerByUUID(buf.readUUID());
        CompoundNBT compound = buf.readNbt();
        return new SyncFlightModeCapPacket(player, compound);
    }

    public static void handle(SyncFlightModeCapPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ((ITardisCapability)msg.player.getCapability(TardisProvider.TARDIS_CAPABILITY, null)).readNBT(msg.compound);
        });
    }
}
