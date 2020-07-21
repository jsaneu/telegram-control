package eu.jsan.forge.telegram.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import eu.jsan.forge.telegram.core.AbstractMod;
import eu.jsan.forge.telegram.core.config.TelegramConfig;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;

@Mod(modid = ControlMod.MODID, name = ControlMod.NAME, version = "1.0.0", acceptableRemoteVersions = "*")
public class ControlMod extends AbstractMod {


    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) throws IOException {
        File file = new File(
            event.getModConfigurationDirectory().getAbsolutePath() + "/" + ControlMod.MODID + "/config.json");
        if (!file.exists()) {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.setPrettyPrinting();
            Gson gson = gsonBuilder.create();
            FileUtils.write(file, gson.toJson(AbstractMod.config), StandardCharsets.UTF_8);
        } else {
            config = new Gson()
                .fromJson(FileUtils.readFileToString(file, StandardCharsets.UTF_8), TelegramConfig.class);
        }
    }

    @EventHandler
    public void serverStarted(FMLServerStartedEvent event) {
        bot = new Bot(config.token, LogManager.getLogger());
        MinecraftForge.EVENT_BUS.register(new Events(bot));
        broadcastServerStarted();
    }

    @EventHandler
    public void serverStopped(FMLServerStoppedEvent event) {
        broadcastServerStopped();
    }
}
