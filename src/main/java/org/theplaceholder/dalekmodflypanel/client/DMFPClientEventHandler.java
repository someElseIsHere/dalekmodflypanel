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
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.theplaceholder.dalekmodflypanel.util.TardisUtils.onGround;

public class DMFPClientEventHandler {
    private static final Map<UUID, Float> playerBobMap = new HashMap<>();
    private static final Map<UUID, Float> playerBobMapLast = new HashMap<>();

    @SubscribeEvent
    public static void renderPlayerPreEvent(final RenderPlayerEvent.Pre e) {
        UUID playerUUID = e.getPlayer().getUUID();
        ClientFlightData.TardisFlightData data = ClientFlightData.getPlayerTardisFlightData(playerUUID);

        if (data != null && data.inFlightMode) {
            TardisData tardisData = ClientTardisCache.getTardisData(data.tardisId);
            if (tardisData != null) {
                e.setCanceled(true);
                JSONModel modelTardis = ExteriorModels.getModel(tardisData.getTardisExterior().getData().getModel(tardisData.getSkinID()));
                ResourceLocation texture = modelTardis.getModelData().getTexture();
                IVertexBuilder vertexBuilder = e.getBuffers().getBuffer(RenderType.text(texture));
                renderTardis(modelTardis, vertexBuilder, e.getPlayer(), e.getMatrixStack(), e.getBuffers(), e.getLight(), e.getLight());
            }
        }
    }

    @SubscribeEvent
    public static void fovUpdateEvent(FOVUpdateEvent event){
        ClientFlightData.TardisFlightData data = ClientFlightData.getPlayerTardisFlightData(event.getEntity().getUUID());
        if (data != null && data.inFlightMode){
            event.setNewfov(1.0F);
        }
    }

    public static void renderTardis(JSONModel MODEL_TARDIS, IVertexBuilder ivertexbuilder, PlayerEntity player, MatrixStack matrixStack, IRenderTypeBuffer iRenderTypeBuffer, int combinedLightIn, int combinedOverlayIn) {
        ClientFlightData.TardisFlightData data = ClientFlightData.getPlayerTardisFlightData(player.getUUID());
        matrixStack.pushPose();
        matrixStack.translate(0.0, 2.5, 0.0);
        matrixStack.mulPose(Vector3f.ZP.rotationDegrees(180.0F));
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(data.rotation));

        playerBobMap.putIfAbsent(player.getUUID(), 1f);
        playerBobMapLast.putIfAbsent(player.getUUID(), 1f);

        float bobValue = !onGround(player)
                ? (float) (Math.cos(data.tickOffGround * 0.1f) * 0.5f) + 0.5f
                : (float) Math.min(1.0F, playerBobMap.get(player.getUUID()) + (playerBobMap.get(player.getUUID()) - (playerBobMapLast.get(player.getUUID()) - 0.1)));

        playerBobMapLast.put(player.getUUID(), playerBobMap.get(player.getUUID()));
        playerBobMap.put(player.getUUID(), bobValue);

        matrixStack.translate(0.0, playerBobMap.get(player.getUUID()), 0.0);
        JSONModel.ModelInformation modelData = MODEL_TARDIS.getModelData();
        modelData.getModel().renderToBuffer(matrixStack, iRenderTypeBuffer, ivertexbuilder, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);

        matrixStack.popPose();
    }
}
