package org.theplaceholder.dalekmodflypanel.client;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

import static org.theplaceholder.dalekmodflypanel.DalekModFlyPanel.MODID;

public class DMFPSounds {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(net.minecraft.util.SoundEvent.class, "dalekmodflypanel");
    public static final RegistryObject<SoundEvent> TARDIS_LAND = buildSound(SOUNDS, "tardis_land");
    public static final RegistryObject<SoundEvent> TARDIS_FLY = buildSound(SOUNDS, "tardis_fly");
    public static RegistryObject<SoundEvent> buildSound(DeferredRegister<SoundEvent> register, String registryName) {
        RegistryObject<SoundEvent> SOUND = register.register(registryName, () -> new SoundEvent(new ResourceLocation(MODID, registryName)));
        return SOUND;
    }
}
