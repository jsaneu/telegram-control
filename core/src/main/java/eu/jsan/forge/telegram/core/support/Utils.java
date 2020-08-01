package eu.jsan.forge.telegram.core.support;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import eu.jsan.forge.telegram.core.AbstractMod;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;

public class Utils {

    public static final char COLOR_FORMAT_CHARACTER = '\u00a7';

    public static final Pattern FORMAT_CODE_PATTERN;

    public static final char[] FORMAT_CHARACTERS = new char[TextFormatting.values().length];

    public static final String MESSAGE = "${message}";

    public static final String PLAYER = "${player}";

    private static final int MAX_BUTTON_COLUMNS = 8;

    private static final Pattern PATTERN = Pattern.compile("(([_*~`|]){2})", Pattern.DOTALL);

    static {
        for (TextFormatting code : TextFormatting.values()) {
            FORMAT_CHARACTERS[code.ordinal()] = code.toString().charAt(1);
        }
        FORMAT_CODE_PATTERN = Pattern.compile(COLOR_FORMAT_CHARACTER + "([" + new String(FORMAT_CHARACTERS) + "])");
    }

    public static String strip(String message) {
        return FORMAT_CODE_PATTERN.matcher(message).replaceAll(StringUtils.EMPTY);
    }

    public static String template(String template, String search, String replacement) {
        return StringUtils.replaceEach(template, new String[]{search}, new String[]{replacement});
    }

    public static String template(String template, String[] search, String[] replacement) {
        return StringUtils.replaceEach(template, search, replacement);
    }

    public static String[] toArray(String... strings) {
        return strings;
    }

    public static String obfuscateCommandParams(String command, String[] params) {
        if (Arrays.stream(AbstractMod.config.obfuscatedCommands)
            .anyMatch(s -> StringUtils.equalsAnyIgnoreCase(command, s))) {
            return AbstractMod.config.i18n.obfuscatedReplacement;
        }
        return String.join(StringUtils.SPACE, params);
    }

    public static InlineKeyboardMarkup getKeyboard(List<InlineKeyboardButton> buttons, int columns) {
        if (columns > MAX_BUTTON_COLUMNS) {
            columns = MAX_BUTTON_COLUMNS;
        }
        int size = buttons.size();
        InlineKeyboardButton[][] result;
        if (size <= columns) {
            result = new InlineKeyboardButton[][]{buttons.toArray(new InlineKeyboardButton[0])};
        } else {
            result = new InlineKeyboardButton[(size + columns - 1) / columns][];
            int x = 0;
            int y = 0;
            result[x] = new InlineKeyboardButton[columns];
            for (InlineKeyboardButton button : buttons) {
                if (y == columns) {
                    y = 0;
                    x++;
                    result[x] = new InlineKeyboardButton[Math.min(size - (x * columns), columns)];
                }
                result[x][y] = button;
                y++;
            }

        }
        return new InlineKeyboardMarkup(result);
    }

    public static String escapeMarkdown(String text) {
        return text == null ? null : PATTERN.matcher(text).replaceAll("\\$1");
    }

    public static String humanReadableByteCountBin(long bytes) {
        long absB = bytes == Long.MIN_VALUE ? Long.MAX_VALUE : Math.abs(bytes);
        if (absB < 1024) {
            return bytes + " B";
        }
        long value = absB;
        CharacterIterator ci = new StringCharacterIterator("KMGTPE");
        for (int i = 40; i >= 0 && absB > 0xfffccccccccccccL >> i; i -= 10) {
            value >>= 10;
            ci.next();
        }
        value *= Long.signum(bytes);
        return String.format("%.1f %ciB", value / 1024.0, ci.current());
    }
}
