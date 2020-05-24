package com.runicrealms.api.chat;

import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;

/**
 * Created by KissOfFate
 * Date: 4/20/2020
 * Time: 8:38 PM
 */
public abstract class ChatChannel {

    /**
     * @return Channel Prefix
     */
    public abstract String getPrefix();

    /**
     * @return Channel NAme
     */
    public abstract String getName();

    /**
     * @return
     */
    public abstract Collection<Player> getRecipients(Player player);

    /**
     * @return
     */
    public abstract String getMessageFormat();
}
