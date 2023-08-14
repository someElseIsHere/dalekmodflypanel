package org.theplaceholder.dalekmodflypanel.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static org.theplaceholder.dalekmodflypanel.util.TardisUtils.getTardisCapability;
import static org.theplaceholder.dalekmodflypanel.util.TardisUtils.isInFlightMode;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Shadow public World level;

    @Shadow public abstract boolean isAlive();

    @Inject(method = "getBoundingBox", at = @At("HEAD"), cancellable = true)
    private void getBoundingBox(CallbackInfoReturnable<AxisAlignedBB> cir) {
        if(level.isClientSide) return;
        if(!(((Entity)(Object)this) instanceof PlayerEntity)) return;
        if(getTardisCapability((PlayerEntity)(Object)this) == null) return;

        EntitySize size = EntitySize.scalable(1F, 2F);
        if(isInFlightMode((PlayerEntity)(Object)this))
            cir.setReturnValue(size.makeBoundingBox(((Entity)(Object)this).position()));
    }
    @Inject(method = "canBeCollidedWith", at = @At("HEAD"), cancellable = true)
    private void canBeCollidedWith(CallbackInfoReturnable<Boolean> cir) {
        if(level.isClientSide) return;
        if(!(((Entity)(Object)this) instanceof PlayerEntity)) return;
        if(getTardisCapability((PlayerEntity)(Object)this) == null) return;

        if(isInFlightMode((PlayerEntity)(Object)this))
            cir.setReturnValue(isAlive());
    }
}
