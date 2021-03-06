package eu.jsan.forge.telegram.core;

import static eu.jsan.forge.telegram.core.AbstractMod.config;
import static eu.jsan.forge.telegram.core.AbstractMod.updateConfiguration;
import static eu.jsan.forge.telegram.core.support.Utils.PLAYER;
import static eu.jsan.forge.telegram.core.support.Utils.escapeMarkdown;
import static eu.jsan.forge.telegram.core.support.Utils.strip;
import static eu.jsan.forge.telegram.core.support.Utils.template;
import static eu.jsan.forge.telegram.core.support.Utils.toArray;

import com.pengrad.telegrambot.Callback;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.AnswerCallbackQuery;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.DeleteMessage;
import com.pengrad.telegrambot.request.EditMessageText;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import eu.jsan.forge.telegram.core.model.BotResponse;
import eu.jsan.forge.telegram.core.model.Player;
import eu.jsan.forge.telegram.core.support.Messages;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Logger;

public abstract class AbstractBot {

    private static final char SEPARATOR_CHAR = ';';

    private static final String COMMAND_STATUS = "/status";

    private static final String COMMAND_PLAYERS = "/players";

    private static final String COMMAND_ACTIONS = "/actions";

    private static final String COMMAND_START = "/start";

    private static final String COMMAND_CMD = "/cmd";

    private static final String RELOAD_CMD = "/reload";

    private static final String SLASH = "/";

    private final TelegramBot telegramBot;

    protected final Logger logger;

    protected AbstractBot(String botToken, Logger logger) {
        telegramBot = new TelegramBot(botToken);
        telegramBot.setUpdatesListener(this::manageUpdates);
        this.logger = logger;
    }


    private int manageUpdates(List<Update> updates) {
        updates.forEach(update -> {
            Message message = update.message();
            if (message != null) {
                String text = message.text();
                if (StringUtils.isNotBlank(text)) {
                    String fromId = message.from().id().toString();
                    if (Arrays.asList(config.chatIds).contains(fromId)) {
                        if (text.startsWith(SLASH)) {
                            manageCommands(text, fromId);
                        } else {
                            say(message.from().username(), message.text());
                        }
                    } else {
                        toTelegram(fromId, template(config.i18n.chatNotFound, "${chatId}", fromId),
                            true);
                    }
                }
            } else if (update.callbackQuery() != null) {
                manageCallback(update);
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    public abstract void say(String nickname, String text);

    protected abstract List<Player> getPlayers();

    private void manageCommands(String text, Object chatId) {
        String command = StringUtils.substringBefore(text, StringUtils.SPACE);
        switch (command) {
            case COMMAND_START:
            case COMMAND_STATUS:
                postToTelegram(Messages.getStatus(chatId, getMotd(), getVersion(),
                    getGameType(), getDifficulty(), getCurrentPlayers(), getMaxPlayers()));
                break;
            case COMMAND_PLAYERS:
                postToTelegram(Messages.listPlayers(chatId, getPlayers()));
                break;
            case COMMAND_ACTIONS:
                postToTelegram(Messages.getPlayers(chatId, getPlayernames()));
                break;
            case COMMAND_CMD:
                executeMinecraftCommand(StringUtils.substringAfter(text, StringUtils.SPACE), chatId);
                break;
            case RELOAD_CMD:
                try {
                    updateConfiguration();
                    toTelegram(chatId, config.i18n.reloadConfigOk, true);
                } catch (IOException e) {
                    logger.error("Error reloading config", e);
                    toTelegram(chatId, template(config.i18n.reloadConfigError, "${error}", e.getMessage()), false);
                }
                break;
            default:
                toTelegram(chatId, template(config.i18n.commandNotFound, "${command}", command),
                    false);
        }
    }

    protected abstract String getMotd();
    protected abstract String getVersion();
    protected abstract String getGameType();
    protected abstract String getDifficulty();
    protected abstract String getCurrentPlayers();
    protected abstract String getMaxPlayers();

    public void toTelegram(Object chatId, String message, boolean disableNotification) {
        postToTelegram(new SendMessage(chatId, strip(message))
            .disableNotification(disableNotification).parseMode(ParseMode.Markdown));

    }

    public void broadcast(String message, boolean disableNotification) {
        if (config.chatIds != null) {
            for (String id : config.chatIds) {
                toTelegram(id, message, disableNotification);
            }
        }
    }

    private void manageCallback(Update update) {
        CallbackQuery callback = update.callbackQuery();
        String data = callback.data();
        Integer chatId = callback.from().id();
        BotResponse response = Messages.GSON.fromJson(data, BotResponse.class);

        Integer messageId = callback.message().messageId();
        if (response.hasCancel()) {
            answerCallback(callback, config.i18n.cancelButton);
            postToTelegram(new DeleteMessage(chatId, messageId));
        } else {
            if (response.hasPlayer()) {
                if (response.hasAction()) {
                    answerCallback(callback, template(config.i18n.telegramCallback, toArray(PLAYER, "${action}"),
                            toArray(response.getPlayer(), response.getAction())));
                    executeMinecraftAction(chatId, response.getPlayer(),
                        config.actions.get(response.getAction()), messageId);
                } else {
                    if (getPlayernames().contains(response.getPlayer())) {
                        answerCallback(callback, response.getPlayer());
                        postToTelegram(Messages.getActions(chatId, response.getPlayer(), messageId));
                    } else {
                        postToTelegram(new EditMessageText(chatId, messageId,
                            template(config.i18n.playerOffline, PLAYER, escapeMarkdown(response.getPlayer())))
                            .parseMode(ParseMode.Markdown));
                    }
                }
            }
        }
    }

    private void answerCallback(CallbackQuery callback, String text) {
        postToTelegram(new AnswerCallbackQuery(callback.id()).text(text));
    }

    protected abstract void executeMinecraftCommand(String command, Object chatId);

    private void executeMinecraftAction(Object chatId, String username, String command, int messageId) {
        List<String> onlinePlayerNames = getPlayernames();
        if (!onlinePlayerNames.isEmpty() && onlinePlayerNames.contains(username)) {
            for (String s : StringUtils.split(command, SEPARATOR_CHAR)) {
                executeMinecraftCommand(template(s, PLAYER, username), chatId);
            }
        } else {
            postToTelegram(new EditMessageText(chatId, messageId,
                template(config.i18n.playerOffline, PLAYER, escapeMarkdown(username)))
                .parseMode(ParseMode.Markdown));
        }
    }

    protected abstract List<String> getPlayernames();

    protected void broadcastCommandResponde(Object chatId, String logContents) {
        if (config.broadcast.commandResponde) {
            toTelegram(chatId, String.format("`%s`",
                escapeMarkdown(StringUtils.removeEnd(logContents, "\n"))),
                config.disableNotification.commandResponde);
        }
    }

    private <T extends BaseRequest<T, R>, R extends BaseResponse> void postToTelegram(T request) {
        telegramBot.execute(request, new Callback<T, R>() {
            @Override
            public void onResponse(T request, R response) {
                if (!response.isOk()) {
                    logger.error("Response is not Ok. Description: " + response.description());
                    logger.error("Request error: " + request);
                }
            }

            @Override
            public void onFailure(T request, IOException e) {
                logger.error("Error processing " + request, e);
            }
        });
    }
}
