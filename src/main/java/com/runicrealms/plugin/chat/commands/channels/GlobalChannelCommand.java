package com.runicrealms.plugin.chat.commands.channels;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CatchUnknown;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import com.runicrealms.plugin.chat.RunicChat;
import com.runicrealms.plugin.chat.api.chat.ChatChannel;
import org.bukkit.entity.Player;

@CommandAlias("global|gl")
@CommandPermission("runicchat.channel")
public class GlobalChannelCommand extends BaseCommand {

    private final ChatChannel channel;

    public GlobalChannelCommand(ChatChannel channel) {
        this.channel = channel;
    }

    @Default
    @CatchUnknown
    public void onCommand(Player player, String[] args) {
        RunicChat.getRunicChatAPI().sendMessage(player, this.channel, String.join(" ", args));
    }

}
