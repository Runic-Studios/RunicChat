package com.runicrealms.channels;

import co.aikar.commands.ACFBukkitUtil;
import com.runicrealms.api.chat.ChatChannel;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KissOfFate
 * Date: 4/20/2020
 * Time: 9:25 PM
 */
public class Local extends ChatChannel {
    @Override
    public String getPrefix() {
        return "&a[%core_level%] &r";
    }

    @Override
    public String getName() {
        return "local";
    }

    @Override
    public List<Player> getRecipients(Player player) {
        List<Player> recipients = new ArrayList<>();

        for (Player target : Bukkit.getOnlinePlayers()) {
            if(target != null /*&& RunicChat.getRunicChatAPI().getPlayerChatChannel(target).getName().equalsIgnoreCase(getName())*/) {
                if (ACFBukkitUtil.isWithinDistance(player, target, 50)) {
                    recipients.add(target);
                }
            }
        }

        return recipients;
    }

    @Override
    public String getMessageFormat() {
        return "%luckperms_meta_name_color%%player_name%: &f%message%";
    }
}
