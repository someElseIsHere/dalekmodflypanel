package org.theplaceholder.dalekmodflypanel.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.PointOfView;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class FlightPacket {

    private boolean enable;
    public FlightPacket(boolean enable) {
        this.enable = enable;
    }

    public static void encode(FlightPacket msg, PacketBuffer buf) {
        buf.writeBoolean(msg.enable);
    }

    public static FlightPacket decode(PacketBuffer buf) {
        return new FlightPacket(buf.readBoolean());
    }

    public static void handle(FlightPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (msg.enable){
                Minecraft.getInstance().options.setCameraType(PointOfView.THIRD_PERSON_BACK);
            }else {
                Minecraft.getInstance().options.setCameraType(PointOfView.FIRST_PERSON);
            }
        });
    }
}
