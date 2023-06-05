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
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@CommandAlias("unmute")
public class Unmute extends BaseCommand {

    public Unmute() {
        RunicChat.getCommandManager().getCommandCompletions().registerCompletion("muted-players", (context) ->
                RunicChat.getRunicChatAPI().getMutes().stream()
                        .map(Bukkit::getPlayer)
                        .filter(Objects::nonNull)
                        .map(HumanEntity::getName)
                        .collect(Collectors.toList()));
    }

    @Default
    @CommandPermission("runicchat.mute")
    @Syntax("<player>")
    @CommandCompletion("@muted-players")
    public void execute(CommandSender sender, @Single String targetName) {
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
            RunicChat.getRunicChatAPI().mute(targetUUID, false);
            Map<UUID, BukkitTask> unmuteTasks = RunicChat.getRunicChatAPI().getUnmuteTasks();
            target.sendMessage(ChatColor.GREEN + "You have been un-muted!");
            if (unmuteTasks.containsKey(targetUUID)) {
                unmuteTasks.get(targetUUID).cancel();
                unmuteTasks.remove(targetUUID);
            }
            sender.sendMessage(ChatColor.GREEN + "Unmuted " + targetName);
        } else {
            sender.sendMessage(ChatColor.RED + targetName + " is not muted!");
        }
    }
}
