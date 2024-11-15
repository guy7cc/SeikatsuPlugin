package io.github.guy7cc.seikatsu.item;

import io.github.guy7cc.seikatsu.system.RegistryObject;
import io.github.guy7cc.seikatsu.util.ComponentUtil;
import io.github.guy7cc.seikatsu.util.TranslationUtil;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.TranslatableComponent;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.CustomModelData;
import net.minecraft.world.item.component.ItemLore;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_21_R2.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public abstract class CustomItem extends RegistryObject {
    public static final String TAG_NAME = "CSBCustomItemData";

    public final int loreLineNum;
    public final boolean stackable;
    public final int maxUseCount;
    public final int cooldownTick;
    public final CustomItemUsage usage;

    protected CustomItem(int id, String name, int loreLineNum, boolean stackable, int maxUseCount, int cooldownTick, CustomItemUsage usage) {
        super(id, name);
        this.loreLineNum = loreLineNum;
        this.stackable = stackable;
        this.maxUseCount = maxUseCount;
        this.cooldownTick = cooldownTick;
        this.usage = usage;
    }

    public static int getId(ItemStack itemStack) {
        net.minecraft.world.item.ItemStack rawItemStack = CraftItemStack.asNMSCopy(itemStack);
        CustomData customData = rawItemStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        CompoundTag tag = customData.copyTag();
        if (tag.contains("Id")) return tag.getInt("Id");
        return -1;
    }

    public final ItemStack give() {
        ItemStack itemStack = new ItemStack(Material.STICK);
        net.minecraft.world.item.ItemStack rawItemStack = CraftItemStack.asNMSCopy(itemStack);

        rawItemStack.set(DataComponents.CUSTOM_NAME, createNameComponent(maxUseCount));
        rawItemStack.set(DataComponents.LORE, createItemLore());
        rawItemStack.set(DataComponents.CUSTOM_MODEL_DATA, new CustomModelData(id));
        rawItemStack.set(DataComponents.CUSTOM_DATA, CustomData.of(createTag(maxUseCount)));

        itemStack = CraftItemStack.asBukkitCopy(rawItemStack);
        return itemStack;
    }

    protected Component createNameComponent(int useCount){
        TranslatableComponent nameComponent = new TranslatableComponent(getTranslationKey());
        nameComponent.setItalic(false);
        if (maxUseCount > 0) {
            TextComponent useCountComponent = new TextComponent(" " + useCount + "/" + maxUseCount);
            ChatColor color;
            if (useCount > 3 && useCount > maxUseCount * 3 / 10) color = ChatColor.AQUA;
            else if (useCount > 1 && useCount > maxUseCount / 10) color = ChatColor.GREEN;
            else color = ChatColor.RED;
            useCountComponent.setColor(color);
            nameComponent.addExtra(useCountComponent);
        }
        return ComponentUtil.toComponent(nameComponent);
    }

    protected ItemLore createItemLore(){
        List<Component> loreComponents = new ArrayList<>();
        for (int i = 0; i < loreLineNum; i++) {
            loreComponents.add(ComponentUtil.toComponent(new TranslatableComponent(getTranslationKey() + ".lore" + i)));
        }
        if (usage != CustomItemUsage.NONE) {
            loreComponents.add(ComponentUtil.toComponent(new TextComponent(" ")));
            loreComponents.add(ComponentUtil.toComponent(new TranslatableComponent(TranslationUtil.guiKey("itemUsage"), new TranslatableComponent(TranslationUtil.guiKey("itemUsage." + usage.getKey())))));
        }
        return new ItemLore(loreComponents);
    }

    protected CompoundTag createTag(int useCount) {
        CompoundTag tag = new CompoundTag();
        tag.putInt("Id", id);
        if(!stackable) tag.putUUID("UUID", UUID.randomUUID());
        if (maxUseCount > 0) tag.putInt("Use", useCount);
        return tag;
    }

    public boolean hasUseCount() {
        return maxUseCount > 0;
    }

    public void decreaseUseCount(Player player, int index) {
        if (maxUseCount <= 0) return;
        ItemStack itemStack = player.getInventory().getItem(index);
        if (getId(itemStack) != id) return;
        net.minecraft.world.item.ItemStack rawItemStack = CraftItemStack.asNMSCopy(itemStack);
        CompoundTag customItemTag = rawItemStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        int useCount = customItemTag.getInt("Use");
        if (useCount > 1) {
            rawItemStack.set(DataComponents.CUSTOM_NAME, createNameComponent(useCount - 1));
            rawItemStack.set(DataComponents.CUSTOM_DATA, CustomData.of(createTag(useCount - 1)));
            itemStack = CraftItemStack.asBukkitCopy(rawItemStack);
            player.getInventory().setItem(index, itemStack);
        } else player.getInventory().setItem(index, null);
    }

    public void decreaseUseCount(Player player, EquipmentSlot slot) {
        int index = -1;
        switch (slot) {
            case HAND -> index = player.getInventory().getHeldItemSlot();
            case OFF_HAND -> index = 45;
            case FEET -> index = 8;
            case LEGS -> index = 7;
            case CHEST -> index = 6;
            case HEAD -> index = 5;
        }
        if (index >= 0) decreaseUseCount(player, index);
    }

    public boolean hasCooldownTick() {
        return cooldownTick > 0;
    }

    public CustomItemInteractionResult use(ItemStack self, Player player, EquipmentSlot slot, Action action) {
        return CustomItemInteractionResult.UNAVAILABLE;
    }

    public CustomItemInteractionResult useOnEntity(ItemStack self, Player player, EquipmentSlot slot, Entity entity) {
        return CustomItemInteractionResult.UNAVAILABLE;
    }

    public void tickInEquipmentSlot(ItemStack self, Player player, EquipmentSlot slot) {

    }

    public void tickInQuickBar(ItemStack self, Player player, int index) {

    }

    public void onInventoryChanged(Player player) {

    }
}
