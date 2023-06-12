package com.runicrealms;

import com.runicrealms.api.RunicChatAPI;
import com.runicrealms.api.chat.ChatChannel;
import com.runicrealms.api.event.ChatChannelMessageEvent;
import com.runicrealms.plugin.common.RunicCommon;
import com.runicrealms.plugin.common.util.ColorUtil;
import com.runicrealms.util.ProfanityFilter;
import com.runicrealms.util.ReflectionUtil;
import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

/**
 * Created by KissOfFate
 * Date: 4/20/2020
 * Time: 10:04 PM
 */
public class ChatManager implements RunicChatAPI, Listener {

    private final List<ChatChannel> registeredChannels = new ArrayList<>();
    private final HashMap<Player, ChatChannel> playerChatChannels = new HashMap<>();
    private final Set<UUID> mutedPlayers = new HashSet<>();

    private final Map<UUID, BukkitTask> unmuteTasks = new HashMap<>();

    private final ProfanityFilter filter = new ProfanityFilter(RunicChat.getWordsToFilter());

    private final Map<UUID, Set<ChatChannel>> spyChannels = new ConcurrentHashMap<>();

    private final Map<UUID, Map<ChatChannel, Boolean>> channelsEnabled = new ConcurrentHashMap<>();

    private final Map<UUID, UUID> whisperTargets = new HashMap<>();

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
    public List<TextComponent> parseMessage(Player sender, TextComponent message) {
        String text = message.getText();
        if (sender.hasPermission("runicchat.color")) text = ColorUtil.format(text);
        text = replaceCoordsWithPlayerLocation(sender, text);
        text = getProfanityFilter().filter(text);
        message.setText(text);
        if (text.toLowerCase().contains("[item]")) {
            return getItemHoverText(sender, message);
        }
        return Collections.singletonList(message);
    }

//        String finalMessage =
//                sender.hasPermission("runicchat.color")
//                        ?
//                        ChatColor.translateAlternateColorCodes('&', message)
//                        :
//                        message;
//        finalMessage = getProfanityFilter().filter(finalMessage);
//        List<TextComponent> textComponentList = new ArrayList<>();
//        // Handle [coords]
//        finalMessage = replaceCoordsWithPlayerLocation(sender, finalMessage);
//        // Handle [Item] hover
//        if (finalMessage.toLowerCase().contains("[item]")) {
//            return itemHoverComponentList(sender, finalMessage);
//        }
//        textComponentList.add(new TextComponent(finalMessage));
//        return textComponentList;
//    }

//    @SuppressWarnings("deprecation")
//    @Override
//    public List<TextComponent> itemHoverComponentList(Player sender, String message) {
//        List<TextComponent> textComponentList = new ArrayList<>();
//        String[] array = message.split("\\[item]");
//        ItemStack heldItem = sender.getInventory().getItemInMainHand();
//        String itemName;
//        if (heldItem.getItemMeta() != null) {
//            itemName =
//                    ChatColor.GREEN + "[" + heldItem.getItemMeta().getDisplayName() + ChatColor.GREEN + "]";
//        } else {
//            itemName = ChatColor.GREEN + "[Item]";
//        }
//        TextComponent item = new TextComponent(itemName + ChatColor.RESET);
//        String itemJson = convertItemStackToJson(heldItem);
//        BaseComponent[] hoverEventComponents = new BaseComponent[]{
//                new TextComponent(itemJson)
//        };
//        item.setHoverEvent(new HoverEvent(
//                HoverEvent.Action.SHOW_ITEM,
//                hoverEventComponents
//        ));
//        if (array.length > 0) {
//            textComponentList.add(new TextComponent(array[0]));
//        }
//        textComponentList.add(item);
//        if (array.length > 1) {
//            textComponentList.add(new TextComponent(array[1]));
//        }
//        return textComponentList;
//    }

