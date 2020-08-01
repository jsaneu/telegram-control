package eu.jsan.forge.telegram.impl;

import eu.jsan.forge.telegram.core.AbstractBot;
import eu.jsan.forge.telegram.core.AbstractMod;
import eu.jsan.forge.telegram.core.model.Player;
import eu.jsan.forge.telegram.core.support.Utils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.apache.logging.log4j.Logger;

public class Bot extends AbstractBot {

    private final MinecraftServer minecraftServer;

    private final TelegramSender sender;

    protected Bot(String botToken, Logger logger) {
        super(botToken, logger);
        minecraftServer = FMLCommonHandler.instance().getMinecraftServerInstance();
        sender = new TelegramSender(minecraftServer);
    }

    @Override
    public void say(String nickname, String text) {
        minecraftServer.getPlayerList()
            .sendMessage(new TextComponentString(Utils.template(AbstractMod.config.i18n.ChatFromTelegram,
                Utils.toArray("${nickname}", Utils.MESSAGE),
                Utils.toArray(nickname, text))));
    }

    @Override
    protected List<Player> getPlayers() {
        List<Player> players = new ArrayList<>();
        List<EntityPlayerMP> playerEntities = minecraftServer.getPlayerList().getPlayers();
        if (!playerEntities.isEmpty()) {
            playerEntities.forEach(entity ->
                players.add(new Player(entity.getName(), entity.getHealth(), entity.getMaxHealth(),
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
        return new TextComponentTranslation("gameMode."+minecraftServer.getGameType().getName()).getUnformattedComponentText();
    }

    @Override
    protected String getDifficulty() {
        return new TextComponentTranslation(minecraftServer.getDifficulty().getDifficultyResourceKey()).getUnformattedComponentText();
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
        sender.resetLog();
        minecraftServer.getCommandManager().executeCommand(sender, command);
        broadcastCommandResponde(chatId, sender.getLogContents());
    }

    @Override
    protected List<String> getPlayernames() {
        return Arrays.asList(minecraftServer.getOnlinePlayerNames());
    }
}
