package com.keko.stellaria.logic.keyBinds;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class KeyBindings {

    public static void registerKeybindings(){
        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            KeybindingsListeners.movementListener(client);
            KeybindingsListeners.checkSwordSlash(client);
            KeybindingsListeners.checkCelestialShieldAttack(client);
        });
    }

}
