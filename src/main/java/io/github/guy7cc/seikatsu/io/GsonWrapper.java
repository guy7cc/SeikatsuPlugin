package io.github.guy7cc.seikatsu.io;

import com.google.gson.*;
import io.github.guy7cc.seikatsu.SeikatsuPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.UUID;

public class GsonWrapper {
    public Gson gson;

    public GsonWrapper() {
        gson = new GsonBuilder()
                .registerTypeAdapter(UUID.class, new UuidAdapter())
                .create();
    }

    public JsonElement toJson(Object serializable) {
        return gson.toJsonTree(serializable);
    }

    public <T> T fromJson(JsonElement json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }

    public void save(@NotNull JsonElement element, @NotNull String fileName) {
        SeikatsuPlugin.log.info(SeikatsuPlugin.plugin.getDataFolder().toString());
        File file = new File(SeikatsuPlugin.plugin.getDataFolder(), fileName);
        try {
            file.getParentFile().mkdirs();
            BufferedWriter writer = new BufferedWriter(new FileWriter(file, false));
            writer.write(element.toString());
            writer.close();
        } catch (IOException exception) {
            SeikatsuPlugin.log.exception(String.format("%s could not be saved due to I/O errors.", fileName), exception);
        } catch (SecurityException exception) {
            SeikatsuPlugin.log.exception(String.format("%s could not be saved due to security problems.", fileName), exception);
        }
    }

    @Nullable
    public JsonElement load(@NotNull String fileName) {
        File file = new File(SeikatsuPlugin.plugin.getDataFolder(), fileName);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            return JsonParser.parseString(sb.toString());
        } catch (FileNotFoundException exception) {
            return null;
        } catch (IOException exception) {
            SeikatsuPlugin.log.exception(String.format("%s could not be read due to I/O errors.", fileName), exception);
        } catch (JsonParseException exception) {
            SeikatsuPlugin.log.exception(String.format("%s could not be read because the file format was invalid.", fileName), exception);
        }
        return null;
    }
}
