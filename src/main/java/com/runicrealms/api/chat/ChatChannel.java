package com.runicrealms.api.chat;

import net.md_5.bungee.api.chat.TextComponent;
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
     * Decorates the message with titles, permission colors, etc.
     *
     * @return a String representing the presentation of the message
     */
    public abstract String getMessageFormat();

    /**
     * A TextComponent that is used as a wrapper on the message to add hover and click logic
     *
     * @param sender       who sent the message
     * @param finalMessage the final message that will be sent in chat (after any logic)
     * @return a TextComponent that can include hover or click events
     */
    public abstract TextComponent getTextComponent(Player sender, String finalMessage);
}
