package io.github.guy7cc.seikatsu.event;

import io.github.guy7cc.seikatsu.SeikatsuPlugin;
import io.github.guy7cc.seikatsu.player.OnlinePlayerStatus;
import org.bukkit.Bukkit;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;

public class EntityEventHandler implements Listener {
    @EventHandler
    public void onEntityPickupItem(EntityPickupItemEvent event){
        if(event.getEntity() instanceof Player player){
            OnlinePlayerStatus status = SeikatsuPlugin.onlinePlayerStatus.get(player);
            if(status != null){
                status.onInventoryChanged();
            }
        }
    }

    @EventHandler
    public void onEntityBreed(EntityBreedEvent event){
        if(event.getBreeder() instanceof Player player){
            SeikatsuPlugin.offlinePlayerStatus.computeIfPresent(player, s -> s.addXp(5));
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event){
        if(event.getDamager() instanceof Player player){
            SeikatsuPlugin.onlinePlayerStatus.computeIfPresent(player, s -> s.onDamageEntity(event.getDamage()));
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event){
        if(event.getDamageSource().getCausingEntity() instanceof Player player2){
            SeikatsuPlugin.offlinePlayerStatus.computeIfPresent(player2, s -> {
                if(s == null) return;
                if(event.getEntity() instanceof ElderGuardian) s.addXp(200);
                else if(event.getEntity() instanceof Wither) s.addXp(300);
                else if(event.getEntity() instanceof Warden) s.addXp(300);
                else if(event.getEntity() instanceof EnderDragon) s.addXp(500);
                else if(event.getEntity() instanceof Player) ;
                else s.addXp(1);
            });
        }
    }

    @EventHandler
    public void onEntityTame(EntityTameEvent event){
        if(event.getOwner() instanceof Player player){
            SeikatsuPlugin.offlinePlayerStatus.computeIfPresent(player, s -> s.addXp(15));
        }
    }
}
