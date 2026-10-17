package com.keko.stellaria.commands;

import com.keko.stellaria.commands.eini.RemoveEinStatueCommand;
import com.keko.stellaria.commands.eini.SummonEinStatueCommand;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class ModCommands {
    public static void init(){
        CommandRegistrationCallback.EVENT.register(SummonEinStatueCommand::register);
        CommandRegistrationCallback.EVENT.register(RemoveEinStatueCommand::register);
    }
}
