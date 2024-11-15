package io.github.guy7cc.seikatsu.gui.sidebar;

import io.github.guy7cc.seikatsu.SeikatsuPlugin;
import io.github.guy7cc.seikatsu.player.OfflinePlayerStatus;
import org.bukkit.entity.Player;

public class LevelComponent extends SidebarComponent{
    private Player player;
    private OfflinePlayerStatus status;
    private int lastLevel = -1;

    public LevelComponent(Player player){
        super("level");
        this.player = player;
    }

    @Override
    public void tick(int globalTick) {
        if(status == null) status = SeikatsuPlugin.offlinePlayerStatus.get(player);
        else {
            if(status.getLevel() != lastLevel) setText("生活Lv." + status.getLevel());
            lastLevel = status.getLevel();
        }
    }
}
