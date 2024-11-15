package io.github.guy7cc.seikatsu.item;

import io.github.guy7cc.seikatsu.SeikatsuPlugin;
import io.github.guy7cc.seikatsu.gui.SelectInventory;
import io.github.guy7cc.seikatsu.gui.SelectInventoryProperty;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ExampleItem extends CustomItem {
    public ExampleItem(int id) {
        super(id, "example_item", 2, false, 10, 60, CustomItemUsage.LEFT_CLICK);
    }

    @Override
    public CustomItemInteractionResult use(ItemStack self, Player player, EquipmentSlot slot, Action action) {
        if (SeikatsuPlugin.cooldown.getCooldown(player, this) > 0) return CustomItemInteractionResult.IN_COOLDOWN;
        if (slot == EquipmentSlot.HAND && (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK)) {
            player.sendMessage("Example");

            List<ItemStack> items = new ArrayList<>();
            for (int i = 0; i < 9; i++) {
                if (i == 4) items.add(new ItemStack(Material.ACACIA_DOOR));
                else items.add(null);
            }
            SelectInventoryProperty property = new SelectInventoryProperty(
                    SelectInventoryProperty.Page.firstPage(1, items),
                    SelectInventoryProperty.Page.middlePage(1, items),
                    SelectInventoryProperty.Page.lastPage(1, items)
            );
            SelectInventory select = new SelectInventory(property, (whoClicked, page, index, itemStack) -> {
                if (index == 4) {
                    whoClicked.sendMessage("hi");
                    return true;
                } else return false;
            });
            select.open(player);

            decreaseUseCount(player, slot);
            SeikatsuPlugin.cooldown.useItem(player, this);
            return CustomItemInteractionResult.SUCCESS;
        }
        return super.use(self, player, slot, action);
    }

    @Override
    public CustomItemInteractionResult useOnEntity(ItemStack self, Player player, EquipmentSlot slot, Entity entity) {
        entity.setPersistent(false);
        decreaseUseCount(player, slot);
        SeikatsuPlugin.cooldown.useItem(player, this);
        return CustomItemInteractionResult.SUCCESS;
    }
}
