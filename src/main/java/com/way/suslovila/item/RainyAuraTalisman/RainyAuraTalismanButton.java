package com.way.suslovila.item.RainyAuraTalisman;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.BookViewScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.sounds.SoundEvents;

public class RainyAuraTalismanButton extends Button {
    private final boolean isForward;
    private final boolean playTurnSound;

    public RainyAuraTalismanButton(int pX, int pY, boolean pIsForward, OnPress pOnPress, boolean pPlayTurnSound) {
        super(pX, pY, 23, 13, TextComponent.EMPTY, pOnPress);
        this.isForward = pIsForward;
        this.playTurnSound = pPlayTurnSound;
    }

    public void renderButton(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, RainyAuraTalismanScreen.TEXTURE_LOCATION);
        int i = 0;
        int j = 192;
        if (this.isHoveredOrFocused()) {
            i += 23;
        }

        if (!this.isForward) {
            j += 13;
        }

        this.blit(pPoseStack, this.x, this.y, i, j, 23, 13);
    }

    public void playDownSound(SoundManager pHandler) {
        if (this.playTurnSound) {
            pHandler.play(SimpleSoundInstance.forUI(SoundEvents.BOOK_PAGE_TURN, 1.0F));
        }

    }
}
