package org.theplaceholder.dalekmodflypanel.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;

public interface ITardisCapability {

    CompoundNBT writeNBT();
    void readNBT(CompoundNBT nbt);
    void syncToPlayer();

    boolean getInFlight();

    void setInFlight(boolean bl);

    int getTardisId();

    void setTardisId(int i);

    void setTardisPos(BlockPos bp);

    BlockPos getTardisPos();

    float getRotation();

    void setRotation(float f);

    void setTickOnGround(int i);
    int getTickOnGround();
}