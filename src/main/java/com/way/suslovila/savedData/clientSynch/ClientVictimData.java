package com.way.suslovila.savedData.clientSynch;


import java.util.UUID;

/**

 */
public class ClientVictimData {

    private static UUID victim;

    public static void set(UUID victim) {
        ClientVictimData.victim = victim;
    }

    public static UUID getVictim() {
        return victim;
    }


}