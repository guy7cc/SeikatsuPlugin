package io.github.guy7cc.seikatsu.command;

import io.github.guy7cc.seikatsu.item.CustomItem;
import io.github.guy7cc.seikatsu.item.CustomItems;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.HumanEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SeikatsuCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 2 && args[0].equals("item") && sender instanceof HumanEntity entity) {
            CustomItem item = CustomItems.REGISTRY.byName(args[1]);
            if (item != null) {
                entity.getInventory().addItem(item.give());
                return true;
            }
        }
        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return CommandUtil.getOptions(args[0], List.of("item"));
        } else if (args.length == 2 && args[0].equals("item")) {
            return CommandUtil.getOptions(args[1], CustomItems.REGISTRY.names().stream().toList());
        }
        return null;
    }
}
