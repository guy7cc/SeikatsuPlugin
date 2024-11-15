package io.github.guy7cc.seikatsu.system;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Registry<T extends RegistryObject> {
    private final String prefix;
    private final Map<Integer, T> idMap = new HashMap<>(128);
    private final Map<String, T> nameMap = new HashMap<>(128);

    public Registry(String prefix) {
        this.prefix = prefix;
    }

    public T register(T item) {
        item.setTranslationKey(prefix);
        idMap.put(item.id, item);
        nameMap.put(item.name, item);
        return item;
    }

    @Nullable
    public T byId(int id) {
        return idMap.get(id);
    }

    @Nullable
    public T byName(String name) {
        return nameMap.get(name);
    }

    public Set<Integer> ids() {
        return idMap.keySet();
    }

    public Set<String> names() {
        return nameMap.keySet();
    }
}
