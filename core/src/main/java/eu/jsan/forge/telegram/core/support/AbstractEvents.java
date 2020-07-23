package eu.jsan.forge.telegram.core.support;

import static eu.jsan.forge.telegram.core.AbstractMod.bot;

import eu.jsan.forge.telegram.core.AbstractMod;
import java.util.Arrays;
import org.apache.commons.lang3.StringUtils;

public abstract class AbstractEvents {

    protected void broadcastWhisper(String from, String to, String text) {
        if (AbstractMod.config.broadcast.playerWhisper && Arrays.stream(AbstractMod.config.skippedSenderNames)
            .noneMatch(s -> s.equals(from))) {
            bot.broadcast(Utils.template(AbstractMod.config.i18n.playerWhisper,
                Utils.toArray("${from}", "${to}", Utils.MESSAGE),
                Utils.toArray(from, to, text)),
                AbstractMod.config.disableNotification.playerWhisper);
        }
    }

    protected void broadcastCommand(String from, String commandName, String[] parameters) {
        if (AbstractMod.config.broadcast.playerCommand
            && Arrays.stream(AbstractMod.config.skippedCommands).noneMatch(s -> s.equals(commandName))) {
            bot.broadcast(Utils.template(AbstractMod.config.i18n.playerCommand,
                Utils.toArray(Utils.PLAYER, "${command}", "${parameters}"),
                Utils.toArray(from, commandName, Utils.obfuscateCommandParams(commandName, parameters))),
                AbstractMod.config.disableNotification.playerCommand);
        }
    }

    protected void broadcastChat(String from, String text) {
        if (AbstractMod.config.broadcast.playerMessage) {
            if (StringUtils.isNotBlank(text)) {
                bot.broadcast(Utils.template(AbstractMod.config.i18n.chatFromGame,
                    Utils.toArray(Utils.PLAYER, Utils.MESSAGE),
                    Utils.toArray(from, text)),
                    AbstractMod.config.disableNotification.playerMessage);
            }
        }
    }

    protected void broadcastDeath(String playername, String text, boolean pvp) {
        final String message = Utils.template(AbstractMod.config.i18n.playerDeath,
            Utils.MESSAGE, text.replaceFirst(playername, String.format("*%s*", playername)));
        if (pvp) {
            if (AbstractMod.config.broadcast.playerDeathPvp) {
                bot.broadcast(message, AbstractMod.config.disableNotification.playerDeathPvp);
            }
        } else if (AbstractMod.config.broadcast.playerDeathPve) {
            bot.broadcast(message, AbstractMod.config.disableNotification.playerDeathPve);
        }
    }

    protected void broadcastLogin(String playername) {
        if (AbstractMod.config.broadcast.playerLogged) {
            bot.broadcast(Utils.template(AbstractMod.config.i18n.playerLogged, Utils.PLAYER, playername),
                AbstractMod.config.disableNotification.playerLogged);
        }
    }

    protected void broadcastLogout(String playername) {
        if (AbstractMod.config.broadcast.playerLoggedOut) {
            bot.broadcast(Utils.template(AbstractMod.config.i18n.playerLoggedOut, Utils.PLAYER, playername),
                AbstractMod.config.disableNotification.playerLoggedOut);
        }
    }

    protected void broadcastDimension(String playername, String dimension) {
        if (AbstractMod.config.broadcast.playerChangedDimension) {
            bot.broadcast(Utils.template(AbstractMod.config.i18n.playerChangedDimension,
                Utils.toArray(Utils.PLAYER, "${dimension}"), Utils.toArray(playername, dimension)),
                AbstractMod.config.disableNotification.playerChangedDimension);
        }
    }

    protected void boardcastAdvancement(String playername, String advancement) {
        if (AbstractMod.config.broadcast.playerAdvancement && StringUtils.isNotBlank(advancement) && !advancement
            .contains("recipes")) {
            bot.broadcast(Utils.template(AbstractMod.config.i18n.playerAdvancement,
                Utils.toArray(Utils.PLAYER, "${advancement}"),
                Utils.toArray(playername, advancement)),
                AbstractMod.config.disableNotification.playerAdvancement);
        }
    }


}
