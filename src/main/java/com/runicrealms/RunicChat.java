package com.runicrealms;

import co.aikar.commands.PaperCommandManager;
import com.runicrealms.api.RunicChatAPI;
import com.runicrealms.channels.Global;
import com.runicrealms.channels.Local;
import com.runicrealms.commands.Channel;
import com.runicrealms.commands.Whisper;
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
    private static PaperCommandManager commandManager;

    @Override
    public void onEnable() {
        runicChatAPI = new ChatManager();
        commandManager = new PaperCommandManager(this);

        commandManager.registerDependency(RunicChatAPI.class, runicChatAPI);

        // Register Chat Channels
        runicChatAPI.registerChatChannel(new Global());
        runicChatAPI.registerChatChannel(new Local());

        // Register Commands
        commandManager.registerCommand(new Channel());
        commandManager.registerCommand(new Whisper());

        // Register Listeners
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerMessageListener(), this);
    }

    public static RunicChatAPI getRunicChatAPI() {
        return runicChatAPI;
    }

    public static PaperCommandManager getCommandManager() {
        return commandManager;
    }

}
