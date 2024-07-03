package io.github.guy7cc.seikatsu.gui.sidebar;

import io.github.guy7cc.seikatsu.SeikatsuPlugin;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class VariableSizedSidebar implements Sidebar {
    private static final int MAX_SIZE = 15;

    private final Player player;
    private final List<SidebarComponent> componentList = new ArrayList<>();

    private Scoreboard scoreboard;
    private Objective objective;
    private List<Team> teams;

    public VariableSizedSidebar(Player player) {
        this.player = player;
        teams = new ArrayList<>();
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
        resize();
        component.registerSidebar(this, componentList.size() - 1);
        refreshText(componentList.size() - 1, component.getText());
    }

    @Override
    public void addComponent(Collection<SidebarComponent> collection) {
        componentList.addAll(collection);
        refreshAll();
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
        if (scoreboard == null || index < 0 || index >= componentList.size() || index >= teams.size()) return;
        for (String entry : teams.get(index).getEntries()) {
            teams.get(index).removeEntry(entry);
            scoreboard.resetScores(entry);
        }
        String entry = "\u00A7" + (char) ('a' + index) + ChatColor.RESET + text;
        teams.get(index).addEntry(entry);
        objective.getScore(entry).setScore(0);
    }

    @Override
    public void setVisible(boolean value) {
        objective.setDisplaySlot(value ? DisplaySlot.SIDEBAR : null);
    }

    private void resize() {
        if (scoreboard == null) return;
        if (componentList.size() > teams.size()) {
            for (int i = teams.size(); i < componentList.size(); i++) {
                teams.add(scoreboard.registerNewTeam("Team" + i));
            }
        } else if (componentList.size() < teams.size()) {
            for (int i = teams.size() - 1; i >= componentList.size(); i--) {
                for (String entry : teams.get(i).getEntries()) {
                    scoreboard.resetScores(entry);
                    teams.get(i).removeEntry(entry);
                }
                teams.get(i).unregister();
                teams.remove(i);
            }
        }
    }

    public void refreshAll() {
        resize();
        for (int i = 0; i < componentList.size() && i < MAX_SIZE; i++) {
            componentList.get(i).registerSidebar(this, i);
            refreshText(i, componentList.get(i).getText());
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
        teams.clear();
    }
}
