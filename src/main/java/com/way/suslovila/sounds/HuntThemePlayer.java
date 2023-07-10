package com.way.suslovila.sounds;


import com.way.suslovila.savedData.clientSynch.ClientVictimData;
import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import software.bernie.shadowed.eliotlash.mclib.math.functions.limit.Min;

import java.util.Objects;

public class HuntThemePlayer {
    public static BossMusicSound bossMusic;

    public static void playBossMusic() {
        Player player = Minecraft.getInstance().player;
        if (bossMusic != null) {
            BossMusicSound sound = bossMusic;
            float f2 = Minecraft.getInstance().options.getSoundSourceVolume(SoundSource.MUSIC);
            if (f2 <= 0) {
                bossMusic = null;
            } else if (player == null || (player != null && bossMusic.getPlayer() != player.level.getPlayerByUUID(ClientVictimData.getVictim()))) {
                bossMusic.setPlayer(null);
            } else if (bossMusic.getPlayer() == null) {
                bossMusic.setPlayer(player);
            }
            if (!Minecraft.getInstance().getSoundManager().isActive(bossMusic))
                Minecraft.getInstance().getSoundManager().play(bossMusic);

        } else {
            if (player != null && Objects.equals(player, player.level.getPlayerByUUID(ClientVictimData.getVictim()))) {
                bossMusic = new BossMusicSound(MCSounds.HuntTheme.get(), player);

            }
            if (bossMusic != null && !Minecraft.getInstance().getSoundManager().isActive(bossMusic)) {
                Minecraft.getInstance().getSoundManager().play(bossMusic);
            }
        }
    }

    public static void stopBossMusic() {
        //if (!ConfigHandler.CLIENT.playBossMusic.get()) return;

        if (bossMusic != null && bossMusic.getPlayer() == Minecraft.getInstance().player)
            bossMusic.setPlayer(null);
    }
}
