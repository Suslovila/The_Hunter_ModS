package com.way.suslovila.savedData.clientSynch;


import java.util.UUID;

/**
 * Class holding the data for victim client-side
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