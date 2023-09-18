package com.runicrealms.plugin.chat.channels;

import com.runicrealms.plugin.chat.api.chat.ChatChannel;
import com.runicrealms.plugin.common.util.ColorUtil;
import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class StaffChannel extends ChatChannel {

    @Override
    public String getName() {
        return "staff";
    }

    @Override
    public List<Player> getRecipients(Player player) {
        return Bukkit.getOnlinePlayers().stream().filter(target -> target.hasPermission("runicchat.staff")).collect(Collectors.toList());
    }

    @Override
    public TextComponent createMessage(Player player, String finalMessage) {
        return new TextComponent(ColorUtil.format(PlaceholderAPI.setPlaceholders(player, "&2[Staff] %core_name_color%%player_name%: &f")) + finalMessage);
    }

    @Override
    public boolean isSpyable() {
        return false;
    }

    @Override
    public boolean canAccess(Player player) {
        return player.hasPermission("runicchat.staff");
    }
}