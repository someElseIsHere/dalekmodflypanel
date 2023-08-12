package org.theplaceholder.dalekmodflypanel.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class TardisStorage implements Capability.IStorage<ITardisCapability> {

    @Nullable
    @Override
    public INBT writeNBT(Capability<ITardisCapability> capability, ITardisCapability instance, Direction side) {
        return instance.writeNBT();
    }

    @Override
    public void readNBT(Capability<ITardisCapability> capability, ITardisCapability instance, Direction side, INBT nbt) {
        instance.readNBT((CompoundNBT) nbt);
    }
}
