package io.github.guy7cc.seikatsu.io;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import io.github.guy7cc.seikatsu.SeikatsuPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.UUID;

public class ExtendedJsonObjectWrapper extends JsonObjectWrapper {
    public ExtendedJsonObjectWrapper(){
        this(new JsonObject());
    }

    public ExtendedJsonObjectWrapper(@NotNull JsonObject object){
        this.object = object;
    }

    public UUID getUUID(String... propertyNames){
        validatePropertyNamesLength(propertyNames);
        JsonPrimitive primitive = getPrimitive(propertyNames);
        if(primitive != null) return SeikatsuPlugin.gson.fromJson(primitive, UUID.class);
        return null;
    }

    public void addProperty(UUID value, String... propertyNames) {
        validatePropertyNamesLength(propertyNames);
        if(propertyNames.length == 1){
            object.addProperty(propertyNames[0], value.toString());
        } else {
            String[] parents = Arrays.copyOfRange(propertyNames, 0, propertyNames.length - 1);
            JsonObject holder = getOrCreateObject(parents);
            holder.addProperty(propertyNames[propertyNames.length - 1], value.toString());
        }
    }
}
