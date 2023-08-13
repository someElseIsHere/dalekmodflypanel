package org.theplaceholder.dalekmodflypanel.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.swdteam.client.tardis.data.ClientTardisCache;
import com.swdteam.client.tardis.data.ExteriorModels;
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

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RenderPlayerTardis {
    private static final Map<UUID, Float> playerBobMap = new HashMap<>();
    @SubscribeEvent
    public void guiOverlayEventPre(final RenderGameOverlayEvent.Pre e) {
        TardisFlightDataManager.TardisFlightData data = TardisFlightDataManager.getPlayerTardisFlightData(Minecraft.getInstance().player.getUUID());
        if (data != null && data.inFlightMode) {
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
        TardisFlightDataManager.TardisFlightData data = TardisFlightDataManager.getPlayerTardisFlightData(e.getPlayer().getUUID());
        if (data != null && data.inFlightMode) {
            TardisData tardisData = ClientTardisCache.getTardisData(data.tardisId);
            if (tardisData == null)
                return;
            e.setCanceled(true);
            JSONModel MODEL_TARDIS = ExteriorModels.getModel(tardisData.getTardisExterior().getData().getModel(tardisData.getSkinID()));
            IVertexBuilder ivertexbuilder = e.getBuffers().getBuffer(RenderType.text(MODEL_TARDIS.getModelData().getTexture()));
            renderTardis(MODEL_TARDIS, ivertexbuilder, e.getPlayer(), e.getMatrixStack(), e.getBuffers(), e.getPartialRenderTick(), e.getLight(), e.getLight());
        }
    }

    public static void renderTardis(JSONModel MODEL_TARDIS, IVertexBuilder ivertexbuilder, PlayerEntity player, MatrixStack matrixStack, IRenderTypeBuffer iRenderTypeBuffer, float partialTicks, int combinedLightIn, int combinedOverlayIn) {
        TardisFlightDataManager.TardisFlightData data = TardisFlightDataManager.getPlayerTardisFlightData(player.getUUID());
        matrixStack.pushPose();
        matrixStack.translate(0.5, 0.01, 0.5);
        matrixStack.translate(0.0, 1.5, 0.0);
        matrixStack.translate(0.0, 1.5, 0.0);
        float scale = MODEL_TARDIS.getModelData().getModel().modelScale;
        matrixStack.scale(scale, scale, scale);
        matrixStack.translate(0.0, -1.5, 0.0);
        matrixStack.translate(0.0, -0.01, 0.0);
        matrixStack.mulPose(Vector3f.ZP.rotationDegrees(180.0F));
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(data.rotation));

        if (!player.isOnGround() || !playerBobMap.containsKey(player.getUUID())) {
            float f = playerBobMap.get(player.getUUID());
            f = (float) (Math.cos((double)(f + partialTicks) * 0.05) * 0.5 + 0.5);
            f = f * f;
            playerBobMap.put(player.getUUID(), f);
        } else {
            playerBobMap.put(player.getUUID(), 1f);
        }

        matrixStack.translate(0.0, playerBobMap.get(player.getUUID()) - 1.0F, 0.0);
        MODEL_TARDIS.getModelData().getModel().renderToBuffer(matrixStack, iRenderTypeBuffer, ivertexbuilder, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);

        matrixStack.popPose();
    }
}
