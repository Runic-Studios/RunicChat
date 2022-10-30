package com.runicrealms.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import com.runicrealms.api.RunicChatAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Created by KissOfFate
 * Date: 5/10/2020
 * Time: 8:11 PM
 */
@CommandAlias("whisper|w|msg|message")
public class Message extends BaseCommand {

    @Dependency
    private RunicChatAPI runicChatAPI;

    @Default
    @CommandPermission("runicchat.message")
    @Syntax("<player> [message]")
    public void execute(Player sender, @Single String targetName, String message) {
        if (targetName.isEmpty()) {
            sender.sendMessage(ChatColor.RED + "Player not found!");
            return;
        }

        Player target = Bukkit.getPlayer(targetName);

        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Player not found!");
            return;
        }

        if (sender == target) {
            sender.sendMessage(ChatColor.RED + "You cannot message yourself!");
            return;
        }

        boolean color = sender.hasPermission("runicchat.color");

        if (color) {
            sender.sendMessage(
                    ChatColor.translateAlternateColorCodes('&',
                            "&8[&bYou &7-> &a" + target.getName() + "&8] " + message));
            target.sendMessage(
                    ChatColor.translateAlternateColorCodes('&',
                            "&8[&a" + sender.getName() + " &7-> &bYou&8] " + message));

            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.hasPermission("runicchat.spy")) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&c[SPY]&8[&b" + sender.getName() + " &7-> &a" + target.getName() + "&8] " + message));
                }
            }

        } else {
            sender.sendMessage(
                    ChatColor.translateAlternateColorCodes('&',
                            "&8[&bYou &7-> &a" + target.getName() + "&8] ") + message);
            target.sendMessage(
                    ChatColor.translateAlternateColorCodes('&',
                            "&8[&a" + sender.getName() + " &7-> &bYou&8] ") + message);

            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.hasPermission("runicchat.spy")) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&c[SPY]&8[&b" + sender.getName() + " &7-> &a" + target.getName() + "&8] ") + message);
                }
            }
        }


    }
}
