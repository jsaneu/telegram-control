package eu.jsan.forge.telegram.impl;

import eu.jsan.forge.telegram.core.AbstractMod;
import java.io.IOException;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import org.apache.logging.log4j.LogManager;

@Mod(modid = ControlMod.MODID, name = ControlMod.NAME, version = "1.0.3", acceptableRemoteVersions = "*")
public class ControlMod extends AbstractMod {

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) throws IOException {
        configDir = event.getModConfigurationDirectory().getAbsolutePath();
        updateConfiguration();
    }

    @EventHandler
    public void serverStarted(FMLServerStartedEvent event) {
        bot = new Bot(config.token, LogManager.getLogger());
        MinecraftForge.EVENT_BUS.register(new Events());
        broadcastServerStarted();
    }

    @EventHandler
    public void serverStopped(FMLServerStoppingEvent event) {
        broadcastServerStopped();
    }
}
