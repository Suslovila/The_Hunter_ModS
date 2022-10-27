package com.way.suslovila.simplybackpacks.items;



import com.way.suslovila.MysticalCreatures;
import com.way.suslovila.simplybackpacks.gui.FilterContainer;
import com.way.suslovila.simplybackpacks.gui.SBContainer;
import com.way.suslovila.simplybackpacks.inventory.BackpackData;
import com.way.suslovila.simplybackpacks.inventory.BackpackManager;
import com.way.suslovila.simplybackpacks.inventory.FilterItemHandler;
import com.way.suslovila.simplybackpacks.inventory.SBItemHandler;
import com.way.suslovila.simplybackpacks.util.BackpackUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class BackpackItem extends Item {
    public BackpackItem(String name, Backpack tier) {
        super(new Properties().stacksTo(1).tab(CreativeModeTab.TAB_TOOLS).fireResistant());
        this.name = name;
        this.tier = tier;
    }

    final String name;
    final Backpack tier;

    public static BackpackData getData(ItemStack stack) {
        if (!(stack.getItem() instanceof BackpackItem))
            return null;
        UUID uuid;
        CompoundTag tag = stack.getOrCreateTag();
        if (!tag.contains("UUID")) {
            uuid = UUID.randomUUID();
            tag.putUUID("UUID", uuid);
        } else
            uuid = tag.getUUID("UUID");
        return BackpackManager.get().getOrCreateBackpack(uuid, ((BackpackItem) stack.getItem()).tier);
    }

    public static boolean isBackpack(ItemStack stack) {
        return stack.getItem() instanceof BackpackItem;
    }

    @Override
    @Nonnull
    public Rarity getRarity(@Nonnull ItemStack stack) {
        return this.tier.rarity;
    }


    @Override
    @Nonnull
    public Component getDescription() {
        return new TranslatableComponent(this.getDescriptionId()).withStyle(this.tier == Backpack.COMMON? ChatFormatting.DARK_AQUA:ChatFormatting.RESET);
    }

    @Override
    public boolean isEnchantable(@Nonnull ItemStack stack) {
        return false;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return MysticalCreatures.SOULBOUND_LOOKUP.contains(enchantment);
    }

    @Override
    @Nonnull
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, @Nonnull InteractionHand handIn) {
        ItemStack backpack = playerIn.getItemInHand(handIn);
        if (!worldIn.isClientSide && playerIn instanceof ServerPlayer && backpack.getItem() instanceof BackpackItem) {
            BackpackData data = BackpackItem.getData(backpack);
            //Old backpack, lets migrate
            BackpackUtils.ifTagContains(backpack, "Inventory", tag -> {
                // Fixes FTBTeam/FTB-Modpack-Issues #478
                if (tag.getCompound("Inventory").contains("Size"))
                    tag.getCompound("Inventory").remove("Size");
                ((SBItemHandler) data.getHandler()).deserializeNBT(tag.getCompound("Inventory"));
                if (tag.contains("Filter")) {
                    data.getFilter().deserializeNBT(tag.getCompound("Filter"));
                    tag.remove("Filter");
                }
                playerIn.sendMessage(new TextComponent("Backpack Migrated"), Util.NIL_UUID);

                tag.remove("Inventory");
            });
            UUID uuid = data.getUuid();

            data.updateAccessRecords(playerIn.getName().getString(), System.currentTimeMillis());


                //open
                NetworkHooks.openGui(((ServerPlayer) playerIn), new SimpleMenuProvider( (windowId, playerInventory, playerEntity) -> new SBContainer(windowId, playerInventory, uuid, data.getTier(), data.getHandler()), backpack.getHoverName()), (buffer -> buffer.writeUUID(uuid).writeInt(data.getTier().ordinal())));

        }
        return InteractionResultHolder.success(playerIn.getItemInHand(handIn));
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new BackpackCaps(stack);
    }

    static class BackpackCaps implements ICapabilityProvider {
        private final ItemStack stack;

        public BackpackCaps(ItemStack stack) {
            this.stack = stack;
        }

        private LazyOptional<IItemHandler> optional = LazyOptional.empty();

        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
            if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
                if(!this.optional.isPresent())
                    this.optional = BackpackManager.get().getCapability(this.stack);
                return this.optional.cast();
            }
            else
                return LazyOptional.empty();
        }
    }

    public static void togglePickup(Player playerEntity, ItemStack stack) {
        CompoundTag nbt = stack.getOrCreateTag();

        boolean Pickup = !nbt.getBoolean("Pickup");

        nbt.putBoolean("Pickup", Pickup);


    }


    public static boolean applyFilter(ItemStack item, ItemStack packItem) {
        LazyOptional<IItemHandler> handlerOptional = packItem.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);

        if (handlerOptional.isPresent() && handlerOptional.resolve().get() instanceof SBItemHandler) {
            BackpackData data = BackpackItem.getData(packItem);
            if (data == null)
                return false;

            FilterItemHandler filterHandler = data.getFilter();

            int filterOpts = packItem.getOrCreateTag().getInt("Filter-OPT");
            boolean whitelist = (filterOpts & 1) > 0;
            boolean nbtMatch = (filterOpts & 2) > 0;

            for (int i = 0; i < 16; i++) {
                ItemStack fStack = filterHandler.getStackInSlot(i);
                if (!fStack.isEmpty()) {
                    if (fStack.sameItem(item)) {
                        if (nbtMatch)
                            return ItemStack.tagMatches(fStack, item) == whitelist;
                        else
                            return whitelist;
                    }
                }
            }

            return !whitelist;
        }
        return false;
    }

    public static boolean pickupEvent(EntityItemPickupEvent event, ItemStack stack) {
        CompoundTag nbt = stack.getTag();
        if (nbt == null)
            return false;

        if (!nbt.getBoolean("Pickup"))
            return false;

        LazyOptional<IItemHandler> optional = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
        if (optional.isPresent()) {
            IItemHandler handler = optional.resolve().get();

            if (!(handler instanceof SBItemHandler))
                return false;

            if (!applyFilter(event.getItem().getItem(), stack))
                return false;

            ItemStack pickedUp = event.getItem().getItem();
            for (int i = 0; i < handler.getSlots(); i++) {
                ItemStack slot = handler.getStackInSlot(i);
                if (slot.isEmpty() || (ItemHandlerHelper.canItemStacksStack(slot, pickedUp) && slot.getCount() < slot.getMaxStackSize() && slot.getCount() < handler.getSlotLimit(i))) {
                    int remainder = handler.insertItem(i, pickedUp.copy(), false).getCount();
                    pickedUp.setCount(remainder);
                    if (remainder == 0)
                        break;
                }
            }
            return pickedUp.isEmpty();
        }
        else
            return false;
    }





}
