package io.github.guy7cc.seikatsu.command;

import io.github.guy7cc.seikatsu.item.CustomItems;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DealerCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player player && args.length == 1) {
            Villager v = (Villager) player.getWorld().spawnEntity(player.getLocation(), EntityType.VILLAGER);
            v.getAttribute(Attribute.MAX_HEALTH).setBaseValue(200);
            v.setHealth(200);
            if(args[0].equals("1")){
                v.setCustomName("ディーラー");
                v.setVillagerType(Villager.Type.PLAINS);
                v.setProfession(Villager.Profession.LIBRARIAN);
                v.setVillagerLevel(5);

                List<MerchantRecipe> recipes = new ArrayList<>();
                ItemStack coins = CustomItems.SEIKATSU_COIN.give();
                MerchantRecipe recipe = null;

                recipe = new MerchantRecipe(CustomItems.MINERS_EMBLEM.give(), 0, Integer.MAX_VALUE, false);
                coins.setAmount(64);
                recipe.addIngredient(coins);
                recipes.add(recipe);

                recipe = new MerchantRecipe(CustomItems.HEART_CONTAINER.give(), 0, Integer.MAX_VALUE, false);
                coins.setAmount(3);
                recipe.addIngredient(coins);
                recipes.add(recipe);

                recipe = new MerchantRecipe(CustomItems.BIG_LIGHT.give(), 0, Integer.MAX_VALUE, false);
                coins.setAmount(12);
                recipe.addIngredient(coins);
                recipes.add(recipe);

                recipe = new MerchantRecipe(CustomItems.SMALL_LIGHT.give(), 0, Integer.MAX_VALUE, false);
                coins.setAmount(12);
                recipe.addIngredient(coins);
                recipes.add(recipe);

                v.setRecipes(recipes);
            } else if(args[0].equals("2")){
                v.setCustomName("両替商");
                v.setVillagerType(Villager.Type.DESERT);
                v.setProfession(Villager.Profession.WEAPONSMITH);
                v.setVillagerLevel(5);

                List<MerchantRecipe> recipes = new ArrayList<>();
                ItemStack coins = CustomItems.SEIKATSU_COIN.give();
                MerchantRecipe recipe = null;

                coins = CustomItems.SEIKATSU_COIN.give();
                coins.setAmount(1);
                recipe = new MerchantRecipe(coins, 0, Integer.MAX_VALUE, false);
                recipe.addIngredient(new ItemStack(Material.IRON_INGOT, 16));
                recipes.add(recipe);

                coins = CustomItems.SEIKATSU_COIN.give();
                coins.setAmount(1);
                recipe = new MerchantRecipe(coins, 0, Integer.MAX_VALUE, false);
                recipe.addIngredient(new ItemStack(Material.GOLD_INGOT, 16));
                recipes.add(recipe);

                coins = CustomItems.SEIKATSU_COIN.give();
                coins.setAmount(1);
                recipe = new MerchantRecipe(coins, 0, Integer.MAX_VALUE, false);
                recipe.addIngredient(new ItemStack(Material.COPPER_INGOT, 16));
                recipes.add(recipe);

                coins = CustomItems.SEIKATSU_COIN.give();
                coins.setAmount(1);
                recipe = new MerchantRecipe(coins, 0, Integer.MAX_VALUE, false);
                recipe.addIngredient(new ItemStack(Material.LAPIS_LAZULI, 32));
                recipes.add(recipe);

                coins = CustomItems.SEIKATSU_COIN.give();
                coins.setAmount(1);
                recipe = new MerchantRecipe(coins, 0, Integer.MAX_VALUE, false);
                recipe.addIngredient(new ItemStack(Material.REDSTONE, 32));
                recipes.add(recipe);

                coins = CustomItems.SEIKATSU_COIN.give();
                coins.setAmount(1);
                recipe = new MerchantRecipe(coins, 0, Integer.MAX_VALUE, false);
                recipe.addIngredient(new ItemStack(Material.EMERALD, 2));
                recipes.add(recipe);

                coins = CustomItems.SEIKATSU_COIN.give();
                coins.setAmount(2);
                recipe = new MerchantRecipe(coins, 0, Integer.MAX_VALUE, false);
                recipe.addIngredient(new ItemStack(Material.DIAMOND, 1));
                recipes.add(recipe);

                coins = CustomItems.SEIKATSU_COIN.give();
                coins.setAmount(4);
                recipe = new MerchantRecipe(coins, 0, Integer.MAX_VALUE, false);
                recipe.addIngredient(new ItemStack(Material.NETHERITE_INGOT, 1));
                recipes.add(recipe);

                v.setRecipes(recipes);
            }
            return true;
        }
        return false;
    }


    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(args.length == 1){
            return CommandUtil.getOptions(args[0], List.of("1", "2"));
        }
        return null;
    }
}
