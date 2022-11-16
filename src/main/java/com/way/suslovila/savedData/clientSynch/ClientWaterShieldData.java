package com.way.suslovila.savedData.clientSynch;

public class ClientWaterShieldData {
    private static boolean doShield;
    public static void setDoShield(boolean doShield3){
        doShield = doShield3;
    }
    public static boolean getShield(){
    return doShield;
    }
}
