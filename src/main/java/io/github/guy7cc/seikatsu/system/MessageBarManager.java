package io.github.guy7cc.seikatsu.system;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import io.github.guy7cc.seikatsu.SeikatsuPlugin;

import java.util.ArrayList;
import java.util.List;

public class MessageBarManager implements Tickable {
    private final List<String> list = new ArrayList<>();
    private int index = 0;

    public MessageBarManager(){

    }

    public void fromJsonArray(JsonArray array){
        for(JsonElement element : array){
            if(element.isJsonPrimitive()) list.add(element.getAsString());
        }
    }

    public JsonArray toJsonArray(){
        JsonArray array = new JsonArray();
        for(String str : list){
            array.add(str);
        }
        return array;
    }

    public void add(String msg){
        list.add(index, msg);
    }

    public void remove(){
        list.remove(index);
        index = index >= list.size() - 1 ? 0 : index + 1;
    }

    @Override
    public void tick(int globalTick) {
        if(list.isEmpty()){
            index = 0;
            SeikatsuPlugin.playerGui.forEach(gui -> {
                gui.bossbar.setProgress(0);
                gui.bossbar.setTitle("ここにメッセージを追加することができます");
            });
        } else {
            if(globalTick % 200 == 0) {
                index = index >= list.size() - 1 ? 0 : index + 1;
            }
            SeikatsuPlugin.playerGui.forEach(gui -> {
                gui.bossbar.setTitle(list.get(index));
                if(list.size() == 1){
                    gui.bossbar.setProgress(1);
                } else {
                    gui.bossbar.setProgress((index + (globalTick % 200) / 200D) / list.size());
                }
            });
        }
    }
}
