package com.runicrealms.plugin.chat.listener;

import com.runicrealms.plugin.chat.RunicChat;
import com.runicrealms.plugin.chat.api.event.ChatChannelMessageEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.UUID;

/**
 * Created by KissOfFate
 * Date: 4/20/2020
 * Time: 8:56 PM
 */
public class PlayerMessageListener implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (event.isCancelled()) return;
        event.setCancelled(true);

        RunicChat.getRunicChatAPI().sendMessage(event.getPlayer(), event.getMessage());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChatChannelMessageSpies(ChatChannelMessageEvent event) {
        for (UUID target : RunicChat.getRunicChatAPI().getSpyingPlayers()) {
            Player player = Bukkit.getPlayer(target);
            if (player == null) continue;
            if (RunicChat.getRunicChatAPI().isSpyingOnChannel(player, event.getChatChannel())) {
                if (!event.getRecipients().contains(player)) event.getSpies().add(player);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onChatChannelMessage(ChatChannelMessageEvent event) {
        if (event.isCancelled()) return;

        if (RunicChat.getRunicChatAPI().getMutes().contains(event.getMessageSender().getUniqueId())) {
            event.setCancelled(true);
            event.getMessageSender().sendMessage(ChatColor.RED + "You are muted!");
            return;
        }

        if (RunicChat.getRunicChatAPI().isChannelMuted(event.getMessageSender(), event.getChatChannel())) {
            event.setCancelled(true);
            event.getMessageSender().sendMessage(ChatColor.RED + "You cannot send messages to a channel you have muted!");
            return;
        }

        event.getRecipients().forEach(player -> {
            if (!RunicChat.getRunicChatAPI().isChannelMuted(player, event.getChatChannel())) {
                TextComponent textComponent = event.getChatChannel().createMessage(event.getMessageSender(), event.getChatMessage());
                player.spigot().sendMessage(RunicChat.getRunicChatAPI().parseMessage(event.getMessageSender(), textComponent).toArray(new TextComponent[0]));
            }
        });

        for (Player spy : event.getSpies()) {
            if (event.getChatChannel().canSpy(event.getMessageSender(), spy) && !event.getRecipients().contains(spy)) {
                TextComponent textComponent = event.getChatChannel().createSpyMessage(event.getMessageSender(), spy, event.getChatMessage());
                spy.spigot().sendMessage(RunicChat.getRunicChatAPI().parseMessage(event.getMessageSender(), textComponent).toArray(new TextComponent[0]));
            }
        }
    }


}
