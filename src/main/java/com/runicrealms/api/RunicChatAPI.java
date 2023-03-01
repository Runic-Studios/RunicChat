package com.runicrealms.api;

import com.runicrealms.api.chat.ChatChannel;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * Created by KissOfFate
 * Date: 4/20/2020
 * Time: 8:25 PM
 */
public interface RunicChatAPI {

    /**
     * Converts an {@link org.bukkit.inventory.ItemStack} to a Json string
     * for sending with {@link net.md_5.bungee.api.chat.BaseComponent}'s.
     *
     * @param itemStack the item to convert
     * @return the Json string representation of the item
     */
    String convertItemStackToJson(ItemStack itemStack);

    /**
     * Returns a list of text components which is mutable
     * Performs any necessary mutation on the chat message.
     * Updates chat colors if player has permission and adds hover/click events as necessary
     *
     * @param sender  of the message
     * @param message the string to mutate
     * @return a list of text components with all necessary changes to original string. Mutable
     */
    List<TextComponent> parseMessage(Player sender, String message);

    /**
     * Returns a list of text components associated with a message containing "[item]", which
     * links the player's held item in chat
     *
     * @param sender  who sent the chat message
     * @param message the contents
     * @return a list of components that have been modified
     */
    List<TextComponent> itemHoverComponentList(Player sender, String message);

    /**
     * Attempt to register the channels channel into the plugin
     *
     * @return true if successful, false if not
     */
    boolean registerChatChannel(ChatChannel channel);

    /**
     * @return List of registered channels
     */
    List<ChatChannel> getChatChannels();

    /**
     * @return chat channel of player
     */
    ChatChannel getPlayerChatChannel(Player player);

    /**
     * Set the chat channel of a user
     *
     * @param player  player
     * @param channel channel to set player too
     * @return chat channel of player
     */
    ChatChannel setPlayerChatChannel(Player player, ChatChannel channel);


    /**
     * Attempt to unregister the channels channel from the plugin
     *
     * @return true if successful, false if not
     */
    boolean unregisterChatChannel(ChatChannel channel);

    /**
     * Mute or Unmute user
     */
    void mute(Player player, boolean mute);

    List<Player> getMutes();
}
