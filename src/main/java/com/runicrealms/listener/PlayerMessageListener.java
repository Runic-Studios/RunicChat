package com.runicrealms.listener;

import com.runicrealms.RunicChat;
import com.runicrealms.api.chat.ChatChannel;
import com.runicrealms.api.event.ChatChannelMessageEvent;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Collection;
import java.util.List;

/**
 * Created by KissOfFate
 * Date: 4/20/2020
 * Time: 8:56 PM
 */
public class PlayerMessageListener implements Listener {

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        event.setCancelled(true);

        ChatChannel channel = RunicChat.getRunicChatAPI().getPlayerChatChannel(event.getPlayer());
        Collection<Player> recipients = channel.getRecipients(event.getPlayer());

        ChatChannelMessageEvent chatChannelMessageEvent = new ChatChannelMessageEvent(event.getPlayer(), channel, recipients, event.getMessage());
        Bukkit.getPluginManager().callEvent(chatChannelMessageEvent);
    }

    @EventHandler
    public void onChatChannelMessage(ChatChannelMessageEvent event) {
        if(RunicChat.getRunicChatAPI().getMutes().contains(event.getMessageSender())) {
            event.setCancelled(true);
            event.getMessageSender().sendMessage(ChatColor.RED + "You are muted!");
            return;
        }
        String formattedMessage = ChatColor.translateAlternateColorCodes('&', PlaceholderAPI.setPlaceholders(event.getMessageSender(), event.getChatChannel().getPrefix() + event.getChatChannel().getMessageFormat()));

        String finalMessage = formattedMessage.replace("%message%", event.getMessageSender().hasPermission("runicchat.color") ? ChatColor.translateAlternateColorCodes('&', event.getChatMessage()) : event.getChatMessage());

        event.getRecipients().forEach(p -> p.sendMessage(finalMessage));
    }


}
