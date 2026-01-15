package pro.noty.spam;

import net.fabricmc.api.ModInitializer;

public class SpamOptimised implements ModInitializer {
    public static final String MOD_ID = "spam_optimised";

    // Settings accessible by GUI and CombatManager
    public static boolean enabled = false;
    public static int cps = 10;
    public static String currentMode = "Sword";
    public static boolean autoCrit = true;

    @Override
    public void onInitialize() {
        // Server-side/Common initialization if needed
    }
}