package io.github.guy7cc.seikatsu.gui;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@FunctionalInterface
public interface SelectInventoryClickEventHandler {
    boolean apply(Player player, int page, int index, ItemStack itemStack);
}
