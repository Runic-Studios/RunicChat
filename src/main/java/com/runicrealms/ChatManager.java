package com.runicrealms;

import com.runicrealms.api.RunicChatAPI;
import com.runicrealms.api.chat.ChatChannel;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KissOfFate
 * Date: 4/20/2020
 * Time: 10:04 PM
 */
public class ChatManager implements RunicChatAPI {

    private List<ChatChannel> registeredChannels = new ArrayList<>();

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
        return this.registeredChannels.get(0);
    }

    @Override
    public boolean unregisterChatChannel(ChatChannel channel) {
        return false;
    }
}
