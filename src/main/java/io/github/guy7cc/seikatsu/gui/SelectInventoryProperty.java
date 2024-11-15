package io.github.guy7cc.seikatsu.gui;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SelectInventoryProperty {
    public List<Page> pageList = new ArrayList<>();

    public SelectInventoryProperty(Page firstPage) {
        pageList.add(firstPage);
    }

    public SelectInventoryProperty(Page... pages) {
        pageList.addAll(List.of(pages));
    }

    public int size() {
        return pageList.size();
    }

    public SelectInventoryProperty addPage(Page page) {
        pageList.add(page);
        return this;
    }

    public Page getPage(int index) {
        return pageList.get(index);
    }

    public record Page(int row, int back, int next, List<ItemStack> itemList) {
        public static Page singlePage(int row, @NotNull List<ItemStack> itemList) {
            return new Page(row, -1, -1, itemList);
        }

        public static Page firstPage(int row, @NotNull List<ItemStack> itemList) {
            row = validateRow(row);
            return new Page(row, -1, row * 9 - 1, itemList);
        }

        public static Page firstPage(int row, int next, @NotNull List<ItemStack> itemList) {
            return new Page(row, -1, next, itemList);
        }

        public static Page middlePage(int row, @NotNull List<ItemStack> itemList) {
            row = validateRow(row);
            return new Page(row, (row - 1) * 9, row * 9 - 1, itemList);
        }

        public static Page middlePage(int row, int back, int next, @NotNull List<ItemStack> itemList) {
            return new Page(row, back, next, itemList);
        }

        public static Page lastPage(int row, @NotNull List<ItemStack> itemList) {
            row = validateRow(row);
            return new Page(row, (row - 1) * 9, -1, itemList);
        }

        public static Page lastPage(int row, int back, @NotNull List<ItemStack> itemList) {
            return new Page(row, back, -1, itemList);
        }

        private static int validateRow(int row) {
            return Math.max(1, Math.min(6, row));
        }
    }
}
