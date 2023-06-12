package com.runicrealms.commands.channels;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CatchUnknown;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import com.runicrealms.RunicChat;
import com.runicrealms.api.chat.ChatChannel;
import org.bukkit.entity.Player;

@CommandAlias("local|l")
@CommandPermission("runicchat.channel")
public class LocalChannelCommand extends BaseCommand {

    private final ChatChannel channel;

    public LocalChannelCommand(ChatChannel channel) {
        this.channel = channel;
    }

    @Default
    @CatchUnknown
    public void onCommand(Player player, String[] args) {
        RunicChat.getRunicChatAPI().sendMessage(player, this.channel, String.join(" ", args));
    }

}