    public List<TextComponent> getItemHoverText(Player player, TextComponent... components) {
        ArrayList<TextComponent> newComponents = new ArrayList<>();

        ItemStack heldItem = player.getInventory().getItemInMainHand();
        if (heldItem.getType() == Material.AIR || heldItem.getItemMeta() == null) return List.of(components);
        String itemName = ChatColor.GREEN + "[" + heldItem.getItemMeta().getDisplayName() + ChatColor.GREEN + "]";

        for (BaseComponent component : components) {
            String text = component.toPlainText();
            String[] parts = text.split("\\[item]", -1); // Split on "[item]", preserving trailing empty strings
            for (int i = 0; i < parts.length; i++) {
                // Add the text before (or between) the "[item]"
                newComponents.add(new TextComponent(parts[i]));
                // If this isn't the last part, it means there was an "[item]" after it
                if (i < parts.length - 1) {
                    TextComponent itemTextComponent = new TextComponent(itemName);
                    itemTextComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, new BaseComponent[]{
                            new TextComponent(convertItemStackToJson(heldItem))
                    }));
                    newComponents.add(itemTextComponent);
                }
            }
        }
        return newComponents;
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

    @Override
    public ProfanityFilter getProfanityFilter() {
        return this.filter;
    }

    @Override
    public Set<UUID> getSpyingPlayers() {
        return spyChannels.keySet();
    }

    @Override
    public Map<ChatChannel, Boolean> getSpySettings(Player player) {
        Map<ChatChannel, Boolean> settings = new HashMap<>();
        getChatChannels().forEach((channel) -> settings.put(channel, false));
        Set<ChatChannel> channelsEnabled = spyChannels.get(player.getUniqueId());
        if (channelsEnabled != null) {
            channelsEnabled.forEach((channel) -> settings.put(channel, true));
        }
        return settings;
    }

    @Override
    public void setSpy(Player player, ChatChannel channel, boolean enabled) {
        if (!spyChannels.containsKey(player.getUniqueId())) spyChannels.put(player.getUniqueId(), new HashSet<>());
        if (enabled) {
            spyChannels.get(player.getUniqueId()).add(channel);
            RunicCommon.getLuckPermsAPI().savePayload(RunicCommon.getLuckPermsAPI().createPayload(player.getUniqueId(), (data) -> data.set("runic.chat-spy." + channel.getName(), true)));
        } else {
            spyChannels.get(player.getUniqueId()).remove(channel);
            RunicCommon.getLuckPermsAPI().savePayload(RunicCommon.getLuckPermsAPI().createPayload(player.getUniqueId(), (data) -> data.set("runic.chat-spy." + channel.getName(), false)));
        }
    }

    @Override
    public boolean isSpyingOnChannel(Player player, ChatChannel channel) {
        return spyChannels.containsKey(player.getUniqueId()) && spyChannels.get(player.getUniqueId()).contains(channel);
    }

    @Override
    public boolean isChannelMuted(Player player, ChatChannel channel) {
        return !channelsEnabled.get(player.getUniqueId()).get(channel);
    }

    @Override
    public void setChannelMuted(Player player, ChatChannel channel, boolean muted) {
        channelsEnabled.get(player.getUniqueId()).put(channel, !muted);
        RunicCommon.getLuckPermsAPI().savePayload(RunicCommon.getLuckPermsAPI().createPayload(player.getUniqueId(), data -> {
            data.set("runic.channel." + channel.getName(), !muted);
        }));
    }

    @Override
    public void sendMessage(Player sender, String message) {
        ChatChannel channel = RunicChat.getRunicChatAPI().getPlayerChatChannel(sender);
        sendMessage(sender, channel, message);
    }

    @Override
    public void sendMessage(Player sender, ChatChannel channel, String message) {
        Collection<Player> recipients = channel.getRecipients(sender);
        ChatChannelMessageEvent chatChannelMessageEvent = new ChatChannelMessageEvent(sender, channel, recipients, message);
        if (Bukkit.isPrimaryThread()) {
            Bukkit.getScheduler().runTaskAsynchronously(RunicChat.getInstance(), () -> Bukkit.getPluginManager().callEvent(chatChannelMessageEvent));
        } else {
            Bukkit.getPluginManager().callEvent(chatChannelMessageEvent);
        }
    }

    @Override
    public @Nullable Player getLastWhisperTarget(Player sender) {
        UUID target = whisperTargets.get(sender.getUniqueId());
        if (target == null) return null;
        return Bukkit.getPlayer(target);
    }

    @Override
    public void setWhisperTarget(Player sender, Player target) {
        whisperTargets.put(sender.getUniqueId(), target.getUniqueId());
    }

    @Override
    public void sendWhisper(Player sender, Player target, String message) {
        setWhisperTarget(sender, target);

        List<TextComponent> parsed = parseMessage(sender, new TextComponent(message));

        List<TextComponent> components = new ArrayList<>();
        components.add(new TextComponent(ColorUtil.format("&9&oYou whisper to &r&o" + target.getName() + "&9&o: &7&o")));
        components.addAll(parsed);
        sender.sendMessage(components.toArray(new TextComponent[0]));

        components.clear();
        components.add(new TextComponent(ColorUtil.format(
                PlaceholderAPI.setPlaceholders(sender, "%core_name_color%") + "&o" + sender.getName() +
                        "&9&o whispers to you: &f")));
        components.addAll(parsed);

        target.sendMessage(components.toArray(new TextComponent[0]));

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getUniqueId().equals(sender.getUniqueId()) || player.getUniqueId().equals(target.getUniqueId()))
                continue;
            if (player.hasPermission("runicchat.spy")) {
                components.clear();
                components.add(new TextComponent(ColorUtil.format(
                        "&cSpy: " + PlaceholderAPI.setPlaceholders(sender, "%core_name_color%") + "&o" + sender.getName() +
                                " &f&o-> " + PlaceholderAPI.setPlaceholders(target, "%core_name_color%") + "&o" + target.getName() + " &f") + message));
                components.addAll(parsed);
                player.sendMessage(components.toArray(new TextComponent[0]));
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        RunicCommon.getLuckPermsAPI().retrieveData(event.getPlayer().getUniqueId()).thenAcceptAsync(data -> {
            if (event.getPlayer().hasPermission("runicchat.spy")) {
                spyChannels.put(event.getPlayer().getUniqueId(), new HashSet<>());
                for (ChatChannel channel : getChatChannels()) {
                    if (data.containsKey("runic.chat-spy." + channel.getName()) && data.getBoolean("runic.chat-spy." + channel.getName())) {
                        spyChannels.get(event.getPlayer().getUniqueId()).add(channel);
                    }
                }
            }
            channelsEnabled.put(event.getPlayer().getUniqueId(), new HashMap<>());
            for (ChatChannel channel : getChatChannels()) {
                if (data.containsKey("runic.channel." + channel.getName())) {
                    channelsEnabled.get(event.getPlayer().getUniqueId()).put(channel, data.getBoolean("runic.channel." + channel.getName()));
                } else {
                    channelsEnabled.get(event.getPlayer().getUniqueId()).put(channel, true);
                }
            }
        });
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        spyChannels.remove(event.getPlayer().getUniqueId());
        channelsEnabled.remove(event.getPlayer().getUniqueId());
        whisperTargets.remove(event.getPlayer().getUniqueId());
    }

}
