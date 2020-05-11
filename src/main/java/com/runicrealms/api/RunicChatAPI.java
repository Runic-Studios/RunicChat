package com.runicrealms.api;

import com.runicrealms.api.chat.ChatChannel;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Created by KissOfFate
 * Date: 4/20/2020
 * Time: 8:25 PM
 */
public interface RunicChatAPI {

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
     * @param player player
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
    void mute(Player player, boolean mute);;

    List<Player> getMutes();
}
