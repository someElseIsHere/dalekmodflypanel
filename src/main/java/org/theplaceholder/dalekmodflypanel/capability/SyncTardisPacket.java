package org.theplaceholder.dalekmodflypanel.capability;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.PointOfView;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;
import org.theplaceholder.dalekmodflypanel.client.ClientFlightData;

import java.util.UUID;
import java.util.function.Supplier;

import static org.theplaceholder.dalekmodflypanel.DalekModFlyPanel.CHANNEL;

public class SyncTardisPacket {
    public int tardisId;
    public boolean isInFlight;
    public float rotation;
    public int tickOnGround;
    public int tickOffGround;
    public UUID player;
    public SyncTardisPacket(UUID player, int tardisId, boolean isInFlight, float rotation, int tickOnGround, int tickOffGround) {
        this.tardisId = tardisId;
        this.isInFlight = isInFlight;
        this.rotation = rotation;
        this.tickOnGround = tickOnGround;
        this.tickOffGround = tickOffGround;
        this.player = player;
    }

    public static void encode(SyncTardisPacket msg, PacketBuffer buf) {
        buf.writeInt(msg.tardisId);
        buf.writeBoolean(msg.isInFlight);
        buf.writeFloat(msg.rotation);
        buf.writeInt(msg.tickOnGround);
        buf.writeInt(msg.tickOffGround);
        buf.writeUUID(msg.player);
    }

    public static SyncTardisPacket decode(PacketBuffer buf) {
        int tardisId = buf.readInt();
        boolean isInFlight = buf.readBoolean();
        float rotation = buf.readFloat();
        int tickOnGround = buf.readInt();
        int tickOffGround = buf.readInt();
        UUID player = buf.readUUID();
        return new SyncTardisPacket(player, tardisId, isInFlight, rotation, tickOnGround, tickOffGround);
    }

    public static void handle(SyncTardisPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (msg.isInFlight){
                Minecraft.getInstance().options.setCameraType(PointOfView.THIRD_PERSON_BACK);
                ClientFlightData.setPlayerTardisFlightData(msg.player, new ClientFlightData.TardisFlightData(msg.tardisId, msg.isInFlight, msg.rotation, msg.tickOnGround, msg.tickOffGround));
            }else {
                Minecraft.getInstance().options.setCameraType(PointOfView.FIRST_PERSON);
                ClientFlightData.removePlayerTardisFlightData(msg.player);
            }
            ctx.get().setPacketHandled(true);
        });
    }

    public static void sync(UUID uuid,TardisCapability capability) {
        CHANNEL.send(PacketDistributor.ALL.noArg(), new SyncTardisPacket(uuid, capability.getTardisId(), capability.isInFlight(), capability.getRotation(), capability.getTickOnGround(), capability.getTickOffGround()));
    }
}
