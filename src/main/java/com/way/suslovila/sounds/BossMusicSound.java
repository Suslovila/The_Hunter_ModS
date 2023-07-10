package com.way.suslovila.sounds;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;

public class BossMusicSound extends AbstractTickableSoundInstance {
    private Player player;
    private int ticksExisted = 0;
    private int timeUntilFade;
    float volumeControl;

    private final SoundEvent soundEvent;
    //ControlledAnimation volumeControl;

    public BossMusicSound(SoundEvent sound, Player player) {
        super(sound, SoundSource.MUSIC);
        this.player = player;
        this.soundEvent = sound;
        this.attenuation = Attenuation.NONE;
        this.looping = true;
        this.delay = 0;
        volumeControl = 1;
        this.x = player.getX();
        this.y = player.getY();
        this.z = player.getZ();

        //volumeControl = new ControlledAnimation(40);
       //volumeControl.setTimer(20);
       // volume = volumeControl.getAnimationFraction();
        timeUntilFade = 200;
    }

    public boolean canPlaySound() {
        return HuntThemePlayer.bossMusic == this;
    }

    public void tick() {
        volumeControl = (float)timeUntilFade/200;
        // If the music should stop playing
        if (player == null || !player.isAlive() || player.isSilent()) {
            // If the player is dead, skip the fade timer and fade out right away
            if (player != null && !player.isAlive()) timeUntilFade = 0;
            player = null;
            if (timeUntilFade > 0) timeUntilFade--;
           // else volumeControl.decreaseTimer();
        }
        // If the music should keep playing
        else {
            //volumeControl.increaseTimer();
            timeUntilFade = 100;
        }

       if (volumeControl < 0.025) {
           stop();
          HuntThemePlayer.bossMusic = null;
    }

       volume = volumeControl;

//        if (ticksExisted % 100 == 0) {
//            Minecraft.getInstance().getMusicManager().stopPlaying();
//        }
//        ticksExisted++;
    }

    public void setPlayer(Player soundSource) {
        this.player = soundSource;
    }

    public Player getPlayer() {
        return player;
    }

    public SoundEvent getSoundEvent() {
        return soundEvent;
    }
}