package io.github.guy7cc.seikatsu.event;

import io.github.guy7cc.seikatsu.SeikatsuPlugin;
import io.github.guy7cc.seikatsu.system.OnlinePlayerStatus;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockEventHandler implements Listener {
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event){
        OnlinePlayerStatus status = SeikatsuPlugin.onlinePlayerStatus.get(event.getPlayer());
        if(status == null) return;
        status.onBlockBreak();
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event){
        OnlinePlayerStatus status = SeikatsuPlugin.onlinePlayerStatus.get(event.getPlayer());
        if(status == null) return;
        status.onBlockPlace();
    }
}
