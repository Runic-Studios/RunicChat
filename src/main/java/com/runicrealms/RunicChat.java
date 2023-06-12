package com.runicrealms;

import co.aikar.commands.PaperCommandManager;
import com.runicrealms.api.RunicChatAPI;
import com.runicrealms.channels.GlobalChannel;
import com.runicrealms.channels.LocalChannel;
import com.runicrealms.channels.StaffChannel;
import com.runicrealms.channels.TradeChannel;
import com.runicrealms.commands.ChannelCommand;
import com.runicrealms.commands.MuteCommand;
import com.runicrealms.commands.ReplyCommand;
import com.runicrealms.commands.SpyCommand;
import com.runicrealms.commands.UnmuteCommand;
import com.runicrealms.commands.WhisperCommand;
import com.runicrealms.commands.channels.GlobalChannelCommand;
import com.runicrealms.commands.channels.LocalChannelCommand;
import com.runicrealms.commands.channels.StaffChannelCommand;
import com.runicrealms.commands.channels.TradeChannelCommand;
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
    private static RunicChat instance;

    public static Set<String> getWordsToFilter() {
        return wordsToFilter;
    }

    public static RunicChatAPI getRunicChatAPI() {
        return runicChatAPI;
    }

    public static PaperCommandManager getCommandManager() {
        return commandManager;
    }

    public static RunicChat getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        // Chat filter
        Yaml yaml = new Yaml();
        File filePath = new File(this.getDataFolder(), "words-to-filter.yml");
        try (FileInputStream input = new FileInputStream(filePath)) {
            List<String> data = yaml.load(input);
            wordsToFilter = new HashSet<>(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ChatManager chatManager = new ChatManager();

        runicChatAPI = chatManager;
        commandManager = new PaperCommandManager(this);

        commandManager.registerDependency(RunicChatAPI.class, runicChatAPI);

        GlobalChannel globalChannel = new GlobalChannel();
        LocalChannel localChannel = new LocalChannel();
        StaffChannel staffChannel = new StaffChannel();
        TradeChannel tradeChannel = new TradeChannel();

        // Register Chat Channels
        runicChatAPI.registerChatChannel(globalChannel);
        runicChatAPI.registerChatChannel(localChannel);
        runicChatAPI.registerChatChannel(staffChannel);
        runicChatAPI.registerChatChannel(tradeChannel);

        // Register Commands
        commandManager.registerCommand(new SpyCommand());
        commandManager.registerCommand(new ChannelCommand());
        commandManager.registerCommand(new WhisperCommand());
        commandManager.registerCommand(new MuteCommand());
        commandManager.registerCommand(new UnmuteCommand());
        commandManager.registerCommand(new ReplyCommand());

        commandManager.registerCommand(new GlobalChannelCommand(globalChannel));
        commandManager.registerCommand(new LocalChannelCommand(localChannel));
        commandManager.registerCommand(new TradeChannelCommand(tradeChannel));
        commandManager.registerCommand(new StaffChannelCommand(staffChannel));

        // Register Listeners
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerMessageListener(), this);
        Bukkit.getServer().getPluginManager().registerEvents(chatManager, this);
    }

}
