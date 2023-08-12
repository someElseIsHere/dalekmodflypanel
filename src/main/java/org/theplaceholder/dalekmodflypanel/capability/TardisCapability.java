package org.theplaceholder.dalekmodflypanel.capability;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.PacketDistributor;
import org.theplaceholder.dalekmodflypanel.DalekModFlyPanel;

public class TardisCapability implements ITardisCapability{
    public boolean isInFlight;
    private int tardisId;
    private BlockPos tardisPos;
    public float rotation;
    private PlayerEntity player;
    private int tickOnGround;
    private float bob;
    private int tickOffGround;


    public TardisCapability(PlayerEntity player) {
        this.isInFlight = false;
        this.tardisId = 0;
        this.tardisPos = BlockPos.ZERO;
        this.rotation = 0.0f;
        this.tickOnGround = 0;
        this.player = player;
        this.bob = 0.0f;
        this.tickOffGround = 0;
    }


    @Override
    public CompoundNBT writeNBT() {
        CompoundNBT tag = new CompoundNBT();
        tag.putBoolean("isInFlight", this.getInFlight());
        tag.putInt("tardisId", this.getTardisId());
        tag.putFloat("rotation", this.getRotation());
        tag.putLong("tardisPos", this.getTardisPos().asLong());
        tag.putInt("tickOnGround", this.getTickOnGround());
        tag.putFloat("bob", this.getBob());
        return tag;
    }

    @Override
    public void readNBT(CompoundNBT tag) {
        this.setInFlight(tag.getBoolean("isInFlight"));
        this.setTardisId(tag.getInt("tardisId"));
        this.setRotation(tag.getFloat("rotation"));
        this.setTardisPos(BlockPos.of(tag.getLong("tardisPos")));
        this.setTickOnGround(tag.getInt("tickOnGround"));
    }

    @Override
    public boolean getInFlight() {
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
    public void setTardisId(int i) {
        tardisId = i;
    }

    @Override
    public void setTardisPos(BlockPos bp) {
        tardisPos = bp;
    }


    @Override
    public BlockPos getTardisPos() {
        return tardisPos;
    }


    @Override
    public float getRotation() {
        return rotation;
    }

    @Override
    public void setRotation(float f) {
        rotation = f;
    }

    @Override
    public void setTickOnGround(int i) {
        tickOnGround = i;
    }
    @Override
    public int getTickOnGround() {
        return tickOnGround;
    }

    @Override
    public float getBob() {
        return bob;
    }

    @Override
    public void setBob(float i) {
        bob = i;
    }

    @Override
    public int getTickOffGround() {
        return tickOffGround;
    }

    @Override
    public void setTickOffGround(int i) {
        tickOffGround = i;
    }
}
