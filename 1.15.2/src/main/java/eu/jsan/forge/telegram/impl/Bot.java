package eu.jsan.forge.telegram.impl;


import eu.jsan.forge.telegram.core.AbstractBot;
import eu.jsan.forge.telegram.core.AbstractMod;
import eu.jsan.forge.telegram.core.model.Player;
import eu.jsan.forge.telegram.core.support.Utils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Logger;

public class Bot extends AbstractBot {

    private final MinecraftServer minecraftServer;

    private final TelegramSource source;

    protected Bot(String botToken, Logger logger) {
        super(botToken, logger);
        minecraftServer = ServerLifecycleHooks.getCurrentServer();
        source = new TelegramSource(minecraftServer);
    }

    @Override
    public void say(String nickname, String text) {
        minecraftServer.getPlayerList().sendMessage(new StringTextComponent(Utils.template(AbstractMod.config.i18n.ChatFromTelegram,
            Utils.toArray("${nickname}", Utils.MESSAGE),
            Utils.toArray(nickname, text))));
    }

    @Override
    protected List<Player> getPlayers() {
        List<Player> players = new ArrayList<>();
        List<ServerPlayerEntity> playerEntities = minecraftServer.getPlayerList().getPlayers();
        if (!playerEntities.isEmpty()) {
            playerEntities.forEach(entity ->
                players.add(new Player(entity.getScoreboardName(), entity.getHealth(), entity.getMaxHealth(),
                    entity.experienceLevel)));
        }
        return players;
    }

    @Override
    protected String getMotd() {
        return Utils.strip(minecraftServer.getMOTD());
    }

    @Override
    protected String getVersion() {
        return minecraftServer.getMinecraftVersion();
    }

    @Override
    protected String getGameType() {
        return minecraftServer.getGameType().getDisplayName().getUnformattedComponentText();
    }

    @Override
    protected String getDifficulty() {
        return minecraftServer.getDifficulty().getDisplayName().getUnformattedComponentText();
    }

    @Override
    protected String getCurrentPlayers() {
        return String.valueOf(minecraftServer.getCurrentPlayerCount());
    }

    @Override
    protected String getMaxPlayers() {
        return String.valueOf(minecraftServer.getMaxPlayers());
    }

    @Override
    protected void executeMinecraftCommand(String command, Object chatId) {
        source.resetLog();
        minecraftServer.getCommandManager().handleCommand(source.getCommandSource(), command);
        if (AbstractMod.config.broadcast.commandResponde) {
            toTelegram(chatId, String.format("`%s`", StringUtils.removeEnd(source.getLogContents(), "\n")),
                AbstractMod.config.disableNotification.commandResponde);
        }
    }

    @Override
    protected List<String> getPlayernames() {
        return Arrays.asList(minecraftServer.getOnlinePlayerNames());
    }
}
