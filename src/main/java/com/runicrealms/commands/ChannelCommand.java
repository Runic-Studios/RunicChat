package com.runicrealms.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Dependency;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.Syntax;
import com.runicrealms.RunicChat;
import com.runicrealms.api.RunicChatAPI;
import com.runicrealms.api.chat.ChatChannel;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by KissOfFate
 * Date: 5/9/2020
 * Time: 4:13 PM
 */
@CommandAlias("ch|chan|channel")
public class ChannelCommand extends BaseCommand {

    @Dependency
    private RunicChatAPI runicChatAPI;

    public ChannelCommand() {
        RunicChat.getCommandManager().getCommandCompletions().registerAsyncCompletion("channels", context -> {
            Set<String> channels = new HashSet<>();
            for (ChatChannel channel : runicChatAPI.getChatChannels())
                if (channel.canAccess(context.getPlayer())) channels.add(channel.getName());
            return channels;
        });
    }

    @Default
    @CommandCompletion("@channels mute|unmute")
    @CommandPermission("runicchat.channel")
    @Syntax("<channel>")
    public void execute(Player player, String[] args) {

        if (args == null || args.length == 0) {
            player.sendMessage(ChatColor.GRAY + "Current Channels: " + runicChatAPI.getChatChannels().stream()
                    .filter((channel) -> channel.canAccess(player))
                    .map((channel) -> ChatColor.GREEN + StringUtils.capitalize(channel.getName().toLowerCase())
                            + (RunicChat.getRunicChatAPI().isChannelMuted(player, channel) ? ChatColor.RED + " (muted)" : "")
                    )
                    .collect(Collectors.joining(ChatColor.GRAY + ", ")));
            return;
        }

        ChatChannel channel = runicChatAPI.getChatChannels().stream().filter(ch -> ch.canAccess(player) && ch.getName().equalsIgnoreCase(args[0])).findFirst().orElse(null);

        if (channel != null) {
            if (args.length > 1 && args[1].equalsIgnoreCase("mute")) {
                player.sendMessage(ChatColor.DARK_GREEN + "Muted chat channel '" + ChatColor.GREEN + channel.getName() + ChatColor.DARK_GREEN + "'");
                RunicChat.getRunicChatAPI().setChannelMuted(player, channel, true);
            } else if (args.length > 1 && args[1].equalsIgnoreCase("unmute")) {
                player.sendMessage(ChatColor.DARK_GREEN + "Unmuted chat channel '" + ChatColor.GREEN + channel.getName() + ChatColor.DARK_GREEN + "'");
                RunicChat.getRunicChatAPI().setChannelMuted(player, channel, false);
            } else {
                player.sendMessage(ChatColor.DARK_GREEN + "Set your chat channel to '" + ChatColor.GREEN + channel.getName() + ChatColor.DARK_GREEN + "'");
                runicChatAPI.setPlayerChatChannel(player, channel);
            }
        } else {
            player.sendMessage(ChatColor.DARK_RED + "The value '" + ChatColor.RED + args[0] + ChatColor.DARK_RED + "' is not a valid channel!");
        }
    }

    @Subcommand("mute")
    @Syntax("<channel>")
    @CommandPermission("runicchat.channel")
    @CommandCompletion("@channels")
    public void mute(Player player, String[] args) {
        if (args == null || args.length == 0) {
            player.sendMessage(ChatColor.RED + "Please specify which channel to mute!");
            return;
        }

        ChatChannel channel = runicChatAPI.getChatChannels().stream().filter(ch -> ch.getName().equalsIgnoreCase(args[0])).findFirst().orElse(null);

        if (channel != null) {
            player.sendMessage(ChatColor.DARK_GREEN + "Muted chat channel '" + ChatColor.GREEN + channel.getName() + ChatColor.DARK_GREEN + "'");
            RunicChat.getRunicChatAPI().setChannelMuted(player, channel, true);
        } else {
            player.sendMessage(ChatColor.DARK_RED + "The value '" + ChatColor.RED + args[0] + ChatColor.DARK_RED + "' is not a valid channel!");
        }
    }

    @Subcommand("unmute")
    @Syntax("<channel>")
    @CommandPermission("runicchat.channel")
    @CommandCompletion("@channels")
    public void unmute(Player player, String[] args) {
        if (args == null || args.length == 0) {
            player.sendMessage(ChatColor.RED + "Please specify which channel to unmute!");
            return;
        }

        ChatChannel channel = runicChatAPI.getChatChannels().stream().filter(ch -> ch.getName().equalsIgnoreCase(args[0])).findFirst().orElse(null);

        if (channel != null) {
            player.sendMessage(ChatColor.DARK_GREEN + "Unmuted chat channel '" + ChatColor.GREEN + channel.getName() + ChatColor.DARK_GREEN + "'");
            RunicChat.getRunicChatAPI().setChannelMuted(player, channel, false);
        } else {
            player.sendMessage(ChatColor.DARK_RED + "The value '" + ChatColor.RED + args[0] + ChatColor.DARK_RED + "' is not a valid channel!");
        }
    }

}
