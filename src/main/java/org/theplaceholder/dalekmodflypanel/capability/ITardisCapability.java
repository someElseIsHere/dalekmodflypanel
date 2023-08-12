package org.theplaceholder.dalekmodflypanel.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;

import java.util.UUID;

public interface ITardisCapability {

    void sync();

    CompoundNBT writeNBT();
    void readNBT(CompoundNBT nbt);

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

    int getTickOffGround();
    void setTickOffGround(int i);
    UUID getPlayer();
    void setPlayer(UUID uuid);

    int getSkinId();
    void setSkinId(int i);
}