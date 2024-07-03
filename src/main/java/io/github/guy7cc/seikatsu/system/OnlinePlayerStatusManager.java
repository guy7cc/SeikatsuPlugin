package io.github.guy7cc.seikatsu.system;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

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
