package com.runicrealms.plugin.chat.api.chat;

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
     * A TextComponent that is used as a wrapper on the message to add hover and click logic
     *
     * @param sender  who sent the message
     * @param message message to be sent
     * @return a TextComponent that can include hover or click events
     */
    public abstract TextComponent createMessage(Player sender, String message);

    /**
     * A TextComponent that is used as a wrapper on the message to add hover and click logic
     *
     * @param sender  who sent the message
     * @param spy     person spying on this message
     * @param message message to be sent
     * @return a TextComponent that can include hover or click events
     */
    public TextComponent createSpyMessage(Player sender, Player spy, String message) {
        return createMessage(sender, message);
    }

    /**
     * Gets if a staff member can spy on this channel
     */
    public abstract boolean isSpyable();

    /**
     * Gets whether spy should be able to see this message.
     * This only ever returns false if the message is not going to be delivered.
     */
    public boolean canSpy(Player sender, Player spy) {
        return true;
    }

    public boolean canAccess(Player player) {
        return true;
    }
}
