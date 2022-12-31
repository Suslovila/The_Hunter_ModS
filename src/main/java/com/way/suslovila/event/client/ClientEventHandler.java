package com.way.suslovila.event.client;

import com.way.suslovila.entity.shadowGrabEntity.ShadowGrabEntity;
import com.way.suslovila.entity.shadowgardenentity.ShadowGardenEntity;
import com.way.suslovila.entity.trap.TrapEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.eventbus.api.SubscribeEvent;
@OnlyIn(Dist.CLIENT)

public enum ClientEventHandler {
    INSTANCE;
    @SubscribeEvent
    public void onRenderHUD(RenderGameOverlayEvent.PreLayer event) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null && player.isPassenger()) {
            if (player.getVehicle() instanceof TrapEntity || player.getVehicle() instanceof ShadowGrabEntity ||player.getVehicle() instanceof ShadowGardenEntity) {
            if (event.getOverlay() == ForgeIngameGui.MOUNT_HEALTH_ELEMENT) {
                   event.setCanceled(true);
               }
                if (event.getType().equals(RenderGameOverlayEvent.ElementType.LAYER)) {
                    Minecraft.getInstance().gui.setOverlayMessage(new TranslatableComponent(""), false);
                }
            }
        }
    }
}
