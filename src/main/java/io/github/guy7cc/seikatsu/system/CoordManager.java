package io.github.guy7cc.seikatsu.system;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.guy7cc.seikatsu.gui.sidebar.CoordComponent;
import io.github.guy7cc.seikatsu.gui.sidebar.SimpleSidebarComponent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CoordManager {
    private List<Coord> list;

    public CoordManager(){
        list = new ArrayList<>();
    }

    public void fromJsonArray(JsonArray array){
        for(JsonElement element : array){
            if(element.isJsonObject()){
                try{
                    Coord coord = new Coord(element.getAsJsonObject());
                    list.add(coord);
                } catch(Exception ignored) {

                }
            }
        }
    }

    public JsonArray toJsonArray(){
        JsonArray array = new JsonArray();
        for(Coord coord : list){
            array.add(coord.toJsonObject());
        }
        return array;
    }

    public void add(Coord coord){
        list.add(coord);
    }

    public Coord get(String label){
        for(Coord coord : list){
            if(label.equals(coord.label)) return coord;
        }
        return null;
    }

    public boolean remove(String label){
        return list.removeIf(coord -> coord.label.equals(label));
    }

    public List<String> getLabels(){
        return list.stream().map(coord -> coord.label).toList();
    }

    public static class Coord{
        public final String label;
        public final long x;
        public final long y;
        public final long z;

        public Coord(@NotNull String label, long x, long y, long z){
            if(label.isEmpty() || label.isBlank()) throw new IllegalArgumentException("Coord label must not be empty or blank.");
            this.label = label;
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public Coord(@NotNull JsonObject object){
            label = object.get("label").getAsString();
            JsonArray coord = object.get("coord").getAsJsonArray();
            x = coord.get(0).getAsLong();
            y = coord.get(1).getAsLong();
            z = coord.get(2).getAsLong();
        }

        public JsonObject toJsonObject(){
            JsonObject object = new JsonObject();
            object.addProperty("label", label);
            JsonArray array = new JsonArray();
            array.add(x);
            array.add(y);
            array.add(z);
            object.add("coord", array);
            return object;
        }
    }
}
