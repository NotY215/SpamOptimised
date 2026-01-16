package pro.noty.spam.client;

import net.fabricmc.api.ClientModInitializer;
import pro.noty.spam.client.event.KeyInputHandler;
import pro.noty.spam.client.engine.CombatManager;

public class SpamOptimisedClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Register the "B" key logic
        KeyInputHandler.register();
        // Register the Tick engine for PVP
        CombatManager.register();
    }
}