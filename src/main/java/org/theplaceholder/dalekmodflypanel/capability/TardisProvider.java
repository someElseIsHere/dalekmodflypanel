package org.theplaceholder.dalekmodflypanel.capability;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TardisProvider implements ICapabilitySerializable<CompoundNBT> {
    @CapabilityInject(ITardisCapability.class)
    public static final Capability<ITardisCapability> TARDIS_CAPABILITY = null;
    private ITardisCapability instance;

    public TardisProvider(PlayerEntity player) {
        instance = new TardisCapability(player);
    }

    @Override
    public CompoundNBT serializeNBT() {
        return (CompoundNBT) TARDIS_CAPABILITY.getStorage().writeNBT(TARDIS_CAPABILITY, instance, null);
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        TARDIS_CAPABILITY.getStorage().readNBT(TARDIS_CAPABILITY, instance, null, nbt);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == TARDIS_CAPABILITY ? LazyOptional.of(() -> instance).cast() : LazyOptional.empty();
    }
}
