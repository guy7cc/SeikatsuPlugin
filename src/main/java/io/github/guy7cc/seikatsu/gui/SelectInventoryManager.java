package io.github.guy7cc.seikatsu.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.HashMap;
import java.util.Map;

public class SelectInventoryManager {
    private Map<Player, SelectInventory> map = new HashMap<>();

    public void open(Player player, SelectInventory inv) {
        player.closeInventory();
        map.put(player, inv);
        player.openInventory(inv.setup());
    }

    public void close(Player player) {
        if (contains(player)) {
            player.closeInventory();
            map.remove(player);
        }
    }

    public boolean contains(Player player) {
        return map.containsKey(player);
    }

    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player player && contains(player)) {
            SelectInventory selectInventory = map.get(player);
            if (selectInventory.getInv() != event.getClickedInventory()) return;
            boolean closeView = selectInventory.onInventoryClick(player, event.getSlot());
            if (closeView) close(player);
            event.setCancelled(true);
        }
    }
}
