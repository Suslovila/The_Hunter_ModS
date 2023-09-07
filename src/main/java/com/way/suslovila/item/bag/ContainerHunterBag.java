package com.way.suslovila.item.bag;

import com.way.suslovila.item.ModItems;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;

public class ContainerHunterBag extends AbstractContainerMenu {
    public static ContainerHunterBag fromNetwork(int windowId, Inventory inv, FriendlyByteBuf buf) {
        InteractionHand hand = buf.readBoolean() ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
        return new ContainerHunterBag(windowId, inv, inv.player.getItemInHand(hand));
    }

    private final ItemStack bag;
    public final SimpleContainer hunterbagInv;


    public ContainerHunterBag(int windowId, Inventory playerInv, ItemStack bag) {
        super(ModItems.HUNTER_BAG_CONTAINER, windowId);

        this.bag = bag;
        if (!playerInv.player.level.isClientSide) {
            hunterbagInv = ItemHunterBag.getInventory(bag);
        } else {
            hunterbagInv = new SimpleContainer(ItemHunterBag.HunterBagSIZE);
        }

        //bag's slots
        for(int k = 0; k < 2; ++k) {
            for(int l = 0; l < 9; ++l) {
                this.addSlot(new BagSlot(hunterbagInv, l + k * 9, 8 + l * 18, 18 + k * 18));
            }
        }

        //player's inventory slots

        for(int i1 = 0; i1 < 3; ++i1) {
            for(int k1 = 0; k1 < 9; ++k1) {
                this.addSlot(new Slot(playerInv, k1 + i1 * 9 + 9, 8 + k1 * 18, 68 + i1 * 18));
            }
        }
        //hotbar slots:
        for (int i = 0; i < 9; ++i) {
            if (playerInv.getItem(i) == bag) {
                addSlot(new SlotLocked(playerInv, i, 8 + i * 18, 126));
            } else {
                addSlot(new Slot(playerInv, i, 8 + i * 18, 126));
            }
        }
    }


    @Override
    public boolean stillValid(@Nonnull Player player) {
        ItemStack main = player.getMainHandItem();
        ItemStack off = player.getOffhandItem();
        return !main.isEmpty() && main == bag || !off.isEmpty() && off == bag;
    }

    @Nonnull
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(pIndex);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (pIndex < this.hunterbagInv.getContainerSize()) {
                if (!this.moveItemStackTo(itemstack1, this.hunterbagInv.getContainerSize(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 0, this.hunterbagInv.getContainerSize(), false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return itemstack;
    }

}
