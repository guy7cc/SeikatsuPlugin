package io.github.guy7cc.seikatsu.system;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.guy7cc.seikatsu.SeikatsuPlugin;
import io.github.guy7cc.seikatsu.io.ExtendedJsonObjectWrapper;
import io.github.guy7cc.seikatsu.io.JsonConverter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class SeatTicker implements Tickable {
    private Queue<UUID> seats;

    public SeatTicker(){
        seats = new ArrayDeque<>();
    }

    public void fromJsonArray(JsonArray array){
        for(JsonElement element : array){
            if(element.isJsonPrimitive() && element.getAsJsonPrimitive().isString()){
                UUID uuid = JsonConverter.toUUID(element.getAsString());
                if(uuid != null) seats.add(uuid);
            }
        }
    }

    public void register(Entity seat){
        seats.add(seat.getUniqueId());
    }

    public JsonArray toJsonArray(){
        JsonArray array = new JsonArray();
        for(UUID uuid : seats){
            array.add(uuid.toString());
        }
        return array;
    }

    @Override
    public void tick(int globalTick) {
        if(globalTick % 20 != 0) return;
        UUID uuid = seats.poll();
        if(uuid == null) return;
        Entity entity = Bukkit.getServer().getEntity(uuid);
        if(entity == null) return;
        if(entity.getPassengers().isEmpty()) {
            entity.remove();
        } else {
            seats.add(uuid);
        }
    }
}
