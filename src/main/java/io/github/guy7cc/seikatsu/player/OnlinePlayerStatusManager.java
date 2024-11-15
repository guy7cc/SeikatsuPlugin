package io.github.guy7cc.seikatsu.player;

import io.github.guy7cc.seikatsu.system.Tickable;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class OnlinePlayerStatusManager implements Tickable {
    private final Map<Player, OnlinePlayerStatus> map = new HashMap<>();

    public OnlinePlayerStatusManager(){
        for(Player player : Bukkit.getOnlinePlayers()){
            onPlayerJoin(player);
        }
    }

    public OnlinePlayerStatus get(Player player){
        return map.get(player);
    }

    public void computeIfPresent(Player player, Consumer<OnlinePlayerStatus> consumer){
        OnlinePlayerStatus status = map.get(player);
        if(status != null) consumer.accept(status);
    }

    @Override
    public void tick(int globalTick) {
        for(OnlinePlayerStatus status : map.values()){
            status.tick(globalTick);
        }
    }

    public void onPlayerJoin(Player player){
        map.put(player, new OnlinePlayerStatus(player));
    }

    public void onPlayerQuit(Player player){
        map.remove(player);
    }
}
