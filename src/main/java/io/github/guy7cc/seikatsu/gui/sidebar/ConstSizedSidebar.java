package io.github.guy7cc.seikatsu.gui.sidebar;

import io.github.guy7cc.seikatsu.SeikatsuPlugin;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ConstSizedSidebar implements Sidebar {
    private final Player player;
    private final List<SidebarComponent> componentList = new ArrayList<>();

    private final int size;
    private Scoreboard scoreboard;
    private Objective objective;
    private Team[] teams;

    public ConstSizedSidebar(Player player, int size) {
        this.player = player;
        this.size = size;
        teams = new Team[size];
        if (SeikatsuPlugin.scoreboardManager != null) {
            setScoreboard(SeikatsuPlugin.scoreboardManager);
        }
    }

    @Override
    public void setScoreboard(ScoreboardManager manager) {
        if (scoreboard == null && manager != null) {
            scoreboard = manager.getNewScoreboard();
            player.setScoreboard(scoreboard);
            String objectiveName = SeikatsuPlugin.SHORT_NAME + "_sidebar_" + player.getName();
            objective = scoreboard.getObjective(objectiveName);
            if (objective != null) objective.unregister();
            objective = scoreboard.registerNewObjective(objectiveName, Criteria.DUMMY, "INFO");
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
            for (int i = 0; i < size; i++) {
                teams[i] = scoreboard.registerNewTeam("Team" + i);
            }
            refreshAll();
        }
    }

    @Override
    public void tick(int globalTick) {
        if (scoreboard == null) return;
        for (SidebarComponent component : componentList) {
            component.tick(globalTick);
        }
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public void addComponent(SidebarComponent component) {
        componentList.add(component);
        component.registerSidebar(this, componentList.size() - 1);
        refreshText(componentList.size() - 1, component.getText());
    }

    @Override
    public void addComponent(Collection<SidebarComponent> collection) {
        for (SidebarComponent component : collection) {
            addComponent(component);
        }
    }

    @Override
    public void setComponent(int index, SidebarComponent component) {
        componentList.set(index, component);
        refreshAll();
    }

    @Override
    public void removeComponent(SidebarComponent component) {
        componentList.remove(component);
        refreshAll();
    }

    @Override
    public void removeComponent(String tag) {
        componentList.removeIf(component -> component.tag.equals(tag));
        refreshAll();
    }

    @Override
    public void refreshText(int index, String text) {
        if (scoreboard == null || index < 0 || index >= teams.length) return;
        for (String entry : teams[index].getEntries()) {
            teams[index].removeEntry(entry);
            scoreboard.resetScores(entry);
        }
        String entry = "\u00A7" + (char) ('a' + index) + ChatColor.RESET + text;
        teams[index].addEntry(entry);
        objective.getScore(entry).setScore(0);
    }

    @Override
    public void setVisible(boolean value) {
        objective.setDisplaySlot(value ? DisplaySlot.SIDEBAR : null);
    }

    private void refreshAll() {
        for (int i = 0; i < size; i++) {
            if (componentList.size() > i) {
                componentList.get(i).registerSidebar(this, i);
                refreshText(i, componentList.get(i).getText());
            } else {
                refreshText(i, "");
            }
        }
    }

    @Override
    public void dispose() {
        for (Team team : teams) {
            for (String entry : team.getEntries()) {
                scoreboard.resetScores(entry);
                team.removeEntry(entry);
            }
            team.unregister();
        }
        scoreboard.clearSlot(DisplaySlot.SIDEBAR);
        objective.unregister();

        scoreboard = null;
        objective = null;
        Arrays.fill(teams, null);
    }
}
