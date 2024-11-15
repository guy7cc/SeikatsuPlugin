package io.github.guy7cc.seikatsu.item;

import io.github.guy7cc.seikatsu.SeikatsuPlugin;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlotGroup;

public class MinersEmblemItemParam extends CustomItemParam<MinersEmblemItem> {
    private AttributeModifier modifier;

    public MinersEmblemItemParam(MinersEmblemItem item){
        this.modifier = new AttributeModifier(item.key, 0.0D, AttributeModifier.Operation.ADD_SCALAR, EquipmentSlotGroup.ANY);
    }

    public void setModifier(Player player, int num){
        AttributeInstance attributeInstance = player.getAttribute(Attribute.MINING_EFFICIENCY);
        if(attributeInstance == null) return;
        attributeInstance.removeModifier(modifier);
        modifier = new AttributeModifier(CustomItems.MINERS_EMBLEM.key, num > 0 ? 0.1D : 0D, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.ANY);
        attributeInstance.addModifier(modifier);
    }
}
