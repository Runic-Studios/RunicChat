package com.runicrealms;

import com.runicrealms.api.RunicChatAPI;
import com.runicrealms.api.chat.ChatChannel;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by KissOfFate
 * Date: 4/20/2020
 * Time: 10:04 PM
 */
public class ChatManager implements RunicChatAPI {

    private final List<ChatChannel> registeredChannels = new ArrayList<>();
    private final HashMap<Player, ChatChannel> playerChatChannels = new HashMap<>();
    private final List<Player> mutedPlayers = new ArrayList<>();

    @Override
    public boolean registerChatChannel(ChatChannel channel) {
        registeredChannels.add(channel);
        return false;
    }

    @Override
    public List<ChatChannel> getChatChannels() {
        return this.registeredChannels;
    }

    @Override
    public ChatChannel getPlayerChatChannel(Player player) {
        return playerChatChannels.containsKey(player) ? playerChatChannels.get(player) : this.registeredChannels.get(0);
    }

    @Override
    public ChatChannel setPlayerChatChannel(Player player, ChatChannel channel) {
        if (!playerChatChannels.containsKey(player)) playerChatChannels.put(player, channel);
        else {
            playerChatChannels.remove(player);
            playerChatChannels.put(player, channel);
        }
        return channel;
    }

    @Override
    public boolean unregisterChatChannel(ChatChannel channel) {
        return false;
    }

    /**
     * Mutes a player, or un-mutes if mute is false
     *
     * @param player to mute or un-mute
     * @param mute   true if player should be muted, false to be un-muted
     */
    @Override
    public void mute(Player player, boolean mute) {
        if (mutedPlayers.contains(player) && !mute) {
            mutedPlayers.remove(player);
        }

        if (!mutedPlayers.contains(player) && mute) {
            mutedPlayers.add(player);
        }
    }

    @Override
    public List<Player> getMutes() {
        return mutedPlayers;
    }
}
