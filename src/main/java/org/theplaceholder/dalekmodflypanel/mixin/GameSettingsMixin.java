package org.theplaceholder.dalekmodflypanel.mixin;

import net.minecraft.client.GameSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.PointOfView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.theplaceholder.dalekmodflypanel.client.RenderPlayerTardis;
import org.theplaceholder.dalekmodflypanel.client.TardisFlightDataManager;

@Mixin(GameSettings.class)
public class GameSettingsMixin {
    @Shadow protected Minecraft minecraft;

    @Inject(method = "setCameraType", at = @At("HEAD"), cancellable = true)
    private void setCameraType(PointOfView pov, CallbackInfo ci){
        TardisFlightDataManager.TardisFlightData data = TardisFlightDataManager.getPlayerTardisFlightData(minecraft.player.getUUID());
        if (data != null && data.inFlightMode){
            ci.cancel();
        }
    }
}
