package com.runicrealms.api;

import com.runicrealms.api.chat.ChatChannel;
import com.runicrealms.util.ProfanityFilter;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

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
    List<TextComponent> parseMessage(Player sender, TextComponent message);

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
    void mute(UUID player, boolean mute);

    /**
     * Get all muted players
     */
    Collection<UUID> getMutes();

    /**
     * Get all tasks that are set to unmute players
     */
    Map<UUID, BukkitTask> getUnmuteTasks();

    ProfanityFilter getProfanityFilter();

    /**
     * Gets all the players currently spying on chat channels.
     */
    Set<UUID> getSpyingPlayers();

    /**
     * Gets the "is spying on" status of each channel for a player.
     */
    Map<ChatChannel, Boolean> getSpySettings(Player player);

    /**
     * Sets whether a player is spying on this chat channel.
     */
    void setSpy(Player player, ChatChannel channel, boolean enabled);

    /**
     * Gets whether a player is spying on a given chat channel
     */
    boolean isSpyingOnChannel(Player player, ChatChannel channel);

    /**
     * Checks if a chat channel is muted for a player
     */
    boolean isChannelMuted(Player player, ChatChannel channel);

    /**
     * Sets a channel muted
     */
    void setChannelMuted(Player player, ChatChannel channel, boolean muted);

    /**
     * Sends a message in a player's current chat channel.
     */
    void sendMessage(Player sender, String message);

    /**
     * Sends a message for a player in a given chat channel.
     */
    void sendMessage(Player sender, ChatChannel channel, String message);

    /**
     * Gets the last target a player whispered to
     */
    @Nullable Player getLastWhisperTarget(Player sender);

    /**
     * Lets the last target of a player who is whispering
     */
    void setWhisperTarget(Player sender, Player target);

    /**
     * Sends a whisper message between players
     */
    void sendWhisper(Player sender, Player target, String message);

}
