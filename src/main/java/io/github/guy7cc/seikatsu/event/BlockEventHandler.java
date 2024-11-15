package io.github.guy7cc.seikatsu.event;

import io.github.guy7cc.seikatsu.SeikatsuPlugin;
import io.github.guy7cc.seikatsu.player.OnlinePlayerStatus;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockEventHandler implements Listener {
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event){
        SeikatsuPlugin.onlinePlayerStatus.computeIfPresent(event.getPlayer(), OnlinePlayerStatus::onBlockBreak);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event){
        SeikatsuPlugin.onlinePlayerStatus.computeIfPresent(event.getPlayer(), OnlinePlayerStatus::onBlockPlace);
    }
}
