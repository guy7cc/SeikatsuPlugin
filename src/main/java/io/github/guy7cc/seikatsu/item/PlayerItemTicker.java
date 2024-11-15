package io.github.guy7cc.seikatsu.item;

import io.github.guy7cc.seikatsu.SeikatsuPlugin;
import io.github.guy7cc.seikatsu.system.Tickable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class PlayerItemTicker implements Tickable {
    @Override
    public void tick(int globalTick) {
        for (Player player : SeikatsuPlugin.plugin.getServer().getOnlinePlayers()) {
            PlayerInventory inv = player.getInventory();
            for (EquipmentSlot slot : EquipmentSlot.values()) {
                ItemStack itemStack = inv.getItem(slot);
                CustomItem item = CustomItems.byItemStack(itemStack);
                if (item == null) return;

                item.tickInEquipmentSlot(itemStack, player, slot);
            }

            for (int i = 36; i <= 45; i++) {
                ItemStack itemStack = inv.getItem(i);
                CustomItem item = CustomItems.byItemStack(itemStack);
                if (item == null) return;

                item.tickInQuickBar(itemStack, player, i);
            }
        }
    }
}
