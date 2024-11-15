package io.github.guy7cc.seikatsu.event;

import io.github.guy7cc.seikatsu.SeikatsuPlugin;
import io.github.guy7cc.seikatsu.item.CustomItem;
import io.github.guy7cc.seikatsu.item.CustomItems;
import io.github.guy7cc.seikatsu.player.OnlinePlayerStatus;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantInventory;
import org.bukkit.inventory.view.MerchantView;

public class InventoryEventHandler implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        SeikatsuPlugin.selectInventory.onInventoryClick(event);
        if(event.getWhoClicked() instanceof Player player){
            SeikatsuPlugin.onlinePlayerStatus.computeIfPresent(player, OnlinePlayerStatus::onInventoryChanged);

            if(player.getOpenInventory() instanceof MerchantView merchantView && event.getRawSlot() == 2 && event.isLeftClick()){
                ItemStack result = merchantView.getItem(2);
                if(result != null) {
                    CustomItem item = CustomItems.byItemStack(result);
                    if(item != null){
                        event.setCancelled(true);
                        MerchantInventory inv = merchantView.getTopInventory();
                        for(ItemStack ingredient : inv.getSelectedRecipe().getIngredients()){
                            int amount = ingredient.getAmount();
                            for(int slot = 0; slot < 2; slot++){
                                ItemStack itemStack = inv.getItem(slot);
                                if(itemStack != null && itemStack.isSimilar(ingredient)){
                                    if(itemStack.getAmount() >= amount) {
                                        itemStack.setAmount(itemStack.getAmount() - amount);
                                        break;
                                    }
                                    else {
                                        amount -= itemStack.getAmount();
                                        inv.setItem(slot, null);
                                    }
                                }
                            }
                        }
                        ItemStack newResult = item.give();
                        newResult.setAmount(result.getAmount());
                        player.getWorld().dropItemNaturally(player.getLocation(), newResult);
                    }
                }
            }
        }
    }
}
