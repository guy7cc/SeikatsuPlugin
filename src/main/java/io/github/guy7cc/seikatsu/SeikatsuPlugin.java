package io.github.guy7cc.seikatsu;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.guy7cc.seikatsu.command.*;
import io.github.guy7cc.seikatsu.event.*;
import io.github.guy7cc.seikatsu.gui.PlayerGuiManager;
import io.github.guy7cc.seikatsu.gui.SelectInventoryManager;
import io.github.guy7cc.seikatsu.io.GsonWrapper;
import io.github.guy7cc.seikatsu.item.CooldownManager;
import io.github.guy7cc.seikatsu.item.PlayerItemTicker;
import io.github.guy7cc.seikatsu.player.OfflinePlayerStatusManager;
import io.github.guy7cc.seikatsu.player.OnlinePlayerStatusManager;
import io.github.guy7cc.seikatsu.system.*;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.ScoreboardManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public final class SeikatsuPlugin extends JavaPlugin {
    public static final boolean ON_RELEASE = false;

    public static final String PLUGIN_NAME = "SeikatsuPlugin";
    public static final String SHORT_NAME = "seikatsu";
    public static final String NAMESPACE = "seikatsu";

    public static SeikatsuPlugin plugin;

    public static ScoreboardManager scoreboardManager;

    public static ProfiledTimer timer;
    public static GeneralTicker ticker;
    public static OnlinePlayerStatusManager onlinePlayerStatus;
    public static OfflinePlayerStatusManager offlinePlayerStatus;
    public static PlayerItemTicker playerItem;
    public static CooldownManager cooldown;
    public static PlayerGuiManager playerGui;
    public static SelectInventoryManager selectInventory;
    public static SeatTicker seat;
    public static MessageBarManager messageBar;
    public static CoordManager coord;
    public static List<String> joinMessage;
    public static Pattern forbiddenCommands;

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
        cooldown = new CooldownManager();
        playerItem = new PlayerItemTicker();
        playerGui = new PlayerGuiManager();
        selectInventory = new SelectInventoryManager();
        seat = new SeatTicker();
        messageBar = new MessageBarManager();
        coord = new CoordManager();
        joinMessage = new ArrayList<>();
        ticker = new GeneralTicker(timer, onlinePlayerStatus, playerItem, playerGui, seat, messageBar, cooldown);

        setupCommand("seikatsu", new SeikatsuCommand());
        setupCommand("sit", new SitCommand());
        setupCommand("hat", new HatCommand());
        setupCommand("info", new InfoCommand());
        setupCommand("addmsg", new AddMsgCommand());
        setupCommand("removemsg", new RemoveMsgCommand());
        setupCommand("coord", new CoordCommand());
        setupCommand("dealer", new DealerCommand());

        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new PlayerEventHandler(onlinePlayerStatus, offlinePlayerStatus, cooldown, playerGui), this);
        pluginManager.registerEvents(new InventoryEventHandler(), this);
        pluginManager.registerEvents(new BlockEventHandler(), this);
        pluginManager.registerEvents(new EntityEventHandler(), this);
        pluginManager.registerEvents(new ServerEventHandler(), this);

        JsonElement element = gson.load("seikatsu.json");
        if(element != null && element.isJsonObject()){
            JsonObject object = element.getAsJsonObject();

            JsonElement seats = object.get("seats");
            if(seats != null && seats.isJsonArray()) seat.fromJsonArray(seats.getAsJsonArray());

            JsonElement players = object.get("players");
            if(players != null && players.isJsonArray()) offlinePlayerStatus.fromJsonArray(players.getAsJsonArray());

            JsonElement messages = object.get("messages");
            if(messages != null && messages.isJsonArray()) messageBar.fromJsonArray(messages.getAsJsonArray());

            JsonElement coords = object.get("coords");
            if(coords != null && coords.isJsonArray()) coord.fromJsonArray(coords.getAsJsonArray());
        }

        JsonElement element2 = gson.load("settings.json");
        if(element2 != null && element2.isJsonObject()){
            JsonObject object = element2.getAsJsonObject();

            JsonElement joinMessage = object.get("joinMessage");
            if(joinMessage != null && joinMessage.isJsonArray()){
                for(JsonElement element3 : joinMessage.getAsJsonArray()){
                    if(element3.isJsonPrimitive()) SeikatsuPlugin.joinMessage.add(element3.getAsString());
                }
            }

            JsonElement forbiddenCommands = object.get("forbiddenCommands");
            if(forbiddenCommands != null && forbiddenCommands.isJsonPrimitive()) {
                try{
                    SeikatsuPlugin.forbiddenCommands = Pattern.compile(forbiddenCommands.getAsString());
                } catch(PatternSyntaxException exception){
                    SeikatsuPlugin.forbiddenCommands = Pattern.compile("(?!)");
                }
            } else {
                SeikatsuPlugin.forbiddenCommands = Pattern.compile("(?!)");
            }
        } else {
            JsonObject object = new JsonObject();
            joinMessage.add("§9゜+.――゜+.――゜§fようこそ！§9゜+.――゜+.――゜");
            joinMessage.add("§f以下のプラグインコマンドを利用することができます！");
            joinMessage.add("§b/sit §fその場に座ります。");
            joinMessage.add("§b/hat §f利き手に持っているアイテムを頭に乗せます。");
            joinMessage.add("§b/info §fGUIの表示・非表示を切り替えます。");
            joinMessage.add("§b/addmsg §f画面上に表示するメッセージを追加します。");
            joinMessage.add("§b/removemsg §f現在画面上に表示されているメッセージだけを削除します。");
            joinMessage.add("§b/coord §f座標ラベルを追加、表示・非表示、消去します。");
            joinMessage.add("§9゜+.――゜゜+.――゜゜+.――゜゜+.――゜゜+.――゜゜+.――゜゜+.――゜");
            JsonArray array = new JsonArray();
            for(String str : joinMessage){
                array.add(str);
            }
            object.add("joinMessage", array);
            object.addProperty("forbiddenCommands", "(?!)");
            gson.save(object, "settings.json");
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
        object.add("coords", coord.toJsonArray());
        gson.save(object, "seikatsu.json");
    }

    public <T extends CommandExecutor & TabCompleter> void setupCommand(String name, T executor) {
        PluginCommand command = getCommand(name);
        if (command == null) return;
        command.setExecutor(executor);
        command.setTabCompleter(executor);
    }
}
