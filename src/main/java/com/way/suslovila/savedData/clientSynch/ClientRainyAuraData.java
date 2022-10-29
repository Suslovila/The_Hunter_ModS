package com.way.suslovila.savedData.clientSynch;


import net.minecraft.core.BlockPos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * Class holding the data for victim client-side
 */
public class ClientRainyAuraData {

    private static ArrayList<BlockPos> blocks;
    private static ArrayList<UUID> entities;

    public static void setBlocksAndEntities(ArrayList<BlockPos> blocks, ArrayList<UUID> entities) {
        ClientRainyAuraData.blocks = blocks;
        ClientRainyAuraData.entities = entities;
    }


    public static ArrayList<BlockPos> getBlocks() {
        return blocks;
    }
    public static ArrayList<UUID> getEntities() {
        return entities;
    }


}