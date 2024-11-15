package io.github.guy7cc.seikatsu.player;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import io.github.guy7cc.seikatsu.system.Const;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.function.Consumer;

public class OfflinePlayerStatusManager {
    private final Map<String, OfflinePlayerStatus> map;

    public OfflinePlayerStatusManager(){
        map = new HashMap<>();
        for(Player player : Bukkit.getOnlinePlayers()){
            onPlayerJoin(player);
        }
    }

    public void fromJsonArray(JsonArray array){
        for(JsonElement element : array){
            if(element.isJsonObject()){
                OfflinePlayerStatus status = OfflinePlayerStatus.fromJsonObject(element.getAsJsonObject());
                if(status != null) {
                    map.put(status.getPlayerName(), status);
                    Player player = Bukkit.getPlayer(status.getPlayerName());
                    if(player != null) player.setPlayerListName(Const.getRank(status.getLevel()) + player.getName());
                }
            }
        }
    }

    public JsonArray toJsonArray(){
        JsonArray array = new JsonArray();
        for(OfflinePlayerStatus status : map.values()){
            array.add(status.object);
        }
        return array;
    }

    public OfflinePlayerStatus get(Player player){
        return map.get(player.getName());
    }

    public void computeIfPresent(Player player, Consumer<OfflinePlayerStatus> consumer){
        OfflinePlayerStatus status = map.get(player.getName());
        if(status != null) consumer.accept(status);
    }

    public void onPlayerJoin(Player player){
        if(!map.containsKey(player.getName())){
            map.put(player.getName(), new OfflinePlayerStatus(player));
        }
        OfflinePlayerStatus status = get(player);
        player.setPlayerListName(Const.getRank(status.getLevel()) + "§f" + player.getName());
    }
}
