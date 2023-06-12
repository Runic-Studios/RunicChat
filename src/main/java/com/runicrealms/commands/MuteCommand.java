package com.runicrealms.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Single;
import co.aikar.commands.annotation.Syntax;
import com.runicrealms.RunicChat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by KissOfFate
 * Date: 5/10/2020
 * Time: 8:28 PM
 */
@CommandAlias("mute")
public class MuteCommand extends BaseCommand {

    public MuteCommand() {
        RunicChat.getCommandManager().getCommandCompletions().registerCompletion("unmuted-players", (context) ->
                Bukkit.getOnlinePlayers().stream()
                        .filter((player) -> !RunicChat.getRunicChatAPI().getMutes().contains(player.getUniqueId()))
                        .map(HumanEntity::getName)
                        .collect(Collectors.toList()));
    }

    @Default
    @CommandPermission("runicchat.mute")
    @Syntax("<player> <minutes>")
    @CommandCompletion("@unmuted-players @range:1-60")
    public void execute(CommandSender sender, @Single String targetName, int time) {
        if (targetName.isEmpty()) {
            sender.sendMessage(ChatColor.RED + "Player not found!");
            return;
        }

        Player target = Bukkit.getPlayer(targetName);

        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Player not found!");
            return;
        }

        UUID targetUUID = target.getUniqueId();

        if (RunicChat.getRunicChatAPI().getMutes().stream().anyMatch((uuid) -> uuid.equals(target.getUniqueId()))) {
            // player is already muted, reset the timer
            Map<UUID, BukkitTask> unmuteTasks = RunicChat.getRunicChatAPI().getUnmuteTasks();
            if (unmuteTasks.containsKey(targetUUID)) {
                unmuteTasks.get(targetUUID).cancel();
                unmuteTasks.remove(targetUUID);
            }
            sender.sendMessage(ChatColor.GREEN + "Muted " + targetName + " again for " + time + " minutes!");
        } else {
            // Mute the target
            RunicChat.getRunicChatAPI().mute(targetUUID, true);
            target.sendMessage(ChatColor.RED + "You have been muted for " + time + " minutes!");
            sender.sendMessage(ChatColor.GREEN + "Muted " + targetName + " for " + time + " minutes!");
        }
        RunicChat.getRunicChatAPI().getUnmuteTasks().put(target.getUniqueId(), Bukkit.getServer().getScheduler().runTaskLaterAsynchronously(RunicChat.getPlugin(RunicChat.class), () -> {
            RunicChat.getRunicChatAPI().mute(targetUUID, false);
            Player player = Bukkit.getPlayer(targetUUID);
            if (player != null) player.sendMessage(ChatColor.GREEN + "You have been un-muted!");
        }, 20 * (60L * time)));
    }

}
