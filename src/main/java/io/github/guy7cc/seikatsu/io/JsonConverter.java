package io.github.guy7cc.seikatsu.io;

import com.google.gson.JsonElement;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class JsonConverter {
    @Nullable
    public static UUID toUUID(String str){
        try {
            return UUID.fromString(str);
        } catch (IllegalArgumentException exception) {
            return null;
        }
    }

    @Nullable
    public static UUID toUUID(JsonElement element){
        if(element.isJsonPrimitive()){
            return toUUID(element.getAsString());
        }
        return null;
    }
}
