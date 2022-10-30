package com.runicrealms.api.chat;

import org.bukkit.entity.Player;

import java.util.Collection;

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
     * @return Channel Name
     */
    public abstract String getName();

    /**
     * A list of players who will receive a given message
     *
     * @param player who sent the message
     * @return a collection of players to receive message
     */
    public abstract Collection<Player> getRecipients(Player player);

    /**
     * @return
     */
    public abstract String getMessageFormat();
}
