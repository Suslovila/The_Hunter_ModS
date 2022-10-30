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
    private static ArrayList<Integer> entities;

    public static void setEntities( ArrayList<Integer> entities) {
        ClientRainyAuraData.entities = entities;
    }
    public static void setBlocks(ArrayList<BlockPos> blocks) {
        ClientRainyAuraData.blocks = blocks;
    }


    public static ArrayList<BlockPos> getBlocks() {
        return blocks;
    }
    public static ArrayList<Integer> getEntities() {
        return entities;
    }


}