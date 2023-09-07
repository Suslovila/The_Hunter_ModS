package com.way.suslovila.item.bag;

import com.way.suslovila.savedData.HunterBagData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.*;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.stream.IntStream;

public class ItemHunterBag extends Item {
    public static final int HunterBagSIZE = 27;

    public ItemHunterBag(Properties props) {
        super(props);
    }


    public static boolean isValid(int slot, ItemStack stack) {
        if (slot < 27) {
            return true;
        } else {
            return false;
        }
    }

    public static SimpleContainer getInventory(ItemStack stack) {
        return new ItemBackedInventory(stack, HunterBagSIZE) {
            @Override
            public boolean canPlaceItem(int slot, @Nonnull ItemStack stack) {
                return isValid(slot, stack);
            }
        };
    }
    public static void putItemsInBackpackFromHunterStorage(ItemStack stack, Level level) {
        int size = 18;
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            List<ItemStack> itemsInBag = HunterBagData.get(level).getItemsInBag();
            int sizeOfBag = itemsInBag.size();
            if (sizeOfBag != 0 && random.nextInt(3) == 2) {
                    int index = random.nextInt(sizeOfBag);
                    ItemStack item = itemsInBag.get(index);
                    getInventory(stack).setItem(i, item);
                    HunterBagData.get(level).removeItemFromBag(itemsInBag.get(index));
            }
        }
    }

    @Nonnull
    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, @Nonnull InteractionHand hand) {
        if (!world.isClientSide) {
            ItemStack stack = player.getItemInHand(hand);
            openMenu((ServerPlayer) player, new MenuProvider() {
                @Override
                public Component getDisplayName() {
                    return stack.getHoverName();
                }

                @Override
                public AbstractContainerMenu createMenu(int syncId, Inventory inv, Player player) {
                    return new ContainerHunterBag(syncId, inv, stack);
                }
            }, buf -> buf.writeBoolean(hand == InteractionHand.MAIN_HAND));
        }
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }

    @Nonnull
    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        Level world = ctx.getLevel();
        BlockPos pos = ctx.getClickedPos();
        Direction side = ctx.getClickedFace();

        BlockEntity tile = world.getBlockEntity(pos);
        if (tile != null) {
            if (!world.isClientSide) {
                Container tileInv;
                if (tile instanceof Container container) {
                    tileInv = container;
                } else {
                    return InteractionResult.FAIL;
                }

                Container bagInv = getInventory(ctx.getItemInHand());
                for (int i = 0; i < bagInv.getContainerSize(); i++) {
                    ItemStack flower = bagInv.getItem(i);
                    ItemStack rem = HopperBlockEntity.addItem(bagInv, tileInv, flower, side);
                    bagInv.setItem(i, rem);
                }

            }

            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }
    @Override
    public void onDestroyed(@Nonnull ItemEntity entity) {
        var container = getInventory(entity.getItem());
        var stream = IntStream.range(0, container.getContainerSize())
                .mapToObj(container::getItem)
                .filter(s -> !s.isEmpty());
        ItemUtils.onContainerDestroyed(entity, stream);
        container.clearContent();
    }


    @Override
    public boolean overrideStackedOnOther(
            @Nonnull ItemStack bag, @Nonnull Slot slot,
            @Nonnull ClickAction clickAction, @Nonnull Player player) {
        return InventoryHelper.overrideStackedOnOther(
                ItemHunterBag::getInventory,
                player.containerMenu instanceof ContainerHunterBag,
                bag, slot, clickAction, player);
    }

    @Override
    public boolean overrideOtherStackedOnMe(
            @Nonnull ItemStack bag, @Nonnull ItemStack toInsert,
            @Nonnull Slot slot, @Nonnull ClickAction clickAction,
            @Nonnull Player player, @Nonnull SlotAccess cursorAccess) {
        return InventoryHelper.overrideOtherStackedOnMe(
                ItemHunterBag::getInventory,
                player.containerMenu instanceof ContainerHunterBag,
                bag, toInsert, clickAction, cursorAccess);
    }

    public static void openMenu(ServerPlayer player, MenuProvider menu, Consumer<FriendlyByteBuf> writeInitialData) {
        NetworkHooks.openGui(player, menu, writeInitialData);
    }
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged)
    {
        return false;
    }
}