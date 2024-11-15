package io.github.guy7cc.seikatsu.gui;

import io.github.guy7cc.seikatsu.SeikatsuPlugin;
import io.github.guy7cc.seikatsu.item.CustomItems;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class SelectInventory {
    private SelectInventoryProperty property;
    private SelectInventoryClickEventHandler handler;
    private int currentPage;
    private Inventory inv;

    public SelectInventory(SelectInventoryProperty property, SelectInventoryClickEventHandler handler) {
        this(property, handler, 0);
    }

    public SelectInventory(SelectInventoryProperty property, SelectInventoryClickEventHandler handler, int currentPage) {
        this.property = property;
        this.handler = handler;
        this.currentPage = currentPage;
    }

    public Inventory getInv() {
        return inv;
    }

    public Inventory setup() {
        SelectInventoryProperty.Page page = property.getPage(currentPage);
        int size = page.row() * 9;
        List<ItemStack> itemList = page.itemList();
        inv = Bukkit.createInventory(null, size);
        for (int i = 0; i < size && i < itemList.size(); i++) {
            if (i == page.back()) {
                inv.setItem(i, CustomItems.BACK_BUTTON.give());
            } else if (i == page.next()) {
                inv.setItem(i, CustomItems.NEXT_BUTTON.give());
            } else {
                inv.setItem(i, itemList.get(i));
            }
        }
        return inv;
    }

    public Inventory setupBack() {
        if (currentPage > 0) currentPage--;
        return setup();
    }

    public Inventory setupNext() {
        if (currentPage < property.size() - 1) currentPage++;
        return setup();
    }

    public void open(Player player) {
        SeikatsuPlugin.selectInventory.open(player, this);
    }

    // Returns true if selection succeeds and the view will close. Returns false if you don't want to close the view.
    public boolean onInventoryClick(Player player, int index) {
        SelectInventoryProperty.Page page = property.getPage(currentPage);
        if (index == page.back()) {
            if (currentPage > 0) currentPage--;
            SeikatsuPlugin.selectInventory.open(player, this);
            return false;
        } else if (index == page.next()) {
            if (currentPage < property.size() - 1) currentPage++;
            SeikatsuPlugin.selectInventory.open(player, this);
            return false;
        } else {
            return handler.apply(player, currentPage, index, inv.getItem(index));
        }
    }
}
