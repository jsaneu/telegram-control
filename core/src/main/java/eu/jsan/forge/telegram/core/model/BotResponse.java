package eu.jsan.forge.telegram.core.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import org.apache.commons.lang3.StringUtils;

public class BotResponse implements Serializable {

    @SerializedName("p")
    private String player;

    @SerializedName("a")
    private String action;

    @SerializedName("c")
    private Boolean cancel;

    public BotResponse() {
    }

    public BotResponse(String player) {
        this.player = player;
    }

    public BotResponse(String player, String action) {
        this.player = player;
        this.action = action;
    }

    public static BotResponse cancel() {
        BotResponse response = new BotResponse();
        response.setCancel(Boolean.TRUE);
        return response;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Boolean getCancel() {
        return cancel;
    }

    public void setCancel(Boolean cancel) {
        this.cancel = cancel;
    }

    public boolean hasAction() {
        return StringUtils.isNotBlank(action);
    }

    public boolean hasPlayer() {
        return StringUtils.isNotBlank(player);
    }

    public boolean hasCancel() {
        return cancel != null;
    }

    @Override
    public String toString() {
        return "MinecraftBotResponse{" +
            "player='" + player + '\'' +
            ", action='" + action + '\'' +
            ", cancel=" + cancel + '}';
    }
}
