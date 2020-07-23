package eu.jsan.forge.telegram.impl;

import eu.jsan.forge.telegram.core.AbstractMod;
import eu.jsan.forge.telegram.core.support.AbstractEvents;
import eu.jsan.forge.telegram.core.support.Utils;
import java.util.Arrays;
import net.minecraft.command.ICommand;
import net.minecraft.command.server.CommandMessage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import org.apache.commons.lang3.StringUtils;

public class Events extends AbstractEvents {

    @SubscribeEvent
    public void onCommandEvent(CommandEvent event) {
        String from = event.getSender().getName();

        if (Arrays.stream(AbstractMod.config.skippedSenderNames).noneMatch(s -> s.equals(from))) {
            ICommand command = event.getCommand();
            String commandName = command.getName();
            String[] parameters = event.getParameters();

            if (command instanceof CommandMessage && parameters.length >= 2) { // whisper
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 1; i < parameters.length; ++i) {
                    if (i > 1) {
                        stringBuilder.append(StringUtils.SPACE);
                    }
                    stringBuilder.append(parameters[i]);
                }
                broadcastWhisper(from, parameters[0], stringBuilder.toString());

            } else { //command
                broadcastCommand(from, commandName, parameters);
            }
        }
    }

    @SubscribeEvent
    public void onChatMessage(ServerChatEvent event) {
        broadcastChat(event.getUsername(), event.getMessage());
    }

    @SubscribeEvent
    public void onPlayerDeath(LivingDeathEvent event) {
        if (event.getEntityLiving() instanceof EntityPlayerMP) {
            final EntityPlayerMP player = (EntityPlayerMP) event.getEntityLiving();
            String playername = player.getGameProfile().getName();
            broadcastDeath(playername, player.getCombatTracker().getDeathMessage().getUnformattedText(),
                event.getSource().getTrueSource() instanceof EntityPlayer);
        }
    }

    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        EntityPlayer player = event.player;
        String playername = player.getDisplayNameString();
        if (AbstractMod.config.welcomeMessage) {
            player.sendMessage(new TextComponentString(Utils.template(AbstractMod.config.i18n.welcomeMessage,
                Utils.PLAYER, playername)));
        }
        broadcastLogin(playername);
    }

    @SubscribeEvent
    public void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        broadcastLogout(event.player.getDisplayNameString());
    }

    @SubscribeEvent
    public void onPlayerChangedDimensionEvent(PlayerEvent.PlayerChangedDimensionEvent event) {
        broadcastDimension(event.player.getDisplayNameString(),
            DimensionManager.getProviderType(event.toDim).getName());
    }

    @SubscribeEvent
    public void onAdvancementEvent(AdvancementEvent event) {
        boardcastAdvancement(event.getEntityPlayer().getDisplayNameString(),
            event.getAdvancement().getDisplayText().getFormattedText());
    }
}
