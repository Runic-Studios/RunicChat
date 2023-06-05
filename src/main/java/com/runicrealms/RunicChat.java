package com.runicrealms;

import co.aikar.commands.PaperCommandManager;
import com.runicrealms.api.RunicChatAPI;
import com.runicrealms.channels.Global;
import com.runicrealms.channels.Local;
import com.runicrealms.channels.Trade;
import com.runicrealms.commands.Channel;
import com.runicrealms.commands.Mute;
import com.runicrealms.commands.Unmute;
import com.runicrealms.commands.Whisper;
import com.runicrealms.listener.PlayerMessageListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by KissOfFate
 * Date: 4/20/2020
 * Time: 8:27 PM
 */
public class RunicChat extends JavaPlugin {
    private static RunicChatAPI runicChatAPI;
    private static PaperCommandManager commandManager;
    private static Set<String> wordsToFilter;

    public static Set<String> getWordsToFilter() {
        return wordsToFilter;
    }

    public static RunicChatAPI getRunicChatAPI() {
        return runicChatAPI;
    }

    public static PaperCommandManager getCommandManager() {
        return commandManager;
    }

    @Override
    public void onEnable() {
        runicChatAPI = new ChatManager();
        commandManager = new PaperCommandManager(this);

        commandManager.registerDependency(RunicChatAPI.class, runicChatAPI);

        // Register Chat Channels
        runicChatAPI.registerChatChannel(new Global());
        runicChatAPI.registerChatChannel(new Local());
        runicChatAPI.registerChatChannel(new Trade());

        // Register Commands
        commandManager.registerCommand(new Channel());
        commandManager.registerCommand(new Whisper());
        commandManager.registerCommand(new Mute());
        commandManager.registerCommand(new Unmute());

        // Register Listeners
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerMessageListener(), this);

        // Chat filter
        Yaml yaml = new Yaml();
        File filePath = new File(this.getDataFolder(), "words-to-filter.yml");
        try (FileInputStream input = new FileInputStream(filePath)) {
            List<String> data = yaml.load(input);
            wordsToFilter = new HashSet<>(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
