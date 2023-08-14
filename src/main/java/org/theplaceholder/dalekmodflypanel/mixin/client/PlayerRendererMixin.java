package org.theplaceholder.dalekmodflypanel.mixin.client;

import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.vector.Vector3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.theplaceholder.dalekmodflypanel.client.ClientFlightData;

@Mixin(PlayerRenderer.class)
public class PlayerRendererMixin {
    @Inject(method = "getRenderOffset(Lnet/minecraft/entity/Entity;F)Lnet/minecraft/util/math/vector/Vector3d;", at = @At("HEAD"), cancellable = true)
    private void getRenderOffset(Entity entity, float offset, CallbackInfoReturnable<Vector3d> cir) {
        if (ClientFlightData.playerTardisFlightDataMap.containsKey(entity.getUUID()))
            cir.setReturnValue(Vector3d.ZERO);
    }
}
