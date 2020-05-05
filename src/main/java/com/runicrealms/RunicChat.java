package com.runicrealms;

import com.runicrealms.api.RunicChatAPI;
import com.runicrealms.channels.Local;
import com.runicrealms.listener.PlayerMessageListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by KissOfFate
 * Date: 4/20/2020
 * Time: 8:27 PM
 */
public class RunicChat extends JavaPlugin {

    private static RunicChatAPI runicChatAPI;

    @Override
    public void onEnable() {
        runicChatAPI = new ChatManager();

        // Register Chat Channels
        runicChatAPI.registerChatChannel(new Local());

        // Register Listeners
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerMessageListener(), this);
    }

    public static RunicChatAPI getRunicChatAPI() { return runicChatAPI; }

}
