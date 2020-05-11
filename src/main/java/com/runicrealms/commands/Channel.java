package com.runicrealms.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import com.runicrealms.api.RunicChatAPI;
import com.runicrealms.api.chat.ChatChannel;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Created by KissOfFate
 * Date: 5/9/2020
 * Time: 4:13 PM
 */
@CommandAlias("ch|chan|channel")
public class Channel extends BaseCommand {

    @Dependency
    private RunicChatAPI runicChatAPI;

    @Default
    @CommandPermission("runicchat.channel")
    @Syntax("<channel>")
    public void execute(Player player, String[] args) {
        String inputChannel = args[0].toLowerCase();
        ChatChannel channel = runicChatAPI.getChatChannels().stream().filter(ch -> ch.getName().equalsIgnoreCase(inputChannel)).findFirst().orElseGet(null);

        if(channel != null) {
            player.sendMessage(ChatColor.DARK_GREEN + "Set your chat channel to '" + ChatColor.GREEN + channel.getName() + ChatColor.DARK_GREEN + "'");
            runicChatAPI.setPlayerChatChannel(player, channel);
        } else {
            player.sendMessage(ChatColor.DARK_RED + "The value '" + ChatColor.RED + inputChannel + ChatColor.DARK_RED + "' is not a valid channel!");
        }
    }

}
