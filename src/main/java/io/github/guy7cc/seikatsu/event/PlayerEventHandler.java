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
import org.bukkit.event.enchantment.EnchantItemEvent;
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
        for(String str : SeikatsuPlugin.joinMessage){
            event.getPlayer().sendMessage(str);
        }
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

    @EventHandler
    public void onPlayerEnchant(EnchantItemEvent event){
        OfflinePlayerStatus status = SeikatsuPlugin.offlinePlayerStatus.get(event.getEnchanter());
        if(status == null) return;
        status.addXp(1);
    }

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event){
        if(SeikatsuPlugin.forbiddenCommands.matcher(event.getMessage()).find()){
            event.setCancelled(true);
            event.getPlayer().sendMessage("§cそのコマンドは無効化されています。");
        }
    }
}
