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

import static org.theplaceholder.dalekmodflypanel.util.TardisUtils.onGround;

public class RenderPlayerTardis {
    private static final Map<UUID, Float> playerBobMap = new HashMap<>();
    private static final Map<UUID, Float> playerBobMapLast = new HashMap<>();
    @SubscribeEvent
    public void guiOverlayEventPre(final RenderGameOverlayEvent.Pre e) {
        ClientTardisFlightDataManager.TardisFlightData data = ClientTardisFlightDataManager.getPlayerTardisFlightData(Minecraft.getInstance().player.getUUID());
        if (data != null && data.inFlightMode) {
            e.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void renderPlayerPreEvent(final RenderPlayerEvent.Pre e) {
        ClientTardisFlightDataManager.TardisFlightData data = ClientTardisFlightDataManager.getPlayerTardisFlightData(e.getPlayer().getUUID());
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
        ClientTardisFlightDataManager.TardisFlightData data = ClientTardisFlightDataManager.getPlayerTardisFlightData(player.getUUID());
        matrixStack.pushPose();
        matrixStack.translate(0.0, 1.5 - 0.01, 0.0);
        matrixStack.mulPose(Vector3f.ZP.rotationDegrees(180.0F));
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(data.rotation));

        playerBobMap.putIfAbsent(player.getUUID(), 1f);
        playerBobMapLast.putIfAbsent(player.getUUID(), 1f);

        float bobValue = !onGround(player)
                ? (float) ((Math.cos(data.tickOffGround + partialTicks) * 0.05) * 0.5 + 0.5) * (float) ((Math.cos(data.tickOffGround + partialTicks) * 0.05) * 0.5 + 0.5)
                : (float) Math.min(1.0F, playerBobMap.get(player.getUUID()) + (playerBobMap.get(player.getUUID()) - (playerBobMapLast.get(player.getUUID()) - 0.001)));

        playerBobMap.put(player.getUUID(), bobValue);
        playerBobMapLast.put(player.getUUID(), playerBobMap.get(player.getUUID()));

        matrixStack.translate(0.0, playerBobMap.get(player.getUUID()) - 1.0F, 0.0);
        JSONModel.ModelInformation modelData = MODEL_TARDIS.getModelData();
        modelData.getModel().renderToBuffer(matrixStack, iRenderTypeBuffer, ivertexbuilder, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);

        matrixStack.popPose();

    }
}
