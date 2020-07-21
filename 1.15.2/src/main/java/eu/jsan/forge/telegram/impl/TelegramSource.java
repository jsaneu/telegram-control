package eu.jsan.forge.telegram.impl;

import javax.annotation.ParametersAreNonnullByDefault;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.command.CommandSource;
import net.minecraft.command.ICommandSource;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class TelegramSource implements ICommandSource {

    private static final String NAME = "Telegram";

    private final MinecraftServer server;

    private final StringBuffer buffer = new StringBuffer();

    public TelegramSource(MinecraftServer server) {
        this.server = server;
    }

    public CommandSource getCommandSource() {
        ServerWorld serverworld = this.server.getWorld(DimensionType.OVERWORLD);
        return new CommandSource(this, new Vec3d(serverworld.getSpawnPoint()), Vec2f.ZERO, serverworld, 4, NAME,
            new StringTextComponent(NAME), this.server, null);
    }


    public void resetLog() {
        this.buffer.setLength(0);
    }

    public String getLogContents() {
        return this.buffer.toString();
    }

    @Override
    public void sendMessage(ITextComponent component) {
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
