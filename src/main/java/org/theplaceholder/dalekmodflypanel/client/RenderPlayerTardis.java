package org.theplaceholder.dalekmodflypanel.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.swdteam.model.javajson.JSONModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Map;
import java.util.UUID;

import static org.theplaceholder.dalekmodflypanel.utils.TardisUtils.isInFlightMode;

public class RenderPlayerTardis {
    public static Map<UUID, JSONModel> playerModelMap;
    public static Map<UUID, Float> playerTardisMap;
    public static Map<UUID, Integer> playerRotationMap;

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
        if (playerModelMap.containsKey(e.getPlayer().getUUID())) {
            e.setCanceled(true);
            JSONModel MODEL_TARDIS = playerModelMap.get(e.getPlayer().getUUID());
            IVertexBuilder ivertexbuilder = e.getBuffers().getBuffer(RenderType.text(MODEL_TARDIS.getModelData().getTexture()));
            renderTardis(MODEL_TARDIS, ivertexbuilder, e.getPlayer(), e.getMatrixStack(), e.getBuffers(), e.getPartialRenderTick(), e.getLight(), e.getLight());
        }
    }

    public static void renderTardis(JSONModel MODEL_TARDIS, IVertexBuilder ivertexbuilder, PlayerEntity player, MatrixStack matrixStack, IRenderTypeBuffer iRenderTypeBuffer, float partialTicks, int combinedLightIn, int combinedOverlayIn) {
        matrixStack.pushPose();
        matrixStack.translate(0.5, 0.01, 0.5);
        matrixStack.translate(0.0, 1.5, 0.0);
        matrixStack.translate(0.0, 1.5, 0.0);
        float scale = MODEL_TARDIS.getModelData().getModel().modelScale;
        matrixStack.scale(scale, scale, scale);
        matrixStack.translate(0.0, -1.5, 0.0);
        matrixStack.translate(0.0, -0.01, 0.0);
        matrixStack.mulPose(Vector3f.ZP.rotationDegrees(180.0F));
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(playerRotationMap.get(player.getUUID())));

        if (!player.isOnGround()) {
            float f = playerTardisMap.get(player.getUUID());
            f = (float) (Math.cos((double)(f + partialTicks) * 0.05) * 0.5 + 0.5);
            f = f * f;
            playerTardisMap.put(player.getUUID(), f);
        } else {
            playerTardisMap.put(player.getUUID(), 1f);
        }

        playerRotationMap.put(player.getUUID(), playerRotationMap.get(player.getUUID()) + 1);
        if (playerRotationMap.get(player.getUUID()) >= 360) {
            playerRotationMap.put(player.getUUID(), 0);
        }

        matrixStack.translate(0.0, playerTardisMap.get(player.getUUID()) - 1.0F, 0.0);
        MODEL_TARDIS.getModelData().getModel().renderToBuffer(matrixStack, iRenderTypeBuffer, ivertexbuilder, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);

        matrixStack.popPose();
    }
}
