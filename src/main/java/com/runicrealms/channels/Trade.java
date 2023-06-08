package com.runicrealms.channels;

import com.runicrealms.api.chat.ChatChannel;
import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Trade extends ChatChannel {

    @Override
    public String getPrefix() {
        return "&3[Trade] &6%guild_prefix%%core_name_color%%player_name%: ";
    }

    @Override
    public String getName() {
        return "trade";
    }

    @Override
    public List<Player> getRecipients(Player player) {
        return new ArrayList<>(Bukkit.getOnlinePlayers());
    }

    @Override
    public String getMessageFormat() {
        return "&f%message%";
    }

    @Override
    public TextComponent getTextComponent(Player player, String finalMessage) {
        TextComponent textComponent = new TextComponent(finalMessage);
        textComponent.setHoverEvent(new HoverEvent
                (
                        HoverEvent.Action.SHOW_TEXT,
                        new Text(PlaceholderAPI.setPlaceholders(player,
                                ChatColor.DARK_AQUA + "Title: " + ChatColor.AQUA + "%core_prefix%" +
                                        ChatColor.GOLD + "\nGuild: %guild_name%" +
                                        ChatColor.GOLD + "\nGuild Score: %guild_score%"
                        ))
                )
        );
        return textComponent;
    }

}
