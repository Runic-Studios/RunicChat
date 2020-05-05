package com.runicrealms.api;

import com.runicrealms.api.chat.ChatChannel;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

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
     * Attempt to unregister the channels channel from the plugin
     *
     * @return true if successful, false if not
     */
    boolean unregisterChatChannel(ChatChannel channel);
}
