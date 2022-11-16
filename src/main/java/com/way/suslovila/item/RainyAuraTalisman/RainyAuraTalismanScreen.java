package com.way.suslovila.item.RainyAuraTalisman;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.way.suslovila.MysticalCreatures;
import com.way.suslovila.savedData.clientSynch.Messages;
import com.way.suslovila.savedData.clientSynch.PacketSyncTalismanButtonToServer;
import net.minecraft.client.gui.chat.NarratorChatListener;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class RainyAuraTalismanScreen extends Screen {
    private final Player owner;
    private final ItemStack talisman;
    Button extinguishFire;
    Button extinguishLava;
    Button extinguishEntity;
    Button protectYourself;
    Button collectWater;
    private final InteractionHand hand;
    public static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(MysticalCreatures.MOD_ID,"textures/gui/talisman.png");


    public RainyAuraTalismanScreen(Player pOwner, ItemStack talisman, InteractionHand pHand) {
        super(NarratorChatListener.NO_TITLE);
        this.owner = pOwner;
        this.talisman = talisman;
        this.hand = pHand;

    }
    protected void init() {
        assert this.minecraft != null;
        this.minecraft.keyboardHandler.setSendRepeatsToGui(true);
        this.extinguishFire = this.addRenderableWidget(new Button(0, 0, 98, 20, new TextComponent("Extinguish Fire"), (p_98177_) -> {
            Messages.sendToServer(new PacketSyncTalismanButtonToServer(hand.name(), "extinguishFire"));
            System.out.println("Clicked on Button!!!");
        }));
        this.collectWater = this.addRenderableWidget(new Button(0, 30, 98, 20, new TextComponent("Extinguish Lava"), (p_98173_) -> {
            Messages.sendToServer(new PacketSyncTalismanButtonToServer(hand.name(), "extinguishLava"));
            System.out.println("Clicked on Button!!!");

        }));
        this.extinguishEntity = this.addRenderableWidget(new Button(0, 60, 98, 20, new TextComponent("Extinguish Entity"), (p_98168_) -> {
            Messages.sendToServer(new PacketSyncTalismanButtonToServer(hand.name(), "extinguishEntity"));
            System.out.println("Clicked on Button!!!");

        }));
        this.extinguishLava = this.addRenderableWidget(new Button(0, 90, 98, 20, new TextComponent("Protect Yourself"), (p_98157_) -> {
            Messages.sendToServer(new PacketSyncTalismanButtonToServer(hand.name(), "protectYourself"));
            System.out.println("Clicked on Button!!!");

        }));
        this.protectYourself = this.addRenderableWidget(new Button(0, 120, 98, 20, new TextComponent("Collect Water"), (p_98157_) -> {
            Messages.sendToServer(new PacketSyncTalismanButtonToServer(hand.name(), "collectWater"));
            System.out.println("Clicked on Button!!!");

        }));
        int i = (this.width - 192) / 2;
        int j = 2;
//        this.forwardButton = this.addRenderableWidget(new PageButton(i + 116, 159, true, (p_98144_) -> {
//            this.pageForward();
//        }, true));
//        this.backButton = this.addRenderableWidget(new PageButton(i + 43, 159, false, (p_98113_) -> {
//            this.pageBack();
//        }, true));
//        this.updateButtonVisibility();
    }

    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(pPoseStack);
        this.setFocused((GuiEventListener)null);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE_LOCATION);
        int i = (this.width - 256) / 2;
        int j = 2;
//        this.x = (this.width - IMAGE_WIDTH) / 2;
//        this.y = (this.height - IMAGE_HEIGHT) / 2;
        this.blit(pPoseStack, i, 2, 0, 0, 256, 399);

        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
    }




}
