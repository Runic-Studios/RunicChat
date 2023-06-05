package com.runicrealms;

import com.runicrealms.api.RunicChatAPI;
import com.runicrealms.api.chat.ChatChannel;
import com.runicrealms.util.ReflectionUtil;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;

/**
 * Created by KissOfFate
 * Date: 4/20/2020
 * Time: 10:04 PM
 */
public class ChatManager implements RunicChatAPI {

    private final List<ChatChannel> registeredChannels = new ArrayList<>();
    private final HashMap<Player, ChatChannel> playerChatChannels = new HashMap<>();
    private final Set<UUID> mutedPlayers = new HashSet<>();

    private final Map<UUID, BukkitTask> unmuteTasks = new HashMap<>();

    private static String replaceCoordsWithPlayerLocation(Player player, String message) {
        // Get player's location and format it as a string
        Location location = player.getLocation();
        String formattedLocation = String.format(player.getName() + "'s location: (%dx, %dy, %dz)", location.getBlockX(), location.getBlockY(), location.getBlockZ());
        // Replace occurrences of [coords] with the player's location
        return message.replace("[coords]", formattedLocation);
    }

    @SuppressWarnings("deprecation")
    @Override
    public String convertItemStackToJson(ItemStack itemStack) {
        // ItemStack methods to get a net.minecraft.server.ItemStack object for serialization
        Class<?> craftItemStackClazz = ReflectionUtil.getOBCClass("inventory.CraftItemStack");
        Method asNMSCopyMethod = ReflectionUtil.getMethod(craftItemStackClazz, "asNMSCopy", ItemStack.class);

        // NMS Method to serialize a net.minecraft.server.ItemStack to a valid Json string
        Class<?> nmsItemStackClazz = ReflectionUtil.getNMSClass("ItemStack");
        Class<?> nbtTagCompoundClazz = ReflectionUtil.getNMSClass("NBTTagCompound");
        Method saveNmsItemStackMethod = ReflectionUtil.getMethod(nmsItemStackClazz, "save", nbtTagCompoundClazz);

        Object nmsNbtTagCompoundObj; // This will just be an empty NBTTagCompound instance to invoke the saveNms method
        Object nmsItemStackObj; // This is the net.minecraft.server.ItemStack object received from the asNMSCopy method
        Object itemAsJsonObject; // This is the net.minecraft.server.ItemStack after being put through saveNmsItem method

        try {
            nmsNbtTagCompoundObj = nbtTagCompoundClazz.newInstance();
            nmsItemStackObj = asNMSCopyMethod.invoke(null, itemStack);
            itemAsJsonObject = saveNmsItemStackMethod.invoke(nmsItemStackObj, nmsNbtTagCompoundObj);
        } catch (Throwable t) {
            Bukkit.getLogger().log(Level.SEVERE, "failed to serialize ItemStack to nms item", t);
            return null;
        }

        // Return a string representation of the serialized object
        return itemAsJsonObject.toString();
    }

    @Override
    public List<TextComponent> parseMessage(Player sender, String message) {
        String finalMessage =
                sender.hasPermission("runicchat.color")
                        ?
                        ChatColor.translateAlternateColorCodes('&', message)
                        :
                        message;
        // Use a regular expression to filter bad words
        String regex = "(?i)\\b(" + String.join("|", RunicChat.getWordsToFilter()) + ")\\b";
        finalMessage = finalMessage.replaceAll(regex, "***");
        List<TextComponent> textComponentList = new ArrayList<>();
        // Handle [coords]
        finalMessage = replaceCoordsWithPlayerLocation(sender, finalMessage);
        // Handle [Item] hover
        if (finalMessage.toLowerCase().contains("[item]")) {
            return itemHoverComponentList(sender, finalMessage);
        }
        textComponentList.add(new TextComponent(finalMessage));
        return textComponentList;
    }

    @SuppressWarnings("deprecation")
    @Override
    public List<TextComponent> itemHoverComponentList(Player sender, String message) {
        List<TextComponent> textComponentList = new ArrayList<>();
        String[] array = message.split("\\[item]");
        ItemStack heldItem = sender.getInventory().getItemInMainHand();
        String itemName;
        if (heldItem.getItemMeta() != null) {
            itemName =
                    ChatColor.GREEN + "[" + heldItem.getItemMeta().getDisplayName() + ChatColor.GREEN + "]";
        } else {
            itemName = ChatColor.GREEN + "[Item]";
        }
        TextComponent item = new TextComponent(itemName + ChatColor.RESET);
        String itemJson = convertItemStackToJson(heldItem);
        BaseComponent[] hoverEventComponents = new BaseComponent[]{
                new TextComponent(itemJson)
        };
        item.setHoverEvent(new HoverEvent(
                HoverEvent.Action.SHOW_ITEM,
                hoverEventComponents
        ));
        if (array.length > 0) {
            textComponentList.add(new TextComponent(array[0]));
        }
        textComponentList.add(item);
        if (array.length > 1) {
            textComponentList.add(new TextComponent(array[1]));
        }
        return textComponentList;
    }

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
    public void mute(UUID player, boolean mute) {
        if (mutedPlayers.contains(player) && !mute) {
            mutedPlayers.remove(player);
        }

        if (!mutedPlayers.contains(player) && mute) {
            mutedPlayers.add(player);
        }
    }

    @Override
    public Set<UUID> getMutes() {
        return mutedPlayers;
    }

    @Override
    public Map<UUID, BukkitTask> getUnmuteTasks() {
        return unmuteTasks;
    }

}
