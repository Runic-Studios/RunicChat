package com.runicrealms.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Dependency;
import co.aikar.commands.annotation.Syntax;
import com.runicrealms.RunicChat;
import com.runicrealms.api.RunicChatAPI;
import com.runicrealms.api.chat.ChatChannel;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by KissOfFate
 * Date: 5/9/2020
 * Time: 4:13 PM
 */
@CommandAlias("ch|chan|channel")
public class Channel extends BaseCommand {

    @Dependency
    private RunicChatAPI runicChatAPI;

    public Channel() {
        RunicChat.getCommandManager().getCommandCompletions().registerAsyncCompletion("channels", context -> {
            Set<String> channels = new HashSet<>();
            for (ChatChannel channel : runicChatAPI.getChatChannels())
                channels.add(channel.getName());
            return channels;
        });
    }

    @Default
    @CommandCompletion("@channels")
    @CommandPermission("runicchat.channel")
    @Syntax("<channel>")
    public void execute(Player player, String[] args) {

        if (args == null || args.length == 0) {
            StringBuilder channelList = new StringBuilder();

            for (ChatChannel channel : runicChatAPI.getChatChannels())
                channelList.append(channel.getName()).append(", ");

            player.sendMessage(ChatColor.GRAY + "Current Channels: " + ChatColor.GREEN + channelList);
            return;
        }

        String inputChannel = args[0].toLowerCase();

        ChatChannel channel = runicChatAPI.getChatChannels().stream().filter(ch -> ch.getName().equalsIgnoreCase(inputChannel)).findFirst().orElse(null);

        if (channel != null) {
            player.sendMessage(ChatColor.DARK_GREEN + "Set your chat channel to '" + ChatColor.GREEN + channel.getName() + ChatColor.DARK_GREEN + "'");
            runicChatAPI.setPlayerChatChannel(player, channel);
        } else {
            player.sendMessage(ChatColor.DARK_RED + "The value '" + ChatColor.RED + inputChannel + ChatColor.DARK_RED + "' is not a valid channel!");
        }
    }

}
