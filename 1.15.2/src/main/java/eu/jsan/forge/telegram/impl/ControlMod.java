package eu.jsan.forge.telegram.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import eu.jsan.forge.telegram.core.AbstractMod;
import eu.jsan.forge.telegram.core.config.TelegramConfig;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppedEvent;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;

@Mod(ControlMod.MODID)
public class ControlMod extends AbstractMod {

    public ControlMod() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void serverStarted(FMLServerStartedEvent event) throws IOException {
        File file = new File(FMLPaths.CONFIGDIR.get().toString() + "/" + ControlMod.MODID + "/config.json");
        if (!file.exists()) {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.setPrettyPrinting();
            Gson gson = gsonBuilder.create();
            FileUtils.write(file, gson.toJson(AbstractMod.config), StandardCharsets.UTF_8);
        } else {
            config = new Gson()
                .fromJson(FileUtils.readFileToString(file, StandardCharsets.UTF_8), TelegramConfig.class);
        }

        bot = new Bot(AbstractMod.config.token, LogManager.getLogger());
        MinecraftForge.EVENT_BUS.register(new Events(bot));
        broadcastServerStarted();
    }

    @SubscribeEvent
    public void serverStopped(FMLServerStoppedEvent event) {
        broadcastServerStopped();
    }
}
