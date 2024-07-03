package io.github.guy7cc.seikatsu.io;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;

public class JsonObjectWrapper {
    public JsonObject object;

    public JsonObjectWrapper(){
        this(new JsonObject());
    }

    public JsonObjectWrapper(@NotNull JsonObject object){
        this.object = object;
    }

    public boolean has(String... propertyNames){
        validatePropertyNamesLength(propertyNames);
        return getElement(propertyNames) != null;
    }

    @Nullable
    public JsonElement getElement(String... propertyNames){
        validatePropertyNamesLength(propertyNames);
        JsonObject child = object;
        for(int i = 0; i < propertyNames.length - 1; ++i){
            if(!child.has(propertyNames[i])) return null;
            JsonElement element = child.get(propertyNames[i]);
            if(!element.isJsonObject()) return null;
            child = element.getAsJsonObject();
        }
        return child.get(propertyNames[propertyNames.length - 1]);
    }

    @Nullable
    public JsonObject getObject(String... propertyNames){
        validatePropertyNamesLength(propertyNames);
        JsonElement element = getElement(propertyNames);
        if(element != null && element.isJsonObject()) return element.getAsJsonObject();
        return null;
    }

    @Nullable
    public JsonPrimitive getPrimitive(String... propertyNames){
        validatePropertyNamesLength(propertyNames);
        JsonElement element = getElement(propertyNames);
        if(element != null && element.isJsonPrimitive()) return element.getAsJsonPrimitive();
        return null;
    }

    @Nullable
    public JsonArray getArray(String... propertyNames){
        validatePropertyNamesLength(propertyNames);
        JsonElement element = getElement(propertyNames);
        if(element != null && element.isJsonArray()) return element.getAsJsonArray();
        return null;
    }

    public boolean getBoolean(String... propertyNames){
        validatePropertyNamesLength(propertyNames);
        JsonPrimitive primitive = getPrimitive(propertyNames);
        if(primitive != null) return primitive.getAsBoolean();
        return false;
    }

    public Number getNumber(String... propertyNames){
        validatePropertyNamesLength(propertyNames);
        JsonPrimitive primitive = getPrimitive(propertyNames);
        if(primitive != null) return primitive.getAsNumber();
        return 0;
    }

    public String getString(String... propertyNames){
        validatePropertyNamesLength(propertyNames);
        JsonPrimitive primitive = getPrimitive(propertyNames);
        if(primitive != null) return primitive.getAsString();
        return "";
    }

    public double getDouble(String... propertyNames){
        validatePropertyNamesLength(propertyNames);
        JsonPrimitive primitive = getPrimitive(propertyNames);
        if(primitive != null) return primitive.getAsDouble();
        return 0D;
    }

    public BigDecimal getBigDecimal(String... propertyNames){
        validatePropertyNamesLength(propertyNames);
        JsonPrimitive primitive = getPrimitive(propertyNames);
        if(primitive != null) return primitive.getAsBigDecimal();
        return BigDecimal.ZERO;
    }

    public BigInteger getBigInteger(String... propertyNames){
        validatePropertyNamesLength(propertyNames);
        JsonPrimitive primitive = getPrimitive(propertyNames);
        if(primitive != null) return primitive.getAsBigInteger();
        return BigInteger.ZERO;
    }

    public float getFloat(String... propertyNames){
        validatePropertyNamesLength(propertyNames);
        JsonPrimitive primitive = getPrimitive(propertyNames);
        if(primitive != null) return primitive.getAsFloat();
        return 0F;
    }

    public long getLong(String... propertyNames){
        validatePropertyNamesLength(propertyNames);
        JsonPrimitive primitive = getPrimitive(propertyNames);
        if(primitive != null) return primitive.getAsLong();
        return 0L;
    }

    public short getShort(String... propertyNames){
        validatePropertyNamesLength(propertyNames);
        JsonPrimitive primitive = getPrimitive(propertyNames);
        if(primitive != null) return primitive.getAsShort();
        return 0;
    }

    public int getInt(String... propertyNames){
        validatePropertyNamesLength(propertyNames);
        JsonPrimitive primitive = getPrimitive(propertyNames);
        if(primitive != null) return primitive.getAsInt();
        return 0;
    }

