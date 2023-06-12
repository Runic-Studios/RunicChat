package com.runicrealms.channels;

import com.runicrealms.api.chat.ChatChannel;
import com.runicrealms.plugin.common.util.ColorUtil;
import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KissOfFate
 * Date: 5/9/2020
 * Time: 4:09 PM
 */
public class GlobalChannel extends ChatChannel {

    @Override
    public String getPrefix() {
        return "&c[Global] &6%guild_prefix%%core_name_color%%player_name%: ";
    }

    @Override
    public String getName() {
        return "global";
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
        String title = PlaceholderAPI.setPlaceholders(player, "%core_prefix%");
        if (title.isEmpty()) title = "None";
        String guildName = PlaceholderAPI.setPlaceholders(player, "%guild_name%");
        if (guildName.isEmpty()) guildName = ChatColor.GRAY + "None";
        String guildScore = PlaceholderAPI.setPlaceholders(player, "%guild_score%");
        if (guildScore.isEmpty()) guildScore = ChatColor.GRAY + "None";
        String titleColor = ColorUtil.format(PlaceholderAPI.setPlaceholders(player, "%core_name_color%"));
        textComponent.setHoverEvent(new HoverEvent
                (
                        HoverEvent.Action.SHOW_TEXT,
                        new Text(
                                ChatColor.DARK_AQUA + "Title: " + titleColor + title +
                                        ChatColor.GOLD + "\nGuild: " + guildName +
                                        ChatColor.GOLD + "\nGuild Score: " + guildScore
                        )
                )
        );
        return textComponent;
    }
}
