package org.theplaceholder.dalekmodflypanel.capability;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.PacketDistributor;
import org.theplaceholder.dalekmodflypanel.DalekModFlyPanel;
import org.theplaceholder.dalekmodflypanel.packet.SyncFlightPacket;

import java.util.UUID;

public class TardisCapability implements ITardisCapability{
    public boolean isInFlight;
    private int tardisId;
    private BlockPos tardisPos;
    public float rotation;
    private UUID player;
    private int tickOnGround;
    private int tickOffGround;
    private int skinId;


    public TardisCapability(UUID player) {
        this.isInFlight = false;
        this.tardisId = 0;
        this.tardisPos = BlockPos.ZERO;
        this.rotation = 0.0f;
        this.tickOnGround = 0;
        this.player = player;
        this.tickOffGround = 0;
        this.skinId = 0;
    }

    public TardisCapability() {
        this.isInFlight = false;
        this.tardisId = 0;
        this.tardisPos = BlockPos.ZERO;
        this.rotation = 0.0f;
        this.tickOnGround = 0;
        this.player = new UUID(0, 0);
        this.tickOffGround = 0;
        this.skinId = 0;
    }

    public void sync() {
        DalekModFlyPanel.NETWORK.send(PacketDistributor.PLAYER.noArg(), new SyncFlightPacket(writeNBT()));
    }

    @Override
    public CompoundNBT writeNBT() {
        CompoundNBT tag = new CompoundNBT();
        tag.putBoolean("isInFlight", this.getInFlight());
        tag.putInt("tardisId", this.getTardisId());
        tag.putFloat("rotation", this.getRotation());
        tag.putLong("tardisPos", this.getTardisPos().asLong());
        tag.putInt("tickOnGround", this.getTickOnGround());
        tag.putInt("tickOffGround", this.getTickOffGround());
        tag.putUUID("player", this.getPlayer());
        tag.putInt("skinId", this.getSkinId());
        return tag;
    }

    @Override
    public void readNBT(CompoundNBT tag) {
        this.setInFlight(tag.getBoolean("isInFlight"));
        this.setTardisId(tag.getInt("tardisId"));
        this.setRotation(tag.getFloat("rotation"));
        this.setTardisPos(BlockPos.of(tag.getLong("tardisPos")));
        this.setTickOnGround(tag.getInt("tickOnGround"));
        this.setTickOffGround(tag.getInt("tickOffGround"));
        this.setPlayer(tag.getUUID("player"));
        this.setSkinId(tag.getInt("skinId"));
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
    public int getTickOffGround() {
        return tickOffGround;
    }

    @Override
    public void setTickOffGround(int i) {
        tickOffGround = i;
    }

    @Override
    public UUID getPlayer() {
        return player;
    }

    @Override
    public void setPlayer(UUID uuid) {
        player = uuid;
    }

    @Override
    public int getSkinId() {
        return skinId;
    }

    @Override
    public void setSkinId(int i) {
        skinId = i;
    }
}
