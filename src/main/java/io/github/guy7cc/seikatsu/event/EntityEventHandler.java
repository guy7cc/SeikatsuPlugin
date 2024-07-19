package io.github.guy7cc.seikatsu.event;

import io.github.guy7cc.seikatsu.SeikatsuPlugin;
import io.github.guy7cc.seikatsu.system.OfflinePlayerStatus;
import io.github.guy7cc.seikatsu.system.OnlinePlayerStatus;
import org.bukkit.Bukkit;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;

public class EntityEventHandler implements Listener {
    @EventHandler
    public void onEntityBreed(EntityBreedEvent event){
        if(event.getBreeder() instanceof Player player){
            OfflinePlayerStatus status = SeikatsuPlugin.offlinePlayerStatus.get(player);
            if(status == null) return;
            status.addXp(5);
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event){
        if(event.getDamager() instanceof Player player){
            OnlinePlayerStatus status = SeikatsuPlugin.onlinePlayerStatus.get(player);
            if(status == null) return;
            status.onDamageEntity(event.getDamage());
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event){
        if(event.getDamageSource().getCausingEntity() instanceof Player player){
            OfflinePlayerStatus status = SeikatsuPlugin.offlinePlayerStatus.get(player);
            if(status == null) return;
            if(event.getEntity() instanceof ElderGuardian) status.addXp(200);
            else if(event.getEntity() instanceof Wither) status.addXp(300);
            else if(event.getEntity() instanceof Warden) status.addXp(300);
            else if(event.getEntity() instanceof EnderDragon) status.addXp(500);
            else if(event.getEntity() instanceof Player) ;
            else status.addXp(1);
        }
    }

    @EventHandler
    public void onEntityTame(EntityTameEvent event){
        if(event.getOwner() instanceof Player player){
            OfflinePlayerStatus status = SeikatsuPlugin.offlinePlayerStatus.get(player);
            if(status == null) return;
            status.addXp(15);
        }
    }

    @EventHandler
    public void onVillagerAcquireTrade(VillagerAcquireTradeEvent event){
        for(Player player : Bukkit.getOnlinePlayers()){
            if(player.getLocation().distance(event.getEntity().getLocation()) < 10){
                OfflinePlayerStatus status = SeikatsuPlugin.offlinePlayerStatus.get(player);
                if(status == null) return;
                status.addXp(1);
            }
        }
    }
}
