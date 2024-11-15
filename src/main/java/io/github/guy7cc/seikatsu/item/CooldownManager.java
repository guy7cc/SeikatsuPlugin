package io.github.guy7cc.seikatsu.item;

import io.github.guy7cc.seikatsu.SeikatsuPlugin;
import io.github.guy7cc.seikatsu.system.Tickable;
import io.github.guy7cc.seikatsu.util.TranslationUtil;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.TranslatableComponent;
import org.bukkit.entity.Player;

import java.util.*;

public class CooldownManager implements Tickable {
    private int tick;
    private Map<UUID, FinishTimeHolder> map = new HashMap<>();

    public void tick(int globalTick) {
        tick = globalTick;
        for (UUID uuid : map.keySet()) {
            Player player = SeikatsuPlugin.plugin.getServer().getPlayer(uuid);
            if (player == null) {
                map.remove(uuid);
                continue;
            }
            CustomItem item = CustomItems.byItemStack(player.getInventory().getItemInMainHand());
            FinishTimeHolder holder = map.get(uuid);
            if (item != null && holder.contains(item)) {
                int finishTime = holder.get(item);
                if ((globalTick <= finishTime - 20 && (finishTime - globalTick) % 20 == 0) || (finishTime - 20 < globalTick && globalTick <= finishTime)) {
                    sendCooldownMessage(player, item);
                }
            }
            holder.remove(globalTick);
        }
    }

    public void sendCooldownMessage(Player player, CustomItem item) {
        FinishTimeHolder holder = map.get(player.getUniqueId());
        if (item != null && holder != null && holder.contains(item)) {
            int finishTime = holder.get(item);
            if (tick <= finishTime - 20) {
                String sec = String.valueOf((finishTime - tick + 19) / 20);
                TranslatableComponent component = new TranslatableComponent(TranslationUtil.guiKey("cooldown"), sec);
                component.setColor(ChatColor.GRAY);
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component);
            } else if (finishTime - 20 < tick && tick < finishTime) {
                String sec = String.format("%.1f", (finishTime - tick) / 20f);
                TranslatableComponent component = new TranslatableComponent(TranslationUtil.guiKey("cooldown"), sec);
                component.setColor(ChatColor.GRAY);
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component);
            } else if (tick >= finishTime) {
                TranslatableComponent component = new TranslatableComponent(TranslationUtil.guiKey("cooldown.available"));
                component.setColor(ChatColor.GRAY);
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component);
            }
        }
    }

    public void sendEmptyCooldownMessage(Player player) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(""));
    }

    public void useItem(Player player, CustomItem item) {
        if (!item.hasCooldownTick()) return;
        useItem(player, item, item.cooldownTick);
    }

    public void useItem(Player player, CustomItem item, int cooldownTick) {
        if (!map.containsKey(player.getUniqueId())) map.put(player.getUniqueId(), new FinishTimeHolder(player));
        FinishTimeHolder holder = map.get(player.getUniqueId());
        holder.put(item, tick + cooldownTick);
        sendCooldownMessage(player, item);
    }

    public int getCooldown(Player player, CustomItem item) {
        if (!map.containsKey(player.getUniqueId())) return 0;
        FinishTimeHolder holder = map.get(player.getUniqueId());
        if (holder.contains(item)) return holder.get(item) - tick;
        return 0;
    }

    private static class FinishTimeHolder {
        private UUID playerUUID;
        private Map<CustomItem, Integer> itemToFinishTime = new HashMap<>();
        private Map<Integer, Set<CustomItem>> finishTimeToItem = new HashMap<>();

        public FinishTimeHolder(Player player) {
            playerUUID = player.getUniqueId();
        }

        public void put(CustomItem item, int time) {
            itemToFinishTime.put(item, time);
            if (!finishTimeToItem.containsKey(time)) finishTimeToItem.put(time, new HashSet<>());
            finishTimeToItem.get(time).add(item);
        }

        public boolean contains(CustomItem item) {
            return itemToFinishTime.containsKey(item);
        }

        public boolean contains(int time) {
            return finishTimeToItem.containsKey(time);
        }

        public int get(CustomItem item) {
            return itemToFinishTime.get(item);
        }

        public Set<CustomItem> get(int time) {
            return finishTimeToItem.get(time);
        }

        public void remove(CustomItem item) {
            int finishTime = itemToFinishTime.remove(item);
            Set<CustomItem> set = finishTimeToItem.get(finishTime);
            if (set != null) set.remove(item);
        }

        public void remove(int time) {
            Set<CustomItem> set = finishTimeToItem.remove(time);
            if (set != null) {
                for (CustomItem item : set) itemToFinishTime.remove(item);
            }
        }
    }
}
