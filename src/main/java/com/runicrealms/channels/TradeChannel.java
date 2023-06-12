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

public class TradeChannel extends ChatChannel {

    @Override
    public String getName() {
        return "trade";
    }

    @Override
    public List<Player> getRecipients(Player player) {
        return new ArrayList<>(Bukkit.getOnlinePlayers());
    }

    @Override
    public TextComponent createMessage(Player player, String message) {
        TextComponent textComponent = new TextComponent(ColorUtil.format(PlaceholderAPI.setPlaceholders(player, "&3[Trade] &6%guild_prefix%%core_name_color%%player_name%: &f")) + message);
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

    @Override
    public boolean isSpyable() {
        return false;
    }

}
