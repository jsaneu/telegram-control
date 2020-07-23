package eu.jsan.forge.telegram.core.support;


import com.google.gson.Gson;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.EditMessageText;
import com.pengrad.telegrambot.request.SendMessage;
import eu.jsan.forge.telegram.core.AbstractMod;
import eu.jsan.forge.telegram.core.model.BotResponse;
import eu.jsan.forge.telegram.core.model.Player;
import java.lang.management.ManagementFactory;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

public class Messages {

    public static final Gson GSON = new Gson();

    private static LinkedHashMap<String, String> actions;

    public static SendMessage listPlayers(Object chatId, List<Player> players) {
        if (!players.isEmpty()) {
            StringBuilder text = new StringBuilder(
                Utils.template(AbstractMod.config.i18n.playerListTitle, "${playersCount}",
                    String.valueOf(players.size())));
            players.forEach(player -> {
                text.append("\n").append(Utils.template(AbstractMod.config.i18n.playerListRow,
                    Utils.toArray(Utils.PLAYER, "${currentHealth}", "${maxHealth}", "${level}"),
                    Utils.toArray(player.getName(),
                        String.valueOf(Math.round(player.getCurrentHealth())),
                        String.valueOf(Math.round(player.getMaxHealth())),
                        String.valueOf(player.getExperienceLevel()))));
            });
            return new SendMessage(chatId, text.toString()).parseMode(ParseMode.Markdown).disableNotification(true);
        } else {
            return new SendMessage(chatId, AbstractMod.config.i18n.noPlayersOnline);
        }
    }

    public static SendMessage getStatus(Object chatId, String motd, String version, String mode,
        String difficulty, String playersOnline, String maxPlayers) {

        String startTime = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date(
            ManagementFactory.getRuntimeMXBean().getStartTime()));

        String freeMemory = Utils.humanReadableByteCountBin(Runtime.getRuntime().freeMemory());
        String maxMemory = Utils.humanReadableByteCountBin(Runtime.getRuntime().maxMemory());
        String totalMemory = Utils.humanReadableByteCountBin(Runtime.getRuntime().totalMemory());

        String text = Utils.template(AbstractMod.config.i18n.status,
            Utils.toArray("${motd}", "${version}", "${mode}", "${difficulty}", "${playersOnline}",
                "${maxPlayers}", "${startTime}", "${freeMemory}", "${maxMemory}", "${totalMemory}"),
            Utils.toArray(motd, version, mode, difficulty, playersOnline, maxPlayers, startTime, freeMemory,
                maxMemory, totalMemory));
        return new SendMessage(chatId, text).parseMode(ParseMode.Markdown).disableNotification(true);
    }

    public static SendMessage getPlayers(Object chatId, List<String> playernames) {
        if (!playernames.isEmpty()) {
            List<InlineKeyboardButton> buttons = new ArrayList<>();
            for (String playername : playernames) {
                String callbackData = GSON.toJson(new BotResponse(playername));
                buttons.add(new InlineKeyboardButton(Utils.strip(playername)).callbackData(callbackData));
            }
            buttons.add(getCancelButton());
            return new SendMessage(chatId, Utils.template(AbstractMod.config.i18n.choosePlayerTitle, "${playersCount}",
                String.valueOf(playernames.size())))
                .parseMode(ParseMode.Markdown)
                .disableWebPagePreview(true)
                .disableNotification(true)
                .replyMarkup(Utils.getKeyboard(buttons, AbstractMod.config.maxButtonColumns));
        } else {
            return new SendMessage(chatId, AbstractMod.config.i18n.noPlayersOnline);
        }
    }

    public static EditMessageText getActions(Object chatId, String playerName, Integer messageId) {

        List<InlineKeyboardButton> buttons = new ArrayList<>();
        for (String action : AbstractMod.config.actions.keySet()) {
            String callbackData = GSON.toJson(new BotResponse(playerName, action));
            buttons.add(new InlineKeyboardButton(action).callbackData(callbackData));
        }
        buttons.add(getCancelButton());
        return new EditMessageText(chatId, messageId,
            Utils.template(AbstractMod.config.i18n.chooseActionTitle, Utils.PLAYER, playerName))
            .parseMode(ParseMode.Markdown)
            .disableWebPagePreview(true)
            .replyMarkup(Utils.getKeyboard(buttons, AbstractMod.config.maxButtonColumns));
    }

    private static InlineKeyboardButton getCancelButton() {
        return new InlineKeyboardButton(AbstractMod.config.i18n.cancelButton)
            .callbackData(GSON.toJson(BotResponse.cancel()));
    }

}
