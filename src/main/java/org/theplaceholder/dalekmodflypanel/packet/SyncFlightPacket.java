package org.theplaceholder.dalekmodflypanel.packet;

import com.swdteam.client.tardis.data.ClientTardisCache;
import com.swdteam.client.tardis.data.ExteriorModels;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.PointOfView;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;
import org.theplaceholder.dalekmodflypanel.client.RenderPlayerTardis;

import java.util.UUID;
import java.util.function.Supplier;

public class SyncFlightPacket {
    private CompoundNBT nbt;
    public SyncFlightPacket(CompoundNBT nbt) {
        this.nbt = nbt;
    }

    public static void encode(SyncFlightPacket msg, PacketBuffer buf) {
        buf.writeNbt(msg.nbt);
    }

    public static SyncFlightPacket decode(PacketBuffer buf) {
        return new SyncFlightPacket(buf.readNbt());
    }

    public static void handle(SyncFlightPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            UUID uuid = msg.nbt.getUUID("player");

            if (uuid.equals(Minecraft.getInstance().player.getUUID())) {
                if (msg.nbt.getBoolean("isInFlight")) {
                    Minecraft.getInstance().options.setCameraType(PointOfView.THIRD_PERSON_BACK);
                }else {
                    Minecraft.getInstance().options.setCameraType(PointOfView.FIRST_PERSON);
                }
            }

            if (msg.nbt.getBoolean("isInFlight")) {
                ResourceLocation rl = ClientTardisCache.getTardisData(msg.nbt.getInt("isInFlight")).getTardisExterior().getData().getModel(msg.nbt.getInt("skinId"));
                RenderPlayerTardis.playerModelMap.put(uuid, ExteriorModels.getModel(rl));
                RenderPlayerTardis.playerBobMap.put(uuid, 0f);
                RenderPlayerTardis.playerRotationMap.put(uuid, msg.nbt.getInt("rotation"));
            }else {
                RenderPlayerTardis.playerModelMap.remove(uuid);
                RenderPlayerTardis.playerBobMap.remove(uuid);
                RenderPlayerTardis.playerRotationMap.remove(uuid);
            }
        });
    }
}
