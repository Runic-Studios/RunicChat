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

/**
 * Created by KissOfFate
 * Date: 5/9/2020
 * Time: 4:09 PM
 */
public class Global extends ChatChannel {

    @Override
    public String getPrefix() {
        return "&c[!] &r";
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
        return "%luckperms_meta_name_color%%player_name%: &f%message%";
    }

    @Override
    public TextComponent getTextComponent(Player player, String finalMessage) {
        TextComponent textComponent = new TextComponent(finalMessage);
        textComponent.setHoverEvent(new HoverEvent
                (
                        HoverEvent.Action.SHOW_TEXT,
                        new Text(PlaceholderAPI.setPlaceholders(player,
                                ChatColor.DARK_AQUA + "Title: " + ChatColor.AQUA + "%core_prefix%" +
                                        ChatColor.GOLD + "\n%runic_guild_prefix%"
                        ))
                )
        );
        return textComponent;
    }
}
