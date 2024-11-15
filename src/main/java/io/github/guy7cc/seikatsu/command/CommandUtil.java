package io.github.guy7cc.seikatsu.command;

import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CommandUtil {
    public static List<String> getOptions(String arg, Collection<String> allOptions) {
        List<String> completion = new ArrayList<>();
        StringUtil.copyPartialMatches(arg, allOptions, completion);
        return completion;
    }
}
