package com.way.suslovila.savedData;
import com.way.suslovila.entity.hunter.appearance.HunterAppearanceFormEntity;
import com.way.suslovila.savedData.IsTheVictim.MessagesBoolean;
import com.way.suslovila.savedData.IsTheVictim.PacketSyncVictimToClientBoolean;
import com.way.suslovila.savedData.clientSynch.Messages;
import com.way.suslovila.savedData.clientSynch.PacketSyncVictimToClient;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.minecraft.world.phys.AABB;
import javax.annotation.Nonnull;
import java.util.List;
import java.util.UUID;

public class SaveVictim extends SavedData {
    private String victim;
    @Nonnull
    public static SaveVictim get(Level level) {
        if (level.isClientSide) {
            throw new RuntimeException("Don't access this client-side!");
        }
        DimensionDataStorage storage = level.getServer().overworld().getDataStorage();
        return storage.computeIfAbsent(SaveVictim::new, SaveVictim::new, "savevictim");
    }
    public SaveVictim() {
    }

    public SaveVictim(CompoundTag tag) {
        String victimUUID = tag.getString("victim");
        victim = victimUUID;
        setDirty();
    }
public String getVictim(){
        return victim;

}
/*
public void tick(Level level){
    level.players().forEach(player -> {
        if (player instanceof ServerPlayer serverPlayer) {
            if (SaveVictim.get(((ServerPlayer) player).level).getVictim() != null) {
                System.out.println("victim is");
                if (!(this.getVictim().equals("novictim"))) {
                    if (UUID.fromString(SaveVictim.get(((ServerPlayer) player).level).getVictim()).equals(((ServerPlayer) player).getUUID())) {
                        System.out.println("He is the Victim!");
                        List<Entity> entities = level.getEntities(player, new AABB((player).getX() - 30, player.getY() - 30, player.getZ() - 30, player.getX() + 30, player.getY() + 30, player.getZ() + 30));
                        for (int i = 0; i < entities.size(); i++) {
                            if (entities.get(i) instanceof HunterAppearanceFormEntity) {
                                System.out.println("we have a hunter here!");
                                System.out.println("There is a victim, lets send info");
                                //Messages.sendToHunter(new PacketSyncVictimToClient(UUID.fromString(SaveVictim.get(level).getVictim())), (HunterAppearanceFormEntity) entities.get(i));
                                Messages.sendToHunter(new PacketSyncVictimToClient(UUID.fromString(SaveVictim.get(level).getVictim())), (HunterAppearanceFormEntity) entities.get(i));

                            }
                        }
                    }

                }
            }
        }




        }

    );
}
*/

public void tick(Level level) {
    List<? extends Player> players = level.players();
    boolean flag = false;
    for (int i = 0; i < players.size(); i++) {
        if (players.get(i) instanceof ServerPlayer serverPlayer) {
            ServerPlayer player = (ServerPlayer) players.get(i);
            if (SaveVictim.get((player).level).getVictim() != null && !(SaveVictim.get((player).level).equals("novictim"))) {
                //System.out.println("victim is");
                if (!(SaveVictim.get((player).level).equals("novictim"))) {
                    if (UUID.fromString(SaveVictim.get((player).level).getVictim()).equals((player.getUUID()))) {
                        flag = true;
                        //System.out.println("He is the Victim!");
                        List<Entity> entities = level.getEntities(player, new AABB((player).getX() - 30, player.getY() - 30, player.getZ() - 30, player.getX() + 30, player.getY() + 30, player.getZ() + 30));
                        for (int k = 0; k < entities.size(); k++) {
                            if (entities.get(k) instanceof HunterAppearanceFormEntity) {
                                //System.out.println("we have a hunter here!");
                                //System.out.println("There is a victim, lets send info");
                                Messages.sendToHunter(new PacketSyncVictimToClient(UUID.fromString(SaveVictim.get(level).getVictim())), (HunterAppearanceFormEntity) entities.get(k));
                                MessagesBoolean.sendToHunter(new PacketSyncVictimToClientBoolean(true), (HunterAppearanceFormEntity) entities.get(k));
                            }
                        }
                    }

                }
            }
        }
    }
    if (!flag) {
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i) instanceof ServerPlayer) {
                ServerPlayer player = (ServerPlayer) players.get(i);
                List<Entity> entities = level.getEntities(player, new AABB((player).getX() - 30, player.getY() - 30, player.getZ() - 30, player.getX() + 30, player.getY() + 30, player.getZ() + 30));
                for (int k = 0; k < entities.size(); k++) {
                    if (entities.get(k) instanceof HunterAppearanceFormEntity) {
                        Messages.sendToHunter(new PacketSyncVictimToClient(UUID.randomUUID()), (HunterAppearanceFormEntity) entities.get(k));
                        MessagesBoolean.sendToHunter(new PacketSyncVictimToClientBoolean(false), (HunterAppearanceFormEntity) entities.get(k));
                    }
                }
            }
        }
    }
}






        @Override
        public CompoundTag save(CompoundTag tag) {
            tag.putString("victim", victim);
        return tag;
        }
    public void changeVictim(String newVictim){
        victim = newVictim;
        setDirty();
    }
}