    public byte getByte(String... propertyNames){
        validatePropertyNamesLength(propertyNames);
        JsonPrimitive primitive = getPrimitive(propertyNames);
        if(primitive != null) return primitive.getAsByte();
        return 0;
    }

    public char getCharacter(String... propertyNames){
        validatePropertyNamesLength(propertyNames);
        JsonPrimitive primitive = getPrimitive(propertyNames);
        if(primitive != null) return primitive.getAsCharacter();
        return '\u0000';
    }

    private static JsonObject innerGetOrCreateObject(JsonObject obj, String propertyName){
        if(obj.has(propertyName) && obj.get(propertyName).isJsonObject()){
            return obj.get(propertyName).getAsJsonObject();
        } else {
            JsonObject newChild = new JsonObject();
            obj.add(propertyName, newChild);
            return newChild;
        }
    }

    public void addObject(JsonObject object, String... propertyNames){
        validatePropertyNamesLength(propertyNames);
        JsonObject child = object;
        for(int i = 0; i < propertyNames.length - 1; ++i){
            child = innerGetOrCreateObject(child, propertyNames[i]);
        }
        child.add(propertyNames[propertyNames.length - 1], object);
    }

    public JsonObject getOrCreateObject(String... propertyNames){
        validatePropertyNamesLength(propertyNames);
        JsonObject child = object;
        for (String propertyName : propertyNames) {
            child = innerGetOrCreateObject(child, propertyName);
        }
        return child;
    }
    
    public void addArray(JsonArray array, String... propertyNames){
        validatePropertyNamesLength(propertyNames);
        JsonObject child = object;
        for(int i = 0; i < propertyNames.length - 1; ++i){
            child = innerGetOrCreateObject(child, propertyNames[i]);
        }
        child.add(propertyNames[propertyNames.length - 1], array);
    }

    public JsonArray getOrCreateArray(String... propertyNames){
        validatePropertyNamesLength(propertyNames);
        JsonObject child = object;
        for(int i = 0; i < propertyNames.length - 1; ++i){
            child = innerGetOrCreateObject(child, propertyNames[i]);
        }
        String propertyName = propertyNames[propertyNames.length - 1];
        if(child.has(propertyName) && child.get(propertyName).isJsonArray()){
            return child.get(propertyName).getAsJsonArray();
        } else {
            JsonArray newChild = new JsonArray();
            child.add(propertyName, newChild);
            return newChild;
        }
    }

    public void addProperty(Number value, String... propertyNames) {
        validatePropertyNamesLength(propertyNames);
        if(propertyNames.length == 1){
            object.addProperty(propertyNames[0], value);
        } else {
            String[] parents = Arrays.copyOfRange(propertyNames, 0, propertyNames.length - 1);
            JsonObject holder = getOrCreateObject(parents);
            holder.addProperty(propertyNames[propertyNames.length - 1], value);
        }
    }

    public void addProperty(Boolean value, String... propertyNames) {
        validatePropertyNamesLength(propertyNames);
        if(propertyNames.length == 1){
            object.addProperty(propertyNames[0], value);
        } else {
            String[] parents = Arrays.copyOfRange(propertyNames, 0, propertyNames.length - 1);
            JsonObject holder = getOrCreateObject(parents);
            holder.addProperty(propertyNames[propertyNames.length - 1], value);
        }
    }

    public void addProperty(Character value, String... propertyNames) {
        validatePropertyNamesLength(propertyNames);
        if(propertyNames.length == 1){
            object.addProperty(propertyNames[0], value);
        } else {
            String[] parents = Arrays.copyOfRange(propertyNames, 0, propertyNames.length - 1);
            JsonObject holder = getOrCreateObject(parents);
            holder.addProperty(propertyNames[propertyNames.length - 1], value);
        }
    }

    public void addProperty(String value, String... propertyNames){
        validatePropertyNamesLength(propertyNames);
        if(propertyNames.length == 1){
            object.addProperty(propertyNames[0], value);
        } else {
            String[] parents = Arrays.copyOfRange(propertyNames, 0, propertyNames.length - 1);
            JsonObject holder = getOrCreateObject(parents);
            holder.addProperty(propertyNames[propertyNames.length - 1], value);
        }
    }

    protected static void validatePropertyNamesLength(String[] propertyNames){
        if(propertyNames.length == 0) throw new IllegalArgumentException("You must pass one member name at least to get access to a JSON Property");
    }
}
