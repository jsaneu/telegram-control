package eu.jsan.forge.telegram.impl;

import eu.jsan.forge.telegram.core.AbstractMod;
import eu.jsan.forge.telegram.core.support.AbstractEvents;
import eu.jsan.forge.telegram.core.support.Utils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class Events extends AbstractEvents {

    @SubscribeEvent
    public void onChatMessage(ServerChatEvent event) {
        broadcastChat(event.getUsername(), event.getMessage());
    }

    @SubscribeEvent
    public void onPlayerDeath(LivingDeathEvent event) {
        if (event.getEntityLiving() instanceof ServerPlayerEntity) {
            final ServerPlayerEntity player = (ServerPlayerEntity) event.getEntityLiving();
            broadcastDeath(player.getGameProfile().getName(),
                player.getCombatTracker().getDeathMessage().getUnformattedComponentText(),
                event.getSource().getTrueSource() instanceof PlayerEntity);
        }
    }

    @SubscribeEvent
    public void onPlayerJoin(PlayerLoggedInEvent event) {
        PlayerEntity player = event.getPlayer();
        String playername = player.getDisplayName().getString();
        if (AbstractMod.config.welcomeMessage) {
            player.sendMessage(new StringTextComponent(Utils.template(AbstractMod.config.i18n.welcomeMessage,
                Utils.PLAYER, playername)), Util.field_240973_b_);
        }

        broadcastLogin(playername);
    }

    @SubscribeEvent
    public void onPlayerLoggedOut(PlayerLoggedOutEvent event) {
        broadcastLogout(event.getPlayer().getDisplayName().getString());
    }

    @SubscribeEvent
    public void onPlayerChangedDimensionEvent(PlayerChangedDimensionEvent event) {
        broadcastDimension(event.getPlayer().getDisplayName().getString(), event.getTo().toString());
    }

    @SubscribeEvent
    public void onAdvancementEvent(AdvancementEvent event) {
        boardcastAdvancement(event.getPlayer().getDisplayName().toString(),
            event.getAdvancement().getDisplayText().getString());
    }
}
