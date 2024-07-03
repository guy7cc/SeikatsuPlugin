package io.github.guy7cc.seikatsu.gui;

import io.github.guy7cc.seikatsu.SeikatsuPlugin;
import io.github.guy7cc.seikatsu.system.OfflinePlayerStatus;
import io.github.guy7cc.seikatsu.system.Tickable;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.ScoreboardManager;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class PlayerGuiManager implements Tickable {
    private final Map<Player, PlayerGui> map = new HashMap<>();

    public PlayerGuiManager() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            map.put(player, new PlayerGui(player));
        }
    }

    @Override
    public void tick(int globalTick) {
        for (PlayerGui gui : map.values()) {
            gui.tick(globalTick);
        }
    }

    public void onPlayerJoin(Player player) {
        OfflinePlayerStatus status = SeikatsuPlugin.offlinePlayerStatus.get(player);
        PlayerGui gui = new PlayerGui(player);
        map.put(player, gui);
        gui.sidebar.setVisible(status.getDisplayInfo());
        gui.bossbar.setVisible(status.getDisplayInfo());
    }

    public void onPlayerQuit(Player player) {
        if (map.containsKey(player)) {
            PlayerGui gui = map.get(player);
            gui.dispose();
            map.remove(player);
        }
    }

    public PlayerGui get(Player player) {
        return map.get(player);
    }

    public void forEach(Consumer<? super PlayerGui> consumer) {
        map.values().forEach(consumer);
    }

    public void setScoreboard(ScoreboardManager manager) {
        for(Map.Entry<Player, PlayerGui> entry : map.entrySet()){
            OfflinePlayerStatus status = SeikatsuPlugin.offlinePlayerStatus.get(entry.getKey());
            PlayerGui gui = entry.getValue();
            gui.sidebar.setScoreboard(manager);
            gui.sidebar.setVisible(status.getDisplayInfo());
            gui.bossbar.setVisible(status.getDisplayInfo());
        }
    }

    public void dispose() {
        for (PlayerGui gui : map.values()) {
            gui.dispose();
        }
    }

    public void reset() {
        dispose();
        for (Player player : Bukkit.getOnlinePlayers()) {
            onPlayerJoin(player);
        }
    }
}
