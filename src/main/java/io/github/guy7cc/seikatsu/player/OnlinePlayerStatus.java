package io.github.guy7cc.seikatsu.player;

import io.github.guy7cc.seikatsu.SeikatsuPlugin;
import io.github.guy7cc.seikatsu.item.CustomItem;
import io.github.guy7cc.seikatsu.item.CustomItemParam;
import io.github.guy7cc.seikatsu.item.CustomItems;
import io.github.guy7cc.seikatsu.system.Tickable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.VillagerAcquireTradeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.PlayerInventory;

import java.util.*;
import java.util.function.Supplier;

public class OnlinePlayerStatus implements Tickable {
    private final Player player;
    private final Map<CustomItem, Integer> itemNum = new HashMap<>();
    private final Map<CustomItem, CustomItemParam<?>> itemParam = new HashMap<>();
    private int elapsedTick = 0;
    private int sprintTick = 0;
    private int brokenBlock = 0;
    private int placedBlock = 0;
    private int consumedItem = 0;
    private double damageDone = 0D;
    private int expReceived = 0;

    public OnlinePlayerStatus(Player player){
        this.player = player;
    }

    @Override
    public void tick(int globalTick) {
        if(player.isSprinting()) ++sprintTick;
        else ++elapsedTick;
        if(elapsedTick >= 6000) {
            elapsedTick -= 6000;
            addXp(1);
        }
        if(sprintTick >= 200) {
            sprintTick -= 200;
            addXp(1);
        }
    }

    public void onBlockBreak(){
        ++brokenBlock;
        if(brokenBlock >= 3) {
            brokenBlock -= 3;
            addXp(1);
        }
    }

    public void onBlockPlace(){
        ++placedBlock;
        if(placedBlock >= 5) {
            placedBlock -= 5;
            addXp(1);
        }
    }

    public void onDamageEntity(double value){
        damageDone += value;
        if(damageDone > 10D){
            damageDone -= 10D;
            addXp(1);
        }
    }

    public void onItemConsume(){
        ++consumedItem;
        if(consumedItem >= 3){
            consumedItem -= 3;
            addXp(1);
        }
    }

    public void onExpChange(int value){
        expReceived += value;
        if(expReceived >= 30){
            expReceived -= 30;
            addXp(1);
        }
    }

    private void addXp(int value){
        SeikatsuPlugin.offlinePlayerStatus.computeIfPresent(player, s -> s.addXp(value));
    }

    public void onInventoryChanged(){
        PlayerInventory inv = player.getInventory();
        Set<CustomItem> set = new HashSet<>();
        itemNum.clear();
        for(int i = 0; i < 46; i++){
            ItemStack itemStack = inv.getItem(i);
            CustomItem item = CustomItems.byItemStack(itemStack);
            if(item == null) continue;
            set.add(item);
            if(itemNum.containsKey(item)){
                itemNum.put(item, itemNum.get(item) + itemStack.getAmount());
            } else {
                itemNum.put(item, itemStack.getAmount());
            }
        }
        for(CustomItem item : set){
            item.onInventoryChanged(player);
        }
    }

    public int getItemNum(CustomItem item){
        return itemNum.getOrDefault(item, 0);
    }

    public <T extends CustomItem> CustomItemParam<T> getItemParam(T item, Supplier<CustomItemParam<T>> supplier){
        if(itemParam.containsKey(item)) return (CustomItemParam<T>) itemParam.get(item);
        else {
            CustomItemParam<T> parameter = supplier.get();
            itemParam.put(item, parameter);
            return parameter;
        }
    }
}
