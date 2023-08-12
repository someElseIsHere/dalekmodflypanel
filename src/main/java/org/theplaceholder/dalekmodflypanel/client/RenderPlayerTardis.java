package org.theplaceholder.dalekmodflypanel.client;

import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.swdteam.client.tardis.data.ExteriorModels;
import com.swdteam.common.init.DMTardis;
import com.swdteam.common.tardis.TardisData;
import com.swdteam.model.javajson.JSONModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
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

            });
        }
    }
}
