package org.theplaceholder.dalekmodflypanel.capability;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static org.theplaceholder.dalekmodflypanel.DalekModFlyPanel.TARDIS_CAP;
import static org.theplaceholder.dalekmodflypanel.utils.TardisFlightUtils.stopPlayerFlight;
import static org.theplaceholder.dalekmodflypanel.utils.TardisUtils.getTardisCapability;
import static org.theplaceholder.dalekmodflypanel.utils.TardisUtils.isInFlightMode;

public class TardisHandler {

    @SubscribeEvent
    public static void onLivingUpdateEvent(LivingEvent.LivingUpdateEvent event) {
        if (event.getEntity() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getEntity();
            if (isInFlightMode(player)) {
                TardisCapability capability = getTardisCapability(player);

                if (!player.isOnGround()){
                    capability.setRotation(capability.getRotation() + 1);
                    if (capability.getRotation() >= 360)
                        capability.setRotation(0);
                }else{
                    capability.setTickOnGround(capability.getTickOnGround() + 1);
                    if (capability.getTickOnGround() >= 40 && player.isShiftKeyDown()){
                        stopPlayerFlight(player);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onHurt(LivingHurtEvent e) {
        if (e.getEntity() instanceof PlayerEntity && isInFlightMode((PlayerEntity)e.getEntity()))
            e.setCanceled(true);
    }

    @SubscribeEvent
    public static void deathEvent(LivingDeathEvent e) {
        if (e.getEntity() instanceof PlayerEntity && isInFlightMode((PlayerEntity)e.getEntity()))
            stopPlayerFlight((PlayerEntity)e.getEntity());
        if (e.getEntity() instanceof PlayerEntity && isInFlightMode((PlayerEntity)e.getEntity()))
            e.setCanceled(true);
    }

    @SubscribeEvent
    public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent e) {
        if (isInFlightMode(e.getPlayer()))
            stopPlayerFlight(e.getPlayer());
    }

    @SubscribeEvent
    public static void playerInteract(PlayerInteractEvent e) {
        if (isInFlightMode(e.getPlayer()))
            e.setCanceled(true);
    }

    @SubscribeEvent
    public static void playerConnect(PlayerEvent.PlayerLoggedInEvent e) {
        if (isInFlightMode(e.getPlayer()))
            stopPlayerFlight(e.getPlayer());
    }

    @SubscribeEvent
    public static void disableItemUse(PlayerInteractEvent.RightClickItem e) {
        if (isInFlightMode(e.getPlayer()))
            e.setCanceled(true);
    }

    @SubscribeEvent
    public void attachCapability(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof PlayerEntity)
            event.addCapability(TARDIS_CAP, new TardisProvider((PlayerEntity) event.getObject()));
    }
}
