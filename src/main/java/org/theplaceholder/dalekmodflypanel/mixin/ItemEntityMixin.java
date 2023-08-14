package org.theplaceholder.dalekmodflypanel.mixin;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static org.theplaceholder.dalekmodflypanel.util.TardisUtils.isInFlightMode;

@Mixin(value = ItemEntity.class)
public class ItemEntityMixin {
    @Inject(method = "playerTouch", at = @At("HEAD"), cancellable = true)
    private void playerTouchMixin(PlayerEntity player, CallbackInfo ci) {
        if (isInFlightMode(player)) {
            ci.cancel();
        }
    }
}
