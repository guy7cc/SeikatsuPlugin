package io.github.guy7cc.seikatsu.gui;

import io.github.guy7cc.seikatsu.gui.sidebar.*;
import io.github.guy7cc.seikatsu.system.Tickable;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

public class PlayerGui implements Tickable {
    public final Sidebar sidebar;
    public final BossBar bossbar;

    public PlayerGui(Player player) {
        sidebar = new VariableSizedSidebar(player);
        sidebar.addComponent(new LevelComponent(player));
        sidebar.addComponent(new XpComponent(player));
        sidebar.addComponent(new RankComponent(player));
        bossbar = Bukkit.createBossBar("", BarColor.GREEN, BarStyle.SOLID);
        bossbar.addPlayer(player);
    }

    @Override
    public void tick(int globalTick) {
        sidebar.tick(globalTick);
    }

    public void dispose() {
        sidebar.dispose();
        bossbar.removeAll();
    }
}
