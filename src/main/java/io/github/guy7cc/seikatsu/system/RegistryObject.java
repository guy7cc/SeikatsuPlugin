package io.github.guy7cc.seikatsu.system;

import io.github.guy7cc.seikatsu.SeikatsuPlugin;
import io.github.guy7cc.seikatsu.util.TranslationUtil;
import org.bukkit.NamespacedKey;

public class RegistryObject {
    public final int id;
    public final String name;
    public final NamespacedKey key;
    private String translationKey;

    public RegistryObject(int id, String name) {
        this.id = id;
        this.name = name;
        this.key = new NamespacedKey(SeikatsuPlugin.NAMESPACE, name);
    }

    public String getTranslationKey() {
        return translationKey;
    }

    protected void setTranslationKey(String prefix) {
        translationKey = TranslationUtil.key(prefix, name);
    }
}
