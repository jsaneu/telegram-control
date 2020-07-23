package eu.jsan.forge.telegram.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import eu.jsan.forge.telegram.core.config.TelegramConfig;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.FileUtils;

public abstract class AbstractMod {

    public static final String MODID = "telegram-control";

    public static final String NAME = "Telegram Control";

    public static TelegramConfig config = new TelegramConfig();

    public static AbstractBot bot;

    protected static String configDir;

    protected static void updateConfiguration() throws IOException {
        File file = new File(configDir + "/" + MODID + "/config.json");
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        Gson gson = gsonBuilder.create();
        if (file.exists()) {
            config = new Gson()
                .fromJson(FileUtils.readFileToString(file, StandardCharsets.UTF_8), TelegramConfig.class);
        }
        FileUtils.write(file, gson.toJson(config), StandardCharsets.UTF_8);
    }

    protected void broadcastServerStarted() {
        if (bot != null && config.broadcast.serverStarted) {
            bot.broadcast(config.i18n.serverStarted, config.disableNotification.serverStarted);
        }
    }

    protected void broadcastServerStopped() {
        if (bot != null && config.broadcast.serverStopped) {
            bot.broadcast(config.i18n.serverStopped, config.disableNotification.serverStopped);
        }
    }

}
