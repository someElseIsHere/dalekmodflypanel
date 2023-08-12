package org.theplaceholder.dalekmodflypanel.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.swdteam.client.tardis.data.ExteriorModels;
import com.swdteam.common.init.DMTardis;
import com.swdteam.common.tardis.TardisData;
import com.swdteam.model.javajson.JSONModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.theplaceholder.dalekmodflypanel.capability.TardisCapability;
import org.theplaceholder.dalekmodflypanel.capability.TardisProvider;

import static org.theplaceholder.dalekmodflypanel.utils.TardisUtils.isInFlightMode;

public class RenderPlayerTardis {
    @SubscribeEvent
    public void guiOverlayEventPre(final RenderGameOverlayEvent.Pre e) {
        PlayerEntity player = Minecraft.getInstance().player;
        if (isInFlightMode(player)) {
            if (e.getType() == RenderGameOverlayEvent.ElementType.FOOD) {
                e.setCanceled(true);
            }
            if (e.getType() == RenderGameOverlayEvent.ElementType.HEALTH) {
                e.setCanceled(true);
            }
            if (e.getType() == RenderGameOverlayEvent.ElementType.EXPERIENCE) {
                e.setCanceled(true);
            }
            if (e.getType() == RenderGameOverlayEvent.ElementType.HOTBAR) {
                e.setCanceled(true);
            }
            if (e.getType() == RenderGameOverlayEvent.ElementType.AIR) {
                e.setCanceled(true);
            }
            if (e.getType() == RenderGameOverlayEvent.ElementType.ARMOR) {
                e.setCanceled(true);
            }
            if (e.getType() == RenderGameOverlayEvent.ElementType.JUMPBAR) {
                e.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void renderPlayerPreEvent(final RenderPlayerEvent.Pre e) {
        PlayerEntity player = e.getPlayer();
        if (isInFlightMode(player)) {
            e.setCanceled(true);
            player.getCapability(TardisProvider.TARDIS_CAPABILITY).ifPresent(capa -> {
                TardisData tardisData = DMTardis.getTardis(capa.getTardisId());
                JSONModel MODEL_TARDIS = ExteriorModels.getModel(tardisData.getTardisExterior().getData().getModel(tardisData.getSkinID()));
                IVertexBuilder ivertexbuilder = e.getBuffers().getBuffer(RenderType.text(MODEL_TARDIS.getModelData().getTexture()));
                renderTardis(MODEL_TARDIS, ivertexbuilder, player, e.getMatrixStack(), e.getBuffers(), (TardisCapability) capa, e.getPartialRenderTick(), e.getLight(), e.getLight());
            });
        }
    }

    public static void renderTardis(JSONModel MODEL_TARDIS, IVertexBuilder ivertexbuilder, PlayerEntity player, MatrixStack matrixStack, IRenderTypeBuffer iRenderTypeBuffer, TardisCapability tardis, float partialTicks, int combinedLightIn, int combinedOverlayIn) {
        matrixStack.pushPose();
        matrixStack.translate(0.5, 0.01, 0.5);
        matrixStack.translate(0.0, 1.5, 0.0);
        matrixStack.translate(0.0, 1.5, 0.0);
        float scale = MODEL_TARDIS.getModelData().getModel().modelScale;
        matrixStack.scale(scale, scale, scale);
        matrixStack.translate(0.0, -1.5, 0.0);
        matrixStack.translate(0.0, -0.01, 0.0);
        matrixStack.mulPose(Vector3f.ZP.rotationDegrees(180.0F));
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(tardis.rotation));

        if (!player.isOnGround()) {
            tardis.setBob((float)(Math.cos((double)(tardis.getBob() + partialTicks) * 0.05) * 0.5 + 0.5));
            tardis.setBob(tardis.getBob()*tardis.getBob());
        } else {
            tardis.setBob(1);
        }

        matrixStack.translate(0.0, tardis.getBob() - 1.0F, 0.0);
        MODEL_TARDIS.getModelData().getModel().renderToBuffer(matrixStack, iRenderTypeBuffer, ivertexbuilder, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);

        matrixStack.popPose();
    }
}
