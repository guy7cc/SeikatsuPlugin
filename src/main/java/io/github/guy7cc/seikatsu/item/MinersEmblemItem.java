package io.github.guy7cc.seikatsu.item;

import io.github.guy7cc.seikatsu.SeikatsuPlugin;
import org.bukkit.entity.Player;

public class MinersEmblemItem extends CustomItem {
    protected MinersEmblemItem(int id) {
        super(id, "miners_emblem", 2, false, 0, 0, CustomItemUsage.ONE_IN_INVENTORY);
    }

    @Override
    public void onInventoryChanged(Player player) {
        SeikatsuPlugin.onlinePlayerStatus.computeIfPresent(player, s -> {
            MinersEmblemItemParam param = (MinersEmblemItemParam) s.getItemParam(this, () -> new MinersEmblemItemParam(this));
            param.setModifier(player, s.getItemNum(this));
        });
    }
}
