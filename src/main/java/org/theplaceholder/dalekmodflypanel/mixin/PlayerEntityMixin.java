package org.theplaceholder.dalekmodflypanel.mixin;

import com.swdteam.common.init.DMTardis;
import com.swdteam.common.tardis.TardisData;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static org.theplaceholder.dalekmodflypanel.util.TardisUtils.getTardisCapability;
import static org.theplaceholder.dalekmodflypanel.util.TardisUtils.isInFlightMode;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    @Inject(method = "jumpFromGround", at = @At("HEAD"), cancellable = true)
    private void jumpFromGround(CallbackInfo ci) {
        TardisData tardisData = DMTardis.getTardis(getTardisCapability((PlayerEntity)(Object)this).getTardisId());

        if (tardisData != null && isInFlightMode((PlayerEntity)(Object)this)) {
            if (tardisData.getFuel() <= 0)
                ci.cancel();
            else tardisData.addFuel(-0.5);
        }
    }
}
