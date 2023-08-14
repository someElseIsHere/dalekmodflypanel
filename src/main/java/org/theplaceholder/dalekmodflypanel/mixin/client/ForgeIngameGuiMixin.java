package org.theplaceholder.dalekmodflypanel.mixin.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IngameGui;
import net.minecraftforge.client.gui.ForgeIngameGui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.theplaceholder.dalekmodflypanel.client.ClientFlightData;

import java.util.UUID;

@Mixin(ForgeIngameGui.class)
public class ForgeIngameGuiMixin {
    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void render(MatrixStack MsIgnored, float fIgnored, CallbackInfo ci){
        UUID uuid = Minecraft.getInstance().player.getUUID();
        if (ClientFlightData.playerTardisFlightDataMap.containsKey(uuid)){
            ci.cancel();
        }
    }
}

