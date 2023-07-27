package com.runicrealms.plugin.chat.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Dependency;
import co.aikar.commands.annotation.Single;
import co.aikar.commands.annotation.Syntax;
import com.runicrealms.plugin.chat.RunicChat;
import com.runicrealms.plugin.chat.api.RunicChatAPI;
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
@CommandPermission("runicchat.message")
public class WhisperCommand extends BaseCommand {

    @Dependency
    private RunicChatAPI runicChatAPI;

    public WhisperCommand() {
        RunicChat.getCommandManager().getCommandCompletions().registerAsyncCompletion("online", context -> {
            Set<String> players = new HashSet<>();
            for (Player player : Bukkit.getOnlinePlayers())
                players.add(player.getName());
            return players;
        });
    }

    @Default
    @CommandCompletion("@online")
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

        RunicChat.getRunicChatAPI().sendWhisper(sender, target, message);
    }

}
