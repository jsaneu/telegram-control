package eu.jsan.forge.telegram.core.support;

import static eu.jsan.forge.telegram.core.AbstractMod.bot;
import static eu.jsan.forge.telegram.core.AbstractMod.config;
import static eu.jsan.forge.telegram.core.support.Utils.*;
import static eu.jsan.forge.telegram.core.support.Utils.MESSAGE;
import static eu.jsan.forge.telegram.core.support.Utils.PLAYER;
import static eu.jsan.forge.telegram.core.support.Utils.obfuscateCommandParams;
import static eu.jsan.forge.telegram.core.support.Utils.template;
import static eu.jsan.forge.telegram.core.support.Utils.toArray;

import java.util.Arrays;
import org.apache.commons.lang3.StringUtils;

public abstract class AbstractEvents {

    protected void broadcastWhisper(String from, String to, String text) {
        if (config.broadcast.playerWhisper && Arrays.stream(config.skippedSenderNames)
            .noneMatch(s -> s.equals(from))) {
            bot.broadcast(template(config.i18n.playerWhisper,
                toArray("${from}", "${to}", MESSAGE), toArray(escapeMarkdown(from), escapeMarkdown(to), escapeMarkdown(text))),
                config.disableNotification.playerWhisper);
        }
    }

    protected void broadcastCommand(String from, String commandName, String[] parameters) {
        if (config.broadcast.playerCommand
            && Arrays.stream(config.skippedCommands).noneMatch(s -> s.equals(commandName))) {
            bot.broadcast(template(config.i18n.playerCommand,
                toArray(PLAYER, "${command}", "${parameters}"),
                toArray(from, String.format("`%s`",escapeMarkdown(commandName)), String.format("`%s`",escapeMarkdown(obfuscateCommandParams(commandName, parameters))))),
                config.disableNotification.playerCommand);
        }
    }

    protected void broadcastChat(String from, String text) {
        if (StringUtils.isNotBlank(text)) {
            boolean mention = Arrays.stream(config.mentions).anyMatch(s -> StringUtils.containsIgnoreCase(text, s));
            if (mention || config.broadcast.playerMessage) {
                boolean disableNotification = mention ? config.disableNotification.mentions
                    : config.disableNotification.playerMessage;
                bot.broadcast(template(config.i18n.chatFromGame,
                    toArray(PLAYER, MESSAGE), toArray(escapeMarkdown(from), escapeMarkdown(text))), disableNotification);
            }
        }
    }

    protected void broadcastDeath(String playername, String text, boolean pvp) {
        final String message = template(config.i18n.playerDeath,
            MESSAGE, escapeMarkdown(text).replaceFirst(playername, String.format("*%s*", playername)));
        if (pvp) {
            if (config.broadcast.playerDeathPvp) {
                bot.broadcast(message, config.disableNotification.playerDeathPvp);
            }
        } else if (config.broadcast.playerDeathPve) {
            bot.broadcast(message, config.disableNotification.playerDeathPve);
        }
    }

    protected void broadcastLogin(String playername) {
        if (config.broadcast.playerLogged) {
            bot.broadcast(template(config.i18n.playerLogged, PLAYER, escapeMarkdown(playername)),
                config.disableNotification.playerLogged);
        }
    }

    protected void broadcastLogout(String playername) {
        if (config.broadcast.playerLoggedOut) {
            bot.broadcast(template(config.i18n.playerLoggedOut, PLAYER, escapeMarkdown(playername)),
                config.disableNotification.playerLoggedOut);
        }
    }

    protected void broadcastDimension(String playername, String dimension) {
        if (config.broadcast.playerChangedDimension) {
            bot.broadcast(template(config.i18n.playerChangedDimension,
                toArray(PLAYER, "${dimension}"), toArray(escapeMarkdown(playername), escapeMarkdown(dimension))),
                config.disableNotification.playerChangedDimension);
        }
    }

    protected void boardcastAdvancement(String playername, String advancement) {
        if (config.broadcast.playerAdvancement && StringUtils.isNotBlank(advancement) && !advancement
            .contains("recipes")) {
            bot.broadcast(template(config.i18n.playerAdvancement,
                toArray(PLAYER, "${advancement}"), toArray(escapeMarkdown(playername), escapeMarkdown(advancement))),
                config.disableNotification.playerAdvancement);
        }
    }

}
