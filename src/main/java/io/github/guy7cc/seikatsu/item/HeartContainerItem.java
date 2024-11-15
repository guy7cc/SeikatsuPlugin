package io.github.guy7cc.seikatsu.item;

import io.github.guy7cc.seikatsu.SeikatsuPlugin;
import org.bukkit.entity.Player;

public class HeartContainerItem extends CustomItem {
    public HeartContainerItem(int id) {
        super(id, "heart_container", 2, false, 0, 0, CustomItemUsage.SOME_IN_INVENTORY);
    }

    @Override
    public void onInventoryChanged(Player player) {
        SeikatsuPlugin.onlinePlayerStatus.computeIfPresent(player, s -> {
            HeartContainerItemParam param = (HeartContainerItemParam) s.getItemParam(this, () -> new HeartContainerItemParam(this));
            param.setModifier(player, s.getItemNum(this));
        });
    }
}
