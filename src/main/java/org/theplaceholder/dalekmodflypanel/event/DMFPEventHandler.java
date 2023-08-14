package org.theplaceholder.dalekmodflypanel.event;

import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.theplaceholder.dalekmodflypanel.capability.SyncTardisPacket;
import org.theplaceholder.dalekmodflypanel.capability.TardisCapability;

import static org.theplaceholder.dalekmodflypanel.util.TardisFlightUtils.stopPlayerFlight;
import static org.theplaceholder.dalekmodflypanel.util.TardisUtils.*;

public class DMFPEventHandler {
    @SubscribeEvent
    public static void livingUpdateEvent(LivingEvent.LivingUpdateEvent event) {
        if (event.getEntity().level.isClientSide)
            return;
        if (event.getEntity() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getEntity();
            if (isInFlightMode(player)) {
                TardisCapability capability = getTardisCapability(player);

                if (!onGround(player)){
                    capability.setRotation(capability.getRotation() + 3);
                    if (capability.getRotation() >= 360)
                        capability.setRotation(0);

                    capability.setTickOnGround(0);
                    capability.setTickOffGround(capability.getTickOffGround() + 1);
                }else{
                    capability.setTickOnGround(capability.getTickOnGround() + 1);
                    if (capability.getTickOnGround() >= 40 && player.isShiftKeyDown()){
                        stopPlayerFlight((ServerPlayerEntity) player);
                    }
                    capability.setTickOffGround(0);
                }
                SyncTardisPacket.sync(player.getUUID(), capability);
            }
        }
    }

    @SubscribeEvent
    public static void deathEvent(LivingDeathEvent e) {
        if (e.getEntity().level.isClientSide)
            return;
        if (e.getEntity() instanceof PlayerEntity && isInFlightMode((PlayerEntity)e.getEntity()))
            e.setCanceled(true);
    }

    @SubscribeEvent
    public static void playerDisconnect(PlayerEvent.PlayerLoggedOutEvent e) {
        if (e.getEntity().level.isClientSide)
            return;
        if (isInFlightMode(e.getPlayer()))
            stopPlayerFlight((ServerPlayerEntity) e.getPlayer());
    }

    @SubscribeEvent
    public static void playerInteract(PlayerInteractEvent e) {
        if (e.getEntity().level.isClientSide)
            return;
        if (isInFlightMode(e.getPlayer()))
            e.setCanceled(true);
    }

    @SubscribeEvent
    public static void playerConnect(PlayerEvent.PlayerLoggedInEvent e) {
        if (e.getEntity().level.isClientSide)
            return;
        if (isInFlightMode(e.getPlayer()))
            stopPlayerFlight((ServerPlayerEntity) e.getPlayer());
    }

    @SubscribeEvent
    public static void playerItemPickup(PlayerEvent.ItemPickupEvent e) {
        if (e.getPlayer().level.isClientSide)
            return;
        if (isInFlightMode(e.getPlayer()))
            e.setCanceled(true);
    }

    @SubscribeEvent
    public static void playerItemDrop(ItemTossEvent e) {
        if (e.getPlayer().level.isClientSide)
            return;
        if (isInFlightMode(e.getPlayer()))
            e.setCanceled(true);
    }
}
