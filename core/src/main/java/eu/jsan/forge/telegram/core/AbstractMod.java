package eu.jsan.forge.telegram.core;

import eu.jsan.forge.telegram.core.config.TelegramConfig;

public abstract class AbstractMod {

    public static final String MODID = "telegram-control";

    public static final String NAME = "Telegram Control";

    public static TelegramConfig config = new TelegramConfig();

    protected AbstractBot bot;

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
