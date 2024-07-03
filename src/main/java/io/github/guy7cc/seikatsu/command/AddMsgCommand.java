package io.github.guy7cc.seikatsu.command;

import io.github.guy7cc.seikatsu.SeikatsuPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AddMsgCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player player && args.length > 0) {
            StringBuilder sb = new StringBuilder();
            sb.append(args[0]);
            for(int i = 1; i < args.length; ++i){
                sb.append(" ").append(args[i]);
            }
            SeikatsuPlugin.messageBar.add(sb.toString());
            return true;
        }
        return false;
    }


    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return List.of();
    }
}
