package eu.jsan.forge.telegram.impl;

import eu.jsan.forge.telegram.core.AbstractMod;
import java.io.IOException;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;

@Mod(ControlMod.MODID)
public class ControlMod extends AbstractMod {

    public ControlMod() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void serverStarted(FMLServerStartedEvent event) throws IOException {
        configDir = FMLPaths.CONFIGDIR.get().toString();
        updateConfiguration();
        bot = new Bot(config.token, LogManager.getLogger());
        MinecraftForge.EVENT_BUS.register(new Events());
        broadcastServerStarted();
    }

    @SubscribeEvent
    public void serverStopped(FMLServerStoppingEvent event) {
        broadcastServerStopped();
    }
}

