package io.github.guy7cc.seikatsu.util;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import net.minecraft.network.chat.Component;
import org.bukkit.craftbukkit.v1_21_R2.util.CraftChatMessage;

public class ComponentUtil {
    public static Component toComponent(BaseComponent apiComponent){
        return CraftChatMessage.fromJSON(ComponentSerializer.toString(apiComponent));
    }
}
