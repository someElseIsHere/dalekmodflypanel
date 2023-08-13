package org.theplaceholder.dalekmodflypanel.capability;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.theplaceholder.dalekmodflypanel.util.capability.SimpleCapabilityStorage;
import org.theplaceholder.dalekmodflypanel.util.capability.SimpleVolatileCapabilityProvider;

import static org.theplaceholder.dalekmodflypanel.DalekModFlyPanel.MODID;

public class TardisCapabilityManager {
    @CapabilityInject(TardisCapability.class)
    @SuppressWarnings("ConstantConditions")
    public static Capability<TardisCapability> CAPABILITY = null;
    public static final ResourceLocation ID = new ResourceLocation(MODID, "capability");

    public static void register() {
        CapabilityManager.INSTANCE.register(TardisCapability.class, SimpleCapabilityStorage.create(() -> CAPABILITY), TardisCapabilityImpl::new);
    }

    @SubscribeEvent
    public static void attach(AttachCapabilitiesEvent<Entity> event){
        if (event.getObject() instanceof PlayerEntity) {
            TardisCapability capability = new TardisCapabilityImpl();
            event.addCapability(ID, SimpleVolatileCapabilityProvider.from(CAPABILITY, () -> capability));
        }
    }

    @SubscribeEvent
    public static void cloned(PlayerEvent.Clone event){
        event.getOriginal().getCapability(CAPABILITY).ifPresent(oldCap -> {
            event.getPlayer().getCapability(CAPABILITY).ifPresent(newCap -> {
                newCap.deserializeNBT(oldCap.serializeNBT());
            });
        });
    }
}
