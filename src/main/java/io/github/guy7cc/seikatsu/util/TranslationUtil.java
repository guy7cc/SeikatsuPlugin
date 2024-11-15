package io.github.guy7cc.seikatsu.util;

import io.github.guy7cc.seikatsu.SeikatsuPlugin;

public class TranslationUtil {
    public static String key(String prefix, String suffix) {
        return prefix + "." + SeikatsuPlugin.NAMESPACE + "." + suffix;
    }

    public static String guiKey(String suffix) {
        return key("gui", suffix);
    }

    public static String chatKey(String suffix){
        return key("chat", suffix);
    }
}
