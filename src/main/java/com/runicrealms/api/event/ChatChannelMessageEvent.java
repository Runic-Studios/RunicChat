package com.runicrealms.api.event;

import com.runicrealms.api.chat.ChatChannel;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.Collection;

/**
 * An ASYNC event to send chat messages
 * Created by KissOfFate
 * Date: 4/20/2020
 * Time: 8:37 PM
 */
public class ChatChannelMessageEvent extends Event implements Cancellable {
    private final Player messageSender;
    private final ChatChannel chatChannel;
    private final Collection<Player> recipients;
    private final String chatMessage;
    private static final HandlerList HANDLERS_LIST = new HandlerList();
    private boolean isCancelled;

    public ChatChannelMessageEvent(final Player messageSender, ChatChannel chatChannel, Collection<Player> recipients, String chatMessage) {
        super(true);
        this.messageSender = messageSender;
        this.chatChannel = chatChannel;
        this.recipients = recipients;
        this.chatMessage = chatMessage;
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

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }
}
