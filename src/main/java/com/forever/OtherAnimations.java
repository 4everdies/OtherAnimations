package com.forever;

import com.forever.module.modules.Animations;
import com.forever.config.Config;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import com.forever.command.CommandAnimations;
import net.minecraftforge.client.ClientCommandHandler;

@Mod(modid = "otheranimations", version = "1.0", acceptedMinecraftVersions = "1.8.9")
public class OtherAnimations {
    public static Animations animations;

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        animations = new Animations();
        Config.load(animations);
        animations.setEnabled(true);
        ClientCommandHandler.instance.registerCommand(new CommandAnimations());
    }
}