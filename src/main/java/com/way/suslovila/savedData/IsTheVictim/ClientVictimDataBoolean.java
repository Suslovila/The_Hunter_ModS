package com.way.suslovila.savedData.IsTheVictim;


import java.util.UUID;

/**
 * Class holding the data for victim client-side
 */
public class ClientVictimDataBoolean {

    private static boolean isVictim;

    public static void set(boolean victim) {
        ClientVictimDataBoolean.isVictim = victim;
    }

    public static boolean getIsVictim() {
        return isVictim;
    }


}