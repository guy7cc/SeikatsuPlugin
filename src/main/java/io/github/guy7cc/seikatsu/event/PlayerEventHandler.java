package io.github.guy7cc.seikatsu.event;

import io.github.guy7cc.seikatsu.SeikatsuPlugin;
import io.github.guy7cc.seikatsu.gui.PlayerGuiManager;
import io.github.guy7cc.seikatsu.item.CooldownManager;
import io.github.guy7cc.seikatsu.item.CustomItem;
import io.github.guy7cc.seikatsu.item.CustomItemInteractionResult;
import io.github.guy7cc.seikatsu.item.CustomItems;
import io.github.guy7cc.seikatsu.player.OfflinePlayerStatusManager;
import io.github.guy7cc.seikatsu.player.OnlinePlayerStatus;
import io.github.guy7cc.seikatsu.player.OnlinePlayerStatusManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Merchant;

public class PlayerEventHandler implements Listener {
    private final OnlinePlayerStatusManager onlinePlayerStatus;
    private final OfflinePlayerStatusManager offlinePlayerStatus;
    private final CooldownManager cooldown;
    private final PlayerGuiManager playerGui;

    public PlayerEventHandler(OnlinePlayerStatusManager onlinePlayerStatus, OfflinePlayerStatusManager offlinePlayerStatus, CooldownManager cooldown, PlayerGuiManager playerGui) {
        this.onlinePlayerStatus = onlinePlayerStatus;
        this.offlinePlayerStatus = offlinePlayerStatus;
        this.cooldown = cooldown;
        this.playerGui = playerGui;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        onlinePlayerStatus.onPlayerJoin(event.getPlayer());
        offlinePlayerStatus.onPlayerJoin(event.getPlayer());
        playerGui.onPlayerJoin(event.getPlayer());
        for(String str : SeikatsuPlugin.joinMessage){
            event.getPlayer().sendMessage(str);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        onlinePlayerStatus.onPlayerQuit(event.getPlayer());
        playerGui.onPlayerQuit(event.getPlayer());
    }

    @EventHandler
    public void onPlayerFish(PlayerFishEvent event){
        offlinePlayerStatus.computeIfPresent(event.getPlayer(), s -> s.addXp(1));
    }

    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent event){
        onlinePlayerStatus.computeIfPresent(event.getPlayer(), OnlinePlayerStatus::onItemConsume);
    }

    @EventHandler
    public void onPlayerExpChange(PlayerExpChangeEvent event){
        onlinePlayerStatus.computeIfPresent(event.getPlayer(), s -> s.onExpChange(event.getAmount()));
    }

    @EventHandler
    public void onPlayerEnchant(EnchantItemEvent event){
        offlinePlayerStatus.computeIfPresent(event.getEnchanter(), s -> s.addXp(1));
    }

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event){
        if(SeikatsuPlugin.forbiddenCommands.matcher(event.getMessage()).find()){
            event.setCancelled(true);
            event.getPlayer().sendMessage("§cそのコマンドは無効化されています。");
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) return;

        for (EquipmentSlot slot : EquipmentSlot.values()) {
            ItemStack itemStack = event.getPlayer().getInventory().getItem(slot);
            CustomItem item = CustomItems.byItemStack(itemStack);
            if (item == null) return;

            CustomItemInteractionResult result = item.use(itemStack, event.getPlayer(), slot, event.getAction());
            if (result.shouldShowMessage) event.getPlayer().spigot().sendMessage(result.message);
            if (result != CustomItemInteractionResult.UNAVAILABLE) event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) return;

        for (EquipmentSlot slot : EquipmentSlot.values()) {
            ItemStack itemStack = event.getPlayer().getInventory().getItem(slot);
            CustomItem item = CustomItems.byItemStack(itemStack);
            if (item == null) return;

            CustomItemInteractionResult result = item.useOnEntity(itemStack, event.getPlayer(), slot, event.getRightClicked());
            if (result.shouldShowMessage) event.getPlayer().spigot().sendMessage(result.message);
            if (result != CustomItemInteractionResult.UNAVAILABLE) event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        CustomItem item = CustomItems.byItemStack(event.getPlayer().getInventory().getItem(event.getNewSlot()));
        if (item == null) cooldown.sendEmptyCooldownMessage(event.getPlayer());
        else cooldown.sendCooldownMessage(event.getPlayer(), item);
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event){
        OnlinePlayerStatus status = SeikatsuPlugin.onlinePlayerStatus.get(event.getPlayer());
        if(status != null){
            status.onInventoryChanged();
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event){
        OnlinePlayerStatus status = SeikatsuPlugin.onlinePlayerStatus.get(event.getPlayer());
        if(status != null) {
            status.onInventoryChanged();
        }
    }
}
