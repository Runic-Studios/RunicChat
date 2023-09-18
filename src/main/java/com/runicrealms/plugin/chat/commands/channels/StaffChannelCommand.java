package com.runicrealms.plugin.chat.commands.channels;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CatchUnknown;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import com.runicrealms.plugin.chat.RunicChat;
import com.runicrealms.plugin.chat.api.chat.ChatChannel;
import org.bukkit.entity.Player;

@CommandAlias("staff|s|sc")
@CommandPermission("runicchat.staff")
public class StaffChannelCommand extends BaseCommand {

    private final ChatChannel channel;

    public StaffChannelCommand(ChatChannel channel) {
        this.channel = channel;
    }

    @Default
    @CatchUnknown
    public void onCommand(Player player, String[] args) {
        RunicChat.getRunicChatAPI().sendMessage(player, this.channel, String.join(" ", args));
    }

}