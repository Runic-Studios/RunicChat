package com.runicrealms.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import com.runicrealms.RunicChat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Created by KissOfFate
 * Date: 5/10/2020
 * Time: 8:28 PM
 */
@CommandAlias("mute")
public class Mute extends BaseCommand {

    @Default
    @CommandPermission("runicchat.mute")
    @Syntax("<player> <minutes>")
    public void execute(Player sender, @Single Player target, int time) {
        RunicChat.getRunicChatAPI().mute(target, true);

        Bukkit.getServer().getScheduler().runTaskLaterAsynchronously(RunicChat.getPlugin(RunicChat.class), () -> {
            RunicChat.getRunicChatAPI().mute(target, false);
        }, 20 * (60 * time));
    }
}
