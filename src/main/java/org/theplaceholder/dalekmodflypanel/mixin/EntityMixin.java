package org.theplaceholder.dalekmodflypanel.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.AxisAlignedBB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static org.theplaceholder.dalekmodflypanel.util.TardisUtils.isInFlightMode;

@Mixin(Entity.class)
public class EntityMixin {
    @Inject(method = "getBoundingBox", at = @At("HEAD"), cancellable = true)
    public void getBoundingBox(CallbackInfoReturnable<AxisAlignedBB> cir) {
        if(!(((Entity)(Object)this) instanceof PlayerEntity)) return;
        EntitySize size = EntitySize.scalable(1F, 2F);
        if(isInFlightMode((PlayerEntity)(Object)this))
            cir.setReturnValue(size.makeBoundingBox(((Entity)(Object)this).position()));
    }
}