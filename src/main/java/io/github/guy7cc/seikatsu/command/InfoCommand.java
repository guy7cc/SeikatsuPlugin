package io.github.guy7cc.seikatsu.command;

import io.github.guy7cc.seikatsu.SeikatsuPlugin;
import io.github.guy7cc.seikatsu.gui.PlayerGui;
import io.github.guy7cc.seikatsu.system.OfflinePlayerStatus;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class InfoCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, Command command, @NotNull String label, String[] args) {if (command.getName().equalsIgnoreCase("info")) {
            if (sender instanceof Player player) {
                OfflinePlayerStatus status = SeikatsuPlugin.offlinePlayerStatus.get(player);
                PlayerGui gui = SeikatsuPlugin.playerGui.get(player);
                boolean displayInfo = !status.getDisplayInfo();
                status.setDisplayInfo(displayInfo);
                gui.sidebar.setVisible(displayInfo);
                gui.bossbar.setVisible(displayInfo);
                return true;
            }
            return false;
        }
        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return List.of();
    }
}