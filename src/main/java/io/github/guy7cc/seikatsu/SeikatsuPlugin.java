package io.github.guy7cc.seikatsu;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.guy7cc.seikatsu.command.*;
import io.github.guy7cc.seikatsu.event.BlockEventHandler;
import io.github.guy7cc.seikatsu.event.EntityEventHandler;
import io.github.guy7cc.seikatsu.event.PlayerEventHandler;
import io.github.guy7cc.seikatsu.gui.PlayerGuiManager;
import io.github.guy7cc.seikatsu.io.GsonWrapper;
import io.github.guy7cc.seikatsu.system.*;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.ScoreboardManager;

public final class SeikatsuPlugin extends JavaPlugin {
    public static final boolean ON_RELEASE = false;

    public static final String PLUGIN_NAME = "SeikatsuPlugin";
    public static final String SHORT_NAME = "seikatsu";

    public static SeikatsuPlugin plugin;

    public static ScoreboardManager scoreboardManager;

    public static ProfiledTimer timer;
    public static GeneralTicker ticker;
    public static OnlinePlayerStatusManager onlinePlayerStatus;
    public static OfflinePlayerStatusManager offlinePlayerStatus;
    public static PlayerGuiManager playerGui;
    public static SeatTicker seat;
    public static MessageBarManager messageBar;

    public static GsonWrapper gson;

    public static LoggerWrapper log;

    @Override
    public void onEnable() {
        plugin = this;

        gson = new GsonWrapper();
        log = new LoggerWrapper(getLogger());

        timer = new ProfiledTimer();
        onlinePlayerStatus = new OnlinePlayerStatusManager();
        offlinePlayerStatus = new OfflinePlayerStatusManager();
        playerGui = new PlayerGuiManager();
        seat = new SeatTicker();
        messageBar = new MessageBarManager();
        ticker = new GeneralTicker(timer, onlinePlayerStatus, playerGui, seat, messageBar);

        setupCommand("sit", new SitCommand());
        setupCommand("hat", new HatCommand());
        setupCommand("info", new InfoCommand());
        setupCommand("addmsg", new AddMsgCommand());
        setupCommand("removemsg", new RemoveMsgCommand());

        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new PlayerEventHandler(onlinePlayerStatus, offlinePlayerStatus, playerGui), this);
        pluginManager.registerEvents(new BlockEventHandler(), this);
        pluginManager.registerEvents(new EntityEventHandler(), this);

        JsonElement element = gson.load("seikatsu.json");
        if(element != null && element.isJsonObject()){
            JsonObject object = element.getAsJsonObject();

            JsonElement seats = object.get("seats");
            if(seats != null && seats.isJsonArray()) seat.fromJsonArray(seats.getAsJsonArray());

            JsonElement players = object.get("players");
            if(players != null && players.isJsonArray()) offlinePlayerStatus.fromJsonArray(players.getAsJsonArray());

            JsonElement messages = object.get("messages");
            if(messages != null && messages.isJsonArray()) messageBar.fromJsonArray(messages.getAsJsonArray());
        }

        Bukkit.getScheduler().runTask(this, () -> {
            // Runs on the first server tick
            scoreboardManager = Bukkit.getScoreboardManager();
            playerGui.setScoreboard(scoreboardManager);
        });
        ticker.runTaskTimer(plugin, 0L, 1L);
    }

    @Override
    public void onDisable() {
        playerGui.dispose();

        JsonObject object = new JsonObject();
        object.add("seats", seat.toJsonArray());
        object.add("players", offlinePlayerStatus.toJsonArray());
        object.add("messages", messageBar.toJsonArray());
        gson.save(object, "seikatsu.json");
    }

    public <T extends CommandExecutor & TabCompleter> void setupCommand(String name, T executor) {
        PluginCommand command = getCommand(name);
        if (command == null) return;
        command.setExecutor(executor);
        command.setTabCompleter(executor);
    }
}
