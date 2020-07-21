package eu.jsan.forge.telegram.core.config;


import java.util.LinkedHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("CanBeFinal")
public class TelegramConfig {

    public String token = "my-secrect-bot-token";

    public Broadcast broadcast = new Broadcast();

    public DisableNotification disableNotification = new DisableNotification();

    public I18n i18n = new I18n();

    public String[] chatIds = new String[]{"-1"};

    public LinkedHashMap<String, String> actions = new LinkedHashMap<>(Stream.of(new String[][]{
        {"\u2764 Heal", "effect ${player} minecraft:instant_health"},
        {"\uD83D\uDCA5 Damage", "effect ${player} minecraft:instant_damage"},
        {"\uD83D\uDEF8 Levitation", "effect ${player} minecraft:levitation 10"},
        {"\uD83D\uDE35 Nausea", "effect ${player} minecraft:nausea 10"},
        {"\uD83D\uDE48 Blindness", "effect ${player} minecraft:blindness 10"},
        {"\uD83E\uDD22 Poison", "effect ${player} minecraft:poison 10"},
        {"\u26A1 Lightning", "execute ${player} ~0 ~0 ~0 /summon lightning_bolt @p"},
        {"\u2601 Go up", "tp ${player} ~ ~150 ~"},
        {"\u2620 Kill", "kill ${player}"},
        {"\uD83E\uDE72 Clear inv.", "clear ${player}"},
        {"\uD83E\uDDB6 Kick", "kick ${player}"},
        {"\uD83D\uDD12 Ban", "ban ${player}"},
        {"\uD83D\uDCA4 Jail", "spawnpoint ${player} -1022 129 55;tp ${player} -1022 129 55"}
    }).collect(Collectors.toMap(data -> data[0], data -> data[1])));

    public int maxButtonColumns = 3;

    public String[] skippedSenderNames = new String[]{"Server", "Telegram"};

    public String[] skippedCommands = new String[]{};

    public String[] obfuscatedCommands = new String[]{"login", "register"};

    public boolean welcomeMessage = false;

    public static class Broadcast {

        public boolean playerLogged = true;

        public boolean playerLoggedOut = true;

        public boolean playerChangedDimension = true;

        public boolean playerAdvancement = true;

        public boolean playerMessage = true;

        public boolean playerWhisper = true;

        public boolean playerCommand = true;

        public boolean playerDeathPvp = true;

        public boolean playerDeathPve = true;

        public boolean serverStarted = true;

        public boolean serverStopped = true;

        public boolean commandResponde = true;
    }

    public static class DisableNotification {

        public boolean playerLogged = true;

        public boolean playerLoggedOut = true;

        public boolean playerChangedDimension = true;

        public boolean playerAdvancement = true;

        public boolean playerMessage = true;

        public boolean playerWhisper = true;

        public boolean playerCommand = true;

        public boolean playerDeathPvp = true;

        public boolean playerDeathPve = true;

        public boolean serverStarted = true;

        public boolean serverStopped = true;

        public boolean commandResponde = true;
    }

    public static class I18n {

        public String welcomeMessage = "Hola \u00A7e${player}\u00A7r! Escribe \u00A7a/login <contrase\u00F1a>\u00A7r "
            + "para identificarte o \u00A7a/register <contrase\u00F1a> <contrase\u00F1a>\u00A7r "
            + "para registrarte.";

        public String playerDeath = "\u2620 ${message}";

        public String serverStarted = "\uD83D\uDFE9 *Server* Up";

        public String serverStopped = "\uD83D\uDFE5 *Server* Down";

        public String playerLogged = "\uD83D\uDFE2 *${player}*";

        public String playerLoggedOut = "\uD83D\uDD34 *${player}*";

        public String playerOffline = "*${player}* is offline";

        public String playerChangedDimension = "\u2708 *${player}* ${dimension}";

        public String playerAdvancement = "\uD83C\uDFC6 *${player}* ${advancement}";

        public String playerWhisper = "\uD83D\uDCAC *${from}* to *${to}*: ${message}";

        public String playerCommand = "\u25B6 *${player}* /${command} ${parameters}";

        public String chatFromGame = "\uD83D\uDD08 *${player}*: ${message}";

        public String ChatFromTelegram = "\u00A76<${nickname}>\u00A7r ${message}";

        public String chatNotFound = "Chat '${chatId}' is not registered";

        public String commandNotFound = "Command not found: ${command}";

        public String cancelButton = "\u274C Cancel";

        public String noPlayersOnline = "No players online";

        public String choosePlayerTitle = "*${playersCount}* Players online";

        public String chooseActionTitle = "*${player}*. Choose action \uD83D\uDE08";

        public String playerListTitle = "*${playersCount}* Players online:";

        public String playerListRow = "\u2022 *${player}* (${currentHealth}/${maxHealth}) lvl: ${level}";

        public String obfuscatedReplacement = "<hidden>";

        public String telegramCallback = "[${player}] - ${action}";

    }
}
