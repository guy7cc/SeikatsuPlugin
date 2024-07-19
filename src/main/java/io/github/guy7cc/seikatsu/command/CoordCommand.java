package io.github.guy7cc.seikatsu.command;

import io.github.guy7cc.seikatsu.SeikatsuPlugin;
import io.github.guy7cc.seikatsu.gui.PlayerGui;
import io.github.guy7cc.seikatsu.gui.sidebar.CoordComponent;
import io.github.guy7cc.seikatsu.gui.sidebar.SimpleSidebarComponent;
import io.github.guy7cc.seikatsu.system.CoordManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CoordCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, Command command, @NotNull String label, String[] args) {
        if(!(sender instanceof Player player)) return false;
        if(args.length == 2 && args[0].equals("add")){
            if(args[1].isEmpty() || args[1].isBlank()){
                player.sendMessage("§c座標ラベルは空文字列もしくは空白であってはいけません。");
                return false;
            }
            CoordManager.Coord coord = new CoordManager.Coord(args[1], player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ());
            SeikatsuPlugin.coord.add(coord);
            player.sendMessage("§a座標ラベル「" + args[1] + "」が追加されました。");
            PlayerGui gui = SeikatsuPlugin.playerGui.get(player);
            if(gui == null) return true;
            gui.sidebar.addComponent(new CoordComponent(coord));
        } else if(args.length == 5 && args[0].equals("add")){
            if(args[1].isEmpty() || args[1].isBlank()){
                player.sendMessage("§c座標ラベルは空文字列もしくは空白であってはいけません。");
                return false;
            }
            try{
                int x = Integer.parseInt(args[2]);
                int y = Integer.parseInt(args[3]);
                int z = Integer.parseInt(args[4]);
                CoordManager.Coord coord = new CoordManager.Coord(args[1], x, y, z);
                SeikatsuPlugin.coord.add(coord);
                player.sendMessage("§a座標ラベル「" + args[1] + "」が追加されました。");
                PlayerGui gui = SeikatsuPlugin.playerGui.get(player);
                if(gui == null) return true;
                gui.sidebar.addComponent(new CoordComponent(coord));
            } catch(NumberFormatException exception){
                player.sendMessage("§c座標の数値が無効です。");
                return false;
            }
        } else if(args.length == 2 && args[0].equals("show")) {
            CoordManager.Coord coord = SeikatsuPlugin.coord.get(args[1]);
            if(coord == null) return false;
            PlayerGui gui = SeikatsuPlugin.playerGui.get(player);
            if(gui == null) return false;
            gui.sidebar.addComponent(new CoordComponent(coord));
            return true;
        } else if(args.length == 2 && args[0].equals("remove")){
            if(SeikatsuPlugin.coord.remove(args[1])){
                player.sendMessage("§a座標ラベル「" + args[1] + "」が消去されました。");
                return true;
            } else {
                player.sendMessage("§cその名前の座標ラベルは存在しません。");
                return false;
            }
        } else if(args.length == 1 && args[0].equals("hide")){
            PlayerGui gui = SeikatsuPlugin.playerGui.get(player);
            if(gui == null) return false;
            gui.sidebar.removeComponent("coord");
            return true;
        }
        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        List<String> completion = new ArrayList<>();
        if(strings.length == 1){
            StringUtil.copyPartialMatches(strings[0], List.of("add", "show", "remove", "hide"), completion);
        } else if(strings.length == 2 && (strings[0].equals("show") || strings[0].equals("remove"))){
            StringUtil.copyPartialMatches(strings[1], SeikatsuPlugin.coord.getLabels(), completion);
        } else if(strings.length == 3 && strings[0].equals("add") && commandSender instanceof Entity entity){
            StringUtil.copyPartialMatches(strings[2], List.of(String.valueOf(entity.getLocation().getBlockX())), completion);
        } else if(strings.length == 4 && strings[0].equals("add") && commandSender instanceof Entity entity){
            StringUtil.copyPartialMatches(strings[3], List.of(String.valueOf(entity.getLocation().getBlockY())), completion);
        } else if(strings.length == 5 && strings[0].equals("add") && commandSender instanceof Entity entity){
            StringUtil.copyPartialMatches(strings[4], List.of(String.valueOf(entity.getLocation().getBlockZ())), completion);
        }
        return completion;
    }
}
