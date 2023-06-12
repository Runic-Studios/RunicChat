package com.runicrealms.channels;

import co.aikar.commands.ACFBukkitUtil;
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
 * Date: 4/20/2020
 * Time: 9:25 PM
 */
public class LocalChannel extends ChatChannel {

    @Override
    public String getPrefix() {
        return "&e[Local] &3%core_prefix_formatted%&r%core_name_color%%player_name%: ";
    }

    @Override
    public String getName() {
        return "local";
    }

    @Override
    public List<Player> getRecipients(Player player) {
        List<Player> recipients = new ArrayList<>();

        for (Player target : Bukkit.getOnlinePlayers()) {
            if (target != null) {
                if (ACFBukkitUtil.isWithinDistance(player, target, 50)) {
                    recipients.add(target);
                }
            }
        }

        return recipients;
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
        String titleColor = ColorUtil.format(PlaceholderAPI.setPlaceholders(player, "%core_name_color%"));
        textComponent.setHoverEvent(new HoverEvent
                (
                        HoverEvent.Action.SHOW_TEXT,
                        new Text(
                                ChatColor.DARK_AQUA + "Title: " + titleColor + title +
                                        ChatColor.GREEN + "\n%core_class% lv. %core_level%"
                        )
                )
        );
        return textComponent;
    }
}