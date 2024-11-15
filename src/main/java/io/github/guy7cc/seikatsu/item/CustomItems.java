package io.github.guy7cc.seikatsu.item;

import io.github.guy7cc.seikatsu.system.Registry;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;

public class CustomItems {
    public static final Registry<CustomItem> REGISTRY = new Registry<>("item");

    public static final CustomItem SEIKATSU_COIN;
    public static final CustomItem MINERS_EMBLEM;
    public static final CustomItem HEART_CONTAINER;
    public static final CustomItem BIG_LIGHT;
    public static final CustomItem SMALL_LIGHT;
    public static final CustomItem BACK_BUTTON;
    public static final CustomItem NEXT_BUTTON;
    public static final CustomItem GOLDEN_EYE;
    public static final CustomItem EXAMPLE_ITEM;

    static{
        int i = 1;
        SEIKATSU_COIN = REGISTRY.register(new SeikatsuCoinItem(i++));
        MINERS_EMBLEM = REGISTRY.register(new MinersEmblemItem(i++));
        HEART_CONTAINER = REGISTRY.register(new HeartContainerItem(i++));
        BIG_LIGHT = REGISTRY.register(new BigLightItem(i++));
        SMALL_LIGHT = REGISTRY.register(new SmallLightItem(i++));
        BACK_BUTTON = REGISTRY.register(new GuiItem(1000, "back_button"));
        NEXT_BUTTON = REGISTRY.register(new GuiItem(1001, "next_button"));
        GOLDEN_EYE = REGISTRY.register(new GuiItem(1002, "golden_eye"));
        EXAMPLE_ITEM = REGISTRY.register(new ExampleItem(10000));
    }

    @Nullable
    public static CustomItem byId(int id) {
        return REGISTRY.byId(id);
    }

    @Nullable
    public static CustomItem byItemStack(ItemStack itemStack) {
        if (itemStack == null) return null;
        int id = CustomItem.getId(itemStack);
        if (id < 0) return null;
        return CustomItems.byId(id);
    }

    public static boolean is(ItemStack itemStack, CustomItem type) {
        CustomItem actualType = CustomItems.byItemStack(itemStack);
        return actualType == type;
    }
}
