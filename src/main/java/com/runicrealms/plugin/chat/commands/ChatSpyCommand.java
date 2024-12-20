package com.runicrealms.plugin.chat.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CatchUnknown;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import com.runicrealms.plugin.chat.RunicChat;
import com.runicrealms.plugin.chat.api.chat.ChatChannel;
import com.runicrealms.plugin.chat.channels.GlobalChannel;
import com.runicrealms.plugin.chat.channels.StaffChannel;
import com.runicrealms.plugin.chat.channels.TradeChannel;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.stream.Collectors;

@CommandAlias("chatspy")
@CommandPermission("runicchat.spy")
public class ChatSpyCommand extends BaseCommand {

    public ChatSpyCommand() {
        RunicChat.getCommandManager().getCommandCompletions().registerAsyncCompletion("spyable-channels", context ->
                RunicChat.getRunicChatAPI().getChatChannels().stream().filter(ChatChannel::isSpyable).map(ChatChannel::getName).collect(Collectors.toList())
        );
    }

    @Default
    @CatchUnknown
    @CommandCompletion("@spyable-channels")
    public void onCommand(Player player, String[] args) {
        if (args.length == 0) {
            player.sendMessage(ChatColor.GOLD + "Chat Spy:");
            RunicChat.getRunicChatAPI().getChatChannels().forEach(channel -> {
                if (channel instanceof GlobalChannel || channel instanceof TradeChannel || channel instanceof StaffChannel)
                    return;
                boolean enabled = RunicChat.getRunicChatAPI().getSpySettings(player).get(channel);
                player.sendMessage(ChatColor.YELLOW + StringUtils.capitalize(channel.getName().toLowerCase()) + ": " + ChatColor.GOLD + (enabled ? "Enabled" : "Disabled"));
            });
        } else {
            for (String channelName : args) {
                ChatChannel channel = RunicChat.getRunicChatAPI().getChatChannels().stream()
                        .filter((target) -> target.getName().equalsIgnoreCase(channelName))
                        .findFirst().orElse(null);
                if (channel == null || channel instanceof GlobalChannel || channel instanceof TradeChannel) {
                    player.sendMessage(ChatColor.YELLOW + "Could not find chat channel \"" + ChatColor.GOLD + channelName + ChatColor.YELLOW + "\"");
                } else {
                    boolean enabled = !RunicChat.getRunicChatAPI().getSpySettings(player).get(channel);
                    player.sendMessage(ChatColor.GOLD + (enabled ? "Enabled" : "Disabled") + ChatColor.YELLOW + " chat spy for channel " + ChatColor.GOLD + channelName);
                    RunicChat.getRunicChatAPI().setSpy(player, channel, enabled);
                }
            }
        }
    }

}
