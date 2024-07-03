package io.github.guy7cc.seikatsu.event;

import io.github.guy7cc.seikatsu.SeikatsuPlugin;
import io.github.guy7cc.seikatsu.gui.PlayerGuiManager;
import io.github.guy7cc.seikatsu.system.OfflinePlayerStatus;
import io.github.guy7cc.seikatsu.system.OfflinePlayerStatusManager;
import io.github.guy7cc.seikatsu.system.OnlinePlayerStatus;
import io.github.guy7cc.seikatsu.system.OnlinePlayerStatusManager;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

public class PlayerEventHandler implements Listener {
    private final OnlinePlayerStatusManager onlinePlayerStatus;
    private final OfflinePlayerStatusManager offlinePlayerStatus;
    private final PlayerGuiManager playerGui;

    public PlayerEventHandler(OnlinePlayerStatusManager onlinePlayerStatus, OfflinePlayerStatusManager offlinePlayerStatus, PlayerGuiManager playerGui) {
        this.onlinePlayerStatus = onlinePlayerStatus;
        this.offlinePlayerStatus = offlinePlayerStatus;
        this.playerGui = playerGui;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        onlinePlayerStatus.onPlayerJoin(event.getPlayer());
        offlinePlayerStatus.onPlayerJoin(event.getPlayer());
        playerGui.onPlayerJoin(event.getPlayer());
        TextComponent message = new TextComponent(
                "§9゜+.――゜+.――゜§f73回生生活鯖へようこそ！§9゜+.――゜+.――゜\n" +
                        "§f以下のプラグインコマンドを利用することができます！\n" +
                        "§b/sit §fその場に座ります。\n" +
                        "§b/hat §f利き手に持っているアイテムを頭に乗せます。\n" +
                        "§b/info §fGUIの表示・非表示を切り替えます。\n" +
                        "§b/addmsg §f画面上に表示するメッセージを追加します。\n" +
                        "§b/removemsg §f現在画面上に表示されているメッセージだけを削除します。\n" +
                        "§9゜+.――゜゜+.――゜゜+.――゜゜+.――゜゜+.――゜゜+.――゜゜+.――゜"
        );
        event.getPlayer().spigot().sendMessage(message);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        onlinePlayerStatus.onPlayerQuit(event.getPlayer());
        playerGui.onPlayerQuit(event.getPlayer());
    }

    @EventHandler
    public void onPlayerFish(PlayerFishEvent event){
        OfflinePlayerStatus status = SeikatsuPlugin.offlinePlayerStatus.get(event.getPlayer());
        if(status == null) return;
        status.addXp(1);
    }

    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent event){
        OnlinePlayerStatus status = onlinePlayerStatus.get(event.getPlayer());
        if(status == null) return;
        status.onItemConsume();
    }

    @EventHandler
    public void onPlayerExpChange(PlayerExpChangeEvent event){
        OnlinePlayerStatus status = onlinePlayerStatus.get(event.getPlayer());
        if(status == null) return;
        status.onExpChange(event.getAmount());
    }
}
