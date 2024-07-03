package io.github.guy7cc.seikatsu.command;

import io.github.guy7cc.seikatsu.SeikatsuPlugin;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SitCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, Command command, @NotNull String label, String[] args){
        if(sender instanceof Entity entity){
            World w = entity.getWorld();
            Chicken seat = (Chicken)w.spawnEntity(entity.getLocation().add(new Vector(0.0, -0.3, 0.0)), EntityType.CHICKEN);

            seat.setBaby();
            seat.setBreed(false);
            seat.setAI(false);
            seat.setGravity(false);
            seat.setSilent(true);
            seat.setInvulnerable(true);
            seat.setHealth(1.0);
            seat.setCollidable(false);
            seat.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 216000, 1, true, false));
            seat.setCustomName(ChatColor.GREEN + "seat");
            seat.setCustomNameVisible(false);
            seat.addPassenger(entity);
            seat.setPersistent(true);

            SeikatsuPlugin.seat.register(seat);
            return true;
        } else {
            return false;
        }
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return List.of();
    }
}
