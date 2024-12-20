package com.runicrealms.plugin.chat.api.event;

import com.runicrealms.plugin.chat.api.chat.ChatChannel;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.Collection;
import java.util.HashSet;

/**
 * An ASYNC event to send chat messages
 * Created by KissOfFate
 * Date: 4/20/2020
 * Time: 8:37 PM
 */
public class ChatChannelMessageEvent extends Event implements Cancellable {
    private static final HandlerList HANDLERS_LIST = new HandlerList();
    private final Player messageSender;
    private final ChatChannel chatChannel;
    private final Collection<Player> recipients;
    private final Collection<Player> spies = new HashSet<>();
    private final String chatMessage;
    private boolean isCancelled;

    public ChatChannelMessageEvent(final Player messageSender, ChatChannel chatChannel, Collection<Player> recipients, String chatMessage) {
        super(true);
        this.messageSender = messageSender;
        this.chatChannel = chatChannel;
        this.recipients = recipients;
        this.chatMessage = chatMessage;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

    public Player getMessageSender() {
        return this.messageSender;
    }

    public ChatChannel getChatChannel() {
        return this.chatChannel;
    }

    public Collection<Player> getRecipients() {
        return this.recipients;
    }

    public Collection<Player> getSpies() {
        return this.spies;
    }

    public String getChatMessage() {
        return this.chatMessage;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.isCancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }
}
