package org.theplaceholder.dalekmodflypanel.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.INBTSerializable;

public interface TardisCapability extends INBTSerializable<CompoundNBT> {

    boolean isInFlight();

    void setInFlight(boolean bl);

    int getTardisId();

    void setTardisId(int i);

    void setInteriorPos(BlockPos bp);

    BlockPos getInteriorPos();

    float getRotation();

    void setRotation(float f);

    void setTickOnGround(int i);
    int getTickOnGround();

    int getTickOffGround();
    void setTickOffGround(int i);
}
