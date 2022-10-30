package com.way.suslovila.simplybackpacks.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.way.suslovila.simplybackpacks.gui.SBContainer;
import com.way.suslovila.simplybackpacks.inventory.BackpackData;
import com.way.suslovila.simplybackpacks.inventory.BackpackManager;
import com.way.suslovila.simplybackpacks.util.BackpackUtils;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraftforge.network.NetworkHooks;

import java.util.Optional;
import java.util.UUID;

public class Open {
    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return Commands.literal("open")
            .requires(cs -> cs.hasPermission(1))
            .then(Commands.argument("UUID", StringArgumentType.string()).suggests(((context, builder) -> SharedSuggestionProvider.suggest(BackpackUtils.getUUIDSuggestions(context), builder))).executes(cs -> open(cs, StringArgumentType.getString(cs, "UUID"))));
    }

    public static int open(CommandContext<CommandSourceStack> ctx, String stringUUID) throws CommandSyntaxException {
        UUID uuid;
        try {
            uuid = UUID.fromString(stringUUID);
        }
        catch(IllegalArgumentException e){
            return 0;
        }
        BackpackManager backpacks = BackpackManager.get();

        if (backpacks.getMap().containsKey(uuid)) {
            ServerPlayer player = ctx.getSource().getPlayerOrException();

            Optional<BackpackData> data = backpacks.getBackpack(uuid);

            data.ifPresent(backpack -> {
                NetworkHooks.openGui(player, new SimpleMenuProvider( (windowId, playerInventory, playerEntity) -> new SBContainer(windowId, playerInventory, uuid, backpack.getTier(), backpack.getHandler()), new TextComponent(backpack.getTier().name)), (buffer -> buffer.writeUUID(uuid).writeInt(backpack.getTier().ordinal())));
            });
        } else
            ctx.getSource().sendFailure(new TranslatableComponent("simplyebackpacks.invaliduuid"));
        return 0;
    }
}