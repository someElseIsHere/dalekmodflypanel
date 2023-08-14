package org.theplaceholder.dalekmodflypanel.mixin;

import com.swdteam.common.init.DMTardis;
import com.swdteam.common.tardis.TardisData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import static org.theplaceholder.dalekmodflypanel.util.TardisUtils.getTardisCapability;
import static org.theplaceholder.dalekmodflypanel.util.TardisUtils.isInFlightMode;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> p_i48580_1_, World p_i48580_2_) {
        super(p_i48580_1_, p_i48580_2_);
    }

    @ModifyArg(method = "jumpFromGround", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;setDeltaMovement(DDD)V", ordinal = 0), index = 1)
    public double jumpFromGround(double x) {
        if(!(((Entity)this) instanceof PlayerEntity))
            return x;
        PlayerEntity player = (PlayerEntity) (Object) this;
        if(isInFlightMode(player)){
            TardisData tardisData = DMTardis.getTardis(getTardisCapability(player).getTardisId());
            if (tardisData.getFuel() <= 0)
                return 0;
        }

        return x;
    }
}
