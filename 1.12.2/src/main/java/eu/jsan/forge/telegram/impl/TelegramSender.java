package eu.jsan.forge.telegram.impl;

import javax.annotation.ParametersAreNonnullByDefault;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class TelegramSender implements ICommandSender {

    private static final String NAME = "Telegram";

    private final MinecraftServer server;

    private final StringBuffer buffer = new StringBuffer();

    public TelegramSender(MinecraftServer server) {
        this.server = server;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void sendMessage(ITextComponent component) {
        this.buffer.append(component.getFormattedText()).append('\n');
    }

    @Override
    public boolean canUseCommand(int permLevel, String commandName) {
        return true;
    }

    @Override
    public World getEntityWorld() {
        return this.server.getEntityWorld();
    }

    @Override
    public boolean sendCommandFeedback() {
        return true;
    }

    @Override
    public MinecraftServer getServer() {
        return this.server;
    }

    public void resetLog() {
        this.buffer.setLength(0);
    }

    public String getLogContents() {
        return this.buffer.toString();
    }
}
