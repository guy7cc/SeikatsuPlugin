package io.github.guy7cc.seikatsu.gui.sidebar;

import io.github.guy7cc.seikatsu.SeikatsuPlugin;
import io.github.guy7cc.seikatsu.system.OfflinePlayerStatus;
import org.bukkit.entity.Player;

public class XpComponent extends SidebarComponent{
    private Player player;
    private OfflinePlayerStatus status;
    private int lastXp = -1;

    public XpComponent(Player player){
        super("xp");
        this.player = player;
    }

    @Override
    public void tick(int globalTick) {
        if(status == null) status = SeikatsuPlugin.offlinePlayerStatus.get(player);
        else {
            if(status.getXp() != lastXp) setText("生活力:" + status.getXp() + "/" + OfflinePlayerStatus.getMaxXp(status.getLevel()));
            lastXp = status.getXp();
        }
    }
}
