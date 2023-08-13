package org.theplaceholder.dalekmodflypanel.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;

public class TardisCapabilityImpl implements TardisCapability {
    private boolean isInFlight;
    private int tardisId;
    private BlockPos tardisPos = BlockPos.ZERO;
    private float rotation;
    private int tickOnGround;
    private int tickOffGround;

    @Override
    public boolean isInFlight() {
        return isInFlight;
    }

    @Override
    public void setInFlight(boolean bl) {
        isInFlight = bl;
    }

    @Override
    public int getTardisId() {
        return tardisId;
    }

    @Override
    public void setTardisId(int id) {
        tardisId = id;
    }

    @Override
    public BlockPos getInteriorPos() {
        return tardisPos;
    }

    @Override
    public void setInteriorPos(BlockPos pos) {
        tardisPos = pos;
    }

    @Override
    public float getRotation() {
        return rotation;
    }

    @Override
    public void setRotation(float rot) {
        rotation = rot;
    }

    @Override
    public int getTickOnGround() {
        return tickOnGround;
    }

    @Override
    public void setTickOnGround(int tick) {
        tickOnGround = tick;
    }

    @Override
    public int getTickOffGround() {
        return tickOffGround;
    }

    @Override
    public void setTickOffGround(int tick) {
        tickOffGround = tick;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putBoolean("isInFlight", isInFlight);
        nbt.putInt("tardisId", tardisId);
        nbt.putFloat("rotation", rotation);
        nbt.putLong("tardisPos", tardisPos.asLong());
        nbt.putInt("tickOnGround", tickOnGround);
        nbt.putInt("tickOffGround", tickOffGround);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        nbt.putBoolean("isInFlight", isInFlight);
        nbt.putInt("tardisId", tardisId);
        nbt.putFloat("rotation", rotation);
        nbt.putLong("tardisPos", tardisPos.asLong());
        nbt.putInt("tickOnGround", tickOnGround);
        nbt.putInt("tickOffGround", tickOffGround);
    }
}
