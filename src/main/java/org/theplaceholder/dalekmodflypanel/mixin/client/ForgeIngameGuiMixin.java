package org.theplaceholder.dalekmodflypanel.mixin.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IngameGui;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.client.gui.ForgeIngameGui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.theplaceholder.dalekmodflypanel.client.ClientFlightData;

@Mixin(ForgeIngameGui.class)
public abstract class ForgeIngameGuiMixin extends IngameGui {
    @Shadow public static boolean renderFood;

    public ForgeIngameGuiMixin(Minecraft p_i46325_1_) {
        super(p_i46325_1_);
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/client/gui/ForgeIngameGui;pre(Lnet/minecraftforge/client/event/RenderGameOverlayEvent$ElementType;Lcom/mojang/blaze3d/matrix/MatrixStack;)Z"))
    private void render(MatrixStack mStack, float partialTicks, CallbackInfo ci) {
        boolean renderHealthMount = minecraft.player.getVehicle() instanceof LivingEntity;
        renderFood = !renderHealthMount && !ClientFlightData.playerTardisFlightDataMap.containsKey(minecraft.player.getUUID());
    }
}
