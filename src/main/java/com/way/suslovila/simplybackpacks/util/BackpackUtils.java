package com.way.suslovila.simplybackpacks.util;


import com.mojang.brigadier.context.CommandContext;

import com.way.suslovila.MysticalCreatures;
import com.way.suslovila.simplybackpacks.configuration.ConfigCache;
import com.way.suslovila.simplybackpacks.inventory.BackpackManager;
import com.way.suslovila.simplybackpacks.items.BackpackItem;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;


import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.Consumer;

public class BackpackUtils {
    public static boolean curiosLoaded = false;

    public static boolean filterItem(ItemStack stack) {
        //check the config whitelist, overrides all checks further.
        if (ConfigCache.WHITELIST.contains(stack.getItem().getRegistryName()))
            return true;

        //check for forge:holds_items
        if (stack.is(MysticalCreatures.HOLDS_ITEMS))
            return false;

        //vanilla method
        if (!stack.getItem().canFitInsideContainerItems())
            return false;

        // if all else fails, check the config blacklist
        return !ConfigCache.BLACKLIST.contains(stack.getItem().getRegistryName());
    }

    public static ItemStack findBackpackForHotkeys(Player player, boolean includeHands) {
        if (includeHands && player.getMainHandItem().getItem() instanceof BackpackItem)
            return player.getMainHandItem();
        if (includeHands && player.getOffhandItem().getItem() instanceof BackpackItem)
            return player.getOffhandItem();



        Inventory inventory = player.getInventory();
        for (int i = 0; i <= 35; i++) {
            ItemStack stack = inventory.getItem(i);
            if (stack.getItem() instanceof BackpackItem)
                return stack;
        }
        return ItemStack.EMPTY;
    }

    @Nonnull
    public static Optional<UUID> getUUID(@Nonnull ItemStack stack) {
        if (stack.getItem() instanceof BackpackItem && stack.hasTag() && stack.getTag().contains("UUID"))
            return Optional.of(stack.getTag().getUUID("UUID"));
        else
            return Optional.empty();
    }

    public static Set<String> getUUIDSuggestions(CommandContext<CommandSourceStack> commandSource) {
        BackpackManager backpacks = BackpackManager.get();
        Set<String> list = new HashSet<>();

        backpacks.getMap().forEach((uuid, backpackData) -> list.add(uuid.toString()));

        return list;
    }

    public static boolean increasedAltChance(UUID uuidIn) {
        if (uuidIn.compareTo(People.FLANKS255) == 0)
            return true;

        if (uuidIn.compareTo(People.ELLPECK) == 0)
            return true;

        return uuidIn.compareTo(People.LONEZTAR) == 0;
    }

    public static void ifTag(ItemStack stack, Consumer<CompoundTag> consumer) {
        if (stack.hasTag())
            consumer.accept(stack.getTag());
    }

    public static void ifTagContains(ItemStack stack, String child, Consumer<CompoundTag> consumer) {
        if (stack.hasTag())
            if (stack.getTag().contains(child))
                consumer.accept(stack.getTag());
    }

    public static Optional<CompoundTag> getTag(ItemStack stack) {
        if (stack.hasTag())
            return Optional.of(stack.getTag());
        return Optional.empty();
    }

    public record Confirmation(String code, UUID player, UUID backpack){}

    public static String generateCode(Random random) {
        return "%08x".formatted(random.nextInt(Integer.MAX_VALUE));
    }

    private static final HashMap<String, Confirmation> confirmationMap = new HashMap<>();

    public static void addConfirmation(String code, UUID player, UUID backpack) {
        confirmationMap.put(code, new Confirmation(code, player, backpack));
    }

    public static void removeConfirmation(String code) {
        confirmationMap.remove(code);
    }

    public static Optional<Confirmation> getConfirmation(String code) {
        return Optional.ofNullable(confirmationMap.get(code));
    }
}
