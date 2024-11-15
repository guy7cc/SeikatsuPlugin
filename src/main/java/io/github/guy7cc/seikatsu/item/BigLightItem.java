package io.github.guy7cc.seikatsu.item;

import io.github.guy7cc.seikatsu.SeikatsuPlugin;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;

public class BigLightItem extends SizeLightItem {
    protected BigLightItem(int id) {
        super(id, "big_light", 1);
    }

    @Override
    public CustomItemInteractionResult useOnEntity(ItemStack self, Player player, EquipmentSlot slot, Entity entity) {
        if (SeikatsuPlugin.cooldown.getCooldown(player, this) > 0) return CustomItemInteractionResult.IN_COOLDOWN;
        if(!(entity instanceof LivingEntity livingEntity)) return CustomItemInteractionResult.UNAVAILABLE;
        AttributeInstance instance = livingEntity.getAttribute(Attribute.SCALE);
        if(instance == null) return CustomItemInteractionResult.UNAVAILABLE;
        AttributeModifier modifier = null;
        for(AttributeModifier m : instance.getModifiers()){
            if(m.getKey().equals(ATTRIBUTE_KEY)) {
                modifier = m;
                break;
            }
        }
        boolean sizeLimited = false;
        if(modifier == null){
            instance.addModifier(new AttributeModifier(ATTRIBUTE_KEY, 0.5D, AttributeModifier.Operation.MULTIPLY_SCALAR_1, EquipmentSlotGroup.ANY));
        } else {
            double scale = modifier.getAmount() + 1;
            instance.removeModifier(modifier);
            if(scale < 1D) scale *= 2D;
            else if(scale <= 3.5D) scale += 0.5D;
            else sizeLimited = true;
            instance.addModifier(new AttributeModifier(ATTRIBUTE_KEY, scale - 1D, AttributeModifier.Operation.MULTIPLY_SCALAR_1, EquipmentSlotGroup.ANY));
        }
        if(!sizeLimited) {
            decreaseUseCount(player, slot);
            SeikatsuPlugin.cooldown.useItem(player, this);
        }
        return CustomItemInteractionResult.SUCCESS;
    }
}
