package com.runicrealms.channels;

import com.runicrealms.api.chat.ChatChannel;
import org.bukkit.Bukkit;
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
        return "&8[&c!&8]&r ";
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
        return "&7%player_name%&8: &f%message%";
    }
}
