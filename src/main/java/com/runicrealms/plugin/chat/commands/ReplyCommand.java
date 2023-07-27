package com.runicrealms.plugin.chat.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CatchUnknown;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import com.runicrealms.plugin.chat.RunicChat;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

@CommandAlias("reply|r")
@CommandPermission("runicchat.message")
public class ReplyCommand extends BaseCommand {

    @Default
    @CatchUnknown
    public void onReply(Player sender, String message) {
        Player target = RunicChat.getRunicChatAPI().getLastWhisperTarget(sender);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "You have no one to reply to! Try messaging them with /msg.");
            return;
        }

        RunicChat.getRunicChatAPI().sendWhisper(sender, target, message);
    }

}
