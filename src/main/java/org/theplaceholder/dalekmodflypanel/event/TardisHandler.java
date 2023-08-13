package org.theplaceholder.dalekmodflypanel.event;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.theplaceholder.dalekmodflypanel.capability.SyncTardisPacket;
import org.theplaceholder.dalekmodflypanel.capability.TardisCapability;

import static org.theplaceholder.dalekmodflypanel.util.TardisFlightUtils.stopPlayerFlight;
import static org.theplaceholder.dalekmodflypanel.util.TardisUtils.getTardisCapability;
import static org.theplaceholder.dalekmodflypanel.util.TardisUtils.isInFlightMode;

public class TardisHandler {
    @SubscribeEvent
    public static void onLivingUpdateEvent(LivingEvent.LivingUpdateEvent event) {
        if (event.getEntity().level.isClientSide)
            return;
        if (event.getEntity() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getEntity();
            if (isInFlightMode(player)) {
                TardisCapability capability = getTardisCapability(player);

                if (!player.isOnGround()){
                    capability.setRotation(capability.getRotation() + 1);
                    if (capability.getRotation() >= 360)
                        capability.setRotation(0);

                    capability.setTickOnGround(0);
                    capability.setTickOffGround(capability.getTickOffGround() + 1);
                }else{
                    capability.setTickOnGround(capability.getTickOnGround() + 1);
                    if (capability.getTickOnGround() >= 40 && player.isShiftKeyDown()){
                        stopPlayerFlight(player);
                    }
                    capability.setTickOffGround(0);
                }
                SyncTardisPacket.sync(player.getUUID(), capability);
            }
        }
    }

    @SubscribeEvent
    public static void onHurt(LivingHurtEvent event) {
        if (event.getEntity().level.isClientSide)
            return;
        if (event.getEntity() instanceof PlayerEntity && isInFlightMode((PlayerEntity)event.getEntity()))
            event.setCanceled(true);
    }

    @SubscribeEvent
    public static void deathEvent(LivingDeathEvent e) {
        if (e.getEntity().level.isClientSide)
            return;
        if (e.getEntity() instanceof PlayerEntity && isInFlightMode((PlayerEntity)e.getEntity()))
            stopPlayerFlight((PlayerEntity)e.getEntity());
        if (e.getEntity() instanceof PlayerEntity && isInFlightMode((PlayerEntity)e.getEntity()))
            e.setCanceled(true);
    }

    @SubscribeEvent
    public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent e) {
        if (e.getEntity().level.isClientSide)
            return;
        if (isInFlightMode(e.getPlayer()))
            stopPlayerFlight(e.getPlayer());
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
            stopPlayerFlight(e.getPlayer());
    }
}
