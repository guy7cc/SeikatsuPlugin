package io.github.guy7cc.seikatsu.item;

import io.github.guy7cc.seikatsu.SeikatsuPlugin;
import org.bukkit.NamespacedKey;

public class SizeLightItem extends CustomItem {
    public static final NamespacedKey ATTRIBUTE_KEY = new NamespacedKey(SeikatsuPlugin.NAMESPACE, "size_light");

    protected SizeLightItem(int id, String name, int loreLineNum) {
        super(id, name, loreLineNum, false, 30, 20, CustomItemUsage.RIGHT_CLICK_ON_ENTITY);
    }
}
