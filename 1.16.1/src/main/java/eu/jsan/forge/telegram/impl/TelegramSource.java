package eu.jsan.forge.telegram.impl;

import java.util.UUID;
import javax.annotation.ParametersAreNonnullByDefault;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.command.CommandSource;
import net.minecraft.command.ICommandSource;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.server.ServerWorld;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class TelegramSource implements ICommandSource {

    private static final String NAME = "Telegram";

    private static final StringTextComponent displayName = new StringTextComponent(NAME);


    private final MinecraftServer server;

    private final StringBuffer buffer = new StringBuffer();

    public TelegramSource(MinecraftServer server) {
        this.server = server;
    }

    public CommandSource getCommandSource() {
        ServerWorld serverworld = this.server.func_241755_D_();
        return new CommandSource(this, Vector3d.func_237491_b_(serverworld.func_241135_u_()), Vector2f.ZERO,
            serverworld, 4, NAME,
            displayName, this.server, null);
    }


    public void resetLog() {
        this.buffer.setLength(0);
    }

    public String getLogContents() {
        return this.buffer.toString();
    }

    @Override
    public void sendMessage(ITextComponent component, UUID p_145747_2_) {
        this.buffer.append(component.getString()).append('\n');
    }

    @Override
    public boolean shouldReceiveFeedback() {
        return true;
    }

    @Override
    public boolean shouldReceiveErrors() {
        return true;
    }

    @Override
    public boolean allowLogging() {
        return true;
    }
}
