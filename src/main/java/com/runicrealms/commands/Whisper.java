package com.runicrealms.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import com.runicrealms.RunicChat;
import com.runicrealms.api.RunicChatAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by KissOfFate
 * Date: 5/10/2020
 * Time: 8:11 PM
 */
@CommandAlias("whisper|w|msg|message|dm")
public class Whisper extends BaseCommand {

    @Dependency
    private RunicChatAPI runicChatAPI;

    public Whisper() {
        RunicChat.getCommandManager().getCommandCompletions().registerAsyncCompletion("online", context -> {
            Set<String> players = new HashSet<>();
            for (Player player : Bukkit.getOnlinePlayers())
                players.add(player.getName());
            return players;
        });
    }

    @Default
    @CommandCompletion("@online")
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
                            "&9&oYou whisper to &r&o" + target.getName() + "&9&o: &7&o" + message));
            target.sendMessage(
                    ChatColor.translateAlternateColorCodes('&',
                            "&r&o" + sender.getName() + "&9&o whispers to you: &7&o" + message));

            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.hasPermission("runicchat.spy")) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&c[SPY]&8[&b" + sender.getName() + " &7-> &a" + target.getName() + "&8] " + message));
                }
            }

        } else {
            sender.sendMessage(
                    ChatColor.translateAlternateColorCodes('&',
                            "&9&oYou whisper to &r&o" + target.getName() + "&9&o: &7&o" + message));
            target.sendMessage(
                    ChatColor.translateAlternateColorCodes('&',
                            "&r&o" + sender.getName() + "&9&o whispers to you: &7&o" + message));

            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.hasPermission("runicchat.spy")) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&c[SPY]&8[&b" + sender.getName() + " &7-> &a" + target.getName() + "&8] ") + message);
                }
            }
        }


    }
}
